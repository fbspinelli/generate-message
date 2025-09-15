package notificaja.com.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static notificaja.com.utils.StringUtils.formatPhoneNumber;

@Data
public class Client {

    private String name;
    private LocalDateTime dueDate;
    private BigDecimal invoiceAmount;
    private String phoneNumber;
    private String account;

    public String getDueDate() {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.dueDate.format(outputFormatter);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = formatPhoneNumber(phoneNumber);
    }

    public String getInvoiceAmount() {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(invoiceAmount);
    }
}
