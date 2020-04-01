/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.module;

import lombok.Getter;
import net.claytry.clayapi.bukkit.utils.JsonConfig;

import java.io.File;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 21:14                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class ModuleConfig {

    private String moduleName;
    @Getter
    private JsonConfig jsonConfig;

    public ModuleConfig(String moduleName) {
        this.moduleName = moduleName;
        final File datafolder = new File("./modules/configs/");
        if(!datafolder.exists()){
            datafolder.mkdir();
        }
        this.jsonConfig = new JsonConfig(moduleName, datafolder);
    }
}
