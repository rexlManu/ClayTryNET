/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.bukkit.utils;

import org.bukkit.ChatColor;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 19.07.2018 / 08:27                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public class ProgressBar {

    private int current;
    private int max;
    private String symbol;
    private String completedColor;
    private String notCompletedColor;
    private StringBuilder builder;
    private int totalBars;

    public ProgressBar() {
    }

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(final int current) {
        this.current = current;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(final int max) {
        this.max = max;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(final String symbol) {
        this.symbol = symbol;
    }

    public String getCompletedColor() {
        return this.completedColor;
    }

    public void setCompletedColor(final String completedColor) {
        this.completedColor = completedColor;
    }

    public String getNotCompletedColor() {
        return this.notCompletedColor;
    }

    public void setNotCompletedColor(final String notCompletedColor) {
        this.notCompletedColor = notCompletedColor;
    }

    public StringBuilder getBuilder() {
        return this.builder;
    }

    public void setBuilder(final StringBuilder builder) {
        this.builder = builder;
    }

    public int getTotalBars() {
        return this.totalBars;
    }

    public void setTotalBars(final int totalBars) {
        this.totalBars = totalBars;
    }

    public String asString() {
        return ProgressBar.getProgressBar(this.current, this.max, this.totalBars, this.symbol, this.completedColor, this.notCompletedColor);
    }

    public String build() {
        return ProgressBar.getProgressBar(this.current, this.max, this.totalBars, this.symbol, this.completedColor, this.notCompletedColor);
    }

    public static String getProgressBar(final int current, final int max, final int totalBars, final String symbol, final String completedColor, final String notCompletedColor) {
        final float percent = (float) current / max;

        final int progressBars = (int) ((int) totalBars * percent);

        final int leftOver = (totalBars - progressBars);

        final StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.translateAlternateColorCodes('&', completedColor));
        for (int i = 0; i < progressBars; i++) {
            sb.append(symbol);
        }
        sb.append(ChatColor.translateAlternateColorCodes('&', notCompletedColor));
        for (int i = 0; i < leftOver; i++) {
            sb.append(symbol);
        }
        return sb.toString();
    }
}