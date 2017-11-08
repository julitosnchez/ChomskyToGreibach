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
        
        GC.añadeProduccion("C", "b");
        GC.añadeProduccion("D", "a");
        GC.añadeProduccion("C", "DA");
        GC.añadeProduccion("A", "CD");
        GC.añadeProduccion("D", "AC");
//        GC.añadeProduccion("F", "b");

 
//        System.out.println(GC.toString());
        GC.renombraVariables();
        GC.ELIMINA1("3", "1");
        GC.ELIMINA1("3", "2");
        GC.ELIMINA2("3");
        GC.ELIMINA1("2", "3");
        GC.ELIMINA1("1", "2");
        System.out.println(GC.toString());
//        GC.ELIMINA2("2");
//        System.out.println(GC.toString());


    }
    
}
