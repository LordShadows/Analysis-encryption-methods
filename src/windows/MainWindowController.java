package windows;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import resourse.Crypt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.util.*;

/**
 * Created by DELL on 31.01.2017.
 * @author Daniel Sandrutski
 */

/**
 * Обработка событий MainWindows
 */
public class MainWindowController {
    private Stage STAGE;

    private double xOffset; // Используются для измения положения окна
    private double yOffset;

    @FXML
    private TextArea ta_Text; // Поле хранящее текст

    @FXML
    private RadioButton rb_AES; //Включение шифрование по стандарту AES

    @FXML
    private RadioButton rb_DES; //Включение шифрование по стандарту DES

    @FXML
    private RadioButton ra_RSA; //Включение шифрование по стандарту RSA

    @FXML
    private RadioButton ra_DESede; //Включение шифрование по стандарту DESede или 3DES

    @FXML
    private RadioButton ra_RSAAES; //Включение шифрование по стандарту RSA-AES

    @FXML
    private AnchorPane headerPane; // Панель, расположенная над заголовоком формы. Используется для перемещения формы

    @FXML
    private TextField tf_NumSymb; // Поле для отображения количества символов

    @FXML
    private ImageView ProgressBar;

    @FXML
    private SVGPath ClosePane;

    @FXML
    void b_Close_Click() {
        System.exit(0);
    }

    @FXML
    void b_Min_Click() {
        STAGE.setIconified(true);
    }

    /**
     * Метод инициализации
     * @param stage Принимает сцену данного окна
     */
    void Initialization(Stage stage) {
        STAGE = stage;
        headerPane.setOnMousePressed(event -> {
            xOffset = STAGE.getX() - event.getScreenX();
            yOffset = STAGE.getY() - event.getScreenY();
        });
        headerPane.setOnMouseDragged(event -> {
            STAGE.setX(event.getScreenX() + xOffset);
            STAGE.setY(event.getScreenY() + yOffset);
        });
        ta_Text.setOnKeyPressed(event -> tf_NumSymb.setText(String.valueOf(ta_Text.getLength())));
        ProgressBar.setVisible(false);
        ClosePane.setVisible(false);
    }

    @FXML
    /**
     * Метод, выполняемый по нажатию кнопки "Выполнить шифрование". Вычисляет время выполнения шифрования и расшифровки всех выбранных алгоритмов шифрования. Вызывает форму результатов.
     */
    void b_Start_Click() {
        ProgressBar.setVisible(true);
        ClosePane.setVisible(true);

        new Thread(()-> { // В отдельном потоке выполняем все выбранные методы шифрования
            String cleartext = ta_Text.getText();
            // Пробное тестирование
            byte[] tempkey = Crypt.generateKey("DES", 56);
            byte[] cryptt = Crypt.encode(cleartext, tempkey, "DES/CBC/PKCS5Padding", "DES");
            String decod = Crypt.decode(cryptt, tempkey, "DES/CBC/PKCS5Padding", "DES");
            // Начало анализа
            StringBuilder allinformation = new StringBuilder(); // Строка для хранения полной информации о тетсировании
            StringBuilder smallinformation = new StringBuilder(); // Строка для хранения только времени тетстирования
            Map<String, Long> resualt = new HashMap<>(); // Сохранение результатов для графика
            if(rb_DES.isSelected()) { // Вычисляем время выполнения шифрования и расшифровки алгоритмом DES
                byte[] key = Crypt.generateKey("DES", 56); // Генерируем ключ
                allinformation.append("Алгоритм DES-56 (DES/ECB/PKCS5Padding):\n");
                allinformation.append("Сгенерированный ключ (56 бит): " + Arrays.toString(key) + "\n");
                long startTime = System.nanoTime(); // Запоминаем время начала шифрования
                byte[] crypttext = Crypt.encode(cleartext, key, "DES/CBC/PKCS5Padding", "DES"); // Шифруем текст
                long estimatedTime1 = System.nanoTime() - startTime; // Вычисляем время шифрования
                String decodetext = Crypt.decode(crypttext, key, "DES/CBC/PKCS5Padding", "DES"); // Расшифровываем текст
                long estimatedTime2 = System.nanoTime() - startTime; // Вычисляем время расшифровки
                if(Objects.equals(decodetext, cleartext)) {
                    allinformation.append("Шифрование и расшифровка выполнены успешно!\n");
                    smallinformation.append("DES (56-битный ключ) ≈ " + String.valueOf(Math.round(estimatedTime2/100000.0)/10.0) + " мс.\n");
                    resualt.put("DES", estimatedTime2);
                }
                else {
                    allinformation.append("Ошибка!!!");
                    smallinformation.append("DES (56-битный ключ) Ошибка выполнения!!!\n");
                }
                allinformation.append("Время затраченное на выполнение шифрования: " + estimatedTime1 + " нс., расшифровки: " + (estimatedTime2 - estimatedTime1) +" нс.\nОбщее время: " + estimatedTime2 + " нс. ≈ " + String.valueOf(Math.round(estimatedTime2/1000000.0)) + " мс.\n");
            }
            if(ra_DESede.isSelected()) {  // Вычисляем время выполнения шифрования и расшифровки алгоритмом DESede
                byte[] key = Crypt.generateKey("DESede", 168);
                allinformation.append("\nАлгоритм DESede-168 (TripleDES/CBC/PKCS5Padding):\n");
                allinformation.append("Сгенерированный ключ (168 бит): " + Arrays.toString(key) + "\n");
                long startTime = System.nanoTime();
                byte[] crypttext = Crypt.encode(cleartext, key, "TripleDES/CBC/PKCS5Padding", "DESede");
                long estimatedTime1 = System.nanoTime() - startTime;
                String decodetext = Crypt.decode(crypttext, key, "TripleDES/CBC/PKCS5Padding", "DESede");
                long estimatedTime2 = System.nanoTime() - startTime;
                if(Objects.equals(decodetext, cleartext)) {
                    allinformation.append("Шифрование и расшифровка выполнены успешно!\n");
                    smallinformation.append("TripleDES (168-битный ключ) ≈ " + String.valueOf(Math.round(estimatedTime2/100000.0)/10.0) + " мс.\n");
                    resualt.put("TripleDES", estimatedTime2);
                }
                else {
                    allinformation.append("Ошибка!!!");
                    smallinformation.append("TripleDES (168-битный ключ) Ошибка выполнения!!!\n");
                }
                allinformation.append("Время затраченное на выполнение шифрования: " + estimatedTime1 + " нс., расшифровки: " + (estimatedTime2 - estimatedTime1) +" нс.\nОбщее время: " + estimatedTime2 + " нс. ≈ " + String.valueOf(Math.round(estimatedTime2/1000000.0)) + " мс.\n");
            }
            if(rb_AES.isSelected()) {  // Вычисляем время выполнения шифрования и расшифровки алгоритмом AES
                byte[] key128 = Crypt.generateKey("AES", 128);
                allinformation.append("\nАлгоритм AES-128 (AES/CBC/PKCS5Padding):\n");
                allinformation.append("Сгенерированный ключ (128 бит): " + Arrays.toString(key128) + "\n");
                long startTime128 = System.nanoTime();
                byte[] crypttext128 = Crypt.encode(cleartext, key128, "AES/CBC/PKCS5Padding", "AES");
                long estimatedTime1128 = System.nanoTime() - startTime128;
                String decodetext128 = Crypt.decode(crypttext128, key128, "AES/CBC/PKCS5Padding", "AES");
                long estimatedTime2128 = System.nanoTime() - startTime128;
                if(Objects.equals(decodetext128, cleartext)) {
                    allinformation.append("Шифрование и расшифровка выполнены успешно!\n");
                    smallinformation.append("AES (128-битный ключ) ≈ " + String.valueOf(Math.round(estimatedTime2128/100000.0)/10.0) + " мс.\n");
                    resualt.put("AES128", estimatedTime2128);
                }
                else {
                    allinformation.append("Ошибка!!!");
                    smallinformation.append("AES (128-битный ключ) Ошибка выполнения!!!\n");
                }
                allinformation.append("Время затраченное на выполнение шифрования: " + estimatedTime1128 + " нс., расшифровки: " + (estimatedTime2128 - estimatedTime1128) +" нс.\nОбщее время: " + estimatedTime2128 + " нс. ≈ " + String.valueOf(Math.round(estimatedTime2128/1000000.0)) + " мс.\n");

            }
            if(ra_RSAAES.isSelected()) {  // Вычисляем время выполнения шифрования и расшифровки алгоритмом RSA-AES
                KeyPair key = Crypt.generateKeysRSA();
                allinformation.append("\nАлгоритм RSA-2048/AES-128:\n");
                allinformation.append("Сгенерированный публичный ключ: " + Arrays.toString(key.getPublic().getEncoded()) + "\n");
                allinformation.append("Сгенерированный приватный ключ: " + Arrays.toString(key.getPrivate().getEncoded()) + "\n");
                long startTime = System.nanoTime();
                byte[] key128 = Crypt.generateKey("AES", 128);
                byte[] cryptkey = Crypt.encryptKeyRSA(key128, key.getPublic());
                byte[] crypttext = Crypt.encode(cleartext, key128, "AES/CBC/PKCS5Padding", "AES");
                long estimatedTime1 = System.nanoTime() - startTime;
                byte[] decodekey = Crypt.decryptKeyRSA(cryptkey, key.getPrivate());
                String decodetext = Crypt.decode(crypttext, decodekey, "AES/CBC/PKCS5Padding", "AES");
                long estimatedTime2 = System.nanoTime() - startTime;
                allinformation.append("Сгенерированный сеансовый ключ: " + Arrays.toString(key128) + "\n");
                if(Objects.equals(decodetext, cleartext)) {
                    allinformation.append("Шифрование и расшифровка выполнены успешно!\n");
                    smallinformation.append("RSA-2048/AES-128 ≈ " + String.valueOf(Math.round(estimatedTime2/100000.0)/10.0) + " мс.\n");
                    resualt.put("RSA/AES", estimatedTime2);
                }
                else {
                    allinformation.append("Ошибка!!!");
                    smallinformation.append("RSA-2048/AES-128 Ошибка выполнения!!!\n");
                }
                allinformation.append("Время затраченное на выполнение шифрования: " + estimatedTime1 + " нс., расшифровки: " + (estimatedTime2 - estimatedTime1) +" нс.\nОбщее время: " + estimatedTime2 + " нс. ≈ " + String.valueOf(Math.round(estimatedTime2/1000000.0)) + " мс.\n");
            }
            if(ra_RSA.isSelected()) {   // Вычисляем время выполнения шифрования и расшифровки алгоритмом RSA
                KeyPair key = Crypt.generateKeysRSA();
                allinformation.append("\nАлгоритм RSA-2048 (RSA/ECB/PKCS1Padding):\n");
                allinformation.append("Сгенерированный публичный ключ: " + Arrays.toString(key.getPublic().getEncoded()) + "\n");
                allinformation.append("Сгенерированный приватный ключ: " + Arrays.toString(key.getPrivate().getEncoded()) + "\n");
                long startTime = System.nanoTime();

                ArrayList<byte[]> crypttext = Crypt.encryptRSA(cleartext, key.getPublic());
                long estimatedTime1 = System.nanoTime() - startTime;
                String decodetext = Crypt.decryptRSA(crypttext, key.getPrivate());
                long estimatedTime2 = System.nanoTime() - startTime;
                if(Objects.equals(decodetext, cleartext)) {
                    allinformation.append("Шифрование и расшифровка выполнены успешно!\n");
                    smallinformation.append("RSA (2048-битный ключ) ≈ " + String.valueOf(Math.round(estimatedTime2/100000.0)/10.0) + " мс.\n");
                    resualt.put("RSA", estimatedTime2);
                }
                else {
                    allinformation.append("Ошибка!!!");
                    smallinformation.append("RSA (2048-битный ключ) Ошибка выполнения!!!\n");
                }
                allinformation.append("Время затраченное на выполнение шифрования: " + estimatedTime1 + " нс., расшифровки: " + (estimatedTime2 - estimatedTime1) +" нс.\nОбщее время: " + estimatedTime2 + " нс. ≈ " + String.valueOf(Math.round(estimatedTime2/1000000.0)) + " мс.\n");
            }
            // После вычисления времени выполнения всех выбранных алгоритмов открываем форму результатов
            Platform.runLater(()-> {
                try {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ResultWindow.fxml"));
                    Parent root = null;
                    root = loader.load();
                    Image ico = new Image("img/securecrt.png");
                    stage.getIcons().add(ico);
                    stage.initStyle(StageStyle.UNDECORATED);
                    Scene scene = new Scene(root, Color.TRANSPARENT);
                    stage.setScene(scene);
                    stage.setTitle("Результат анализа - by DarkCom");
                    stage.initStyle(StageStyle.TRANSPARENT);
                    ResultWindowController controller = loader.getController();
                    controller.Initialization(stage, resualt, allinformation.toString(), smallinformation.toString(), cleartext.length(), cleartext.getBytes().length);
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            ProgressBar.setVisible(false);
            ClosePane.setVisible(false);
        }).start();
    }

    @FXML
    /**
     * Метод заполнения поля исходного текста, случайными данными
     */
    void b_SampleText_Click() {
        InputStream in = Crypt.class.getResourceAsStream("/files/Text.txt");
        BufferedReader pubIn = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        do {
            try {
                tmp = pubIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (tmp != null) sb.append(tmp + "\n");
        } while (tmp != null);
        ta_Text.setText(sb.toString());
        tf_NumSymb.setText(String.valueOf(ta_Text.getLength()));
    }
}
