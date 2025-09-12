package notificaja.com.adapters.mkauth;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "mk-auth")
@RegisterClientHeaders(AuthHeaderFactory.class)
public interface MkAuthClient {

    @GET
    String getToken(@HeaderParam("authorization") String authorization);

    @GET
    @Path("/titulo/listar/{params}")
    String findTitles(@PathParam("params") String params);

    @GET
    @Path("/cliente/listar/{params}")
    String findClients(@PathParam("params") String params);
}
