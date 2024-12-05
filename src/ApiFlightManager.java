import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class ApiFlightManager {

    private static final String BASE_URL = "https://api.aviationstack.com/v1/flights";
    private static final String ACCESS_KEY = "e21dc4e994031f77d179e34ca6718de6";

    public String fetchFlightData(Aeroport arrival) {
        try {
            String url = BASE_URL + "?access_key=" + ACCESS_KEY+"&arr_iata="+arrival.getCodeIATA();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonObject parseJson(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
