package implementacaobd;

import java.util.ArrayList;

public class Consulta {
    
    public static void main(String[] args) {
        String s = "\"abc'";
        System.out.println("S="+s);
        String ls = s.replace("'","");
        System.out.println("SL="+s.length());
        System.out.println("SLS="+ls.length());
        
                            
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
    private Arvore arvOriginal;
    private Arvore arvOtimizada;
    
    public Consulta(String consulta){
        this.consulta = consulta.trim().toUpperCase();
        
        colunas = new ArrayList();
        tabelas = new ArrayList();
        joins = new ArrayList();
        parenteses = new ArrayList();
        wheres = new ArrayList();
        abreP = new ArrayList();
        fechaP = new ArrayList();
        arvOriginal = null;
        arvOtimizada = null;
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
            if(s1[1].contains(" WHERE")){
                return "Erro na clausula WHERE";
            }
        }else{
            String[] s2 = s1[1].split(" WHERE ");
            if(s2.length != 2){
                return "Erro na clausula WHERE";
            }
            from = s2[0];
            where = s2[1];
        }
        
        
        
        
        if(!from.contains(" JOIN ")){
            tabelas.add(from.trim());
        }else{
            if(!from.contains(" ON ")){
                return "Clausula 'ON' não encontrada";
            }
            String[] s4 = from.split(" JOIN ");
            tabelas.add(s4[0].trim());
            for(int i=1; i<s4.length; i++){
                String[] s5 = s4[i].split(" ON ");
                tabelas.add(s5[0].trim());
            }
        }
        joins = new ArrayList<>();
        String[] temp_f = from.split(" JOIN ");
        for(int i=0; i<temp_f.length-1; i++){
            if(i==0){
                String s = temp_f[i]+" JOIN "+temp_f[i+1];
                //joins.add(s);
                String[] t2 = temp_f[i+1].split(" ON ");
                joins.add(t2[1]);
                //tabelas.add(temp_f[i].replace(" ",""));
                //tabelas.add(t2[0].replace(" ", ""));
            }else{
                //String s = joins.get(joins.size()-1);
                String s = " JOIN "+temp_f[i+1];
                //joins.add(s);
                String[] t2 = temp_f[i+1].split(" ON ");
                joins.add(t2[1]);
                //tabelas.add(t2[0].replace(" ", ""));
            }
        }
            
        for(int i=0; i<joins.size(); i++){
            if(!joins.get(i).contains(" = "))
                    return "Sem ' = ' no ON do JOIN...";
            String[] s = joins.get(i).split(" AND ");
            for(int j=0; j<s.length; j++){
                String[] ss = s[j].split(" = ");
                if(ss.length!=2){
                    return "Erro na clausula ON..."; 
                }
                for(int k=0; k<ss.length; k++){
                    if((ss[k].length() - ss[k].replace(".", "").length() != 1)){
                        return "Erro nas colunas da clausula ON...";
                    }
                    String[] sss = ss[k].split("[.]");
                    if(sss.length < 2){
                        return "Erro nas colunas da clausula ON...";
                    }
                    int x = 0;
                    for(int y=0; y<i+2; y++){
                        if(sss[0].equals(tabelas.get(y))){
                            x = 1;
                        }
                    }
                    if(x==0){
                        return "Tabela da clausula ON não encontrada...";
                    }
                }
            }
            /*String[] ss = joins.get(i).split(" = ");
            for(int j=0; j<ss.length; j++){
                if(ss[j].trim().contains(" ")){
                    return "Erro na clausula ON...";
                }
                if((ss[j].length() - ss[j].replace(".", "").length()) != 1){
                    return "Erro nas colunas da clausula ON...";
                }
                System.out.println("SS[j]"+ss[j]);
                String[] sss = ss[j].split("[.]");
                if(sss.length < 2){
                    return "Erro nas colunas da clausula ON...";
                }
                System.out.println("SSS[0]"+sss[0]);
                if(!tabelas.contains(sss[0])){
                    return "Tabela da clausula ON não encontrada...";
                }
            }
            if(i<1){    //na primeira vez, é "tab1 JOIN tab2 ON tab1.x=tab2.x"
                String[] s = joins.get(i).split(" ON ");
                if(!s[1].contains(" = "))
                    return "Sem ' = ' no ON do JOIN...";
                String ss[] = joins.get(i).split(" = ");
                for(int j=0; j<ss.length; j++){
                    if(ss[j].trim().contains(" ")){
                        return "Erro na clausula ON...";
                    }
                    if((ss[j].length() - ss[j].replace(".", "").length()) != 1){
                        return "Erro nas colunas da clausula ON...";
                    }
                    System.out.println("SS[j]"+ss[j]);
                    String[] sss = ss[j].split("[.]");
                    if(sss.length < 2){
                        return "Erro nas colunas da clausula ON...";
                    }
                    System.out.println("SSS[0]"+sss[0]);
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
            }*/
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
            colunas.add(s6[i].trim());
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
                String[] ss = s.split(" AND | OR ");
                for(int x=0; x<ss.length; x++){
                    if(!ss[x].contains("@")){
                        wheres.add(ss[x]);
                    }
                }
            }else{
                if(where.contains(")")){
                    return "Erro na utilização do parênteses";
                }
                String[] ss = where.split(" AND | OR ");
                for(int x=0; x<ss.length; x++){
                    if(!ss[x].contains("@")){
                        wheres.add(ss[x]);
                    }
                }
            }
        }
        for(int i=0; i<parenteses.size(); i++){
            String[] s = parenteses.get(i).split(" AND | OR ");
            for(int j=0; j<s.length; j++){
                if(!s[j].contains("@")){
                    wheres.add(s[j]);
                }
            }
        }
        for(int j=0; j<wheres.size(); j++){
            if(!(wheres.get(j).contains(" > ") || wheres.get(j).contains(" < ") || wheres.get(j).contains(" = "))){
                return "Erro nos operadores na cláusula WHERE";
            }
            String[] ss = wheres.get(j).split(" > | < | = ");
            if(ss.length != 2){
                return "Erro nos operadores na cláusula WHERE";
            }
            if(!(ss[0].contains(".") || ss[1].contains("."))){
                return "Erro nas tabelas presentes na cláusula WHERE";
            }                    
            int tab = 0;
            if(ss[0].contains("'")){
                String ls = ss[0].replace("'","");
                if(ss[0].length()-ls.length()!=2){
                    return "Erro nos operadores na claúsula WHERE";
                }

            }else{
                if(ss[0].contains("\"")){
                    String ls = ss[0].replace("\"","");
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
            }
            if(ss[1].contains("'")){
                String ls = ss[1].replace("'","");
                if(ss[1].length()-ls.length()!=2){
                    return "Erro nos operadores na claúsula WHERE";
                }
            }else{
                if(ss[1].contains("\"")){
                    String ls = ss[1].replace("\"","");
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
            }
            if(tab == 0){
                return "Erro nas tabelas presentes na cláusula WHERE";
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
        for(int i=0; i<wheres.size(); i++){
            System.out.println("W="+wheres.get(i));
        }
        return "OK";
    }
    
    public void geraAlgebraOriginal(){
        if(tabelas.size() == 1){
            arvOriginal = new Arvore("FROM",tabelas.get(0));
        }else{
            for(int i=0; i<joins.size(); i++){
                Arvore j = arvOriginal;
                arvOriginal = new Arvore("JOIN",joins.get(i));
                Arvore a = new Arvore("FROM",tabelas.get(i+1));
                if(i==0){
                    Arvore b = new Arvore("FROM",tabelas.get(0));
                    arvOriginal.addFilho(b,a);
                }else{
                    arvOriginal.addFilho(j,a);
                }
            }
        }
        Arvore j = arvOriginal;
        arvOriginal = new Arvore("WHERE", where);
        arvOriginal.addFilho(j);
        j = arvOriginal;
        arvOriginal = new Arvore("SELECT", select);
        arvOriginal.addFilho(j);
    }
    
    public void printaArvoreOriginal(){
        int x = alturaArvore(arvOriginal);
        int d = getDistArvore(arvOriginal);
        int y = x;
        while(y>0){
            System.out.println(levelArv(arvOriginal,y,d));
            y--;
        }
    }
    
    private String levelArv(Arvore a, int x, int d){
        if(a == null){
            return "";
        }
        if(x == alturaArvore(a)){
            String s = "";
            for(int i=0; i<d; i++){
                s+="     ";
            }
            s+=a.getOperador()+" "+a.getTexto()+"\n";
            return s;
        }
        int dd = d;
        if(a.getDir()!=null)
            dd = dd - 1;
        return levelArv(a.getEsq(), x, dd) + levelArv(a.getDir(), x, d+1);
    }
    
    private int getDistArvore(Arvore a){
        if(a == null){
            return 0;
        }
        return a.getDist()+getDistArvore(a.getEsq());
    }
    
    private int alturaArvore(Arvore a){
        if(a == null){
            return 0;
        }
        int x = alturaArvore(a.getEsq());
        int y = alturaArvore(a.getEsq());
        if(x>y){
            return x+1;
        }else{
            return y+1;
        }
    }
    
    public void mostraArvoreOriginal(){
        PainelScroll p = new PainelScroll(arvOriginal);
    }
    
    public void mostraArvoreOtimizada(){
        PainelScroll p = new PainelScroll(arvOtimizada);
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
