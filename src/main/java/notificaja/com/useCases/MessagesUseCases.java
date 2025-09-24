package notificaja.com.useCases;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import notificaja.com.adapters.dynamoDb.dto.Message;
import notificaja.com.adapters.dynamoDb.dto.Template;
import notificaja.com.entities.Client;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static notificaja.com.utils.DateUtils.getLocalDateSp;


@ApplicationScoped
public class MessagesUseCases {

    @Inject
    CustomerDataProvider customerDataProvider;

    @Inject
    private TemplateRepository templateRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    ProcessMessage processMessage;

    void onStart(@Observes StartupEvent ev) {
//        // lembrar de verificar na sexta e sabado se tem restrição de envio para sab e dom, se tiver adiantar exemplo
//        // dia 10
//        // cai em um domingo enviar
//        // dia 09 sabado, se tiver restrição de envio sabado envia sexta dia 8
        Thread t = new Thread(() -> {
            try {
                System.out.println("Vou iniciar");
                Thread.sleep(500);
                LocalDate today = getLocalDateSp();
                System.out.println("Today: " + today);
                //futuramente pegar todos os templates, os que não tiverem interface implementadas não gera msg
                List<Template> templates = templateRepository.findByClientId("d7151c57-6256-48ee-9f70-50227d9e4489");

                templates = (templates);

                templates.forEach(template -> {
                    LocalDate dueDate = today.plusDays(template.getDaysOffset());
                    List<Client> clients = customerDataProvider.findClientsByDueDate(dueDate);
                    clients.forEach(client -> {
                        String textMessage = replacePlaceholders(template.getMessage(), client);
                        Message message = new Message(UUID.randomUUID().toString(), template.getClientId(),
                                client.getPhoneNumber(), LocalDateTime.now().toString(), "null", template.getId(),
                                textMessage);
                        System.out.println("Messagem gerada: " + message);
                        messageRepository.put(message);
                        System.out.println("Finalizado.");
                    });
                });
                //processMessage.runProcessMessage("d7151c57-6256-48ee-9f70-50227d9e4489");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    private List<Template> removedTemplatesWithRestriction(List<Template> templates) {
        DayOfWeek dayOfWeek = getLocalDateSp().getDayOfWeek();
        String today = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return templates.stream().filter(template ->
                template.getDays().contains(today)
        ).toList();
    }

    private List<Template> enviarMsgSabEDomingo(final List<Template> templates) {
        List<Template> resultado = new ArrayList<>();
//
//        for (Template t : templates) {
//            resultado.add(t); // mantém o original
//
//            List<String> dias = t.getDays();
//            int offsetOriginal = t.getDaysOffset();
//
//            // se o template NÃO inclui sábado/domingo
//            if (!dias.contains("Saturday") && !dias.contains("Sunday")) {
//                // cópia para sábado
//                Template sabado = new Template(t);
//                sabado.setDaysOffset(offsetOriginal + 1);
//                resultado.add(sabado);
//
//                // cópia para domingo
//                Template domingo = new Template(t);
//                domingo.setDaysOffset(offsetOriginal + 2);
//                resultado.add(domingo);
//            }
//        }
//
//        return resultado;
        //eu na sexta feira
        //pegar a lista de templates, filtrar as linhas que não contain Saturday e Sunday array tamanho 5
        List<Template> templatesOfWekend = templates.stream().filter(template ->
                !(template.getDays().contains(DayOfWeek.SATURDAY.getDisplayName(TextStyle.FULL, Locale.ENGLISH)) &&
                template.getDays().contains(DayOfWeek.SUNDAY.getDisplayName(TextStyle.FULL, Locale.ENGLISH))))
                .toList();
        //mexer no offset as de sabado soma 1 e de domingo soma 2 do offset
        templatesOfWekend.forEach(template -> template.setDaysOffset(template.getDaysOffset() + 1));
        //offset lembrete 3 + 1 = 4 dias antes adiantou lembrete no offset de atraso -3 +1 = -2 vai cobrar antes atraso
        return null;
    }

    private List<Template> enviarMsgDomingo() {
        //eu no sabado
        //pegar a lista de templates, filtrar as linhas que não contain APENAS o Sunday, array tamanho 6
        //mexer no offset somar 1
        //offset lembrete 3 + 1 = 4 dias antes adiantou lembrete no offset de atraso -3 +1 = -2 vai cobrar antes atraso
        return null;
    }

    private String replacePlaceholders(String text, Client client) {
        text = text.replace("{{nome_do_cliente}}", client.getName());
        text = text.replace("{{data_de_vencimento}}", client.getDueDate());
        text = text.replace("{{valor_da_mensalidade}}", client.getInvoiceAmount());
        return text;
    }
}
