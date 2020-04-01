/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.module;

import com.arangodb.internal.util.IOUtils;
import com.google.gson.JsonParser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 14:29                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class ModuleManager {

    private List<Module> modules;
    private File moduleDirectory;

    public ModuleManager() {
        this.modules = new ArrayList<>();
        this.moduleDirectory = new File("./modules");
        if (!moduleDirectory.exists()) moduleDirectory.mkdir();

    }

    public void enableModules(){
        for (File file : Objects.requireNonNull(moduleDirectory.listFiles())) {
            if(file.getName().endsWith(".jar")){
                enableModule(file);
            }
        }
    }

    public void enableModule(File file) {
        try {
            /*
                old methode
             */
//            final Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
//            addURL.setAccessible(true);
//            addURL.invoke(URLClassLoader.getSystemClassLoader(), file.toURI().toURL());

            final URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, Module.class.getClassLoader());

            /*
            find the module.json
             */
            final JarFile jarFile = new JarFile(file);
            final Enumeration<JarEntry> entries = jarFile.entries();
            final String className = readModuleFile(entries, jarFile);
            final Class<?> cw = urlClassLoader.loadClass(className);
            if (className == null) {
                throw new NullPointerException("The module.json could net be loaded.");
            }
//            final Class<?> mainClass = Class.forName(className);
            final Module module = (Module) cw.newInstance();
            modules.add(module);
            System.out.println("Module " + module.getModuleName() + " " + module.getVersion() + " enabled from " + module.getAuthor());
            runAsync(module);
        } catch ( IllegalAccessException | IOException | ClassNotFoundException | InstantiationException e) {
            System.out.println("The main class could not be loaded.");
            e.printStackTrace();
            return;
        }
    }

    public void disableModule(Module module) {
        module.onDisable();
        System.out.println("Module " + module.getModuleName() + " " + module.getVersion() + " disabled from " + module.getAuthor());
    }

    private String readModuleFile(Enumeration<JarEntry> entries, JarFile jarFile) {
        while (entries.hasMoreElements()) {
            final JarEntry jarEntry = entries.nextElement();
            final String name = jarEntry.getName();
            if (name.equals("module.json")) {
                try {
                    final InputStream inputStream = jarFile.getInputStream(jarFile.getEntry(name));
                    final String rawJson = IOUtils.toString(inputStream);
                    inputStream.close();
                    return new JsonParser().parse(rawJson).getAsJsonObject().get("main").getAsString();
                } catch (IOException e) {
                    System.out.println("The module.json could not been loaded.");
                }
            }
        }
        return null;
    }

    public void disableAllModule() {
        this.modules.forEach(this::disableModule);
    }

    private void runAsync(Module module) {
        if (module.isAsync()) {
            new Thread(module::onEnable).start();
        } else {
            module.onEnable();
        }
    }

    public static void main(String[] args) {
        final ModuleManager moduleManager = new ModuleManager();
        moduleManager.enableModules();
    }

}
