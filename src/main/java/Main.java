import java.io.IOException;

import components.Dumpy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for dumpy using FXML.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        new Dumpy();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(
                                            "/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Dumpy");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
