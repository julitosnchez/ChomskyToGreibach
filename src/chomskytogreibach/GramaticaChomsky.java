/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chomskytogreibach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author julitosnchez
 */
public class GramaticaChomsky extends Exception {
    
    Map<String,ArrayList<String>> producciones1; //Producciones de tipo 1 (del tipo A -> BC tal que A,B,C pertenecen NT
    Map<String,ArrayList<String>> produccionesAlias;

    Map<String,Integer> alias;
    Map<Integer,String> aliasNumToGen;

    
    public GramaticaChomsky()
    {
        producciones1 = new HashMap<String,ArrayList<String>>();
        produccionesAlias = new HashMap<String,ArrayList<String>>();
        
        alias = new HashMap<String,Integer>();
        aliasNumToGen = new HashMap<Integer,String>();
    }
    
    public boolean isTerminal(char A)
    {
        char symbol = A;
        if(symbol >= 'a' && symbol <= 'z')
            return true;
        return false;
    }
    
//  Generamos un alias para cada una de las variables de la gramática
//  Por tanto, todas las variables serán A1,A2,A3,...etc
    
    public void generaAlias(int orden)
    {
        int i = 1;
        for(Map.Entry<String,ArrayList<String>> entry: producciones1.entrySet())
        {
            if(!alias.containsKey(entry.getKey()))
            {
                alias.put(entry.getKey(),i);
                aliasNumToGen.put(i, entry.getKey());
                i++;
            }
        }
    }
    
    public void renombraVariables()
    {
        this.generaAlias(0);
        for(Map.Entry<String,ArrayList<String>> entry: producciones1.entrySet())
        {
            String generador = Integer.toString(alias.get(entry.getKey()));
            ArrayList<String> producido = new ArrayList();
            for(int i=0; i<entry.getValue().size();i++)
            {
                String g = entry.getValue().get(i);
                if(g.length()==1) //SImbolo terminal
                    producido.add(g);
                else { //Sustituimos por sus respectivos nombres
                    String a = Integer.toString(alias.get(String.valueOf(g.charAt(0))));
                     a += Integer.toString(alias.get(String.valueOf(g.charAt(1))));
                    producido.add(a);
                }
            }
            produccionesAlias.put(generador, producido);                
        }
    }
    
    /*
        @params String Ak - Generador
        @params String Aj - Produccion a eliminar
        @returns Elimina Ak -> Aj alpha  y para cada producción B -> beta ===> añade Ak -> beta alpha
    */
    
    public void ELIMINA1(String Ak,String Aj)
    {
        String A = this.aliasNumToGen.get(Integer.valueOf(Ak));
        String B = this.aliasNumToGen.get(Integer.valueOf(Aj));
        
        ArrayList<String> producciones = this.producciones1.get(A);
        int i = 0;
        while(!String.valueOf(producciones.get(i).charAt(0)).equals(B))
             i++;
        
        //1. ELiminar A -> B alpha
        String alpha = this.producciones1.get(A).get(i).replace(B, "");
        String alphaAlias = this.produccionesAlias.get(Ak).get(i).replace(Aj, "");
        this.producciones1.get(A).remove(i);
        this.produccionesAlias.get(Ak).remove(i);
    //    this.produccionesAlias.get(Ak).remove(i);
        
        //2. Para cada produccion B -> beta
        // 2.1 Añadir A -> beta alpha
        producciones = this.producciones1.get(B);
        ArrayList<String> produccionesAlias = this.produccionesAlias.get(Aj);
        for(int j=0;j<producciones.size();j++)
        {
            this.producciones1.get(A).add(producciones.get(j) + alpha);
            this.produccionesAlias.get(Ak).add(produccionesAlias.get(j)+ alphaAlias);
        
        }
        
    }
    
    public void CNFtoGNF()
    {
        //for(Map.Entry<String,ArrayList<String>> entry: produccionesAlias.entrySet())
            

    }
    
    public void añadeProduccion(String generador,String generado) throws Exception
    {
        boolean tipo2 = generado.length() == 1;
        
        if(generado.length() > 2 || isTerminal(generador.charAt(0)) || (tipo2 &&  !isTerminal(generado.charAt(0))) || (!tipo2 && (isTerminal(generado.charAt(0)) || isTerminal(generado.charAt(1)))))
            throw new Exception("PRODUCCION NO AÑADIDA CORRECTAMENTE: A -> BC o A -> a");
        else
            //Existe ya símbolo GENERADOR
            if(producciones1.containsKey(generador))
                producciones1.get(generador).add(generado);
            else 
            {
                    ArrayList<String> p = new ArrayList(); p.add(generado);
                    producciones1.put(generador, p);
            }                   
    }
    
    @Override
    public String toString()
    {
        String out = "";
        for(Map.Entry<String,ArrayList<String>> entry: produccionesAlias.entrySet()){
            for(int i = 0; i<entry.getValue().size();i++)
            {
                out += "A"+entry.getKey() + " -> ";
                if(entry.getValue().get(i).length() == 1)
                    out += entry.getValue().get(i)+"\n";
                else
                    out += "A" + String.valueOf(entry.getValue().get(i).charAt(0)) + " " + "A" + String.valueOf(entry.getValue().get(i).charAt(1)) + "\n";
            }
        }
        
        return out;
    }
    
    
    
}
