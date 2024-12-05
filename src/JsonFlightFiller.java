import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JsonFlightFiller {
    private ArrayList<Flight> flights = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public JsonFlightFiller(String jsonString, World w) {
        flights = new ArrayList<>();
        try {
            InputStream is = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("data");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                try {
                    // Extraire les données de l'API
                    String airLineCode = result.getJsonObject("airline").getString("iata", "UNKNOWN");
                    String airlineName = result.getJsonObject("airline").getString("name", "UNKNOWN");
                    String departureIATA = result.getJsonObject("departure").getString("iata", "UNKNOWN");

                    String departureTimeStr = result.getJsonObject("departure").getString("scheduled", null);
                    String arrivalTimeStr = result.getJsonObject("arrival").getString("scheduled", null);

                    int number = result.getInt("flight_number", 0);

                    // Conversion des dates en LocalDateTime
                    LocalDateTime departureTime = departureTimeStr != null
                            ? LocalDateTime.parse(departureTimeStr, formatter)
                            : null;

                    LocalDateTime arrivalTime = arrivalTimeStr != null
                            ? LocalDateTime.parse(arrivalTimeStr, formatter)
                            : null;

                    // Création d'un objet Flight
                    Flight flight = new Flight(airLineCode, airlineName, departureTime, arrivalTime, number, departureIATA);
                    flights.add(flight);

                } catch (Exception e) {
                    System.out.println("Erreur dans le parsing d'un vol : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur dans la lecture des données JSON : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void fillFlightsFromApi(String jsonResponse) {
        try {
            System.out.println("Parsing du JSON...");
            JsonReader rdr = Json.createReader(new StringReader(jsonResponse));
            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("data");

            if (results != null) {
                for (JsonObject flightData : results.getValuesAs(JsonObject.class)) {
                    try {
                        String airLineCode = flightData.getJsonObject("flight").getString("iata", null);
                        String airlineName = flightData.getJsonObject("airline").getString("name", null);
                        int number = flightData.getJsonObject("flight").getInt("number", 0);
                        String departureIATA = flightData.getJsonObject("departure").getString("iata", null);

                        LocalDateTime departureTime = LocalDateTime.parse(
                                flightData.getJsonObject("departure").getString("scheduled", null), formatter);
                        LocalDateTime arrivalTime = LocalDateTime.parse(
                                flightData.getJsonObject("arrival").getString("scheduled", null), formatter);

                        Flight flight = new Flight(airLineCode, airlineName, departureTime, arrivalTime, number, departureIATA);
                        flights.add(flight);
                        System.out.println("Vol ajouté : " + flight);
                    } catch (Exception e) {
                        System.out.println("Erreur lors du traitement d'un vol : " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Erreur : Pas de données trouvées dans 'data'.");
            }
        } catch (Exception e) {
            System.out.println("Erreur globale lors de l'analyse du JSON : " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Méthode pour afficher les informations de chaque vol
    public void displayFlight() {
        for (Flight flight : flights) {
            System.out.println(flight.toString());
        }
    }
    public ArrayList<Flight> getFlights() {
        return flights;
    }
}
