/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diss3;

import gui.TabViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author MarekPC
 */
public class DISS3 extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(TabViewController.class.getResource("tabView.fxml"));
        Scene scene = new Scene(root);       
        primaryStage.setScene(scene);
        primaryStage.show();
//        HladacRieseni hladac = new HladacRieseni();
//        hladac.pridajMikrobusyNaLinkyTest();
//        hladac.testMikrobusyConfig(0);
        

    }

}
