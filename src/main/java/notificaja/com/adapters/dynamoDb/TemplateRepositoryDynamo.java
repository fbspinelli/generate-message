package notificaja.com.adapters.dynamoDb;


import jakarta.enterprise.context.ApplicationScoped;
import notificaja.com.adapters.dynamoDb.dto.Template;
import notificaja.com.useCases.TemplateRepository;
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
public class TemplateRepositoryDynamo implements TemplateRepository {

    private static final TableSchema<Template> MESSAGE_TEMPLATE_SCHEMA =
            TableSchema.fromBean(Template.class);

    private final  DynamoDbTable<Template> templateTable;


    public TemplateRepositoryDynamo() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.create();
        this.templateTable = enhancedClient.table("template", MESSAGE_TEMPLATE_SCHEMA);
    }

    public Template findById(String id) {
        Template template = templateTable.getItem(Key.builder().partitionValue(id).build());
        System.out.println(template.toString());
        return template;
    }

    public List<Template> findAll() {
        List<Template> results = new ArrayList<>();
        templateTable.scan().items().forEach(results::add);
        return results;
    }

    public List<Template> findByClientId(String clientId) {
        List<Template> results = new ArrayList<>();

        SdkIterable<Page<Template>> pages =
                templateTable
                        .index("clientId-displayOrder-index")
                        .query(r -> r.queryConditional(
                                QueryConditional.keyEqualTo(k -> k.partitionValue(clientId))
                        ));

        for (Page<Template> page : pages) {
            results.addAll(page.items());
        }

        return results;
    }
}
