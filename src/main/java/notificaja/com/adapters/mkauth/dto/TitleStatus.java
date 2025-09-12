package notificaja.com.adapters.mkauth.dto;

public enum TitleStatus {
    ABERTO("aberto"),
    VENCIDO("vencido"),
    PAGO("pago");

    private final String status;

    TitleStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
