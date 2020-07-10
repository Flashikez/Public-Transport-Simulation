/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import objects.Vozidlo;
import simulation.*;

/**
 *
 * @author MarekPC
 */
public class Nastup_v_cakani_varianta_B extends OSPABA.Process {

    TriangularRNG nastupAutobusRNG;
//    private final DeterministicRNG nastupAutobusRNG;

    public Nastup_v_cakani_varianta_B(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
//        nastupAutobusRNG = new DeterministicRNG(3.0 / 60.0);
        nastupAutobusRNG = new TriangularRNG(0.01, 0.07, 0.02, ((MySimulation) mySim()).seedGenerator);
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.start:
                processStart(message);
                break;
            case Mc.nastup_autobus:
                processNastup(message);
                break;
        }

    }

    private void processStart(MessageForm message) {
        MyMessage msg = (MyMessage) message;

        MyMessage cestujuci = msg.cestujuciSpravy;
        Vozidlo vozidloSpravy = msg.vozidloSpravy;
        cestujuci.casZacatiaNastupu = mySim().currentTime();
        // Nastup pre jedneho cestujuceho
        int dvere = msg.dvereSpravy;
        vozidloSpravy.obsadDvere(dvere);
        vozidloSpravy.pridajCestujuceho(cestujuci);
        msg.setCode(Mc.nastup_autobus);
        double holdTime = nastupAutobusRNG.sample();
//        System.out.println("NASTUPUJE V CAKANI");
        hold(holdTime, msg);
    }

    private void processNastup(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        Vozidlo vozidloSpravy = msg.vozidloSpravy;
        vozidloSpravy.uvolniDvere(msg.dvereSpravy);
        msg.cestujuciSpravy.casNastupuDoVozidla = mySim().currentTime();
        assistantFinished(message);

    }

}
