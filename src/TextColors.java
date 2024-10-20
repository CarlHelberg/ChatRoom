import Color.Color;

public class TextColors {

    // Method to colorize text using the Color enum
    public static String colorize(String colorName, String text) {
        try {
            // Convert the string to a Color enum constant
            Color color = Color.valueOf("COLOR_" + colorName.toUpperCase()); // Get the Color enum based on the color name
            // Use the colorize method from the Color enum
            return Color.colorize(color, text); // Return the colorized text
        } catch (IllegalArgumentException e) { // Catch if the color does not exist
            // If the color does not exist, return the original text
            System.out.println("Invalid color name: " + colorName + ". " + text); // Print error message
        }
        return colorName; // Return the color name if an error occurred
    }
}
