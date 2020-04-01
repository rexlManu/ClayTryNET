/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.utils;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 19:02                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import com.mojang.authlib.*;
import com.mojang.util.*;
import java.net.*;
import java.io.*;
import org.yaml.snakeyaml.external.biz.base64Coder.*;
import java.lang.reflect.*;
import com.mojang.authlib.properties.*;
import java.util.*;
import com.google.gson.*;

public class GameProfileBuilder
{
    private static final String SERVICE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
    private static final String JSON_SKIN = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}";
    private static final String JSON_CAPE = "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"},\"CAPE\":{\"url\":\"%s\"}}}";
    private static final Gson gson;
    private static final HashMap<UUID, CachedProfile> cache;
    private static long cacheTime;

    public static GameProfile fetch(final UUID uuid) {
        try {
            return fetch(uuid, false);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static GameProfile fetch(final UUID uuid, final boolean forceNew) {
        try {
            if (!forceNew && GameProfileBuilder.cache.containsKey(uuid) && GameProfileBuilder.cache.get(uuid).isValid()) {
                return GameProfileBuilder.cache.get(uuid).profile;
            }
            final HttpURLConnection connection = (HttpURLConnection)new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);
            if (connection.getResponseCode() == 200) {
                final String json = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                final GameProfile result = (GameProfile)GameProfileBuilder.gson.fromJson(json, (Class)GameProfile.class);
                GameProfileBuilder.cache.put(uuid, new CachedProfile(result));
                return result;
            }
            if (!forceNew && GameProfileBuilder.cache.containsKey(uuid)) {
                return GameProfileBuilder.cache.get(uuid).profile;
            }
            final JsonObject error = (JsonObject)new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getErrorStream())).readLine());
            throw new IOException(error.get("error").getAsString() + ": " + error.get("errorMessage").getAsString());
        }
        catch (IOException | JsonSyntaxException ex3) {
            return null;
        }
    }

    public static GameProfile getProfile(final UUID uuid, final String name, final String skin) {
        return getProfile(uuid, name, skin, null);
    }

    public static GameProfile getProfile(final UUID uuid, final String name, final String skinUrl, final String capeUrl) {
        final GameProfile profile = new GameProfile(uuid, name);
        final boolean cape = capeUrl != null && !capeUrl.isEmpty();
        final List<Object> args = new ArrayList<>();
        args.add(System.currentTimeMillis());
        args.add(UUIDTypeAdapter.fromUUID(uuid));
        args.add(name);
        args.add(skinUrl);
        if (cape) {
            args.add(capeUrl);
        }
        profile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString(String.format(cape ? "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"},\"CAPE\":{\"url\":\"%s\"}}}" : "{\"timestamp\":%d,\"profileId\":\"%s\",\"profileName\":\"%s\",\"isPublic\":true,\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", args.toArray(new Object[args.size()])))));
        return profile;
    }

    public static void setCacheTime(final long time) {
        GameProfileBuilder.cacheTime = time;
    }

    static {
        gson = new GsonBuilder().disableHtmlEscaping().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeAdapter(GameProfile.class, new GameProfileSerializer()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
        cache = new HashMap<UUID, CachedProfile>();
        GameProfileBuilder.cacheTime = 99999999L;
    }

    private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile>
    {
        public GameProfile deserialize(final JsonElement json, final Type type, final JsonDeserializationContext context) throws JsonParseException {
            final JsonObject object = (JsonObject)json;
            final UUID id = object.has("id") ? ((UUID)context.deserialize(object.get("id"), UUID.class)) : null;
            final String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
            final GameProfile profile = new GameProfile(id, name);
            if (object.has("properties")) {
                for (final Map.Entry<String, Property> prop : ((PropertyMap)context.deserialize(object.get("properties"), PropertyMap.class)).entries()) {
                    profile.getProperties().put(prop.getKey(), prop.getValue());
                }
            }
            return profile;
        }

        public JsonElement serialize(final GameProfile profile, final Type type, final JsonSerializationContext context) {
            final JsonObject result = new JsonObject();
            if (profile.getId() != null) {
                result.add("id", context.serialize(profile.getId()));
            }
            if (profile.getName() != null) {
                result.addProperty("name", profile.getName());
            }
            if (!profile.getProperties().isEmpty()) {
                result.add("properties", context.serialize(profile.getProperties()));
            }
            return result;
        }
    }

    private static class CachedProfile
    {
        private final long timestamp;
        private GameProfile profile;

        public CachedProfile(final GameProfile profile) {
            this.timestamp = System.currentTimeMillis();
            this.profile = profile;
        }

        public boolean isValid() {
            return GameProfileBuilder.cacheTime < 0L || System.currentTimeMillis() - this.timestamp < GameProfileBuilder.cacheTime;
        }
    }
}
