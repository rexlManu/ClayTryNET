/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi;

import lombok.Getter;
import net.claytry.clayapi.both.database.ArangoConnector;
import net.claytry.clayapi.module.ModuleManager;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 14:28                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen,   Zustimmung von Emmanuel Lampe.
 ******************************************************************************************/

public class ClayAPI {

    @Getter
    private static ClayAPI instance;
    @Getter
    private ModuleManager moduleManager;
    @Getter
    private ArangoConnector arangoConnector;

    public void onEnable() {
        instance = this;

        this.arangoConnector = new ArangoConnector();
        this.arangoConnector.connect();
        this.moduleManager = new ModuleManager();
    }

    public void onDisable() {
        this.moduleManager.disableAllModule();
    }

}
