import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/view.fxml"));
        primaryStage.setTitle("Алгоритм Дейкстры");
        primaryStage.initStyle(StageStyle.DECORATED);
        Scene scene = new Scene(root, 1200, 625);
        primaryStage.setScene(scene);
        primaryStage.show();
        Controller.getGraphView().init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}