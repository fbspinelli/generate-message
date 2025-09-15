package notificaja.com.adapters.dynamoDb;

import jakarta.enterprise.context.ApplicationScoped;
import notificaja.com.adapters.dynamoDb.dto.Message;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@ApplicationScoped
public class MessageRepository {

    private static final TableSchema<Message> MESSAGE_SCHEMA =
            TableSchema.fromBean(Message.class);

    private final DynamoDbTable<Message> messageTable;

    public MessageRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.create();
        this.messageTable = enhancedClient.table("messages", MESSAGE_SCHEMA);
    }

    public void put(Message message) {
        messageTable.putItem(message);
    }
}
