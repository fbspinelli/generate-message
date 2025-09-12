package notificaja.com.adapters.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import notificaja.com.adapters.mkauth.dto.ClientDTO;
import notificaja.com.entities.Client;

@ApplicationScoped
public class MkAuthMapper {

    public Client toClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getTitle().getNome());
        client.setDueDate(clientDTO.getTitle().getDatavenc());
        client.setInvoiceAmount(clientDTO.getTitle().getValor());
        String phoneNumber = clientDTO.getCelular() != null ? clientDTO.getCelular() : clientDTO.getCelular2();
        client.setPhoneNumber(phoneNumber);
        return client;
    }
}
