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
public enum Varianta_Nastupov {
     ODCHOD_HNED {
        @Override
        public String toString() {
            return "Okamžitý odchod vozidla";
        }

    },
    POCKAJ_90_SEKUND {
        @Override
        public String toString() {
            return "Čakanie 1,5 minúty na ďaľších";
        }

    };
}
