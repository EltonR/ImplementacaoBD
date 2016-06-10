package implementacaobd;

import java.util.ArrayList;

public class Consulta {
    
    private String consulta;
    private String[] consulta_v;
    private ArrayList colunas;
    private ArrayList tabelas;
    private ArrayList joins;
    private ArrayList wheres;
    
    public Consulta(String consulta){
        this.consulta = consulta.trim().toUpperCase();
        consulta_v = this.consulta.split(" ");
        colunas = new ArrayList();
        tabelas = new ArrayList();
        joins = new ArrayList();
        wheres = new ArrayList();
    }
    
    public String testa(){
        if(consulta.equalsIgnoreCase("")){
            return "Consulta vazia!";
        }
        if(!consulta_v[0].equals("SELECT"))
            return "Consulta não inicia com SELECT";
        if(!consulta.contains("FROM"))
            return "Clausula 'FROM' não encontrada";
        int i=1;
        while(!consulta_v[i].equalsIgnoreCase("FROM")){
            String[] cols=consulta_v[i].split(",");
            for(int j=0; j<cols.length; j++)
                colunas.add(cols[j].trim());
            i++;
        }
        
        while(i<consulta_v.length && !consulta_v[i].equals("WHERE")){
            tabelas.add(consulta_v[i]);
            i++;
        }
        if(i<consulta_v.length){
            while(i<consulta_v.length){
                wheres.add(consulta_v[i]);
                i++;
            }
        }
        System.out.println(toString());
        return "OK";
    }

    @Override
    public String toString() {
        String s = "SELECT ";
        for(int i=0; i<colunas.size(); i++)
            s+=(" "+colunas.get(i));
        s+=" FROM ";
        for(int i=0; i<tabelas.size(); i++)
            s+=(" "+tabelas.get(i));
        s+=" WHERE ";
        for(int i=0; i<wheres.size(); i++)
            s+=(" "+wheres.get(i));
        return s;
    }
    
    
    
}
