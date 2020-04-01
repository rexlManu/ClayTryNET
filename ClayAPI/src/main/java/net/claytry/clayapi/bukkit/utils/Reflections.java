/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 13.07.2018 / 15:45                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public final class Reflections {

    private static String OBC_PREFIX;
    private static String NMS_PREFIX;
    private static String VERSION;
    private static Pattern MATCH_VARIABLE;

    static {
        Reflections.OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
        Reflections.NMS_PREFIX = Reflections.OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");
        Reflections.VERSION = Reflections.OBC_PREFIX.replace("org.bukkit.craftbukkit", "").replace(".", "");
        Reflections.MATCH_VARIABLE = Pattern.compile("\\{([^\\}]+)\\}");
    }

    private static String expandVariables(final String name) {
        final StringBuffer output = new StringBuffer();
        final Matcher matcher = Reflections.MATCH_VARIABLE.matcher(name);
        while (matcher.find()) {
            final String variable = matcher.group(1);
            String replacement;
            if ("nms".equalsIgnoreCase(variable)) {
                replacement = Reflections.NMS_PREFIX;
            } else if ("obc".equalsIgnoreCase(variable)) {
                replacement = Reflections.OBC_PREFIX;
            } else {
                if (!"version".equalsIgnoreCase(variable)) {
                    throw new IllegalArgumentException("Unknown variable: " + variable);
                }
                replacement = Reflections.VERSION;
            }
            if (replacement.length() > 0 && matcher.end() < name.length() && name.charAt(matcher.end()) != '.') {
                replacement = String.valueOf(replacement) + ".";
            }
            matcher.appendReplacement(output, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(output);
        return output.toString();
    }

    private static Class<?> getCanonicalClass(final String canonicalName) {
        try {
            return Class.forName(canonicalName);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot find " + canonicalName, e);
        }
    }

    public static Class<?> getClass(final String lookupName) {
        return Reflections.getCanonicalClass(Reflections.expandVariables(lookupName));
    }

    public static ConstructorInvoker getConstructor(final String className, final Class<?>... params) {
        return Reflections.getConstructor(Reflections.getClass(className), params);
    }

    public static ConstructorInvoker getConstructor(final Class<?> clazz, final Class<?>... params) {
        final Constructor<?>[] declaredConstructors;
        for (int length = (declaredConstructors = clazz.getDeclaredConstructors()).length, i = 0; i < length; ++i) {
            final Constructor<?> constructor = declaredConstructors[i];
            if (Arrays.equals(constructor.getParameterTypes(), params)) {
                constructor.setAccessible(true);
                return new ConstructorInvoker() {
                    @Override
                    public Object invoke(final Object... arguments) {
                        try {
                            return constructor.newInstance(arguments);
                        } catch (final Exception e) {
                            throw new RuntimeException("Cannot invoke constructor " + constructor, e);
                        }
                    }
                };
            }
        }
        throw new IllegalStateException(String.format("Unable to find constructor for %s (%s).", clazz, Arrays.asList(params)));
    }

    public static Class<?> getCraftBukkitClass(final String name) {
        return Reflections.getCanonicalClass(String.valueOf(Reflections.OBC_PREFIX) + "." + name);
    }

    public static <T> FieldAccessor<T> getField(final Class<?> target, final String name, final Class<T> fieldType) {
        return Reflections.getField(target, name, fieldType, 0);
    }

    public static <T> FieldAccessor<T> getField(final String className, final String name, final Class<T> fieldType) {
        return Reflections.getField(Reflections.getClass(className), name, fieldType, 0);
    }

    public static <T> FieldAccessor<T> getField(final Class<?> target, final Class<T> fieldType, final int index) {
        return Reflections.getField(target, null, fieldType, index);
    }

    public static <T> FieldAccessor<T> getField(final String className, final Class<T> fieldType, final int index) {
        return Reflections.getField(Reflections.getClass(className), fieldType, index);
    }

    private static <T> FieldAccessor<T> getField(final Class<?> target, final String name, final Class<T> fieldType, int index) {
        final Field[] declaredFields;
        for (int length = (declaredFields = target.getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return new FieldAccessor<T>() {
                    @Override
                    public T get(final Object target) {
                        try {
                            return (T) field.get(target);
                        } catch (final IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public void set(final Object target, final Object value) {
                        try {
                            field.set(target, value);
                        } catch (final IllegalAccessException e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public boolean hasField(final Object target) {
                        return field.getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
        }
        if (target.getSuperclass() != null) {
            return (FieldAccessor<T>) Reflections.getField(target.getSuperclass(), name, (Class<Object>) fieldType, index);
        }
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

    public static MethodInvoker getMethod(final String className, final String methodName, final Class<?>... params) {
        return Reflections.getTypedMethod(Reflections.getClass(className), methodName, null, params);
    }

    public static MethodInvoker getMethod(final Class<?> clazz, final String methodName, final Class<?>... params) {
        return Reflections.getTypedMethod(clazz, methodName, null, params);
    }

    public static Method getMethodSimply(final Class<?> clazz, final String method) {
        final Method[] methods;
        for (int length = (methods = clazz.getMethods()).length, i = 0; i < length; ++i) {
            final Method m = methods[i];
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

    public static Class<?> getMinecraftClass(final String name) {
        return Reflections.getCanonicalClass(String.valueOf(Reflections.NMS_PREFIX) + "." + name);
    }

    public static MethodInvoker getTypedMethod(final Class<?> clazz, final String methodName, final Class<?> returnType, final Class<?>... params) {
        final Method[] declaredMethods;
        for (int length = (declaredMethods = clazz.getDeclaredMethods()).length, i = 0; i < length; ++i) {
            final Method method = declaredMethods[i];
            if (((methodName == null || method.getName().equals(methodName)) && returnType == null) || (method.getReturnType().equals(returnType) && Arrays.equals(method.getParameterTypes(), params))) {
                method.setAccessible(true);
                return new MethodInvoker() {
                    @Override
                    public Object invoke(final Object target, final Object... arguments) {
                        try {
                            return method.invoke(target, arguments);
                        } catch (final Exception e) {
                            throw new RuntimeException("Cannot invoke method " + method, e);
                        }
                    }
                };
            }
        }
        if (clazz.getSuperclass() != null) {
            return Reflections.getMethod(clazz.getSuperclass(), methodName, params);
        }
        throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, Arrays.asList(params)));
    }

    public static Class<Object> getUntypedClass(final String lookupName) {
        final Class<Object> clazz = (Class<Object>) Reflections.getClass(lookupName);
        return clazz;
    }

    public static <T> T newInstance(final Class<T> type) {
        try {
            return type.newInstance();
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface ConstructorInvoker {
        Object invoke(final Object... p0);
    }

    public interface FieldAccessor<T> {
        T get(final Object p0);

        void set(final Object p0, final Object p1);

        boolean hasField(final Object p0);
    }

    public interface MethodInvoker {
        Object invoke(final Object p0, final Object... p1);
    }

}
