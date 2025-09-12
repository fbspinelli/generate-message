package notificaja.com.adapters.mkauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientDTO {
    private String login;
    private String celular;
    private String celular2;
    private TitleDTO title;

}
