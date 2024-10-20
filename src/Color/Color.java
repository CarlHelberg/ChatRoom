package Color;

public enum Color {
    COLOR_RESET("\u001b[0m"),
    COLOR_BLACK("\u001b[30m"),
    COLOR_RED("\u001b[31m"),
    COLOR_GREEN("\u001b[32m"),
    COLOR_YELLOW("\u001b[33m"),
    COLOR_BLUE("\u001b[34m"),
    COLOR_PURPLE("\u001b[35m"),
    COLOR_CYAN("\u001b[36m"),
    COLOR_WHITE("\u001b[37m");

    public final String code;

    private Color(String var3) {
        this.code = var3;
    }

    public static String colorize(Color var0, String var1) {
        return var0.code + var1 + COLOR_RESET.code;
    }
}
