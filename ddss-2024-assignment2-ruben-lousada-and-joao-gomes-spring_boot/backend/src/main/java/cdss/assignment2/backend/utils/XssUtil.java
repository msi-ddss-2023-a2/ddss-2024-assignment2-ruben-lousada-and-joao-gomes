package cdss.assignment2.backend.utils;

public class XssUtil {

    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }

        return input.replace("<", "&lt;").replace(">", "&gt;");
    }
}