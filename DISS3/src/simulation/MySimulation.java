package simulation;

import OSPABA.*;
import OSPStat.Stat;
import OSPStat.WStat;
import agents.*;
import objects.Utilities;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import managers.Doprava_Manager;
import managers.Okolie_Manager;
import objects.Config;
import objects.Linka;
import objects.Vozidlo;

public class MySimulation extends Simulation {

    public Random seedGenerator;
    public Config config;
    public Sim_Replications_Stats statistics;

    public MySimulation(Config cfg, long seed) {

        config = cfg;
        if (seed < 0) {
            seedGenerator = new Random();
        } else {
            seedGenerator = new Random(seed);
        }

        init();
    }

    public String getTimeString() {
        return Utilities.toTimeString(currentTime());
    }

    @Override
    public void prepareSimulation() {
        super.prepareSimulation();
        // Create global statistcis
        statistics = new Sim_Replications_Stats(config);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Reset entities, queues, local statistics, etc...
        config.resetState();
        model_Agent().initSimulation(config);
    }

    @Override
    public void replicationFinished() {
        // Collect local statistics into global, update UI, etc...       
        super.replicationFinished();
        Okolie_Manager okolieMan = (Okolie_Manager) _okolie_Agent.manager();
        Doprava_Manager dopravaMan = (Doprava_Manager) _doprava_Agent.manager();

        double pocetCelkovo = okolieMan.pocetCestujucich;
        double pocetNaStadione = okolieMan.pocetNaStadione;
        double pocetVcas = pocetNaStadione - okolieMan.pocetCoPrisliNeskoro;
        double pocetNeskoro = okolieMan.pocetCoPrisliNeskoro;
        statistics.dobaCakaniaNaVozidlo.addSample(okolieMan.dobaCakaniaNaVozidlo.mean());
        statistics.pocetVsetkychCestujucich.addSample(okolieMan.pocetCestujucich);
        statistics.percentoCoPrisloVcas.addSample(pocetVcas / pocetNaStadione);
        statistics.percentoCoPrisloNeskoro.addSample(pocetNeskoro / pocetNaStadione);
        statistics.pocetNaStadione.addSample(pocetNaStadione);
        statistics.casPrichodStadion.addSample(okolieMan.casPrichoduNaStadion.mean());
        statistics.addPoctyVoVozidlach();

        statistics.percentoMikrobus.addSample(okolieMan.pocetMikrobusom / pocetNaStadione);
        statistics.casDokoncenia.addSample(dopravaMan.casUkonceniaDopravy);

        statistics.cakanieA.addSample(okolieMan.dobaCakaniaPreLinky.get(Config.Linka_A.linkaA).mean());
        statistics.cakanieB.addSample(okolieMan.dobaCakaniaPreLinky.get(Config.Linka_B.linkaB).mean());
        statistics.cakanieC.addSample(okolieMan.dobaCakaniaPreLinky.get(Config.Linka_C.linkaC).mean());

        double sumaMikroA = 0;
        for (Vozidlo vozidlo : config.vozidlaLinkaA) {
            sumaMikroA += vozidlo.ziskMikrobusu;

        }
        double sumaMikroB = 0;
        for (Vozidlo vozidlo : config.vozidlaLinkaB) {
            sumaMikroB += vozidlo.ziskMikrobusu;

        }
        double sumaMikroC = 0;
        for (Vozidlo vozidlo : config.vozidlaLinkaC) {
            sumaMikroC += vozidlo.ziskMikrobusu;

        }
//        System.out.println(sumaMikroA + " " + sumaMikroB + " " + sumaMikroC);
        statistics.ziskyMikrobusovNaLinkach.get(Config.Linka_A.linkaA).addSample(sumaMikroA);
        statistics.ziskyMikrobusovNaLinkach.get(Config.Linka_B.linkaB).addSample(sumaMikroB);
        statistics.ziskyMikrobusovNaLinkach.get(Config.Linka_C.linkaC).addSample(sumaMikroC);

//        double sum = okolieMan.poctyNaStadioneLinky.get(Config.Linka_A.linkaA).sampleSize() + okolieMan.poctyNaStadioneLinky.get(Config.Linka_B.linkaB).sampleSize() + okolieMan.poctyNaStadioneLinky.get(Config.Linka_C.linkaC).sampleSize();
//        double sum2 = pocetNaStadione;
//        System.out.println(sum);
//        System.out.println(sum2);
        statistics.percentoNeskoroA.addSample(okolieMan.poctyNeskoroLinky.get(Config.Linka_A.linkaA).sampleSize() / okolieMan.poctyNaStadioneLinky.get(Config.Linka_A.linkaA).sampleSize());
        statistics.percentoNeskoroB.addSample(okolieMan.poctyNeskoroLinky.get(Config.Linka_B.linkaB).sampleSize() / okolieMan.poctyNaStadioneLinky.get(Config.Linka_B.linkaB).sampleSize());
        statistics.percentoNeskoroC.addSample(okolieMan.poctyNeskoroLinky.get(Config.Linka_C.linkaC).sampleSize() / okolieMan.poctyNaStadioneLinky.get(Config.Linka_C.linkaC).sampleSize());

//        System.out.println("FINISHED");
//        System.out.println("REPLICATION FINISHED: "+getTimeString());
    }

    @Override
    public void simulationFinished() {
        // Dysplay simulation results
        super.simulationFinished();
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
//        System.out.println("INIT");
        setModel_Agent(new Model_Agent(Id.model_Agent, this, null));
        setOkolie_Agent(new Okolie_Agent(Id.okolie_Agent, this, model_Agent()));
        setDoprava_Agent(new Doprava_Agent(Id.doprava_Agent, this, model_Agent()));
        setZastavky_Agent(new Zastavky_Agent(Id.zastavky_Agent, this, doprava_Agent()));
        setVozidla_agent(new Vozidla_agent(Id.vozidla_agent, this, doprava_Agent()));
    }

    private Model_Agent _model_Agent;

    public Model_Agent model_Agent() {
        return _model_Agent;
    }

    public void setModel_Agent(Model_Agent model_Agent) {
        _model_Agent = model_Agent;
    }

    private Okolie_Agent _okolie_Agent;

    public Okolie_Agent okolie_Agent() {
        return _okolie_Agent;
    }

    public void setOkolie_Agent(Okolie_Agent okolie_Agent) {
        _okolie_Agent = okolie_Agent;
    }

    private Doprava_Agent _doprava_Agent;

    public Doprava_Agent doprava_Agent() {
        return _doprava_Agent;
    }

    public void setDoprava_Agent(Doprava_Agent doprava_Agent) {
        _doprava_Agent = doprava_Agent;
    }

    private Zastavky_Agent _zastavky_Agent;

    public Zastavky_Agent zastavky_Agent() {
        return _zastavky_Agent;
    }

    public void setZastavky_Agent(Zastavky_Agent zastavky_Agent) {
        _zastavky_Agent = zastavky_Agent;
    }

    private Vozidla_agent _vozidla_agent;

    public Vozidla_agent vozidla_agent() {
        return _vozidla_agent;
    }

    public void setVozidla_agent(Vozidla_agent vozidla_agent) {
        _vozidla_agent = vozidla_agent;
    }
    //meta! tag="end"

    public class Sim_Replications_Stats {

        public Stat casDokoncenia, casPrichodStadion;
        public Stat percentoCoPrisloNeskoro, percentoMikrobus, dobaCakaniaNaVozidlo, pocetVsetkychCestujucich, pocetNaStadione, percentoCoPrisloVcas;
        public HashMap<Vozidlo, Stat> poctyCestujucichVozidiel;
        public Stat percentoNeskoroA, percentoNeskoroB, percentoNeskoroC;
        public Stat cakanieA, cakanieB, cakanieC;
        public Map<Linka, Stat> ziskyMikrobusovNaLinkach;

//        private Config config;
        public Sim_Replications_Stats(Config cfg) {
            percentoCoPrisloNeskoro = new Stat();
            percentoMikrobus = new Stat();
            dobaCakaniaNaVozidlo = new Stat();
            pocetVsetkychCestujucich = new Stat();
            percentoCoPrisloVcas = new Stat();
            casDokoncenia = new Stat();
            pocetNaStadione = new Stat();
            ziskyMikrobusovNaLinkach = new HashMap<>();
            ziskyMikrobusovNaLinkach.put(Config.Linka_A.linkaA, new Stat());
            ziskyMikrobusovNaLinkach.put(Config.Linka_B.linkaB, new Stat());
            ziskyMikrobusovNaLinkach.put(Config.Linka_C.linkaC, new Stat());
            casPrichodStadion = new Stat();
            percentoNeskoroA = new Stat();
            percentoNeskoroB = new Stat();
            percentoNeskoroC = new Stat();
            cakanieA = new Stat();
            cakanieB = new Stat();
            cakanieC = new Stat();
//            config = cfg;
            poctyCestujucichVozidiel = new HashMap<>();

            for (Vozidlo vozidlo : cfg.vozidlaLinkaA) {
                poctyCestujucichVozidiel.put(vozidlo, new Stat());

            }
            for (Vozidlo vozidlo : cfg.vozidlaLinkaB) {
                poctyCestujucichVozidiel.put(vozidlo, new Stat());

            }
            for (Vozidlo vozidlo : cfg.vozidlaLinkaC) {
                poctyCestujucichVozidiel.put(vozidlo, new Stat());

            }

        }

        public void addPoctyVoVozidlach() {
            for (Vozidlo vozidlo : poctyCestujucichVozidiel.keySet()) {
                Stat stat = poctyCestujucichVozidiel.get(vozidlo);
                stat.addSample(vozidlo.celkovyPocetCoIsloVozidlom);
            }

        }

    }
}
