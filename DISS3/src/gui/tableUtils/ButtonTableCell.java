/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tableUtils;


import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author MarekPC
 */
public class ButtonTableCell<S> extends TableCell<S,Button> {
    private final Button actionButton;

    public ButtonTableCell(String label, Function<S, S> function,String styleClass) {
        if(!styleClass.isEmpty())
        this.getStyleClass().add(styleClass);

        this.actionButton = new Button(label);
        this.actionButton.setOnAction((ActionEvent e) -> {
            function.apply(getCurrentItem());
        });
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    public S getCurrentItem() {
        return (S) getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function< S, S> function,String styleClass) {
        return param -> new ButtonTableCell<>(label, function,styleClass);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {                
            setGraphic(actionButton);
        }
    }
    
    
}
