/*
 * © Copyright - Emmanuel Lampe aka. rexlManu 2018.
 */
package net.claytry.clayapi.both;

/******************************************************************************************
 *    Urheberrechtshinweis                                                       
 *    Copyright © Emmanuel Lampe 2018                                       
 *    Erstellt: 23.05.2018 / 19:42                           
 *
 *    Alle Inhalte dieses Quelltextes sind urheberrechtlich geschützt.                    
 *    Das Urheberrecht liegt, soweit nicht ausdrücklich anders gekennzeichnet,       
 *    bei Emmanuel Lampe. Alle Rechte vorbehalten.                      
 *
 *    Jede Art der Vervielfältigung, Verbreitung, Vermietung, Verleihung,        
 *    öffentlichen Zugänglichmachung oder andere Nutzung           
 *    bedarf der ausdrücklichen, schriftlichen Zustimmung von Emmanuel Lampe.  
 ******************************************************************************************/

public final class Message {

    //&8» &2&lC&alayTry &8┃ &cDazu hast du keine Berechtigung&8.

    public static String generatePrefix(final String prefixName) {
        return "§8» " + split(prefixName) + " §8┃ §r";
    }

    private static String split(String prefixName) {
        final String[] data = prefixName.split("");
        final StringBuilder builder = new StringBuilder();
        data[0] = "§2§l" + data[0] + "§a";
        for (String datum : data) {
            builder.append(datum);
        }
        return builder.toString();
    }

    public static String getPrefix() {
        return "§8» §2§lC§alayTry §8┃ §r";
    }

    public static String offlinePlayer(String input) {
        return getPrefix()+"§7Der Spieler §a" + input + "§7 ist nicht online§8.";
    }

    public static String consoleMessage() {
        return getPrefix()+"§7Bitte führe diesen Command nur als Spieler aus§8.";
    }

    public static String permissionMessage() {
        return getPrefix()+"§cDazu hast du keine Berechtigung§8.";
    }
}
