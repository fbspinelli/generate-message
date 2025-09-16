package notificaja.com.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static notificaja.com.utils.DateUtils.getLocalDateSp;

@Data
public class Client {

    private String name;
    private LocalDateTime dueDate;
    private BigDecimal invoiceAmount;
    private String phoneNumber;
    private String account;

    public String getDueDate() {
        LocalDate today = getLocalDateSp();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dueDate = this.dueDate.toLocalDate();
        if (dueDate.equals(today)) {
            return "hoje";
        } else if (dueDate.equals(tomorrow)) {
            return "amanhã";
        } else {
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return this.dueDate.format(outputFormatter);
        }
    }

    //55 55 9671-3236 = 12 não coloca
    //55 11 99671-3236 = 13 não coloca
    // 55 9671-3236 == 10 tem que colocar
    // 55 99671-3236 == 11 tem que colocar
    // 11 96685-8807 == 11 tem que colocar
    public void setPhoneNumber(String phoneNumber) {
        String numberStr = phoneNumber.replaceAll("\\D", "");

        if (numberStr.startsWith("55") && numberStr.length() > 11) {
            this.phoneNumber = numberStr;
        } else {
            this.phoneNumber = "55" + numberStr;
        }
    }

    public String getInvoiceAmount() {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(invoiceAmount);
    }
}
