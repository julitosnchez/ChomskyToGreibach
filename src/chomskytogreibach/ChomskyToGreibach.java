/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chomskytogreibach;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julitosnchez
 */
public class ChomskyToGreibach {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        GramaticaChomsky GC = new GramaticaChomsky();  
        
        GC.añadeProduccion("A", "b");
        GC.añadeProduccion("B", "c");
        //GC.añadeProduccion("C", "DA");
        GC.añadeProduccion("C", "e");
        GC.añadeProduccion("D", "GB");
        GC.añadeProduccion("E", "AH");
        GC.añadeProduccion("F", "FD");
        GC.añadeProduccion("F", "HE");
        GC.añadeProduccion("F", "GC");
        GC.añadeProduccion("G", "FF");
        GC.añadeProduccion("G", "d");
        GC.añadeProduccion("H", "GC");


 
        //ESPECIFICAMOS ORDEN
        // 0 - Orden ALfabetico
        // 1- Inversa Orden ALfabetico
          GC.renombraVariables(0);
//        GC.ELIMINA1("3", "1");
//        GC.ELIMINA1("3", "2");
//        GC.ELIMINA2("3");
//        GC.ELIMINA1("2", "3");
//        GC.ELIMINA1("1", "2");
//        GC.ELIMINA1("-3", "1");
//        GC.ELIMINA1("-3", "1");
        GC.CNFtoGNF();
        System.out.println(GC.toString());
//        GC.ELIMINA2("2");
//        System.out.println(GC.toString());


    }
    
}
