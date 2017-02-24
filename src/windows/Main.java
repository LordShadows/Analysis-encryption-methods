package windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    /**
     * Метод start класса Main
     * @param primaryStage
     */
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml")); // Создаем загрузщик для MainWindows.fxml
        Parent root = loader.load();

        Image ico = new Image("img/securecrt.png");
        primaryStage.getIcons().add(ico); // Добавление иконки
        primaryStage.initStyle(StageStyle.UNDECORATED); // Отключаем рамку окна, а также стандарную панель заголовка
        Scene scene = new Scene(root, Color.TRANSPARENT); // Создаем новую сцену
        primaryStage.setScene(scene);
        primaryStage.setTitle("Вычисление времени шифрования - by DarkCom"); // Заголовок окна. Так как отключена стандартная панель заголовка, отображается только в панели задач
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        MainWindowController controller = loader.getController();
        controller.Initialization(primaryStage); // Передаем сцену в контроллер, инициализируя его

        primaryStage.show(); // Выводим окно
    }


    public static void main(String[] args) {
        launch(args);
    }
}
