package notificaja.com.adapters.dynamoDb;


import jakarta.enterprise.context.ApplicationScoped;
import notificaja.com.adapters.dynamoDb.dto.MessageTemplate;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TemplateRepository {

    private static final TableSchema<MessageTemplate> MESSAGE_TEMPLATE_SCHEMA =
            TableSchema.fromBean(MessageTemplate.class);

    private final  DynamoDbTable<MessageTemplate> templateTable;


    public TemplateRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.create();
        this.templateTable = enhancedClient.table("template", MESSAGE_TEMPLATE_SCHEMA);
    }

    public MessageTemplate findById(String id) {
        MessageTemplate messageTemplate = templateTable.getItem(Key.builder().partitionValue(id).build());
        System.out.println(messageTemplate.toString());
        return messageTemplate;
    }

    public List<MessageTemplate> findAll() {
        List<MessageTemplate> results = new ArrayList<>();
        templateTable.scan().items().forEach(results::add);
        return results;
    }

    public List<MessageTemplate> findByClientId(String clientId) {
        List<MessageTemplate> results = new ArrayList<>();

        SdkIterable<Page<MessageTemplate>> pages =
                templateTable
                        .index("clientId-displayOrder-index")
                        .query(r -> r.queryConditional(
                                QueryConditional.keyEqualTo(k -> k.partitionValue(clientId))
                        ));

        for (Page<MessageTemplate> page : pages) {
            results.addAll(page.items());
        }

        return results;
    }
}
