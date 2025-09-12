package notificaja.com.adapters.mkauth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientsResponse extends PageableResponse{

    @JsonProperty("clientes")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ClientDTO> clients = new ArrayList<>();;
}
