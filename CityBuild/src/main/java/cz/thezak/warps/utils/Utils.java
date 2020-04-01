package cz.thezak.warps.utils;

import cz.thezak.warps.Warps;

public final class Utils {

    public void getWarp(final String w) {
        Warps.plugin.getConfig().set("tempwarp.name", (Object)w);
    }

    public void setItem(final String item) {
        final String w = Warps.plugin.getConfig().getString("tempwarp.name");
        Warps.plugin.getConfig().set("warps." + w + ".item", (Object)item);
        Warps.plugin.saveConfig();
        Warps.plugin.reloadConfig();
    }
}
