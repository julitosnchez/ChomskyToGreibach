/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chomskytogreibach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author julitosnchez
 */
public class GramaticaChomsky extends Exception {
    
    Map<String,ArrayList<String>> producciones1; //Producciones de tipo 1 (del tipo A -> BC tal que A,B,C pertenecen NT
    //Cada posicion del Array de string es un "PAQUETE" con la produccion, ejemplo si A -> BC, ArrayList<String> (0) = "BC"
    Map<String,ArrayList<String>> produccionesAlias;

    Map<String,Integer> alias;
    
    public GramaticaChomsky()
    {
        producciones1 = new HashMap<String,ArrayList<String>>();
        produccionesAlias = new HashMap<String,ArrayList<String>>();
        
        alias = new HashMap<String,Integer>();
    }
    
    public boolean isTerminal(char A)
    {
        // || symbol == '(' || symbol == ')' || symbol == ',' || symbol == '+' || symbol == '-' || symbol == '/'
        char symbol = A;
        if(symbol >= 'a' && symbol <= 'z' || symbol == '(' || symbol == ')' || symbol == ',' || symbol == '+' || symbol == '-' || symbol == '/')
            return true;
        return false;
    }
    
//  Generamos un alias para cada una de las variables de la gramática
//  Por tanto, todas las variables serán A1,A2,A3,...etc
    
    public void generaAlias(int orden)
    {
        int tamañoProducciones = producciones1.entrySet().size();
        ArrayList<Integer> asignados = new ArrayList();
        int i;
        if(orden == 0)
            i = 1;
        else 
            i = producciones1.entrySet().size();
        for(Map.Entry<String,ArrayList<String>> entry: producciones1.entrySet())
        {
            if(!alias.containsKey(entry.getKey()))
            {
                if(orden == 2){
                    Random r = new Random();
                    int valorRandom = r.nextInt(tamañoProducciones)+1;
                    while(asignados.contains(valorRandom))
                        valorRandom = r.nextInt(tamañoProducciones)+1;
                    alias.put(entry.getKey(), valorRandom);
                    asignados.add(valorRandom);
                }
                else {
                    alias.put(entry.getKey(),i);
                    if(orden == 0)
                        i++;
                    else if (orden == 1)
                        i--;
                }
            }
        }
    }
    
    public void renombraVariables(int orden)
    {
        this.generaAlias(orden);
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
                    int l = 1;
                    String valor = String.valueOf(g.charAt(0));
                    while(!(g.charAt(l) >= 'A' && g.charAt(l) <= 'Z'))
                    {
                        valor += String.valueOf(g.charAt(l));
                        l++;
                    }
                    String a = Integer.toString(alias.get(valor));
                    String segundoValor = String.valueOf(g.charAt(l));
                    l++;
                    while(l < g.length()) { segundoValor += String.valueOf(g.charAt(l)); l++; }
                     a += Integer.toString(alias.get(segundoValor));
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
        ArrayList<String> producciones = this.produccionesAlias.get(Ak);
        
        int i = 0;
        while(!String.valueOf(producciones.get(i).charAt(0)).equals(Aj))
             i++;
        
        //1. ELiminar A -> B alpha
        String alphaAlias = this.produccionesAlias.get(Ak).get(i).replaceFirst(Aj, "");
        this.produccionesAlias.get(Ak).remove(i);
        
        //2. Para cada produccion B -> beta
        // 2.1 Añadir A -> beta alpha
        producciones = this.produccionesAlias.get(Aj);
        for(int j=0;j<producciones.size();j++)
            this.produccionesAlias.get(Ak).add(producciones.get(j)+ alphaAlias);        
    }
    
    
    public void ELIMINA2(String generador)
    {
        ArrayList<String> producidoAlias = this.produccionesAlias.get(generador);
        int nIter = producidoAlias.size();
        for(int i=nIter-1;i>=0;i--)
            if(generador.equals(String.valueOf(producidoAlias.get(i).charAt(0)))) //Si A -> A loquesea
            {
                //A -> A alpha
                //Nos quedamos con alpha reemplazando A por vacío
                String alphaAlias = producidoAlias.get(i).replaceFirst(generador, "");
                this.produccionesAlias.get(generador).remove(i);
                
                //Añadimos BA -> alpha
                ArrayList<String> produccionesBalias = new ArrayList();
                produccionesBalias.add(alphaAlias);
                produccionesBalias.add(alphaAlias+"-"+generador);
                
                //Añadir Elementos a Bx
                if(!this.produccionesAlias.containsKey("-"+generador))
                {
                    this.produccionesAlias.put("-"+generador, produccionesBalias);
                } else {                  
                    this.produccionesAlias.get("-"+generador).add(produccionesBalias.get(0));
                    this.produccionesAlias.get("-"+generador).add(produccionesBalias.get(1));
                }
            }
            else{
                this.produccionesAlias.get(generador).add(producidoAlias.get(i)+"-"+generador);
            }
    }
    
    public void CNFtoGNF()
    {
        boolean otraPasada = true;
        ArrayList<Pair<String,ArrayList<String> > > arrayProduccionesInversa = new ArrayList<>();
        
        while(otraPasada){
            
            otraPasada = false;
            arrayProduccionesInversa.clear();
            
            for(Map.Entry<String,ArrayList<String>> entry: produccionesAlias.entrySet())
            {
               ArrayList<String> producido = entry.getValue();
               Pair<String,ArrayList<String> > par = new Pair<String,ArrayList<String> >(entry.getKey(),producido);
               arrayProduccionesInversa.add(par);
              for(int i=0;i<producido.size();i++)
              {
                   String primeraProduccion = null;
                  if(!isTerminal(producido.get(i).charAt(0))) //MIRAMOS QUE NO ESTAMOS ANTE A -> d 
                  {
                      primeraProduccion = String.valueOf(producido.get(i).charAt(0));
                      if(Integer.valueOf(entry.getKey()) > (Integer.valueOf(primeraProduccion)) && otraPasada == false)
                     {
                           this.ELIMINA1(entry.getKey(), primeraProduccion);
                            otraPasada = true;
                            break;
                     }
                      if(Integer.valueOf(entry.getKey()).equals(Integer.valueOf(primeraProduccion)) && otraPasada == false)
                      {
                          this.ELIMINA2(entry.getKey());
                            otraPasada = true;
                            break;
                      }
                 }
            }
              if(otraPasada == true)
                  break;
         }
            
    }
        otraPasada = true;
        while(otraPasada){
            otraPasada = false;
            for(int i=arrayProduccionesInversa.size()-1;i >= 0;i--)
            {
                String productor = arrayProduccionesInversa.get(i).getKey();
                ArrayList<String> producido = arrayProduccionesInversa.get(i).getValue();
                for(int j=0;j<producido.size();j++)
                {
                    String primeraProduccion = null;
                    if(!isTerminal(producido.get(j).charAt(0)) && producido.get(j).charAt(0) != '-')
                    {
                      primeraProduccion = String.valueOf(producido.get(j).charAt(0));
                      if(Integer.valueOf(productor) < (Integer.valueOf(primeraProduccion)))
                      {
                        this.ELIMINA1(productor, primeraProduccion);
                        otraPasada = true;
                      }
                    }
                }
            }
        }
}
    
    public void añadeProduccion(String generador,String generado) throws Exception
    {
        boolean tipo2 = generado.length() == 1; //Es una produccion de la forma A -> a
        
        if( (generado.length() > 2  && (generado.charAt(1) != '_' && generado.charAt(2) != '_' && generado.charAt(4) != '_')) || isTerminal(generador.charAt(0)) || (tipo2 &&  !isTerminal(generado.charAt(0))) || (!tipo2 && (isTerminal(generado.charAt(0)) || isTerminal(generado.charAt(1)))))
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
                String p = entry.getKey();
                if(Integer.valueOf(p) > 0)
                    out += "A" + entry.getKey() + " -> ";
                else {
                    p = p.replace("-", "");
                    out += "B"+p + " -> ";
                }
                

                for(int j=0; j < entry.getValue().get(i).length(); j++)
                {
                    String a = String.valueOf(entry.getValue().get(i).charAt(j));
                        if(isTerminal(entry.getValue().get(i).charAt(j)) && !a.equals("-"))
                            out += String.valueOf(entry.getValue().get(i).charAt(j)) + " ";
                        else if(String.valueOf(entry.getValue().get(i).charAt(j)).equals("-"))
                        {
                            out += "B" + String.valueOf(entry.getValue().get(i).charAt(j+1)) + " ";
                            j = j+1;
                        } else
                            out += "A" + String.valueOf(entry.getValue().get(i).charAt(j)) + " ";
                }
                    out += "\n";
                
                }
            }
        
        
        return out;
    }
    
    public String asignacionesToString()
    {
        String out = "";
        for(Map.Entry<String,Integer> entry: alias.entrySet())
        {
            String linea = entry.getKey();
            linea += " ---> ";
            linea += "A"+ entry.getValue().toString() + "\n";
            
            out += linea;
        }
        
        return out;
    }
    
}
