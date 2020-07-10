/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import OSPDataStruct.SimQueue;
import simulation.MyMessage;

/**
 *
 * @author MarekPC
 */
public class Pool_Ludia_Zastavka {

    boolean generovanieSpustene;
    int pocetOcakavanych;
    SimQueue<MyMessage> ludia;
    int pocetCelkovo;

    public Pool_Ludia_Zastavka(int pocetOcak) {
        pocetOcakavanych = pocetOcak;
        pocetCelkovo = 0;
        ludia = new SimQueue<>();
        generovanieSpustene = false;
    }

    public int pocetLudi() {
        return ludia.size();
    }

    public void pridajCestujuceho(MyMessage msg) {
        ludia.enqueue(msg);
        pocetCelkovo++;

    }

    public MyMessage peekPrveho() {
        if (pocetLudi() == 0) {
            return null;
        }
        return ludia.peek();
    }

    public MyMessage dajCestujuceho() {
        if (pocetLudi() == 0) {
            return null;
        }
        return ludia.dequeue();

    }

    public boolean vygenerovanychDost() {
        return pocetOcakavanych == pocetCelkovo + 1;
    }

    public void reset() {
        pocetCelkovo = 0;
        ludia.clear();
        generovanieSpustene = false;
    }

}
