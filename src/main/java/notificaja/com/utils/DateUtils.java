package notificaja.com.utils;

import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtils {

    public static LocalDate getLocalDateSp() {
        ZoneId spZone = ZoneId.of("America/Sao_Paulo");
        return LocalDate.now(spZone);
    }
}
