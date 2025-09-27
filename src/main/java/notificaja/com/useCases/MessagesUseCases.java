package notificaja.com.useCases;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        Thread t = new Thread(() -> {
            try {
                System.out.println("Vou iniciar");
                Thread.sleep(500);
                LocalDate today = getLocalDateSp();
                System.out.println("Today: " + today);
                //futuramente pegar todos os templates, os que não tiverem interface implementadas não gera msg
                List<Template> templates = templateRepository.findByClientId("d7151c57-6256-48ee-9f70-50227d9e4489");

                templates = anticipateWeekendTemplates(templates);

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
                    });
                });
                System.out.println("Finalizado.");
                //processMessage.runProcessMessage("d7151c57-6256-48ee-9f70-50227d9e4489");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        });
        t.start();
    }

    private List<Template> anticipateWeekendTemplates(List<Template> templates) {
        DayOfWeek todayDayOfWeek = getLocalDateSp().getDayOfWeek();
        log.info("Today: {}", todayDayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        List<Template> templatesWithAnticipations = new ArrayList<>();

        if (DayOfWeek.FRIDAY == todayDayOfWeek) {
            log.info("Anticipating templates of friday");
            for (Template template : templates) {
                templatesWithAnticipations.add(template);
                List<String> days = template.getDays();

                if (!days.contains("Saturday") && !days.contains("Sunday")) {
                    templatesWithAnticipations.add(duplicateWithSumOffset(template, 1));
                    templatesWithAnticipations.add(duplicateWithSumOffset(template, 2));
                }
            }
        }
        else if (DayOfWeek.SATURDAY == todayDayOfWeek) {
            log.info("Anticipating templates of saturday");
            for (Template template : templates) {
                templatesWithAnticipations.add(template);
                List<String> days = template.getDays();

                if (days.contains("Saturday") && !days.contains("Sunday")) {
                    templatesWithAnticipations.add(duplicateWithSumOffset(template, 1));
                }
            }
        }
        return templatesWithAnticipations;
    }

    private static Template duplicateWithSumOffset(Template template, int sumOffset) {
        log.info("Template {} será duplicado, com offset de {}", template.getType(), sumOffset);
        Template copy = new Template(template);
        copy.setDaysOffset(template.getDaysOffset() + sumOffset);
        return copy;
    }

    private String replacePlaceholders(String text, Client client) {
        text = text.replace("{{nome_do_cliente}}", client.getName());
        text = text.replace("{{data_de_vencimento}}", client.getDueDate());
        text = text.replace("{{valor_da_mensalidade}}", client.getInvoiceAmount());
        return text;
    }
}
