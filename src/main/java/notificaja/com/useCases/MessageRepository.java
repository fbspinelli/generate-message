package notificaja.com.useCases;

import notificaja.com.adapters.dynamoDb.dto.Message;

public interface MessageRepository {

    void put(Message message);
}
