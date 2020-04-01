/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.utils;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 19:01                           
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
import java.net.*;
import org.json.simple.*;
import java.util.logging.*;
import java.io.*;
import org.json.simple.parser.*;
import java.util.*;

public class DataUtil
{
    static Map<String, String> uuid_nodashes;
    static Map<String, String> name;
    static Map<String, UUID> uuid;
    static Map<String, GameProfile> profiles;

    public static String getName(final String uuid) {
        if (DataUtil.name.containsKey(uuid)) {
            return DataUtil.name.get(uuid);
        }
        try {
            final URL api = new URL("https://api.mojang.com/user/profiles/UUID/names".replace("UUID", uuid.replace("-", "")));
            final BufferedReader reader = new BufferedReader(new InputStreamReader(api.openStream()));
            final Object obj = new JSONParser().parse((Reader)reader);
            final JSONArray array = (JSONArray)obj;
            final JSONObject object = (JSONObject)array.get(array.size() - 1);
            reader.close();
            final String output = (String)object.get((Object)"name");
            DataUtil.name.put(uuid, output);
            return output;
        }
        catch (IOException | ParseException ex3) {
            return null;
        }
    }

    public static String getUUID_NoDashes(final String name) {
        if (DataUtil.uuid_nodashes.containsKey(name)) {
            return DataUtil.uuid_nodashes.get(name);
        }
        try {
            final URL api = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(api.openStream()));
            final Object obj = new JSONParser().parse((Reader)reader);
            final JSONObject object = (JSONObject)obj;
            reader.close();
            final String output = (String)object.get((Object)"id");
            DataUtil.uuid_nodashes.put(name, output);
            return output;
        }
        catch (IOException | ParseException ex3) {
            Logger.getLogger(DataUtil.class.getName()).log(Level.SEVERE, null, ex3);
            return null;
        }
    }

    public static GameProfile getProfile(final String name) {
        try {
            if (DataUtil.profiles.containsKey(name)) {
                return DataUtil.profiles.get(name);
            }
            final GameProfile profile = GameProfileBuilder.fetch(getUUID(name));
            DataUtil.profiles.put(profile.getName(), profile);
            return profile;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static UUID getUUID(final String name) {
        if (DataUtil.uuid.containsKey(name)) {
            return DataUtil.uuid.get(name);
        }
        try {
            final URL api = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(api.openStream()));
            final Object obj = new JSONParser().parse((Reader)reader);
            final JSONObject object = (JSONObject)obj;
            reader.close();
            final UUID output = UUID.fromString(((String)object.get((Object)"id")).replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5"));
            DataUtil.uuid.put(name, output);
            return output;
        }
        catch (IOException | ParseException ex3) {
            Logger.getLogger(DataUtil.class.getName()).log(Level.SEVERE, null, ex3);
            return null;
        }
    }

    static {
        DataUtil.uuid_nodashes = new HashMap<String, String>();
        DataUtil.name = new HashMap<String, String>();
        DataUtil.uuid = new HashMap<String, UUID>();
        DataUtil.profiles = new HashMap<String, GameProfile>();
    }
}
