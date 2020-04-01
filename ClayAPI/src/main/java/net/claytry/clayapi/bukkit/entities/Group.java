/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.entities;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 19:07                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import java.util.*;

public class Group {
    private int uniqueId;
    private String name;
    private String prefix;
    private int priority;
    private ArrayList<String> serverPermissions;
    private ArrayList<String> bungeePermissions;
    private ArrayList<String> inherits;

    public int getUniqueId() {
        return this.uniqueId;
    }

    public void setUniqueId(final int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public ArrayList<String> getServerPermissions() {
        return this.serverPermissions;
    }

    public void setServerPermissions(final ArrayList<String> serverPermissions) {
        this.serverPermissions = serverPermissions;
    }

    public ArrayList<String> getBungeePermissions() {
        return this.bungeePermissions;
    }

    public void setBungeePermissions(final ArrayList<String> bungeePermissions) {
        this.bungeePermissions = bungeePermissions;
    }

    public ArrayList<String> getInherits() {
        return this.inherits;
    }

    public void setInherits(final ArrayList<String> inherits) {
        this.inherits = inherits;
    }
}