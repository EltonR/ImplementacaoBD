package implementacaobd;

import java.util.ArrayList;

public class Consulta {
    
    public static void main(String[] args) {
        String s = "13'5\"64";
        String s1 = "123a45";
        String s2 = "123.5";
        /*String[] ss = s.split(" AND | OR ");
        for(int i=0; i<ss.length; i++){
            System.out.println(ss[i]);
        }*/
        System.out.println(s);
        System.out.println(s.contains("\""));
    }
    
    private String consulta;
    private String select;
    private String from;
    private String where;
    private ArrayList<String> colunas;
    private ArrayList<String> tabelas;
    private ArrayList<String> joins;
    private ArrayList<String> wheres;
    private ArrayList<String> parenteses;
    private ArrayList<Integer> abreP;
    private ArrayList<Integer> fechaP;
    
    public Consulta(String consulta){
        this.consulta = consulta.trim().toUpperCase();
        
        colunas = new ArrayList();
        tabelas = new ArrayList();
        joins = new ArrayList();
        parenteses = new ArrayList();
        abreP = new ArrayList();
        fechaP = new ArrayList();
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
        joins = new ArrayList<>();
        String[] temp_f = from.split(" JOIN ");
        for(int i=0; i<temp_f.length-1; i++){
            if(i==0){
                String s = temp_f[i]+" JOIN "+temp_f[i+1];
                joins.add(s);
                String[] t2 = temp_f[i+1].split(" ON ");
                tabelas.add(temp_f[i].replace(" ",""));
                tabelas.add(t2[0].replace(" ", ""));
            }else{
                String s = joins.get(joins.size()-1);
                s += " JOIN "+temp_f[i+1];
                joins.add(s);
                String[] t2 = temp_f[i+1].split(" ON ");
                tabelas.add(t2[0].replace(" ", ""));
            }
        }
        
        
            
        for(int i=0; i<joins.size(); i++){
            if(i<1){    //na primeira vez, é "tab1 JOIN tab2 ON tab1.x=tab2.x"
                String s = joins.get(i).substring(joins.get(i).indexOf("ON")+3);
                if(!s.contains(" = "))
                    return "Sem ' = ' no ON do JOIN...";
                String ss[] = s.split(" = ");
                for(int j=0; j<ss.length; j++){
                    if(ss[j].trim().contains(" ")){
                        return "Erro na clausula ON...";
                    }
                    if((ss[j].length() - ss[j].replace(".", "").length()) != 1){
                        return "Erro nas colunas da clausula ON...";
                    }
                    String[] sss = ss[j].split("[.]");
                    if(sss.length < 2){
                        return "Erro nas colunas da clausula ON...";
                    }
                    if(!tabelas.contains(sss[0])){
                        return "Tabela da clausula ON não encontrada...";
                    }
                }
            }else{      //a partir da segunda é o join anterior + " JOIN tabx ON tabx.colx = taby.coly"
                String[] st = joins.get(i).split(" JOIN ");
                String ssr = st[st.length-1];
                String s = ssr.substring(ssr.indexOf("ON")+3);
                if(!s.contains(" = "))
                    return "Sem ' = ' no ON do JOIN...";
                String ss[] = s.split(" = ");
                for(int j=0; j<ss.length; j++){
                    if(ss[j].trim().contains(" ")){
                        return "Erro na clausula ON...";
                    }
                    if((ss[j].length() - ss[j].replace(".", "").length()) != 1){
                        return "Erro nas colunas da clausula ON...";
                    }
                    String[] sss = ss[j].split("[.]");
                    if(sss.length < 2){
                        return "Erro nas colunas da clausula ON...";
                    }
                    if(!tabelas.contains(sss[0])){
                        //precisa adicionar todas as tabelas antes desse ponto!
                        //return "Tabela da clausula ON não encontrada...";
                    }
                }
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
        for(int i=0; i<colunas.size(); i++){
            if((colunas.get(i).length() - colunas.get(i).replace(".", "").length()) > 1)
                return "Coluna inválida na clausula 'SELECT'";
            if(colunas.get(i).trim().contains(" "))
                return "Coluna inválida na clausula 'SELECT'";
        }
        
        if(where!=null){
            if(where.contains("(")){
                int np = 0;
                for(int i=0; i<where.length(); i++){
                    if(where.substring(i, i+1).equals("(")){
                        abreP.add(i);
                        np++;
                    }
                    if(where.substring(i, i+1).equals(")")){
                        np--;
                        fechaP.add(i);
                        if(np<0){
                            return "Erro na utilização do parênteses";
                        }
                    }
                }
                if(np!=0){
                    return "Erro na utilização do parênteses";
                }
                
                String s = where;
                
                int i=0;
                int j=0;
                while(abreP.size()!=0){
                    for(int v=0; v<abreP.size(); v++){
                    }
                    String ss = s.substring(abreP.get(i)+1,fechaP.get(0));
                    if(!ss.contains("(")){
                        parenteses.add(ss);
                        //s = s.substring(0,abreP.get(i))+" '"+String.valueOf(parenteses.size()-1)+"' "+s.substring(fechaP.get(0)+1,s.length()-1);
                        int dif = s.length();
                        s = s.replace("("+ss+")", " @"+String.valueOf(parenteses.size()-1)+"@ ");
                        dif-=s.length();
                        for(int k=1; k<fechaP.size(); k++){
                            if(k>i){
                                int x = abreP.get(k);
                                x-=dif;
                                abreP.set(k, x);
                            }
                            int y = fechaP.get(k);
                            y-=dif;
                            fechaP.set(k, y);
                        }
                        abreP.remove(i);
                        fechaP.remove(0);
                        i=0;
                        j++;
                    }else{
                        i++;
                    }
                }
            }else{
                if(where.contains(")")){
                    return "Erro na utilização do parênteses";
                }
            }
        }
        for(int i=0; i<parenteses.size(); i++){
            String[] s = parenteses.get(i).split(" AND | OR ");
            for(int j=0; j<s.length; j++){
                System.out.println("S=="+s[j]);
                if(!s[j].contains("@")){
                    if(!(s[j].contains(" > ") || s[j].contains(" < ") || s[j].contains(" = "))){
                        return "Erro nos operadores na cláusula WHERE";
                    }
                    String[] ss = s[j].split(" > | < | = ");
                    if(ss.length != 2){
                        return "Erro nos operadores na cláusula WHERE";
                    }
                    System.out.println("SS[0]=="+ss[0]);
                    System.out.println("SS[1]=="+ss[1]);
                    if(!(ss[0].contains(".") || ss[1].contains("."))){
                        return "Erro nas tabelas presentes na cláusula WHERE";
                    }                    
                    int tab = 0;
                    if(ss[0].contains("'") || ss[0].contains("\"")){
                        String ls = ss[0].replace("'","").replace("\"","");
                        if(ss[0].length()-ls.length()!=2){
                            return "Erro nos operadores na claúsula WHERE";
                        }
                    }else{
                        if(ss[0].contains(".")){
                            String ls = ss[0].replace(".","");
                            if(ss[0].length()-ls.length()!=1){
                                return "Erro nos operadores na claúsula WHERE";
                            }
                            String[] sss = ss[0].split("[.]");
                            if(sss.length!=2){
                                return "Erro nos operadores na claúsula WHERE";
                            }
                            if(!sss[0].matches("[0-9]+")){
                                if(!tabelas.contains(sss[0].trim())){
                                    return "Erro nas tabelas presentes na cláusula WHERE";
                                }else{
                                    tab = 1;
                                }  
                            }else{
                                if(!sss[1].matches("[0-9]+")){
                                    return "Erro nos operadores na claúsula WHERE";
                                }
                            }
                        }else{
                            if(!ss[1].matches("[0-9]+")){
                                return "Erro nos operadores na claúsula WHERE";
                            }
                        }
                    }
                    if(ss[1].contains("'") || ss[1].contains("\"")){
                        String ls = ss[1].replace("'","").replace("\"","");
                        if(ss[1].length()-ls.length()!=2){
                            return "Erro nos operadores na claúsula WHERE";
                        }
                    }else{
                        if(ss[1].contains(".")){
                            String ls = ss[1].replace(".","");
                            if(ss[1].length()-ls.length()!=1){
                                return "Erro nos operadores na claúsula WHERE";
                            }
                            String[] sss = ss[1].split("[.]");
                            if(sss.length!=2){
                                return "Erro nos operadores na claúsula WHERE";
                            }
                            if(!sss[0].matches("[0-9]+")){
                                if(!tabelas.contains(sss[0].trim())){
                                    return "Erro nas tabelas presentes na cláusula WHERE";
                                }else{
                                    tab = 1;
                                }
                            }else{
                                if(!sss[1].matches("[0-9]+")){
                                    return "Erro nos operadores na claúsula WHERE";
                                }
                            }
                        }else{
                            if(!ss[1].matches("[0-9]+")){
                                return "Erro nos operadores na claúsula WHERE";
                            }
                        }
                    }
                    if(tab == 0){
                        return "Erro nas tabelas presentes na cláusula WHERE";
                    }
                }
            }
        }
        if(parenteses.size()==0){
            String[] s = where.split(" AND | OR ");
            for(int j=0; j<s.length; j++){
                if(!(s[j].contains(" > ") || s[j].contains(" < ") || s[j].contains(" = "))){
                    return "Erro nos operadores na cláusula WHERE";
                }
                String[] ss = s[j].split(" > | < | = ");
                if(!(ss[0].contains(".") || ss[1].contains("."))){
                    return "Erro nas tabelas presentes na cláusula WHERE";
                }
                if(ss[0].contains(".")){
                    String[] sss = ss[0].split("[.]");
                    if(!tabelas.contains(sss[0].trim())){
                        return "Erro nas tabelas presentes na cláusula WHERE";
                    }                            
                }
                if(ss[1].contains(".")){
                    String[] sss = ss[1].split("[.]");
                    if(!tabelas.contains(sss[0].trim())){
                        return "Erro nas tabelas presentes na cláusula WHERE";
                    }                            
                }
            }
            
        }
        
        for(int i=0; i<tabelas.size(); i++){
            System.out.println("T="+tabelas.get(i));
        }
        for(int i=0; i<colunas.size(); i++){
            System.out.println("C="+colunas.get(i));
        }
        for(int i=0; i<joins.size(); i++){
            System.out.println("J="+joins.get(i));
        }
        for(int i=0; i<parenteses.size(); i++){
            System.out.println("P="+parenteses.get(i));
        }
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
