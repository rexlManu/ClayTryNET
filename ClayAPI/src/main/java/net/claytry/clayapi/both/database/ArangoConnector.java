/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.both.database;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import lombok.Getter;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 17:46                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class ArangoConnector {

    @Getter
    private ArangoDB arangoDB;
    @Getter
    private ArangoDatabase database;

    public ArangoConnector() {
    }

    public void connect() {
        this.arangoDB = new ArangoDB.Builder().host("lol", 8529).user("claytry").password("a").build();
        this.database = arangoDB.db("claytry");
    }

    public void disconnect() {
    }

}
