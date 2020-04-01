/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 19.07.2018 / 08:16                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class TextComponentBuilder {

    private final String text;
    private String hover;
    private String click;
    private ClickEvent.Action action;

    public TextComponentBuilder(final String text) {
        this.text = text;
    }

    public TextComponentBuilder addHover(final String hover) {
        this.hover = hover;
        return this;
    }

    public TextComponentBuilder addClickEvent(final ClickEvent.Action clickEventAction, final String value) {
        this.action = clickEventAction;
        this.click = value;
        return this;
    }

    public TextComponent build() {
        final TextComponent textComponent = new TextComponent();
        textComponent.setText(this.text);
        if (this.hover != null) {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.hover).create()));
        }
        if (this.click != null && (this.action != null)) {
            textComponent.setClickEvent(new ClickEvent(this.action, this.click));
        }
        return textComponent;
    }

    public enum ClickEventType {
        RUN_COMMAND, SUGGEST_COMMAND, OPEN_URL
    }
}