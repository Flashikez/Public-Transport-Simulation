/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tableUtils;

import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;

/**
 *
 * @author MarekPC
 */
public class TableUtils {

    private static <T> void wrapStringTableColumn(TableColumn<T, String> column) {
        column.setCellFactory(tc -> {
            TableCell<T, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(column.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    public static <T> void wrapStringTableColumns(TableColumn<T, String>... column) {
        for (TableColumn<T, String> tableColumn : column) {
            wrapStringTableColumn(tableColumn);

        }
    }

    public static <T> void doubleColumnAsProgress(TableColumn<T, Double>... columns) {
        for (TableColumn<T, Double> column : columns) {

            column.setCellFactory(tc -> new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        this.setStyle(null);
                        this.setGraphic(null);
                        return;
                    }
                    if (empty) {
                        this.setStyle(null);
                        this.setGraphic(null);
                    } else {
                        // Set progressBar
                        if (item > 0) {
                            ProgressIndicator pi = new ProgressIndicator(item);
                            this.setGraphic(pi);
                        }

                    }

                }
            }
            );

        }
    }

    public static <T> void intAsColor(TableColumn<T, Integer>... columns) {
        for (TableColumn<T, Integer> column : columns) {

            column.setCellFactory(tc -> new TableCell<T, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        this.setStyle(null);
                        return;
                    }
                    if (empty) {
                        this.setStyle(null);
                    } else {
                        if (item == -1) {

//                            this.setStyle("-fx-background-color: black; -fx-border-width:10 0 0 0;-fx-border-color:#f4f4f4;");
                        } else if (item == 1) {
                            this.setStyle("-fx-background-color: green; -fx-border-width:3 3 3 3;-fx-border-color:#f4f4f4;");
                        } else if (item == 0) {
                            this.setStyle("-fx-background-color: red; -fx-border-width:3 3 3 3;-fx-border-color:#f4f4f4;");
                        }

                    }

                }
            }
            );

        }
    }

}
