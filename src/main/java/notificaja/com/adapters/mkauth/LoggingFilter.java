package notificaja.com.adapters.mkauth;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) {
        //System.out.println("URL final: " + requestContext.getUri().toString());
        //System.out.println("Method: " + requestContext.getMethod());
    }
}
