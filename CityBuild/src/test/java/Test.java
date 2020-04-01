import net.claytry.citybuild.utils.Utils;

public final class Test {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            System.out.println(i);
        }

        System.out.println(Utils.formatDouble(25.5674564566).replace(",", "."));
    }
}
