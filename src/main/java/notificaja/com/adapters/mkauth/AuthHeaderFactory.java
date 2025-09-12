package notificaja.com.adapters.mkauth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

@ApplicationScoped
public class AuthHeaderFactory implements ClientHeadersFactory {

    @Inject
    private MkAuthService mkAuthService;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incoming, MultivaluedMap<String, String> outgoing) {
        if (!outgoing.containsKey("authorization")) {
            outgoing.putSingle("authorization", "Bearer " + mkAuthService.getToken());
        }
        return outgoing;
    }

}
