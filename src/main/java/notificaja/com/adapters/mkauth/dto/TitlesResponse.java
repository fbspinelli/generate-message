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
public class TitlesResponse extends PageableResponse{

    @JsonProperty("titulos")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<TitleDTO> titles = new ArrayList<>();
}
