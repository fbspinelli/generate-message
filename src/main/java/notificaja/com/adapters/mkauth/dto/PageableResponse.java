package notificaja.com.adapters.mkauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class PageableResponse {

    @JsonProperty("total_registros")
    private int totalRegistros;

    @JsonProperty("consulta_atual")
    private int consultaAtual;

    @JsonProperty("pagina_atual")
    private int paginaAtual;

    @JsonProperty("total_paginas")
    private int totalPaginas;

    private Text error;

    private String status;

    @JsonProperty("mensagem")
    private String message;

    @Data
    static class Text {
        private String text;
    }
}

