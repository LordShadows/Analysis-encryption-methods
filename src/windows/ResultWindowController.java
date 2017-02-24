package windows;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by DELL on 31.01.2017.
 * @author Daniel Sandrutski
 */

/**
 * Обработка событий ResultWindows
 */
public class ResultWindowController {
    private Stage STAGE;

    private double xOffset; // Используются для измения положения окна
    private double yOffset;

    @FXML
    private TextField tf_TotalSize; // Поле для размера файла

    @FXML
    private BarChart bc_Chart; // График

    @FXML
    private TextArea ta_SmallInformation;

    @FXML
    private TextField tf_NumbSymb;

    @FXML
    private TextArea ta_AllInformation;

    @FXML
    private AnchorPane headerPane;

    @FXML
    void b_Close_Click() { STAGE.close(); }

    @FXML
    void b_Min_Click() {
        STAGE.setIconified(true);
    }

    /**
     * Метод инициализации
     * @param stage
     * @param resualts
     * @param allinformation
     * @param smallinformation
     * @param numbsymbols
     * @param size
     */
    void Initialization(Stage stage, Map<String, Long> resualts, String allinformation, String smallinformation, int numbsymbols, long size) {
        STAGE = stage;
        headerPane.setOnMousePressed(event -> {
            xOffset = STAGE.getX() - event.getScreenX();
            yOffset = STAGE.getY() - event.getScreenY();
        });
        headerPane.setOnMouseDragged(event -> {
            STAGE.setX(event.getScreenX() + xOffset);
            STAGE.setY(event.getScreenY() + yOffset);
        });

        ta_AllInformation.setText(allinformation);
        ta_SmallInformation.setText(smallinformation);

        ArrayList<XYChart.Series> series = new ArrayList<>();
        if(resualts.containsKey("DES")){
            XYChart.Series tempseries = new XYChart.Series();
            tempseries.getData().add(new XYChart.Data("DES", Math.round(resualts.get("DES")/10000.0)/100.0));
            series.add(tempseries);
        }
        if(resualts.containsKey("TripleDES")){
            XYChart.Series tempseries = new XYChart.Series();
            tempseries.getData().add(new XYChart.Data("TripleDES", Math.round(resualts.get("TripleDES")/10000.0)/100.0));
            series.add(tempseries);
        }
        if(resualts.containsKey("AES128")){
            XYChart.Series tempseries = new XYChart.Series();
            tempseries.getData().add(new XYChart.Data("AES128", Math.round(resualts.get("AES128")/10000.0)/100.0));
            series.add(tempseries);
        }
        if(resualts.containsKey("AES256")){
            XYChart.Series tempseries = new XYChart.Series();
            tempseries.getData().add(new XYChart.Data("AES256", Math.round(resualts.get("AES256")/10000.0)/100.0));
            series.add(tempseries);
        }
        if(resualts.containsKey("RSA")){
            XYChart.Series tempseries = new XYChart.Series();
            tempseries.getData().add(new XYChart.Data("RSA", Math.round(resualts.get("RSA")/10000.0)/100.0));
            series.add(tempseries);
        }
        if(resualts.containsKey("RSA/AES")){
            XYChart.Series tempseries = new XYChart.Series();
            tempseries.getData().add(new XYChart.Data("RSA/AES", Math.round(resualts.get("RSA/AES")/10000.0)/100.0));
            series.add(tempseries);
        }
        bc_Chart.getData().addAll(series);
        bc_Chart.setLegendVisible(false);
        bc_Chart.getYAxis().setLabel("мс.");
        tf_NumbSymb.setText(String.valueOf(numbsymbols));
        tf_TotalSize.setText(String.valueOf(Math.round(size/1024.0)) + " КБ");
    }
}