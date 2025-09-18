package notificaja.com.adapters.lambda;

import jakarta.enterprise.context.ApplicationScoped;
import notificaja.com.useCases.ProcessMessage;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@ApplicationScoped
public class LambdaService implements ProcessMessage {

    private final LambdaClient lambdaClient;
    private final String functionName = "start-task-process-message";

    public LambdaService() {
        this.lambdaClient = LambdaClient.builder()
                .region(Region.SA_EAST_1) // São Paulo
                .build();
    }

    public void runProcessMessage(String clientId) {
        String payload = String.format("{\"clientId\":\"%s\"}", clientId);

        InvokeRequest request = InvokeRequest.builder()
                .functionName(functionName)
                .invocationType(InvocationType.REQUEST_RESPONSE)
                .payload(SdkBytes.fromUtf8String(payload))
                .build();

        try {
            InvokeResponse response = lambdaClient.invoke(request);
            System.out.println("Lambda invocada. StatusCode: " + response.statusCode());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lambdaClient.close();
        }
    }
}
