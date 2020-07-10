/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.variantaChooser;

import objects.Utilities;
import gui.tableUtils.ButtonTableCell;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import objects.Autobus;
import objects.Config;
import objects.Mikrobus;
import objects.Vozidlo;
import objects.Zastavka;
import objects.enums.Varianta_Nastupov;
import objects.enums.Varianta_Vozidla;

/**
 * FXML Controller class
 *
 * @author MarekPC
 */
public class VariantaController implements Initializable {

    @FXML
    private TextField tfCasZapasu;
    @FXML
    private ComboBox<Varianta_Nastupov> cmbNastupy;
    @FXML
    private ComboBox<Config> cmbKonfigy;
    @FXML
    private TableView<VariantaTableItem> tvA;
    @FXML
    private TableColumn<VariantaTableItem, Integer> tcA_ID, tcB_ID, tcC_ID;
    @FXML
    private TableColumn<VariantaTableItem, Varianta_Vozidla> tc_A_Varianta, tc_B_Varianta, tc_C_Varianta;
    @FXML
    private TableColumn<VariantaTableItem, Zastavka> tc_A_Zastavka, tc_B_Zastavka, tc_C_Zastavka;
    @FXML
    private TableColumn<VariantaTableItem, String> tc_A_Cas, tc_B_Cas, tc_C_Cas;
    @FXML
    private TableColumn<VariantaTableItem, Button> tc_A_Zmaz, tc_B_Zmaz, tc_C_Zmaz;
    @FXML
    private Button btnPridajA;
    @FXML
    private TableView<VariantaTableItem> tvB;
    @FXML
    private Button btnPridajB;
    @FXML
    private TableView<VariantaTableItem> tvC;

    @FXML
    private Button btnPridajC;
    @FXML
    private Button btnCreateConfig, btnUloz;
    @FXML
    private Label lbError;

    private Config chosenConfig;
    private int idCounter;
    private List<Config> savedConfigs;

    public VariantaController(Config runConfig) {
        this.chosenConfig = runConfig;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        createdConfig = Config.makeDefault();
        idCounter = chosenConfig.maxVozidloID();
        showConfig(chosenConfig);
        try {
            savedConfigs = Config.loadSavedConfigs("mikrobusyConfigs.txt");
            cmbKonfigy.getItems().addAll(FXCollections.observableArrayList(savedConfigs));
            cmbKonfigy.getSelectionModel().selectedItemProperty()
                    .addListener(new ChangeListener<Config>() {
                        @Override
                        public void changed(ObservableValue ov, Config value, Config new_value) {
                            chosenConfig = new_value;
                            showConfig(chosenConfig);

                        }
                    });
//        cmbKonfigy.getSelectionModel().select("Náhodná");
            btnUloz.setOnAction(e -> {
                try {
                    chosenConfig = makeConfig();
                    Config.saveConfig("mikrobusyConfigs.txt", chosenConfig);
                    cmbKonfigy.getItems().add(chosenConfig);
                    cmbKonfigy.getSelectionModel().select(chosenConfig);

                } catch (IOException ex) {
                    Logger.getLogger(VariantaController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

        } catch (IOException ex) {
            Logger.getLogger(VariantaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        cmbNastupy.getItems().addAll(Varianta_Nastupov.values());
        cmbNastupy.getSelectionModel().select(chosenConfig.variantaNastupov);

        tfCasZapasu.setText(Utilities.toTimeString(chosenConfig.casStartZapasu()));
        setupTables();

        btnCreateConfig.setOnAction(e -> {
            //Make config from tables
            try {

                chosenConfig = makeConfig();
                Stage st = (Stage) btnCreateConfig.getScene().getWindow();
                st.close();

            } catch (Exception ex) {
                lbError.setText(ex.getMessage());
            }

        });

        btnPridajA.setOnAction(e -> {
            VariantaTableItem nova = new VariantaTableItem(new Vozidlo(++idCounter, Varianta_Vozidla.AUTOBUS_A, Config.Linka_A.AA, 11 * 60), Varianta_Vozidla.AUTOBUS_A, Config.Linka_A.AA, "11:00:00");
            tvA.getItems().add(nova);
        });
        btnPridajB.setOnAction(e -> {
            VariantaTableItem nova = new VariantaTableItem(new Vozidlo(++idCounter, Varianta_Vozidla.AUTOBUS_A, Config.Linka_A.AA, 11 * 60), Varianta_Vozidla.AUTOBUS_A, Config.Linka_B.BA, "11:00:00");
            tvB.getItems().add(nova);
        });
        btnPridajC.setOnAction(e -> {
            VariantaTableItem nova = new VariantaTableItem(new Vozidlo(++idCounter, Varianta_Vozidla.AUTOBUS_A, Config.Linka_A.AA, 11 * 60), Varianta_Vozidla.AUTOBUS_A, Config.Linka_C.CA, "11:00:00");
            tvC.getItems().add(nova);
        });

    }

    private Config makeConfig() {
        double startZapasu = Utilities.stringToDoubleMins(tfCasZapasu.getText());
        Varianta_Nastupov varNastupov = cmbNastupy.getValue();
        List<Vozidlo> linkaA = constructVozidla(tvA);
        List<Vozidlo> linkaB = constructVozidla(tvB);
        List<Vozidlo> linkaC = constructVozidla(tvC);
        return new Config(startZapasu, linkaA, linkaB, linkaC, varNastupov);
    }

    public Config getConfig() {

        return chosenConfig;
    }

    public void setConfig(Config conf) {
        this.chosenConfig = conf;
    }

    private List<Vozidlo> constructVozidla(TableView<VariantaTableItem> tab) {
        List<Vozidlo> vozidla = new ArrayList<>();
        List<VariantaTableItem> list = tab.getItems();
        for (VariantaTableItem variantaTableItem : list) {
            Vozidlo nove;
            if (variantaTableItem.varianta == Varianta_Vozidla.AUTOBUS_A || variantaTableItem.varianta == Varianta_Vozidla.AUTOBUS_B) {
                nove = new Autobus(variantaTableItem.getId(), variantaTableItem.varianta, variantaTableItem.startZastavka, Utilities.stringToDoubleMins(variantaTableItem.startTime));

            } else {
                nove = new Mikrobus(variantaTableItem.getId(), variantaTableItem.varianta, variantaTableItem.startZastavka, Utilities.stringToDoubleMins(variantaTableItem.startTime));
            }

            vozidla.add(nove);
        }
        return vozidla;
    }

    private void setupTables() {
        tcA_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcB_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcC_ID.setCellValueFactory(new PropertyValueFactory<>("id"));

        setVarianty();

        setZastavky();
        setCasy();

        tc_A_Zmaz.setCellFactory(ButtonTableCell.<VariantaTableItem>forTableColumn("X", (VariantaTableItem s) -> {
            tvA.getItems().remove(s);
            return s;
        }, ""));
        tc_B_Zmaz.setCellFactory(ButtonTableCell.<VariantaTableItem>forTableColumn("X", (VariantaTableItem s) -> {
            tvB.getItems().remove(s);
            return s;
        }, ""));
        tc_C_Zmaz.setCellFactory(ButtonTableCell.<VariantaTableItem>forTableColumn("X", (VariantaTableItem s) -> {
            tvC.getItems().remove(s);
            return s;
        }, ""));

    }

    private void showConfig(Config conf) {
        tvA.getItems().clear();
        tvB.getItems().clear();
        tvC.getItems().clear();
        tfCasZapasu.setText(Utilities.toTimeString(conf.casStartZapasu()));
        cmbNastupy.getSelectionModel().select(conf.variantaNastupov);
        tvA.getItems().addAll(FXCollections.observableArrayList(makeTableList(conf.vozidlaLinkaA, conf)));
        tvB.getItems().addAll(FXCollections.observableArrayList(makeTableList(conf.vozidlaLinkaB, conf)));
        tvC.getItems().addAll(FXCollections.observableArrayList(makeTableList(conf.vozidlaLinkaC, conf)));
    }

    private List<VariantaTableItem> makeTableList(List<Vozidlo> list, Config cfg) {
        List<VariantaTableItem> retList = new ArrayList<>();

        for (Vozidlo vozidlo : list) {
//            Pair<Zastavka, Double> startPair = cfg.getStartovaciPair(vozidlo);
            VariantaTableItem item = new VariantaTableItem(vozidlo, vozidlo.varianta(), vozidlo.startZastavka, Utilities.toTimeString(vozidlo.startCas));
            retList.add(item);

        }
        return retList;

    }

    private void setVarianty() {
        tc_A_Varianta.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().varianta));
        tc_B_Varianta.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().varianta));
        tc_C_Varianta.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().varianta));
        tc_A_Varianta.setCellFactory(tc -> {
            ComboBox<Varianta_Vozidla> combo = new ComboBox<>();
            combo.getItems().addAll(Varianta_Vozidla.values());
            TableCell<VariantaTableItem, Varianta_Vozidla> cell = new TableCell<VariantaTableItem, Varianta_Vozidla>() {
                @Override
                protected void updateItem(Varianta_Vozidla reason, boolean empty) {
                    super.updateItem(reason, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        combo.setValue(reason);
                        setGraphic(combo);
                    }
                }
            };
            combo.setOnAction(e
                    -> tvA.getItems().get(cell.getIndex()).setVarianta(combo.getValue()));
            return cell;
        });
        tc_B_Varianta.setCellFactory(tc -> {
            ComboBox<Varianta_Vozidla> combo = new ComboBox<>();
            combo.getItems().addAll(Varianta_Vozidla.values());
            TableCell<VariantaTableItem, Varianta_Vozidla> cell = new TableCell<VariantaTableItem, Varianta_Vozidla>() {
                @Override
                protected void updateItem(Varianta_Vozidla reason, boolean empty) {
                    super.updateItem(reason, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        combo.setValue(reason);
                        setGraphic(combo);
                    }
                }
            };
            combo.setOnAction(e
                    -> tvB.getItems().get(cell.getIndex()).setVarianta(combo.getValue()));
            return cell;
        });
        tc_C_Varianta.setCellFactory(tc -> {
            ComboBox<Varianta_Vozidla> combo = new ComboBox<>();
            combo.getItems().addAll(Varianta_Vozidla.values());
            TableCell<VariantaTableItem, Varianta_Vozidla> cell = new TableCell<VariantaTableItem, Varianta_Vozidla>() {
                @Override
                protected void updateItem(Varianta_Vozidla reason, boolean empty) {
                    super.updateItem(reason, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        combo.setValue(reason);
                        setGraphic(combo);
                    }
                }
            };
            combo.setOnAction(e
                    -> tvC.getItems().get(cell.getIndex()).setVarianta(combo.getValue()));
            return cell;
        });

    }

    private void setZastavky() {
        tc_A_Zastavka.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getZastavka()));
        tc_B_Zastavka.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getZastavka()));
        tc_C_Zastavka.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getZastavka()));
        tc_A_Zastavka.setCellFactory(tc -> {
            ComboBox<Zastavka> combo = new ComboBox<>();
            combo.getItems().addAll(Config.Linka_A.listA);
            TableCell<VariantaTableItem, Zastavka> cell = new TableCell<VariantaTableItem, Zastavka>() {
                @Override
                protected void updateItem(Zastavka reason, boolean empty) {
                    super.updateItem(reason, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        combo.setValue(reason);
                        setGraphic(combo);
                    }
                }
            };
            combo.setOnAction(e
                    -> tvA.getItems().get(cell.getIndex()).setZastavka(combo.getValue()));
            return cell;
        });

        tc_B_Zastavka.setCellFactory(tc -> {
            ComboBox<Zastavka> combo = new ComboBox<>();
            combo.getItems().addAll(Config.Linka_B.listB);
            TableCell<VariantaTableItem, Zastavka> cell = new TableCell<VariantaTableItem, Zastavka>() {
                @Override
                protected void updateItem(Zastavka reason, boolean empty) {
                    super.updateItem(reason, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        combo.setValue(reason);
                        setGraphic(combo);
                    }
                }
            };
            combo.setOnAction(e
                    -> tvB.getItems().get(cell.getIndex()).setZastavka(combo.getValue()));
            return cell;
        });

        tc_C_Zastavka.setCellFactory(tc -> {
            ComboBox<Zastavka> combo = new ComboBox<>();
            combo.getItems().addAll(Config.Linka_C.listC);
            TableCell<VariantaTableItem, Zastavka> cell = new TableCell<VariantaTableItem, Zastavka>() {
                @Override
                protected void updateItem(Zastavka reason, boolean empty) {
                    super.updateItem(reason, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        combo.setValue(reason);
                        setGraphic(combo);
                    }
                }
            };
            combo.setOnAction(e
                    -> tvC.getItems().get(cell.getIndex()).setZastavka(combo.getValue()));
            return cell;
        });

    }

    private void setCasy() {
//        tc_A_Cas.setEditable(true);
        tc_A_Cas.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        tc_A_Cas.setCellFactory(
                TextFieldTableCell.forTableColumn());

        tc_A_Cas.setOnEditCommit(
                (TableColumn.CellEditEvent<VariantaTableItem, String> t)
                -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setTime(t.getNewValue())
        );

        tc_B_Cas.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        tc_B_Cas.setCellFactory(
                TextFieldTableCell.forTableColumn());

        tc_B_Cas.setOnEditCommit(
                (TableColumn.CellEditEvent<VariantaTableItem, String> t)
                -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setTime(t.getNewValue())
        );

        tc_C_Cas.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        tc_C_Cas.setCellFactory(
                TextFieldTableCell.forTableColumn());

        tc_C_Cas.setOnEditCommit(
                (TableColumn.CellEditEvent<VariantaTableItem, String> t)
                -> (t.getTableView().getItems().get(
                        t.getTablePosition().getRow())).setTime(t.getNewValue())
        );
    }

}
