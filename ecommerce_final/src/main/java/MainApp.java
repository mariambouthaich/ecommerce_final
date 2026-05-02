import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Point d'entrée — démarre avec le Splash Screen animé
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Démarrer avec le splash screen
        Parent root = FXMLLoader.load(
                getClass().getResource("/fxml/splash.fxml"));

        primaryStage.setTitle("🛍 E-Commerce");
        primaryStage.setScene(new Scene(root, 520, 380));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
