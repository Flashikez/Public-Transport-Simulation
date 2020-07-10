/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.runGUI;

/**
 *
 * @author MarekPC
 */
public class SpeedClass {

    String name;
    double planTime, sleepTime;

    public SpeedClass(String name, double planTime, double sleepTime) {
        this.name = name;
        this.planTime = planTime;
        this.sleepTime = sleepTime;
    }

    @Override
    public String toString() {
        return name;
    }

}
