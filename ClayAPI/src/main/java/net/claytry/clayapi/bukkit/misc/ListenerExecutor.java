/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.misc;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 28.07.2018 / 18:35                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

import org.bukkit.plugin.*;
import org.bukkit.event.*;

public class ListenerExecutor implements EventExecutor
{
    private final Class<? extends Event> cls;
    private final EventListener listener;
    private boolean disable;

    public ListenerExecutor(final Class<? extends Event> cls, final EventListener listener) {
        this.cls = cls;
        this.listener = listener;
    }

    public void execute(final Listener ll, final Event event) throws EventException {
        if (this.disable) {
            event.getHandlers().unregister(ll);
            return;
        }
        if (this.cls.equals(event.getClass())) {
            this.listener.on(event);
        }
    }

    public void setDisable(final boolean disable) {
        this.disable = disable;
    }

    public EventListener getListener() {
        return this.listener;
    }
}

