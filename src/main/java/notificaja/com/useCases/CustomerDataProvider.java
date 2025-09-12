package notificaja.com.useCases;

import notificaja.com.entities.Client;

import java.time.LocalDate;
import java.util.List;

public interface CustomerDataProvider {

    //falta lidar com paginação
    List<Client> findClientsByDueDate(LocalDate date);
}
