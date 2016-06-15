package implementacaobd;

import java.util.ArrayList;

public class Consulta {
    
    public static void main(String[] args) {
        
    }
    
    private String consulta;
    private String select;
    private String from;
    private String where;
    private ArrayList colunas;
    private ArrayList tabelas;
    private ArrayList joins;
    private ArrayList wheres;
    
    public Consulta(String consulta){
        this.consulta = consulta.trim().toUpperCase();
        
        colunas = new ArrayList();
        tabelas = new ArrayList();
        joins = new ArrayList();
    }
    
    public String testa(){
        if(consulta.equalsIgnoreCase("")){
            return "Consulta vazia!";
        }
        
        String[] consulta_v = this.consulta.split(" ");
        if(!consulta_v[0].equalsIgnoreCase("SELECT"))
            return "Consulta não inicia com SELECT";
        
        if(!consulta.contains(" FROM "))
            return "Clausula 'FROM' não encontrada";
        
        String[] s0 = consulta.split("SELECT ");
        String[] s1 = s0[1].split(" FROM ");
        
        select = s1[0];
        
        if(!s1[1].contains(" WHERE ")){
            from = s1[1];
        }else{
            String[] s2 = s1[1].split(" WHERE ");
            from = s2[0];
            where = s2[1];
        }
        
        
        
        
        String s3[] = from.split(",");
        for(int i=0; i<s3.length; i++){
            if(!s3[i].contains(" JOIN ")){
                tabelas.add(s3[i].replace(" ", ""));
            }else{
                if(!s3[i].contains(" ON ")){
                    return "Clausula 'ON' não encontrada";
                }
                joins.add(s3[i]);
                String[] s4 = s3[i].split(" JOIN ");
                String[] s5 = s4[1].split(" ON ");
                tabelas.add(s4[0].replace(" ",""));
                tabelas.add(s5[0].replace(" ", ""));
            }
        }
        
        String[] s6 = select.split(",");
        for(int i=0; i<s6.length; i++){
            String[] s7 = s6[i].split("[.]");
            if(s7.length<=1 || s7[1].replace(" ","").equalsIgnoreCase("")){
                return "Coluna inválida na clausula 'SELECT'";
            }
            if(!tabelas.contains(s7[0].replace(" ", ""))){
                return "Clausula 'SELECT' possui coluna não presente na clausula"
                        + "'FROM'";
            }
            colunas.add(s6[i]);
        }
        
        
        if(where.contains("(")){
            int np = 0;
            for(int i=0; i<where.length(); i++){
                if(where.substring(i, i+1).equals("(")){
                    np++;
                }
                if(where.substring(i, i+1).equals(")")){
                    np--;
                    if(np<0){
                        return "Erro na utilização do parênteses";
                    }
                }
            }
            if(np!=0){
                return "Erro na utilização do parênteses";
            }
        }else{
            if(where.contains(")")){
                return "Erro na utilização do parênteses";
            }
        }
        
        for(int i=0; i<tabelas.size(); i++){
            System.out.println("T="+tabelas.get(i));
        }
        for(int i=0; i<colunas.size(); i++){
            System.out.println("C="+colunas.get(i));
        }
        //System.out.println(toString());
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
