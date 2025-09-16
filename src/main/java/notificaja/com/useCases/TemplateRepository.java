package notificaja.com.useCases;

import notificaja.com.adapters.dynamoDb.dto.Template;

import java.util.List;

public interface TemplateRepository {
    List<Template> findByClientId(String clientId);
}
