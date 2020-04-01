package net.claytry.clayapi.bukkit.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


/**
 * Created by rexlManu on 29.08.2017.
 * Plugin by rexlManu
 * https://rexlGames.de
 * Coded with IntelliJ
 */
public class UUIDFetcher {

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    private String name;
    private UUID id;

    private static void getUUID(final String name, final Consumer<UUID> action) {
        UUIDFetcher.pool.execute(() -> {
            try {
                action.accept(UUIDFetcher.getUUID(name));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static UUID getUUID(final String name) throws Exception {
        return UUIDFetcher.getUUIDAt(name, System.currentTimeMillis());
    }

    private static UUID getUUIDAt(final String name, final long timestamp) throws Exception {

        final HttpURLConnection connection = (HttpURLConnection)
                new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s?at=%d",
                        name, timestamp / 1000)).openConnection();

        connection.setReadTimeout(5000);

        final UUIDFetcher uuidFetcher = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), UUIDFetcher.class);
        return uuidFetcher.id;
    }

    private static void getName(final UUID uuid, final Consumer<String> action) {
        UUIDFetcher.pool.execute(() -> action.accept(UUIDFetcher.getName(uuid)));
    }

    public static String getName(final UUID uuid) {

        try {

            final HttpURLConnection connection = (HttpURLConnection)
                    new URL(String.format("https://api.mojang.com/user/profiles/%s/names",
                            uuid.toString().replace("-", ""))).openConnection();

            connection.setReadTimeout(5000);

            final JsonObject[] jsonObject = UUIDFetcher.gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), JsonObject[].class);
            return jsonObject[(jsonObject.length - 1)].get("name").getAsString();

        } catch (final Exception exc) {
        }

        return null;
    }

    private void getUUIDAt(final String name, final long timestamp, final Consumer<UUID> action) {
        UUIDFetcher.pool.execute(() -> {
            try {
                action.accept(UUIDFetcher.getUUIDAt(name, timestamp));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static final Map<String, String> uuidCache = new HashMap<>();

    public static String getUUIDAlternative(final String user) {
        if (UUIDFetcher.uuidCache.containsKey(user)) {
            return (String) UUIDFetcher.uuidCache.get(user);
        }
        try {
            final URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + user);
            final InputStream stream = url.openStream();
            final InputStreamReader inr = new InputStreamReader(stream);
            final BufferedReader reader = new BufferedReader(inr);
            String s = null;
            final StringBuilder sb = new StringBuilder();
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            final String result = sb.toString();

            final JsonElement element = new JsonParser().parse(result);
            final JsonObject obj = element.getAsJsonObject();

            String uuid = obj.get("id").toString();

            uuid = uuid.substring(1);
            uuid = uuid.substring(0, uuid.length() - 1);

            UUIDFetcher.uuidCache.put(user, uuid);

            return uuid;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class UUIDTypeAdapter extends TypeAdapter<UUID> {
        @Override
        public void write(final JsonWriter out, final UUID value)
                throws IOException {
            out.value(UUIDTypeAdapter.fromUUID(value));
        }

        @Override
        public UUID read(final JsonReader in) throws IOException {
            return UUIDTypeAdapter.fromString(in.nextString());
        }

        public static String fromUUID(final UUID value) {
            return value.toString().replace("-", "");
        }

        public static UUID fromString(final String input) {
            return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
    }

}
