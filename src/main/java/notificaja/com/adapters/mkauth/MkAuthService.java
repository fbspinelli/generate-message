package notificaja.com.adapters.mkauth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import notificaja.com.adapters.mkauth.dto.ClientDTO;
import notificaja.com.adapters.mkauth.dto.ClientsResponse;
import notificaja.com.adapters.mkauth.dto.TitleDTO;
import notificaja.com.adapters.mkauth.dto.TitleStatus;
import notificaja.com.adapters.mkauth.dto.TitlesResponse;
import notificaja.com.adapters.exceptions.TokenException;
import notificaja.com.adapters.mapper.MkAuthMapper;
import notificaja.com.entities.Client;
import notificaja.com.useCases.CustomerDataProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MkAuthService implements CustomerDataProvider {

    @Inject
    @RestClient
    private MkAuthClient mkAuthClient;

    @ConfigProperty(name = "mk-auth.client.id")
    private String clientId;

    @ConfigProperty(name = "mk-auth.client.secret")
    private String clientSecret;

    @Inject
    private MkAuthMapper mkAuthMapper;

    private static final ObjectMapper mapper = new ObjectMapper();

    private String token;

    @Override
    public List<Client> findClientsByDueDate(LocalDate date) {
        List<TitleDTO> titles;
        if(date.isBefore(getLocalDateSp())) {
            titles = findExpiredTitles(date);
        } else if (date.equals(getLocalDateSp())) {
            titles = findTitlesDueToday();
        } else {
            titles = findOpenTitles(date);
        }
        List<ClientDTO> clientDTOS = enrichCustomerData(titles);
        return clientDTOS.stream().map(clientDTO -> mkAuthMapper.toClient(clientDTO)).toList();
    }

    public synchronized String getToken() {
        if (token == null || expiredToken()) {
            token = mkAuthClient.getToken(getAuthorizationHeader());
        }
        System.out.println(token);
        return token;
    }

    private boolean expiredToken() {
        try {
            String tokenPayloadBase64 = token.split("\\.")[1];
            byte[] decodedBytes = Base64.getDecoder().decode(tokenPayloadBase64);
            String tokenPayload = new String(decodedBytes, StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> tokenPayloadMap = mapper.readValue(tokenPayload,
                    new TypeReference<Map<String, Object>>() {
                    });

            long exp = ((Number) tokenPayloadMap.get("exp")).longValue();
            long now = Instant.now().getEpochSecond();
            return now > (exp - 60);
        } catch (Exception ex) {
            throw new TokenException("Error validating token expiration", ex);
        }
    }

    private String getAuthorizationHeader() {
        String basicAuth = clientId + ":" + clientSecret;
        String authBase64 = Base64.getEncoder().encodeToString(basicAuth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + authBase64;
    }

    private List<TitleDTO> findOpenTitles(LocalDate date) {
        String paramsDues = buildParams(1, TitleStatus.ABERTO, date);
        TitlesResponse openTitles = toObject(mkAuthClient.findTitles(paramsDues), TitlesResponse.class);
        return openTitles.getTitles();
    }

    private List<TitleDTO> findTitlesDueToday() {

        String paramsDues = buildParams(1, TitleStatus.VENCIDO, getLocalDateSp());
        TitlesResponse titlesExpired = toObject(mkAuthClient.findTitles(paramsDues), TitlesResponse.class);

        String paramsOpen = buildParams(1, TitleStatus.ABERTO, getLocalDateSp());
        TitlesResponse titlesOpen = toObject(mkAuthClient.findTitles(paramsOpen), TitlesResponse.class);

        List<TitleDTO> titles = new ArrayList<>();
        titles.addAll(titlesExpired.getTitles());
        titles.addAll(titlesOpen.getTitles());

        return titles;
    }

    private List<TitleDTO> findExpiredTitles(LocalDate date) {
        String paramsDues = buildParams(1, TitleStatus.VENCIDO, date);
        TitlesResponse titlesExpired = toObject(mkAuthClient.findTitles(paramsDues), TitlesResponse.class);
        return titlesExpired.getTitles();
    }

    private List<ClientDTO> enrichCustomerData(List<TitleDTO> titles) {
        List<ClientDTO> clients = titles.stream().map(title -> {
            ClientsResponse clientsResponse = toObject(mkAuthClient.findClients("login=" + title.getLogin()),
                    ClientsResponse.class);
            ClientDTO clientDTO = clientsResponse.getClients().getFirst();
            clientDTO.setTitle(title);
            return clientDTO;
        }).toList();
        return clients;
    }

    private static LocalDate getLocalDateSp() {
        ZoneId spZone = ZoneId.of("America/Sao_Paulo");
        return LocalDate.now(spZone);
    }

    private static String buildParams(int page, TitleStatus status, LocalDate dueDate) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("pagina=" + page + "&");
        stringBuilder.append("status=" + status.getStatus() + "&");
        stringBuilder.append("datavenc=" + dueDate.toString() + "&");
        stringBuilder.append("deltitulo=0&");
        stringBuilder.append("cli_ativado=s&");
        return stringBuilder.toString();
    }

    public static <T> T toObject (String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao desserializar JSON: "+ json +" para " + type.getSimpleName(), e);
        }
    }
}
