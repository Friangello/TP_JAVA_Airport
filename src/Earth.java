import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

public class Earth extends Group {
    private static final int EARTH_RADIUS = 300; // Rayon de la Terre en pixels
    private Group earthGroup;
    private final Sphere sphere;
    private final Rotate rotateY;
    private final List<Sphere> yellowSpheres = new ArrayList<>();

    public Earth() {
        // Initialisation du groupe principal
        earthGroup = new Group();
        this.getChildren().add(earthGroup);

        // Création de la sphère représentant la Terre
        sphere = new Sphere(EARTH_RADIUS);

        // Application d'une texture
        PhongMaterial material = new PhongMaterial();
        try {
            Image texture = new Image("file:Data/earth_lights_4800.png");
            if (texture.isError()) {
                System.out.println("Erreur de chargement de la texture !");
            } else {
                material.setDiffuseMap(texture);
                System.out.println("Texture appliquée avec succès !");
            }
        } catch (Exception e) {
            System.out.println("Exception lors du chargement de la texture : " + e.getMessage());
        }
        sphere.setMaterial(material);
        earthGroup.getChildren().add(sphere);

        // Configuration de la rotation
        rotateY = new Rotate(0, Rotate.Y_AXIS);
        earthGroup.getTransforms().add(rotateY);

        // Ajout de l'AnimationTimer pour la rotation
        startRotation();
    }

    // Démarrer la rotation automatique
    private void startRotation() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long time) {
                double angle = (time / 1_000_000_000.0) * (360.0 / 15.0); // Un tour en 15 secondes
                rotateY.setAngle(angle % 360); // Réduit l'angle entre 0 et 360 degrés
            }
        };
        animationTimer.start();
    }

    // Création d'une sphère colorée pour représenter un aéroport
    public Sphere createSphere(Aeroport a, Color color) {
        double latitude = Math.toRadians(a.getLatitude());
        double longitude = Math.toRadians(a.getLongitude());

        // Calcul des coordonnées 3D
        double x = EARTH_RADIUS * Math.cos(latitude) * Math.sin(longitude);
        double y = -EARTH_RADIUS * Math.sin(latitude);
        double z = -EARTH_RADIUS * Math.cos(latitude) * Math.cos(longitude);

        System.out.println("Coordonnées calculées pour " + a.getNom() + ": X=" + x + ", Y=" + y + ", Z=" + z);

        // Création de la sphère
        Sphere sphere = new Sphere(5); // Rayon de la sphère
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color);
        sphere.setMaterial(material);

        sphere.setTranslateX(x);
        sphere.setTranslateY(y);
        sphere.setTranslateZ(z);

        return sphere;
    }

    // Afficher une sphère rouge à un emplacement
    public synchronized void displayRedSphere(Aeroport a) {
        Sphere redSphere = createSphere(a, Color.RED);
        earthGroup.getChildren().add(redSphere);
    }

    public void clearYellowBalls() {
        System.out.println("Nettoyage des sphères jaunes...");
        yellowSpheres.forEach(sphere -> {
            if (earthGroup.getChildren().contains(sphere)) {
                earthGroup.getChildren().remove(sphere);
            }
        });
        yellowSpheres.clear();
        System.out.println("Sphères jaunes nettoyées. Nombre d'enfants dans earthGroup : " + earthGroup.getChildren().size());
    }

    public synchronized void displayYellowBall(Aeroport aeroport) {
        Sphere yellowSphere = createSphere(aeroport, Color.YELLOW);
        earthGroup.getChildren().add(yellowSphere);
        yellowSpheres.add(yellowSphere);
    }
}
