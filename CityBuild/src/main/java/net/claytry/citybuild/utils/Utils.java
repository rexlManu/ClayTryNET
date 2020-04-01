package net.claytry.citybuild.utils;

public final class Utils {

    public static final String formatDouble(Object input) {
        return String.format("%.2f", input).replace(",", ".");
    }
}
