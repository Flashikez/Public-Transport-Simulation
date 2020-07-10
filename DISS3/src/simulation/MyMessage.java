package simulation;

import OSPABA.*;
import objects.Config;
import objects.Linka;
import objects.Vozidlo;
import objects.Zastavka;

public class MyMessage extends MessageForm {

    public MyMessage cestujuciSpravy;

    public Config config;

    public Linka linkaSpravy;
    public Zastavka zastavkaSpravy;
    public Vozidlo vozidloSpravy;
    public int dvereSpravy;

    public boolean nastupilVcakaniVariantaB;
    public double casPrichoduNaZastavku, casPrichoduNaStadion, casNastupuDoVozidla,casZacatiaNastupu;

    public MyMessage(Simulation sim, Config cfg) {
        super(sim);

        config = cfg;
    }

    public MyMessage(MyMessage original) {
        super(original);
        // copy() is called in superclass
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessage(this);
    }

    @Override
    protected void copy(MessageForm message) {
        super.copy(message);
        MyMessage original = (MyMessage) message;
        // Copy attributes
        config = original.config;
        linkaSpravy = original.linkaSpravy;
        zastavkaSpravy = original.zastavkaSpravy;
        casPrichoduNaStadion = original.casPrichoduNaStadion;
        casPrichoduNaZastavku = original.casPrichoduNaZastavku;
        casNastupuDoVozidla = original.casNastupuDoVozidla;
        vozidloSpravy = original.vozidloSpravy;
        dvereSpravy = original.dvereSpravy;
        nastupilVcakaniVariantaB = original.nastupilVcakaniVariantaB;
        cestujuciSpravy = original.cestujuciSpravy;
    }
}
