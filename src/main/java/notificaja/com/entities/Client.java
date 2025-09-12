package notificaja.com.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Client {

    private String name;
    private String dueDate;
    private BigDecimal invoiceAmount;
    private String phoneNumber;
    private String account;
}
