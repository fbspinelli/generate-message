package notificaja.com.adapters.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import notificaja.com.adapters.mkauth.dto.ClientDTO;
import notificaja.com.entities.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class MkAuthMapper {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Client toClient(ClientDTO clientDTO) {

        Client client = new Client();
        client.setName(clientDTO.getTitle().getNome());
        client.setDueDate(LocalDateTime.parse(clientDTO.getTitle().getDatavenc(), formatter));
        client.setInvoiceAmount(clientDTO.getTitle().getValor());
        String phoneNumber = clientDTO.getCelular() != null ? clientDTO.getCelular() : clientDTO.getCelular2();
        client.setPhoneNumber(phoneNumber);
        return client;
    }
}
