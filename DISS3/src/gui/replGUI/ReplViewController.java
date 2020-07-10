/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.replGUI;

import OSPABA.ISimDelegate;
import OSPABA.SimState;
import OSPABA.Simulation;
import objects.Utilities;
import gui.runGUI.MainController;
import gui.tableUtils.TableUtils;
import gui.variantaChooser.VariantaController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
import objects.Config;
import objects.Vozidlo;
import simulation.MySimulation;

/**
 * FXML Controller class
 *
 * @author MarekPC
 */
public class ReplViewController implements Initializable, ISimDelegate {

    @FXML
    private VBox tabRepl;
    @FXML
    private ChoiceBox<String> cbSeed;
    @FXML
    private TextField tfSeed;
    @FXML
    private TextField tfReplications;
    @FXML
    private Button btnConfig;
    @FXML
    private Label lbKonfig;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Label lbReplication;
    @FXML
    private Label lbCasZapasu,lbLinkaA,lbLinkaB,lbLinkaC;
    @FXML
    private TableView<VozidloStatTableItem> tvVozidlaA, tvVozidlaB, tvVozidlaC;
    @FXML
    private TableColumn<VozidloStatTableItem, Integer> tcA_Vozidla_ID, tcB_Vozidla_ID, tcC_Vozidla_ID;
    @FXML
    private TableColumn<VozidloStatTableItem, String> tcA_Vozidla_Varianta, tcB_Vozidla_Varianta, tcC_Vozidla_Varianta;
    @FXML
    private TableColumn<VozidloStatTableItem, String> tcA_Celkovo, tcB_Celkovo, tcC_Celkovo;
    @FXML
    private VBox vBox;
    @FXML
    private Label lbPriemernyCasCakania, lbCasDokoncenia, lbPocetStadion, lbPrichodStadion;
    @FXML
    private Label lbPercentoNeskoro;
    @FXML
    private Label lbPercentoVcas;
    @FXML
    private Label lbPercentoMikrobus;
    @FXML
    private Button btnVozidla;
    @FXML
    private Label lbCelkovyPocet;
    private MySimulation sim;
    private Config runConfig;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
        btnStop.setOnAction(e -> {
            sim.stopReplication();
            sim.stopSimulation();
//            sim.simulationFinished();
//            toogleControls(false);
        });

//        toogleControls(false);
        btnConfig.setOnAction(e -> {

            configDialog();

        });

        btnStart.setOnAction((ActionEvent e) -> {
            int seed = -1;
            if (tfSeed.isVisible()) {
                seed = Integer.parseInt(tfSeed.getText());
            }
            sim = new MySimulation(runConfig, seed);
            sim.onReplicationDidFinish(((t) -> {
                refresh(t);
            }));

            sim.onSimulationWillStart((t) -> {
                Platform.runLater(() -> {
                    lbCasZapasu.setText("Čas zápasu: " + Utilities.toTimeString(sim.config.casStartZapasu()));

                });
            });
            sim.simulateAsync(Integer.parseInt(tfReplications.getText()));

        });
//        for (int i = 0; i < 2; i++) {
//            ColumnConstraints cc = new ColumnConstraints();
//            cc.setHgrow(Priority.ALWAYS); // allow column to grow
//            cc.setFillWidth(true); // ask nodes to fill space for column
//            // other settings as needed...
//            gridA.getColumnConstraints().add(cc);
//        }
//        for (int i = 0; i < 2; i++) {
//            ColumnConstraints cc = new ColumnConstraints();
//            cc.setHgrow(Priority.ALWAYS); // allow column to grow
//            cc.setFillWidth(true); // ask nodes to fill space for column
//            // other settings as needed...
//            gridB.getColumnConstraints().add(cc);
//        }

        tcA_Vozidla_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tcB_Vozidla_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tcC_Vozidla_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));

        tcA_Vozidla_Varianta.setCellValueFactory(new PropertyValueFactory<>("varianta"));
        tcB_Vozidla_Varianta.setCellValueFactory(new PropertyValueFactory<>("varianta"));
        tcC_Vozidla_Varianta.setCellValueFactory(new PropertyValueFactory<>("varianta"));

        tcA_Celkovo.setCellValueFactory(new PropertyValueFactory<>("celkovo"));
        tcB_Celkovo.setCellValueFactory(new PropertyValueFactory<>("celkovo"));
        tcC_Celkovo.setCellValueFactory(new PropertyValueFactory<>("celkovo"));
       
        TableUtils.wrapStringTableColumns(tcA_Vozidla_Varianta, tcB_Vozidla_Varianta, tcC_Vozidla_Varianta);

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

    @Override
    public void simStateChanged(Simulation smltn, SimState ss) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void refresh(Simulation smltn) {
        MySimulation.Sim_Replications_Stats stats = sim.statistics;
        Platform.runLater(() -> {
            lbReplication.setText("Replikácia: " + smltn.currentReplication());
            lbCasDokoncenia.setText(Utilities.toTimeString(stats.casDokoncenia.mean()));
            lbCelkovyPocet.setText(String.format("%.3f", stats.pocetVsetkychCestujucich.mean()));
            lbPercentoVcas.setText(String.format("%.3f", stats.percentoCoPrisloVcas.mean() * 100));
            lbPercentoNeskoro.setText(String.format("%.3f", stats.percentoCoPrisloNeskoro.mean() * 100));
            lbPocetStadion.setText(String.format("%.3f", stats.pocetNaStadione.mean()));
            lbPriemernyCasCakania.setText(String.format("%.3f minút", stats.dobaCakaniaNaVozidlo.mean()));
            lbPrichodStadion.setText(Utilities.toTimeString(stats.casPrichodStadion.mean()));
            lbPercentoMikrobus.setText(String.format("%.3f", stats.percentoMikrobus.mean() * 100));
            lbLinkaA.setText(String.format("Linka A:\t Priemerná doba čakania: %.5f \t Priemerné %% čo prišlo neskoro: %.3f",stats.cakanieA.mean(),stats.percentoNeskoroA.mean()*100));
            lbLinkaB.setText(String.format("Linka B:\t Priemerná doba čakania: %.5f \t Priemerné %% čo prišlo neskoro: %.3f",stats.cakanieB.mean(),stats.percentoNeskoroB.mean()*100));
            lbLinkaC.setText(String.format("Linka C:\t Priemerná doba čakania: %.5f \t Priemerné %% čo prišlo neskoro: %.3f",stats.cakanieC.mean(),stats.percentoNeskoroC.mean()*100));
            refreshTables(stats, runConfig.vozidlaLinkaA, tvVozidlaA);
            refreshTables(stats, runConfig.vozidlaLinkaB, tvVozidlaB);
            refreshTables(stats, runConfig.vozidlaLinkaC, tvVozidlaC);
        });

    }

    private void refreshTables(MySimulation.Sim_Replications_Stats stats, List<Vozidlo> vozidla, TableView<VozidloStatTableItem> table) {
        table.getItems().clear();
        for (Vozidlo vozidlo : vozidla) {
            table.getItems().add(new VozidloStatTableItem(vozidlo, stats));
        }

    }

}
