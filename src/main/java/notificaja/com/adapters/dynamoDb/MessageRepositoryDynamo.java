package notificaja.com.adapters.dynamoDb;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import notificaja.com.adapters.dynamoDb.dto.Message;
import notificaja.com.useCases.MessageRepository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@ApplicationScoped
@Slf4j
public class MessageRepositoryDynamo implements MessageRepository {

    private static final TableSchema<Message> MESSAGE_SCHEMA =
            TableSchema.fromBean(Message.class);

    private final DynamoDbTable<Message> messageTable;

    public MessageRepositoryDynamo() {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.create();
        this.messageTable = enhancedClient.table("messages", MESSAGE_SCHEMA);
    }

    public void put(Message message) {
        log.info("Save message");
        messageTable.putItem(message);
    }
}
