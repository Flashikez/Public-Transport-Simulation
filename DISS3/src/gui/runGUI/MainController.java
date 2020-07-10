/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.runGUI;

import OSPABA.SimState;
import OSPABA.Simulation;
import objects.Utilities;
import gui.tableUtils.TableUtils;
import gui.variantaChooser.VariantaController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import managers.Doprava_Manager;
import managers.Okolie_Manager;
import managers.Zastavky_Manager;
import objects.Config;
import objects.Linka;
import objects.Vozidlo;
import objects.Zastavka;
import simulation.MySimulation;

/**
 * FXML Controller class
 *
 * @author MarekPC
 */
public class MainController implements Initializable, OSPABA.ISimDelegate {

    @FXML
    private VBox runTab;
    @FXML
    private ChoiceBox<String> cbSeed;
    @FXML
    private TextField tfSeed;
    @FXML
    private Button btnConfig;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private ChoiceBox<SpeedClass> cbSpeed;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnContinue;
    @FXML
    private Label lbTime, lbCasStart, lbKonfig;
    @FXML
    private Label lbCelkovyPocet, lbPocetVcas, lbPocetNeskoro, lbCakanie, lbPercentoNeskoro;
    @FXML
    private Label lbPocetZastavky;
    @FXML
    private Label lbPocetVozidla;
    @FXML
    private Label lbPocetStadion;
    @FXML
    private TableView<Vozidlo> tvVozidlaA, tvVozidlaB, tvVozidlaC;
    @FXML
    private TableColumn<Vozidlo, Integer> tcA_Vozidla_ID, tcB_Vozidla_ID, tcC_Vozidla_ID;
    @FXML
    private TableColumn<Vozidlo, String> tcA_Vozidla_Varianta, tcB_Vozidla_Varianta, tcC_Vozidla_Varianta;
    @FXML
    private TableColumn<Vozidlo, Boolean> tcA_Vozidla_Dvere, tcB_Vozidla_Dvere, tcC_Vozidla_Dvere;
    @FXML
    private TableColumn<Vozidlo, Integer> tcA_Vozidla_Dvere1, tcB_Vozidla_Dvere1, tcC_Vozidla_Dvere1;
    @FXML
    private TableColumn<Vozidlo, Integer> tcA_Vozidla_Dvere2, tcB_Vozidla_Dvere2, tcC_Vozidla_Dvere2;
    @FXML
    private TableColumn<Vozidlo, Integer> tcA_Vozidla_Dvere3, tcB_Vozidla_Dvere3, tcC_Vozidla_Dvere3;
    @FXML
    private TableColumn<Vozidlo, Integer> tcA_Vozidla_Dvere4, tcB_Vozidla_Dvere4, tcC_Vozidla_Dvere4;
    @FXML
    private TableColumn<Vozidlo, String> tcA_Vozidla_Cinnost, tcB_Vozidla_Cinnost, tcC_Vozidla_Cinnost;
    @FXML
    private TableColumn<Vozidlo, String> tcA_Vozidla_Aktivita, tcB_Vozidla_Aktivita, tcC_Vozidla_Aktivita;
    @FXML
    private TableColumn<Vozidlo, Double> tcA_Vozidla_Progress, tcB_Vozidla_Progress, tcC_Vozidla_Progress;
    @FXML
    private TableColumn<Vozidlo, String> tcA_Vozidla_Obsadenost, tcB_Vozidla_Obsadenost, tcC_Vozidla_Obsadenost;

    @FXML
    private TableView<Zastavka> tvZastavkyA, tvZastavkyB, tvZastavkyC;
    @FXML
    private TableColumn<Zastavka, String> tcA_Zastavky_ID, tcB_Zastavky_ID, tcC_Zastavky_ID;
    @FXML
    private TableColumn<Zastavka, Integer> tcA_Zastavky_Pocet, tcB_Zastavky_Pocet, tcC_Zastavky_Pocet;
    @FXML
    private TableColumn<Zastavka, Integer> tcA_Zastavky_PocetCelkovo, tcB_Zastavky_PocetCelkovo, tcC_Zastavky_PocetCelkovo;
    @FXML
    private TableColumn<Zastavka, String> tcA_Zastavky_Vozidla, tcB_Zastavky_Vozidla, tcC_Zastavky_Vozidla;

    private MySimulation sim;
    private Config runConfig;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cbSeed.setItems(FXCollections.observableArrayList("Náhodná", "Fixná"));
        cbSeed.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue ov, String value, String new_value) {

                        if (new_value.compareTo("Fixná") == 0) {
//                            System.out.println(new_value);
                            tfSeed.setVisible(true);

                        } else {

                            tfSeed.setVisible(false);
                        }

                    }
                });
        cbSeed.getSelectionModel().select("Náhodná");

        runConfig = Config.makeDefault();
        SpeedClass oneToOne = new SpeedClass("1:1", 1 / 60d, 1);
        SpeedClass oneTo5 = new SpeedClass("1:5", 1 / 60d, 1 / 5d);
        SpeedClass oneTo10 = new SpeedClass("1:10", 1 / 60d, 1 / 10d);
        SpeedClass oneTo50 = new SpeedClass("1:50", 1 / 60d, 1 / 50d);
        SpeedClass oneTo100 = new SpeedClass("1:100", 1 / 60d, 1 / 100d);
        SpeedClass oneTo200 = new SpeedClass("1:200", 1 / 60d, 1 / 200d);
        SpeedClass oneTo500 = new SpeedClass("1:500", 1 / 60d, 1 / 500d);
        SpeedClass oneTo1000 = new SpeedClass("1:1 000", 1 / 60d, 1 / 1000d);
        SpeedClass oneTo2000 = new SpeedClass("1:2 000", 2 / 60d, 1 / 1000d);
        SpeedClass oneTo5000 = new SpeedClass("1:5 000", 5 / 60d, 1 / 1000d);
        SpeedClass oneTo100000 = new SpeedClass("1:10 000", 10 / 60d, 1 / 1000d);

        cbSpeed.setItems(FXCollections.observableArrayList(oneToOne, oneTo5, oneTo10, oneTo50, oneTo100, oneTo200, oneTo500, oneTo1000, oneTo2000, oneTo5000, oneTo100000));

        cbSpeed.getSelectionModel().select(oneToOne);
        cbSpeed.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SpeedClass>() {
            @Override
            public void changed(ObservableValue<? extends SpeedClass> observable, SpeedClass oldValue, SpeedClass newValue) {
                sim.setSimSpeed(newValue.planTime, newValue.sleepTime);

            }

        });
        btnPause.setOnAction(e -> {
            sim.pauseSimulation();

        });

        btnContinue.setOnAction(e -> {
            sim.resumeSimulation();
        });
        btnStop.setOnAction(e -> {
            sim.stopSimulation();
            sim.simulationFinished();
            toogleControls(false);
        });

        toogleControls(false);

        btnStart.setOnAction(e -> {
//            try {
//                Config.saveConfig(Config.saveFileName, runConfig);
            cbSpeed.getSelectionModel().select(oneToOne);
            startSimulation();
//            } catch (IOException ex) {
//                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
//            }
        });

        btnConfig.setOnAction(e -> {

            configDialog();

        });

        setupTables();

    }

    @Override
    public void simStateChanged(Simulation smltn, SimState ss) {
        if (ss == SimState.stopped) {
            System.out.println("DADADAD");
            toogleControls(false);
        }

    }

    @Override
    public void refresh(Simulation smltn) {

//        System.out.println(sim.currentTime());
        MySimulation mySim = (MySimulation) smltn;
        Doprava_Manager dopra_man = (Doprava_Manager) mySim.doprava_Agent().manager();
        Okolie_Manager okolie_man = (Okolie_Manager) mySim.okolie_Agent().manager();

        mySim.invokeAsync(() -> {
            Platform.runLater(() -> {
                lbTime.setText(mySim.getTimeString());
                lbCelkovyPocet.setText(okolie_man.pocetCestujucich + "");
                lbPocetStadion.setText(okolie_man.pocetNaStadione + "");
                lbPocetNeskoro.setText(okolie_man.pocetCoPrisliNeskoro + "");
                lbPocetVcas.setText((okolie_man.pocetNaStadione - okolie_man.pocetCoPrisliNeskoro) + "");
                lbCakanie.setText(String.format("%.5f", okolie_man.dobaCakaniaNaVozidlo.mean()));
                if (okolie_man.pocetNaStadione == 0) {
                    lbPercentoNeskoro.setText(("N/A"));
                } else {
                    lbPercentoNeskoro.setText(String.format("%.3f %%", ((double)okolie_man.pocetCoPrisliNeskoro * 100 / okolie_man.pocetNaStadione)));
                }
                lbPocetZastavky.setText(dopra_man.countPocetLudiNaZastavkach() + "");
                lbPocetVozidla.setText(dopra_man.countPocetLudiVoVozidlach() + "");
                refreshVozidlaTables(mySim);
                refreshZastavkyTables(mySim);

//
            });

        });

//        System.out.println(mySim.getTimeString() + "\t" + dopra_man.linkaA.priradeneVozidla().get(0).stateString(mySim.currentTime()));
    }

    private void startSimulation() {
        lbCasStart.setText(Utilities.toTimeString(runConfig.casStartZapasu()));
        int seed = -1;
        if (tfSeed.isVisible()) {
            seed = Integer.parseInt(tfSeed.getText());
        }
        sim = new MySimulation(runConfig, seed);
        sim.onSimulationDidFinish((t) -> {
            System.out.println("FINISGHED");
        });

        sim.registerDelegate(this);
        sim.setSimSpeed(1 / 60d, 1);

//        Thread t = new Thread(() -> {
        sim.simulateAsync(1);

//        });
//        t.setDaemon(true);
        toogleControls(true);
//        t.start();

    }

    private void setupTables() {
        setupVozidlaTables();
        setupZastavkyTables();

    }

    private void setupZastavkyTables() {
        tcA_Zastavky_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tcB_Zastavky_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tcC_Zastavky_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));

        tcA_Zastavky_Pocet.setCellValueFactory(new PropertyValueFactory<>("pocetNaZastavke"));
        tcB_Zastavky_Pocet.setCellValueFactory(new PropertyValueFactory<>("pocetNaZastavke"));
        tcC_Zastavky_Pocet.setCellValueFactory(new PropertyValueFactory<>("pocetNaZastavke"));

        tcA_Zastavky_PocetCelkovo.setCellValueFactory(new PropertyValueFactory<>("pocetCelkovo"));
        tcB_Zastavky_PocetCelkovo.setCellValueFactory(new PropertyValueFactory<>("pocetCelkovo"));
        tcC_Zastavky_PocetCelkovo.setCellValueFactory(new PropertyValueFactory<>("pocetCelkovo"));

        tcA_Zastavky_Vozidla.setCellValueFactory(new PropertyValueFactory<>("vozidlaNaZastavkeString"));
        tcB_Zastavky_Vozidla.setCellValueFactory(new PropertyValueFactory<>("vozidlaNaZastavkeString"));
        tcC_Zastavky_Vozidla.setCellValueFactory(new PropertyValueFactory<>("vozidlaNaZastavkeString"));
        TableUtils.wrapStringTableColumns(tcA_Zastavky_Vozidla, tcB_Zastavky_Vozidla, tcC_Zastavky_Vozidla);

    }

    private void setupVozidlaTables() {
        tcA_Vozidla_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcB_Vozidla_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcC_Vozidla_ID.setCellValueFactory(new PropertyValueFactory<>("id"));

        tcA_Vozidla_Varianta.setCellValueFactory(new PropertyValueFactory<>("typ"));
        tcB_Vozidla_Varianta.setCellValueFactory(new PropertyValueFactory<>("typ"));
        tcC_Vozidla_Varianta.setCellValueFactory(new PropertyValueFactory<>("typ"));

        tcA_Vozidla_Obsadenost.setCellValueFactory(new PropertyValueFactory<>("obsadenost"));
        tcB_Vozidla_Obsadenost.setCellValueFactory(new PropertyValueFactory<>("obsadenost"));
        tcC_Vozidla_Obsadenost.setCellValueFactory(new PropertyValueFactory<>("obsadenost"));

        tcA_Vozidla_Aktivita.setCellValueFactory(new PropertyValueFactory<>("aktivita"));
        tcB_Vozidla_Aktivita.setCellValueFactory(new PropertyValueFactory<>("aktivita"));
        tcC_Vozidla_Aktivita.setCellValueFactory(new PropertyValueFactory<>("aktivita"));

        tcA_Vozidla_Progress.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProgress(sim.currentTime())));
        tcB_Vozidla_Progress.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProgress(sim.currentTime())));
        tcC_Vozidla_Progress.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProgress(sim.currentTime())));

        tcA_Vozidla_Dvere1.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(0)));
        tcA_Vozidla_Dvere2.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(1)));
        tcA_Vozidla_Dvere3.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(2)));
        tcA_Vozidla_Dvere4.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(3)));

        tcB_Vozidla_Dvere1.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(0)));
        tcB_Vozidla_Dvere2.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(1)));
        tcB_Vozidla_Dvere3.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(2)));
        tcB_Vozidla_Dvere4.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(3)));

        tcC_Vozidla_Dvere1.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(0)));
        tcC_Vozidla_Dvere2.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(1)));
        tcC_Vozidla_Dvere3.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(2)));
        tcC_Vozidla_Dvere4.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().stavDveri(3)));

        TableUtils.intAsColor(tcA_Vozidla_Dvere1, tcA_Vozidla_Dvere2, tcA_Vozidla_Dvere3, tcA_Vozidla_Dvere4);
        TableUtils.intAsColor(tcB_Vozidla_Dvere1, tcB_Vozidla_Dvere2, tcB_Vozidla_Dvere3, tcB_Vozidla_Dvere4);
        TableUtils.intAsColor(tcC_Vozidla_Dvere1, tcC_Vozidla_Dvere2, tcC_Vozidla_Dvere3, tcC_Vozidla_Dvere4);

        TableUtils.doubleColumnAsProgress(tcA_Vozidla_Progress, tcB_Vozidla_Progress, tcC_Vozidla_Progress);

        TableUtils.wrapStringTableColumns(tcA_Vozidla_Aktivita, tcB_Vozidla_Aktivita, tcC_Vozidla_Aktivita);

    }

    private void refreshVozidlaTables(MySimulation mySim) {
        // Refresh linka A

        Doprava_Manager dopra_man = (Doprava_Manager) mySim.doprava_Agent().manager();
        Linka linkaA = dopra_man.linkaA;
        tvVozidlaA.getItems().clear();
        tvVozidlaA.getItems().addAll(FXCollections.observableArrayList(linkaA.priradeneVozidla()));

        Linka linkaB = dopra_man.linkaB;
        tvVozidlaB.getItems().clear();
        tvVozidlaB.getItems().addAll(FXCollections.observableArrayList(linkaB.priradeneVozidla()));

        Linka linkaC = dopra_man.linkaC;
        tvVozidlaC.getItems().clear();
        tvVozidlaC.getItems().addAll(FXCollections.observableArrayList(linkaC.priradeneVozidla()));

    }

    private void refreshZastavkyTables(MySimulation mySim) {
        Zastavky_Manager zastav_man = (Zastavky_Manager) mySim.zastavky_Agent().manager();
        tvZastavkyA.getItems().clear();
        tvZastavkyA.getItems().addAll(FXCollections.observableArrayList(zastav_man.zastavkyA()));
        tvZastavkyB.getItems().clear();
        tvZastavkyB.getItems().addAll(FXCollections.observableArrayList(zastav_man.zastavkyB()));
        tvZastavkyC.getItems().clear();
        tvZastavkyC.getItems().addAll(FXCollections.observableArrayList(zastav_man.zastavkyC()));

    }

    private void configDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(VariantaController.class.getResource("varianta.fxml"));
            VariantaController contr = new VariantaController(runConfig);
            loader.setController(contr);

            Scene scene = new Scene(loader.load());
//            VariantaController contr = loader.getController();

            Stage stage2 = new Stage();
            stage2.setScene(scene);
            stage2.initModality(Modality.APPLICATION_MODAL);
            stage2.showAndWait();
            runConfig = contr.getConfig();
            lbKonfig.setText("Config vytvorený");

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void toogleControls(boolean b) {
        // False ak nebezi simulacia
        if (!b) {
            cbSpeed.setDisable(true);
            btnPause.setDisable(true);
            btnContinue.setDisable(true);
            btnStop.setDisable(true);
            btnStart.setDisable(false);
            btnConfig.setDisable(false);
        } else {
            btnConfig.setDisable(true);
            btnStart.setDisable(true);
            cbSpeed.setDisable(false);
            btnPause.setDisable(false);
            btnContinue.setDisable(false);
            btnStop.setDisable(false);
            btnConfig.setDisable(true);

        }

    }

}
