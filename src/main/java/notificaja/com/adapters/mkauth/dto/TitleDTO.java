package notificaja.com.adapters.mkauth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TitleDTO {
    private String uuid;
    private String titulo;
    private BigDecimal valor;
    private String valorpag;
    private String datavenc;
    private String nossonum;
    private String linhadig;
    private String nome;
    private String login;

    @JsonProperty("cpf_cnpj")
    private String cpfCnpj;

    private String tipo;
    private String email;
    private String endereco;
    private String numero;
    private String bairro;
    private String complemento;
    private String cidade;
    private String estado;
    private String cep;
    private String status;

    @JsonProperty("cli_ativado")
    private String cliAtivado;

    @JsonProperty("uuid_lanc")
    private String uuidLanc;

    private String pix;

    @JsonProperty("pix_link")
    private String pixLink;

    @JsonProperty("pix_qr")
    private String pixQr;
}
