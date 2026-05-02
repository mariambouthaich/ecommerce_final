package controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.geometry.Insets;

/**
 * Utilitaire : Notification Toast
 * Affiche un petit message animé en bas de l'écran pendant 2 secondes.
 * Usage : ToastController.show(stage, "✅ Produit ajouté !", "success");
 */
public class ToastController {

    public enum Type { SUCCESS, ERROR, WARNING, INFO }

    // ────────────────────────────────────────────────────────
    // Afficher un toast
    // ────────────────────────────────────────────────────────
    public static void show(Stage ownerStage, String message, Type type) {
        // Créer le label
        Label label = new Label(message);
        label.setStyle(getStyle(type));
        label.setPadding(new Insets(12, 24, 12, 24));

        HBox box = new HBox(label);
        box.setStyle("-fx-background-color: transparent;");

        // Créer une mini-fenêtre flottante
        Stage toast = new Stage();
        toast.initOwner(ownerStage);
        toast.initStyle(StageStyle.TRANSPARENT);
        toast.setAlwaysOnTop(true);
        toast.setResizable(false);

        Scene scene = new Scene(box);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        toast.setScene(scene);

        // Positionner en bas au centre de la fenêtre owner
        toast.setOnShown(e -> {
            toast.setX(ownerStage.getX()
                    + ownerStage.getWidth() / 2
                    - toast.getWidth() / 2);
            toast.setY(ownerStage.getY()
                    + ownerStage.getHeight() - 80);
        });

        toast.show();

        // Animation : slide up + fade in
        TranslateTransition slideUp = new TranslateTransition(
                Duration.millis(300), box);
        slideUp.setFromY(20);
        slideUp.setToY(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), box);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        slideUp.play();
        fadeIn.play();

        // Disparaître après 2 secondes
        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), box);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.millis(1800));
        fadeOut.setOnFinished(e -> toast.close());
        fadeOut.play();
    }

    // ────────────────────────────────────────────────────────
    // Style selon le type
    // ────────────────────────────────────────────────────────
    private static String getStyle(Type type) {
        String base = "-fx-font-size: 13px; -fx-font-weight: bold; "
                + "-fx-background-radius: 25; -fx-text-fill: white; ";
        return switch (type) {
            case SUCCESS -> base + "-fx-background-color: #10b981;";
            case ERROR   -> base + "-fx-background-color: #ef4444;";
            case WARNING -> base + "-fx-background-color: #f59e0b;";
            case INFO    -> base + "-fx-background-color: #6366f1;";
        };
    }
}