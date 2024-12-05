import javafx.application.Platform;

public class YellowBallUpdater implements Runnable {
    private final Earth earth;
    private final Aeroport nearestAirport;
    private final World world;

    public YellowBallUpdater(Earth earth, Aeroport nearestAirport, World world) {
        this.earth = earth;
        this.nearestAirport = nearestAirport;
        this.world = world;
    }

    @Override
    public void run() {
        try {
            // Récupération des données de l'API
            ApiFlightManager apiManager = new ApiFlightManager();
            String jsonResponse = apiManager.fetchFlightData(nearestAirport);

            System.out.println("Réponse JSON brute : " + jsonResponse);

            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                // Analyse et remplissage des vols dynamiquement
                JsonFlightFiller filler = new JsonFlightFiller("", world);
                filler.fillFlightsFromApi(jsonResponse);

                // Mise à jour de l'interface graphique sur le thread principal
                Platform.runLater(() -> {
                    earth.clearYellowBalls(); // Nettoyer les anciens points jaunes

                    // Affichage des boules jaunes pour les aéroports de départ
                    for (Flight flight : filler.getFlights()) {
                        System.out.println("Traitement du vol : " + flight);
                        Aeroport departure = world.findByCode(flight.getDepartureIATA());
                        if (departure != null) {
                            System.out.println("Aéroport trouvé pour le code IATA " + flight.getDepartureIATA() + " : " + departure);
                            earth.displayYellowBall(departure);
                        } else {
                            System.out.println("Aucun aéroport trouvé pour le code IATA : " + flight.getDepartureIATA());
                        }
                    }
                });
            } else {
                System.out.println("Erreur : Impossible de récupérer les données de l'API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}