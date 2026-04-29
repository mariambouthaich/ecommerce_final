import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée de l'application E-Commerce JavaFX
 * Lance l'interface client → login-client.fxml
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/fxml/login-client.fxml"));
        primaryStage.setTitle("🛍 E-Commerce — Connexion");
        primaryStage.setScene(new Scene(root, 480, 420));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
