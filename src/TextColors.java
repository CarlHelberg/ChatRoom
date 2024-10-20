import Color.Color;

public class TextColors {

    // Method to colorize text using the Color enum
    public static String colorize(String colorName, String text) {
        try {
            // Convert the string to a Color enum constant
            Color color = Color.valueOf("COLOR_" + colorName.toUpperCase());
            // Use the colorize method from the Color enum
            return Color.colorize(color, text);
        } catch (IllegalArgumentException e) {
            // If the color does not exist, return the original text
            System.out.println("Invalid color name: " + colorName + ". " + text);
        }
        return colorName;
    }
}