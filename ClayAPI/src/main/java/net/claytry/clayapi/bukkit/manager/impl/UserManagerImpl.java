/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.manager.impl;

import net.claytry.clayapi.both.user.User;
import net.claytry.clayapi.bukkit.manager.UserManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 19:20                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class UserManagerImpl implements UserManager {

    private Set<User> onlineUsers;

    public UserManagerImpl() {
        this.onlineUsers = new HashSet<>();
    }

    @Override
    public User getUser(UUID uuid) {

        return null;
    }

    @Override
    public User getUser(String name) {
        return null;
    }

    @Override
    public Set<User> getOnlineUsers() {
        return onlineUsers;
    }
}
