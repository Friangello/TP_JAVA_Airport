import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Interface extends Application {

    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final double zoomSpeed = 10.0;

    // Objet World pour gérer les aéroports
    private World world;

    @Override
    public void start(Stage primaryStage) {
        // Charger la liste des aéroports depuis le CSV
        world = new World("Data/airport-codes_no_comma.csv");

        // Création d'un objet Earth
        Earth earth = new Earth();

        // Création de la scène avec un objet Earth
        Scene scene = new Scene(earth, 800, 600, true);

        // Configuration de la caméra perspective
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000); // Position initiale de la caméra
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(35);

        scene.setCamera(camera);

        // Ajout de gestionnaires d'événements pour la souris
        scene.addEventHandler(MouseEvent.ANY, event -> {
            // Gestion du clic gauche
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED && event.getButton() == MouseButton.PRIMARY) {
                mouseOldX = event.getSceneX();
                mouseOldY = event.getSceneY();
                System.out.println("Clic gauche détecté : (" + event.getSceneX() + ", " + event.getSceneY() + ")");
            }

            // Déplacement avec la souris pour zoomer/dézoomer
            if (event.getEventType() == MouseEvent.MOUSE_DRAGGED && event.getButton() == MouseButton.PRIMARY) {
                mousePosX = event.getSceneX();
                mousePosY = event.getSceneY();

                // Zoom ou dézoom avec le mouvement vertical
                double deltaY = mousePosY - mouseOldY;

                camera.setTranslateZ(camera.getTranslateZ() + deltaY * zoomSpeed);
                System.out.println("Zoom modifié : " + camera.getTranslateZ());

                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
            }

            // Gestion du clic droit
            if (event.getButton() == MouseButton.SECONDARY && event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                PickResult pickResult = event.getPickResult();

                // Vérifier si le clic a eu lieu sur un objet
                if (pickResult != null && pickResult.getIntersectedNode() != null) {
                    // Récupérer les coordonnées de texture
                    Point2D textureCoordinates = pickResult.getIntersectedTexCoord();

                    if (textureCoordinates != null) {
                        double x = textureCoordinates.getX();
                        double y = textureCoordinates.getY();

                        // Conversion des coordonnées en longitude et latitude
                        double longitude = 360 * (x - 0.5);
                        double latitude = 180 * (0.5 - y);

                        System.out.println("Clic droit - Latitude : " + latitude + ", Longitude : " + longitude);

                        // Recherche de l'aéroport le plus proche
                        Aeroport nearestAirport = world.findNearest(longitude, latitude);
                        if (nearestAirport != null) {
                            // Affichage dans la console
                            System.out.println("Aéroport le plus proche : " + nearestAirport);

                            // Afficher une sphère rouge sur l'aéroport
                            earth.displayRedSphere(nearestAirport);

                            // Lancer un thread pour exécuter YellowBallUpdater
                            Thread updaterThread = new Thread(new YellowBallUpdater(earth, nearestAirport, world));
                            updaterThread.start();
                        } else {
                            System.out.println("Aucun aéroport trouvé à proximité.");
                        }
                    } else {
                        System.out.println("Erreur : Impossible de récupérer les coordonnées de texture.");
                    }
                } else {
                    System.out.println("Erreur : Aucun point d'intersection trouvé.");
                }
            }
        });

        // Configure la fenêtre principale (Stage)
        primaryStage.setTitle("Affichage de la Terre avec interactions");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}