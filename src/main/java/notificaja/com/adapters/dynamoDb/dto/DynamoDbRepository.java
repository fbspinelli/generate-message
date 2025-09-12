package notificaja.com.adapters.dynamoDb.dto;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DynamoDbRepository {
    @Inject
    DynamoDbClient dynamoDbClient;

    @Inject
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public List<Template> findAll() {
        DynamoDbTable<Template> table =
                dynamoDbEnhancedClient.table("template", TableSchema.fromBean(Template.class));

        PageIterable<Template> results = table.scan(ScanEnhancedRequest.builder().build());

        List<Template> templates = new ArrayList<>();
        results.items().forEach(templates::add);

        return templates;
    }
}
