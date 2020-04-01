package net.claytry.clayapi.bukkit.utils;
/*
 * Class created by rexlManu
 * Twitter: @rexlManu | Website: rexlManu.de
 * Coded with IntelliJ
 */

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class JsonConfig {

    private final String filename;
    private final File source;
    @Getter
    private JsonObject jsonObject;
    @Getter
    private final JsonObject defaultObject;

    public JsonConfig(final String filename, final File datafolder) {
        this.filename = filename;
        datafolder.mkdir();
        this.source = new File(datafolder, filename + ".json");
        this.defaultObject = new JsonObject();
    }

    public JsonConfig addDefault(final String key, final Object value) {
        this.defaultObject.add(key, new JsonPrimitive(value.toString()));
        return this;
    }

    public JsonConfig load() {
        if (!this.source.exists()) {
            try {
                this.source.createNewFile();
                this.write(this.source, this.convertToString(this.defaultObject));
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        this.read();
        return this;
    }

    public JsonConfig save() throws IOException {
        this.write(this.source, this.convertToString(this.jsonObject));
        return this;
    }

    private void read() {
        try {
            this.jsonObject = new JsonParser().parse(new String(Files.readAllBytes(this.source.toPath()))).getAsJsonObject();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private void write(final File file, final String string) throws IOException {
        final FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(string);
        fileWriter.flush();
        fileWriter.close();
    }

    public void setString(final String key, final String value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setBoolean(final String key, final Boolean value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setInteger(final String key, final Integer value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setLong(final String key, final Long value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setFloat(final String key, final Float value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setDouble(final String key, final Double value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setShort(final String key, final Short value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setByte(final String key, final Byte value) {
        this.jsonObject.addProperty(key, value);
    }

    public void setLocation(final String key, final Location location) {
        this.setString(key + ".World", location.getWorld().getName());
        this.setDouble(key + ".X", location.getX());
        this.setDouble(key + ".Y", location.getY());
        this.setDouble(key + ".Z", location.getZ());
        this.setFloat(key + ".Yaw", location.getYaw());
        this.setFloat(key + ".Pitch", location.getPitch());
    }

    public Location getLocation(final String key) {
        if (this.getString(key + ".World") == null) {
            return null;
        }
        final String world = this.getString(key + ".World");
        final Double x = this.getDouble(key + ".X");
        final Double y = this.getDouble(key + ".Y");
        final Double z = this.getDouble(key + ".Z");
        final Float yaw = this.getFloat(key + ".Yaw");
        final Float pitch = this.getFloat(key + ".Pitch");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public Integer getInteger(final String key) {
        return this.jsonObject.get(key).getAsInt();
    }

    public Boolean getBoolean(final String key) {
        return this.jsonObject.get(key).getAsBoolean();
    }

    public String getString(final String key) {
        return this.jsonObject.get(key).getAsString();
    }

    public Float getFloat(final String key) {
        return this.jsonObject.get(key).getAsFloat();
    }

    public Double getDouble(final String key) {
        return this.jsonObject.get(key).getAsDouble();
    }

    public Long getLong(final String key) {
        return this.jsonObject.get(key).getAsLong();
    }

    public Short getShort(final String key) {
        return this.jsonObject.get(key).getAsShort();
    }

    public Byte getByte(final String key) {
        return this.jsonObject.get(key).getAsByte();
    }

    public void setObject(final String key, final Object value) {
        this.jsonObject.add(key, new JsonPrimitive(value.toString()));
    }

    public String convertToString(final JsonObject jsonObject) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject);
    }

}
