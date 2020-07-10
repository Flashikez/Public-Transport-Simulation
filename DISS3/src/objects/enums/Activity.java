/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects.enums;

/**
 *
 * @author MarekPC
 */
public enum Activity {
    CESTUJE {
        @Override
        public String toString() {
            return "cestovanie";
        }

    },
    VYSTUP {
        @Override
        public String toString() {
            return "vystupovanie";
        }

    },
    NASTUP {
        @Override
        public String toString() {
            return "nastupovanie";
        }
    },
    UKONCIL {
        @Override
        public String toString() {
            return "Ukončil dopravu, stojí na štadióne";
        }
    },
    CAKA {
        @Override
        public String toString() {
            return "čaká na štart";
        }
    },
    CAKA_NASTUPY {
        @Override
        public String toString() {
            return "čaká na ďaľších cestujúcich 1.5 minúty (VARIANTA B)";
        }
    }

}
