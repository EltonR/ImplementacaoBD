package implementacaobd;

import java.util.ArrayList;
import java.util.Locale;

public class TestaErros {
    
    private String consulta;
    private String[] query_v;
    private ArrayList colunas;
    private ArrayList tabelas;
    private ArrayList wheres;
    
    public TestaErros(String query){
        consulta = query.toUpperCase(Locale.ROOT).trim();
        query_v = consulta.replaceAll(",", " ").replaceAll("  ", " ").split(" ");
        colunas = new ArrayList();
        tabelas = new ArrayList();
        wheres = new ArrayList();
        System.out.println("QUERY: "+consulta);
    }
    
    public String testa(){
        
        if(consulta.equalsIgnoreCase("")){
            return "Consulta vazia!";
        }
        if(!query_v[0].equals("SELECT"))
            return "Consulta não inicia com SELECT";
        if(!consulta.contains("FROM"))
            return "Clausula 'FROM' não encontrada";
        int i=1;

        while(!query_v[i].equalsIgnoreCase("FROM")){
            colunas.add(query_v[i]);
            i++;
        }
        
        if(consulta.contains("JOIN")){
            
        }
        
        if(consulta.contains(" WHERE ")){
            while(!query_v[i].equals("WHERE"))
                tabelas.add(query_v[i]);
        }
        
        if(consulta.contains(" GROUP BY ")){
            while(! (query_v[i].equals("GROUP") && query_v[i+1].equals("BY")))
                tabelas.add(query_v[i]);
        }
        
        
        
        while(i<query_v.length && !query_v[i].equalsIgnoreCase("WHERE") && !query_v[i].equalsIgnoreCase("Order By")
                && !query_v[i].equalsIgnoreCase("LIMIT")){
            colunas.add(query_v[i]);
            i++;
        }
        
        

        
        return "OK";
    }
    
}
