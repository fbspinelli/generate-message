package notificaja.com.adapters.dynamoDb.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Template {
    private String id;
    private String clientId;
    private List<String> days;
    private String daysOffset;
    private Integer displayOrder;
    private String message;
    private String provider;
    private String type;
}