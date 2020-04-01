/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.module;

import lombok.Getter;
import lombok.Setter;
import net.claytry.clayapi.bukkit.bridge.BukkitPlayerBridge;
import net.claytry.clayapi.bungeecord.bridge.BungeePlayerBridge;

/****************************************************************************************** *    Urheberrechtshinweis
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

public abstract class Module {

    @Getter
    private String moduleName;
    @Getter
    private String version;
    @Getter
    private String author;
    @Getter
    private ModuleType moduleType;
    @Getter
    @Setter
    private boolean async;
    @Getter
    private ModuleConfig moduleConfig;

    public Module(String moduleName, String version, String author, ModuleType moduleType, boolean async) {
        this.moduleName = moduleName;
        this.version = version;
        this.author = author;
        this.moduleType = moduleType;
        this.async = async;
        this.moduleConfig = new ModuleConfig(getModuleName());
    }

    public Module(String moduleName, ModuleType moduleType) {
        this(moduleName, "1.0", "none", moduleType, false);
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
