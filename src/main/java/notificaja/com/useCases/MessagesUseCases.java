package notificaja.com.useCases;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import notificaja.com.adapters.dynamoDb.dto.DynamoDbRepository;
import notificaja.com.adapters.dynamoDb.dto.Template;
import notificaja.com.entities.Client;

import java.time.LocalDate;
import java.util.List;


@ApplicationScoped
public class MessagesUseCases {

    @Inject
    CustomerDataProvider customerDataProvider;

    @Inject
    private DynamoDbRepository dynamoDB;

    void onStart(@Observes StartupEvent ev) {
        // lembrar de verificar na sexta e sabado se tem restrição de envio para sab e dom, se tiver adiantar exemplo
        // dia 10
        // cai em um domingo enviar
        // dia 09 sabado, se tiver restrição de envio sabado envia sexta dia 8
        System.out.println("Vou iniciar");
        List<Template> templates = dynamoDB.findAll();
        templates.forEach(template -> System.out.printf(template.toString()));
//        List<Client> clients = customerDataProvider.findClientsByDueDate(LocalDate.now());
//        System.out.println("Clients lenght: " + clients.size());
        System.out.println("Finalizer");
    }
}
