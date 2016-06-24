package implementacaobd;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;

public class Consulta {
    
    public static void main(String[] args) {
        String s = "@3@ n";
        System.out.println(s.split("@").length);
        
                            
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
    private ArrayList<ArrayList<String>> colsSelect;
    private ArrayList<ArrayList<String>> colsWhere;
    private ArrayList<ArrayList<String>> colsJoin;
    private ArrayList<ArrayList<String>> whereAllBefore;
    private ArrayList<String> whereAfter;
    private ArrayList<String> whereBefore;
    
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
        colsWhere = new ArrayList<>();
        colsSelect = new ArrayList<>();
        colsJoin = new ArrayList<>();
        whereBefore = new ArrayList<>();
        whereAfter = new ArrayList<>();
        whereAllBefore = new ArrayList();
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
            from = s1[1].replace("(", "").replace(")", "");
            if(s1[1].contains(" WHERE")){
                return "Erro na clausula WHERE";
            }
        }else{
            String[] s2 = s1[1].split(" WHERE ");
            if(s2.length != 2){
                return "Erro na clausula WHERE";
            }
            from = s2[0].replace("(", "").replace(")", "");;
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
                if(abreP.size()!=fechaP.size()){
                    return "Erro na utilização dos parênteses";
                }
                for(int i=0; i<abreP.size(); i++){
                    System.out.println("ABREP="+abreP.get(i));
                }
                for(int i=0; i<fechaP.size(); i++){
                    System.out.println("FECHAP="+fechaP.get(i));
                }
                String s = where;
                
                int i=0;
                int j=0;
                while(abreP.size()!=0){
                    System.out.println("ABREP S="+abreP.size());
                    System.out.println("FECHAP S="+fechaP.size());
                    for(int k=0; k<abreP.size(); k++){
                        System.out.println("ABREP="+abreP.get(k));
                    }
                    for(int k=0; k<fechaP.size(); k++){
                        System.out.println("FECHAP="+fechaP.get(k));
                    }
                    for(int v=0; v<abreP.size(); v++){
                    }
                    System.out.println("I======"+i);
                    String ss = s.substring(abreP.get(i)+1,fechaP.get(0));
                    System.out.println("SS==="+ss);
                    if(!ss.contains("(")){
                        System.out.println("ENTRA");
                        parenteses.add(ss);
                        //s = s.substring(0,abreP.get(i))+" '"+String.valueOf(parenteses.size()-1)+"' "+s.substring(fechaP.get(0)+1,s.length()-1);
                        int dif = s.length();
                        System.out.println("DIF ANTES=="+dif);
                        System.out.println("S ANTES == "+s);
                        s = s.replaceFirst("\\("+ss+"\\)", " @"+String.valueOf(parenteses.size()-1)+"@ ");
                        System.out.println("S DEPOIS == "+s);
                        dif-=s.length();
                        System.out.println("DIF DEPOIS=="+dif);
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
                parenteses.add(s);
                /*String[] ss = s.split(" AND | OR ");
                for(int x=0; x<ss.length; x++){
                    if(!ss[x].contains("@")){
                        wheres.add(ss[x]);
                    }
                }*/
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
                }else{
                    String[] sss = s[j].trim().split("@");
                    if(sss.length != 2)
                        return "Erro na clausula WHERE";
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
            if(ss[0].trim().contains(" ") || ss[1].trim().contains(" ")){
                return "Erro na clausula WHERE";
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
                System.out.println("NO "+arvOriginal.getOperador());
                System.out.println("ESQ "+arvOriginal.getEsq().getOperador());
                System.out.println("DIR "+arvOriginal.getDir().getOperador());
            }
        }
        Arvore j = arvOriginal;
        if(wheres.size()!=0){
            arvOriginal = new Arvore("WHERE", where);
            arvOriginal.addFilho(j);
            j = arvOriginal;
        }
        arvOriginal = new Arvore("SELECT", select);
        arvOriginal.addFilho(j);
        setDadosArvore(arvOriginal);
    }
    
    private void printaDadosArvores(Arvore a){
        if(a == null){
            return;
        }
        System.out.println(a.getOperador()+"   "+a.getTexto());
        System.out.println("    Altura="+a.getAlt());
        System.out.println("    Margem="+a.getMargem());
        System.out.println("    Nivel="+a.getNivel());
        printaDadosArvores(a.getEsq());
        printaDadosArvores(a.getDir());
    }
    
    private void setDadosArvore(Arvore a){
        int altura = alturaArvore(a);
        setMargemArvore(a,getDistArvore(a));
        setAlturaArvore(a, 0);
        setNivelArvore(a, altura);
        printaDadosArvores(a);
    }
    
    private void setAlturaArvore(Arvore a, int alt){
        if(a == null){
            return;
        }
        System.out.println(a.getOperador() + alturaArvore(a));
        a.setAlt(alt);
        setAlturaArvore(a.getEsq(), alt+1);
        setAlturaArvore(a.getDir(), alt+1);
    }
    
    private void setMargemArvore(Arvore a, int dd){
        if(a == null){
            return;
        }
        a.setMargem(dd);
        if(a.getDir() == null){
            setMargemArvore(a.getEsq(), dd);
        }else{
            setMargemArvore(a.getEsq(), dd-1);
            setMargemArvore(a.getDir(), dd+1);
        }
    }
    
    private void setNivelArvore(Arvore a, int alt){
        int dd = 0;
        for(int i=0; i<alt; i++){
            dd = setNivelAltura(a,i,dd);
        }
    }
    
    private int setNivelAltura(Arvore a, int alt, int dd){
        if(a==null){
            return dd;
        }
        if(a.getAlt()==alt){
            a.setNivel(dd);
            return dd+1;
        }
        int r = setNivelAltura(a.getEsq(), alt, dd);
        return setNivelAltura(a.getDir(), alt, r);
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
        int y = alturaArvore(a.getDir());
        if(x>y){
            return x+1;
        }else{
            return y+1;
        }
    }
    
    public void geraArvoreOtimizada(){
        for(int i=0; i<tabelas.size(); i++){
            colsSelect.add(new ArrayList<>());
            colsWhere.add(new ArrayList<>());
            colsJoin.add(new ArrayList<>());
            whereAllBefore.add(new ArrayList<>());
        }
        for(int i=0; i<colunas.size(); i++){
            String[] s = colunas.get(i).trim().split("[.]");
            for(int j=0; j<tabelas.size(); j++){
                if(s[0].equalsIgnoreCase(tabelas.get(j))){
                    colsSelect.get(j).add(colunas.get(i));
                }
            }
        }
        for(int i=0; i<joins.size(); i++){
            String[] s = joins.get(i).trim().split(" = ");
            String[] ss1 = s[0].trim().split("[.]");
            String[] ss2 = s[1].trim().split("[.]");
            for(int j=0; j<tabelas.size(); j++){
                if(ss1[0].equalsIgnoreCase(tabelas.get(j))){
                    colsSelect.get(j).add(s[0]);
                }
                if(ss2[0].equalsIgnoreCase(tabelas.get(j))){
                    colsSelect.get(j).add(s[1]);
                }
            }
        }
        
        verificaParentese(parenteses.get(parenteses.size()-1));
        
        for(int i=0; i<whereBefore.size(); i++){
            for(int j=0; j<tabelas.size(); j++){
                if(whereBefore.get(i).contains(tabelas.get(j)+".")){
                    whereAllBefore.get(j).add(whereBefore.get(i));
                    getColunasWhere(whereBefore.get(i),j);
                }
            }
        }
        
        
        
        for(int i=0; i<whereBefore.size(); i++){
            System.out.println("WHEREBEFORE== "+whereBefore.get(i));
        }
        for(int i=0; i<whereAfter.size(); i++){
            System.out.println("WHEREAFTER== "+whereAfter.get(i));
        }
        
        if(tabelas.size() == 1){
            arvOtimizada = getBranchTable(0);
        }else{
            for(int i=0; i<joins.size(); i++){
                Arvore j = arvOtimizada;
                arvOtimizada = new Arvore("JOIN",joins.get(i));
                Arvore a = getBranchTable(i+1);
                if(i==0){
                    Arvore b = getBranchTable(0);
                    arvOtimizada.addFilho(b,a);
                }else{
                    arvOtimizada.addFilho(j,a);
                }
                System.out.println("NO "+arvOtimizada.getOperador());
                System.out.println("ESQ "+arvOtimizada.getEsq().getOperador());
                System.out.println("DIR "+arvOtimizada.getDir().getOperador());
            }
        }
        String whe = "";
        for(int i=0; i<whereAfter.size(); i++){
            if(!whe.equals("")){
                whe+=" AND ";
            }
            whe += whereAfter.get(i);
        }
        if(!whe.equalsIgnoreCase("")){
            Arvore j = arvOtimizada;
            arvOtimizada = new Arvore("WHERE", whe);
            arvOtimizada.addFilho(j);
        }
        Arvore k = arvOtimizada;
        arvOtimizada = new Arvore("SELECT", select);
        arvOtimizada.addFilho(k);
        setDadosArvore(arvOtimizada);
        
        
        
        /*for(int i=0; i<parenteses.size(); i++){
            if(parenteses.get(i).contains(" OR ")){
                String[] s = parenteses.get(i).trim().split(" AND ");
                for(int j=0; j<s.length; j++){
                    if(s[j].contains(" OR ")){
                        ArrayList<String> tabProv = new ArrayList();
                        String[] ss = s[j].trim().split(" OR ");
                        for(int k=0; k<ss.length; k++){
                            String[] sss = ss[k].trim().split(" > | < | = ");
                            if(!(sss[0].contains("\"") || sss[0].contains("'"))){
                                String[] ssss = sss[0].split("[.]");
                                if(ssss.length == 2){
                                    if(!ssss[0].matches("[0-9]+")){
                                        tabProv.add(ssss[0].trim());
                                    }
                                    if(!ssss[1].matches("[0-9]+")){
                                        tabProv.add(ssss[1].trim());
                                    }
                                }
                            }
                            if(!(sss[1].contains("\"") || sss[1].contains("'"))){
                                String[] ssss = sss[1].split("[.]");
                                if(ssss.length == 2){
                                    if(!ssss[0].matches("[0-9]+")){
                                        tabProv.add(ssss[0].trim());
                                    }
                                    if(!ssss[1].matches("[0-9]+")){
                                        tabProv.add(ssss[1].trim());
                                    }
                                }
                            }
                        }
                        int x = 0;
                        while (x<tabProv.size()-1){
                            for(int k=x; k<tabProv.size(); k++){
                                if(!tabProv.get(x).trim().equalsIgnoreCase(tabProv.get(k).trim())){
                                    k=tabProv.size();
                                    x=k;
                                    whereWithOR.add(s[j]);
                                }
                            }
                        }
                        String ret = "";
                        for(int k=0; k<s.length; k++){
                            if(k!=j){
                                if(ret.equalsIgnoreCase("")){
                                    ret+=s[k];
                                }else{
                                    ret+=" AND "+s[k];
                                }
                            }
                        }
                        parenteses.remove(i);
                        parenteses.add(ret);
                        i-=2;
                    }
                }
            }
        }*/
        //getWheres();
    }
    
    private void getColunasWhere(String str, int x){
        String[] s = str.split(" OR ");
        
        for(int i=0; i<s.length; i++){
            String[] ss = s[i].split(" > | < | = ");
            if(!(ss[0].contains("'") || ss[0].contains("\""))){
                if(!ss[0].replace("[.]", "").matches("[0-9]+")){
                    colsWhere.get(x).add(ss[0]);
                }
            }
            if(!(ss[1].contains("'") || ss[1].contains("\""))){
                if(!ss[1].replace("[.]", "").matches("[0-9]+")){
                    colsWhere.get(x).add(ss[1]);
                }
            }
        }
    }
    
    private Arvore getBranchTable(int x){
        Arvore a = new Arvore("FROM",tabelas.get(x));
        String sel = "";
        Set<String> hs = new HashSet<>();
        hs.addAll(colsSelect.get(x));
        hs.addAll(colsJoin.get(x));
        hs.addAll(colsWhere.get(x));
        colsSelect.get(x).clear();
        colsSelect.get(x).addAll(hs);
        for(int i=0; i<colsSelect.get(x).size(); i++){
            if(!sel.equalsIgnoreCase("")){
                sel+=", ";
            }
            sel += colsSelect.get(x).get(i);
        }
        if(!sel.equalsIgnoreCase("")){
            Arvore b = a;
            a = new Arvore("SELECT",sel);
            a.addFilho(b);
        }
        String whe = "";
        for(int i=0; i<whereAllBefore.get(x).size(); i++){
            if(!whe.equalsIgnoreCase("")){
                whe+=" AND ";
            }
            whe+=whereAllBefore.get(x).get(i);
        }
        if(!whe.equalsIgnoreCase("")){
            Arvore b = a;
            a = new Arvore("WHERE",whe);
            a.addFilho(b);
        }
        return a;
    }
    
    private void verificaParentese(String p){
        String[] s = p.trim().split(" AND ");
        for(int i=0; i<s.length; i++){
            verificaAnd(s[i]);
        }
    }
    
    private void verificaAnd(String p){
        System.out.println("P=="+p);
        if(!p.contains(" OR ")){
            if(p.contains("@")){
                verificaParentese(parenteses.get(Integer.valueOf(p.replace("@", "").trim())));
            }else{
                String tab1 = "";
                String tab2 = "";
                String[] s = p.split(" > | < | = ");
                System.out.println("s[0]==="+s[0]);
                System.out.println("s[1]==="+s[1]);
                if(!(s[0].contains("'") || s[0].contains("\""))){
                    System.out.println("ENTRA S[0]");
                    if(!s[0].replace("[.]","").matches("[0-9]+")){
                        System.out.println("ENTRA 2 S[0]");
                        String[] ss = s[0].split("[.]");
                        tab1 = ss[0];
                        for(int i=0; i<tabelas.size(); i++){
                            if(ss[0].equalsIgnoreCase(tabelas.get(i))){
                                colsWhere.get(i).add(s[0]);
                            }
                        }
                    }
                }
                if(!(s[1].contains("'") || s[1].contains("\""))){
                    System.out.println("ENTRA S[1]");
                    if(!s[1].replace("[.]","").matches("[0-9]+")){
                        System.out.println("ENTRA 2 S[1]");
                        String[] ss = s[1].split("[.]");
                        tab2 = ss[0];
                        for(int i=0; i<tabelas.size(); i++){
                            if(ss[0].equalsIgnoreCase(tabelas.get(i))){
                                colsWhere.get(i).add(s[1]);
                            }
                        }
                    }
                }
                if(!tab1.equalsIgnoreCase("")){
                    if(tab2.equalsIgnoreCase("")){
                        whereBefore.add(p);
                    }else{
                        if(tab1.equalsIgnoreCase(tab2)){
                            whereBefore.add(p);
                        }else{
                            whereAfter.add(p);
                        }
                    }
                }
            }
        }else{
            verificaOr(p);
        }
    }
    
    private void verificaOr(String p){
        System.out.println("P===="+p);
        String[] s = p.split(" OR ");
        System.out.println("S[0]="+s[0]);
        System.out.println("S[1]="+s[1]);
        ArrayList<String> first = verificaLadoOr(s[0]);
        System.out.println("FIRST.SIZE="+first.size());
        Set<String> hs = new HashSet<>();
        hs.addAll(first);
        first.clear();
        first.addAll(hs);
        
        if(first.size()>1){
            whereAfter.add(p);
            return;
        }
        
        for(int i=1; i<s.length; i++){
            ArrayList<String> next = verificaLadoOr(s[1]);
            hs = new HashSet<>();
            hs.addAll(next);
            next.clear();
            next.addAll(hs);
            
            if(next.size()>1){
                whereAfter.add(p);
                return;
            }
            if(first.get(0).equals(next.get(0))){
                whereBefore.add(p);
                return;
            }
        }
        whereAfter.add(p);
    }
    
    private ArrayList verificaLadoOr(String p){
        ArrayList<String> ret = new ArrayList();
        String[] s = p.split("@");
        String f = "";
        for(int i=0; i<s.length; i++){
            if(s[i].matches("[0-9]+")){
                f+=" "+parenteses.get(Integer.valueOf(s[i].replace("@", "").trim()))+" ";
            }else{
                f+=s[i]+" ";
            }
        }
        System.out.println("F==="+f);
        for(int i=0; i<tabelas.size(); i++){
            if(f.trim().contains(tabelas.get(i)+".")){
                ret.add(tabelas.get(i));
            }
        }
        String[] ss = f.split(" AND | OR ");
        System.out.println("\n\n\nF===="+f+"\n\n\n");
        for(int i=0; i<ss.length; i++){
            System.out.println("\n\n\nSS===="+ss[i]+"\n\n\n");
            String[] sss = ss[i].split(" > | < | = ");
            for(int j=0; j<sss.length; j++){
                System.out.println("\n\n\nSSS==="+sss[i]+"\n\n\n");
                if(!(sss[i].contains("'") || sss[i].contains("\""))){
                    if(!sss[i].replace(".", "").matches("[0-9]+")){
                        String[] ssss = sss[i].split("[.]");
                        for(int k=0; k<tabelas.size(); k++){
                            if(ssss[0].equalsIgnoreCase(tabelas.get(k))){
                                colsWhere.get(k).add(sss[i]);
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }
    /*
    private void getWheres(){
        for(int i=0; i<parenteses.size(); i++){
            String[] s = parenteses.get(i).split(" AND ");
            for(int j=0; j<s.length; j++){
                if(s[j].contains(" OR ")){
                    String[] ss = s[j].split(" OR ");
                    ArrayList bef;
                    ArrayList aft;
                    if(ss[0].trim().endsWith("@")){
                        bef = parenteseContain(ss[0]);
                    }else{
                        bef = new ArrayList();
                        for(int k=0; k<tabelas.size(); k++){
                            if(ss[0].contains(" "+tabelas.get(k)+".")){
                                bef.add(tabelas.get(k));
                            }
                        }
                    }
                    Set<String> hs = new HashSet<>();
                    hs.addAll(bef);
                    bef.clear();
                    bef.addAll(hs);
                    if(bef.size() > 1){
                        String x = "";
                    }
                    for(int k=1; k<ss.length; k++){
                        if(ss[1].trim().contains("@")){
                            aft = parenteseContain(ss[1]);
                        }else{
                            aft = new ArrayList();
                            for(int l=0; l<tabelas.size(); l++){
                                if(ss[1].contains(" "+tabelas.get(l)+".")){
                                    aft.add(tabelas.get(l));
                                }
                            }
                        }
                        
                    }
                    
                }
            }
        }
    }*/
    
    private ArrayList parenteseContain(String p){
        if(p.contains("@")){
            String[] ss = p.split("@");
            for(int i=0; i<ss.length; i++){
                if(ss[i].length() == 1 && ss[i].matches("[0-9]+")){
                    String sss = parenteses.get(Integer.valueOf(ss[i]));
                    return parenteseContain(p.replace("@"+ss[i]+"@", sss));
                }
            }
        }
        ArrayList<String> list = new ArrayList<>();
        for(int i=0; i<tabelas.size(); i++){
            if(p.contains(" "+tabelas.get(i)+".")){
                list.add(tabelas.get(i));
            }
        }
        return list;
    }
    
    public void mostraArvoreOriginal(){
        PainelArvore p = new PainelArvore(arvOriginal);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        p.setSize(dimension);
        p.setExtendedState(p.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        p.setVisible(true);
    }
    
    public void mostraArvoreOtimizada(){
        PainelArvore p = new PainelArvore(arvOtimizada);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        p.setSize(dimension);
        p.setExtendedState(p.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        p.setVisible(true);
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
