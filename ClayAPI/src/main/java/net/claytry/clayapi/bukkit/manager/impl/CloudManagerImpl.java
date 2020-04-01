/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.manager.impl;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 18:28                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import de.dytanic.cloudnet.api.*;
import de.dytanic.cloudnet.bridge.CloudServer;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import net.claytry.clayapi.bukkit.manager.CloudManager;

import java.util.*;

public class CloudManagerImpl implements CloudManager
{
    private final CloudAPI api;
    private final CloudServer server;

    public CloudManagerImpl() {
        this.api = CloudAPI.getInstance();
        this.server = CloudServer.getInstance();
    }

    @Override
    public Collection<ServerInfo> getServers(final String group) {
        return this.api.getServers(group);
    }

    @Override
    public ServerInfo getServerInfo(final String serverId) {
        return this.api.getServerInfo(serverId);
    }

    @Override
    public void changeToIngame(final boolean ingame) {
        this.server.changeToIngame();
    }


}
