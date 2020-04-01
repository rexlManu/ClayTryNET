/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.both.user;

import com.arangodb.ArangoCollection;
import com.arangodb.entity.BaseDocument;
import net.claytry.clayapi.ClayAPI;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 17:42                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class UserDocument {

    private static ArangoCollection collection = ClayAPI.getInstance().getArangoConnector().getDatabase().collection("players");

    private User user;

    public UserDocument(User user) {
        super();
        this.user = user;
    }

    public BaseDocument getDocument() {
        return collection.getDocument(user.getUUID().toString(), BaseDocument.class);
    }

    public void updateDocument(BaseDocument baseDocument) {
        collection.updateDocument(user.getUUID().toString(), baseDocument);
    }

}
