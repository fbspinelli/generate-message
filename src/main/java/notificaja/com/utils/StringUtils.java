package notificaja.com.utils;

public class StringUtils {

    public static String formatPhoneNumber(String number) {
        String numberStr = number.replaceAll("\\D", "");

        if (numberStr.startsWith("55")) {
            return numberStr;
        } else {
            return "55" + numberStr;
        }
    }
}
