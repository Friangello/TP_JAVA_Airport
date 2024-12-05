import java.time.LocalDateTime;

public class Flight {
    private String airLineCode;// Code de la compagnie aérienne
    private String airlineName;// Nom complet de la compagnie aérienne
    private LocalDateTime arrivalTime;// Heure d'arrivée prévue
    private LocalDateTime departureTime;// Heure de départ prévue
    private int number;// Numéro du vol
    private String departureIATA; // Code IATA de l'aéroport de départ

    public Flight(String airLineCode, String airlineName, LocalDateTime departureTime, LocalDateTime arrivalTime, int number,String departureIATA) {
        this.airLineCode = airLineCode;
        this.airlineName = airlineName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.number = number;
        this.departureIATA = departureIATA;
    }


    @Override
    public String toString() {
        return "Flight{" +
                "airLineCode='" + airLineCode + '\'' +
                ", airlineName='" + airlineName + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", number=" + number +
                ", departureIATA='" + departureIATA + '\'' +
                '}';
    }

    // Méthode getArrival()
    public LocalDateTime getArrival() {

        return arrivalTime;
    }

    // Méthode getDeparture()
    public LocalDateTime getDeparture() {

        return departureTime;
    }
    public String getDepartureIATA() {

        return departureIATA;
    }

}
