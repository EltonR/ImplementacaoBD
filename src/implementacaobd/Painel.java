package implementacaobd;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.JPanel;
 
public class Painel extends JPanel{
    private Arvore arv;
    
    public Painel(Arvore arv){
        this.arv = arv;
    }
    
    public void desenhar(){
        paint(getGraphics());
    }
 
    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
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
    
    private void drawNo(Arvore a, Graphics2D g2d, int y, int d, int x){
        if(a==null){
            return;
        }
        if(y == alturaArvore(a)){
            g2d.setStroke(new BasicStroke(5f));
            g2d.drawString(a.getOperador(),30*d,((x-y)*20)+100);
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawString(a.getTexto(),30*d+10,((x-y)*20+10)+100);
        }
        int dd = d;
        if(a.getDir()!=null)
            dd = dd - 1;
        drawNo(a.getEsq(), g2d, y, dd, x);
        drawNo(a.getDir(), g2d, y, d+1, x);
    } 
    
    private void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        int x = alturaArvore(arv);
        int d = getDistArvore(arv);
        int y = x;
        while(y>0){
            drawNo(arv, g2d, y, d, x);
            y--;
        }
        
        
        
        /*
        int n = (getWidth()-20)/30;
        for(int i=0; i<=n; i++){
            g2d.drawLine(10+30*i, 10, 10+30*i, getHeight()-10);
        }
        g2d.drawLine(10, 10, 10+n*30, 10);
        g2d.drawLine(10, getHeight()-10, 10+n*30, getHeight()-10);
        float[] dashingPattern1 = {1f, 1f};
        Stroke stroke1 = new BasicStroke(1f, BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_MITER, 1.0f, dashingPattern1, 1.0f);
        g2d.setStroke(stroke1);
        g2d.drawLine(10, getHeight()/2, 10+n*30, getHeight()/2);

        if (tipo!=0){
            int meio = getHeight()/2;
            int meioS = getHeight()/4;
            int meioI = getHeight()-getHeight()/4;
            
            
            g2d.setStroke(new BasicStroke(2f));
            g2d.setColor(Color.red);
            if(System.getProperty("java.version").startsWith("1.8")){
                codigo = codigo.substring(0,1)+codigo;
            }
            String[] c=codigo.split("");
            switch(tipo){
                case 1:
                    for(int i=1; i<c.length; i++){
                        g2d.drawString(c[i],10+30*(i-1)+14,10);
                        if(c[i].equalsIgnoreCase("0")){
                            g2d.drawLine(10+30*(i-1), meioS, 10+30*(i-1)+30, meioS);
                            if(i!=c.length-1){
                                if(c[i+1].equalsIgnoreCase("1")){
                                    g2d.drawLine(10+30*(i-1)+30,meioS,10+30*(i-1)+30,meioI);
                                }
                            }
                        }else{
                            if(c[i].equalsIgnoreCase("1")){
                                g2d.drawLine(10+30*(i-1), meioI, 10+30*(i-1)+30, meioI);
                                if(i!=c.length-1){
                                    if(c[i+1].equalsIgnoreCase("0")){
                                        g2d.drawLine(10+30*(i-1)+30,meioI,10+30*(i-1)+30,meioS);
                                    }
                                }
                            }
                        }
                    }
                break;
                case 2:
                    int u = 0;
                    for(int i=1; i<c.length; i++){
                        g2d.drawString(c[i],10+30*(i-1)+14,10);
                       if(i==1){
                           if(c[i].equalsIgnoreCase("0")){
                               g2d.drawLine(10, meioS, 40, meioS);
                           }else{
                               g2d.drawLine(10, meioS, 10, meioI);
                               g2d.drawLine(10, meioI, 40, meioI);
                               u=1;
                           }
                       }else{
                           if(c[i].equalsIgnoreCase("0")){
                               if(u==0){
                                   g2d.drawLine(10+30*(i-1), meioS, 10+30*(i-1)+30, meioS);
                               }else{
                                   g2d.drawLine(10+30*(i-1), meioI, 10+30*(i-1)+30, meioI);
                               }
                           }else{
                               if(u==0){
                                   g2d.drawLine(10+30*(i-1),meioS,10+30*(i-1),meioI);
                                   g2d.drawLine(10+30*(i-1), meioI, 10+30*(i-1)+30, meioI);
                                   u=1;
                               }else{
                                   g2d.drawLine(10+30*(i-1),meioI,10+30*(i-1),meioS);
                                   g2d.drawLine(10+30*(i-1), meioS, 10+30*(i-1)+30, meioS);
                                   u=0;
                               } 
                           }
                       } 
                    }
                break;
                case 3:
                    int x=0;
                    for(int i=1; i<c.length; i++){
                        g2d.drawString(c[i],10+30*(i-1)+14,10);
                        if(c[i].equalsIgnoreCase("0")){
                            g2d.drawLine(10+30*(i-1),meio,10+30*(i-1)+30, meio);
                            if(i!=c.length-1){
                                if(c[i+1].equalsIgnoreCase("1")){
                                    if(x==0){
                                        g2d.drawLine(10+30*(i-1)+30,meio,10+30*(i-1)+30, meioS);
                                    }else{
                                        g2d.drawLine(10+30*(i-1)+30,meio,10+30*(i-1)+30, meioI);
                                    }
                                }
                            }
                        }else{
                            if(x==0){
                                x=1;
                                g2d.drawLine(10+30*(i-1),meioS,10+30*(i-1)+30, meioS);
                                if(i!=c.length-1){
                                    if(c[i+1].equalsIgnoreCase("0")){
                                        g2d.drawLine(10+30*(i-1)+30,meioS,10+30*(i-1)+30, meio);
                                    }else{
                                        g2d.drawLine(10+30*(i-1)+30,meioS,10+30*(i-1)+30, meioI);
                                    }
                                }
                            }else{
                                x=0;
                                g2d.drawLine(10+30*(i-1),meioI,10+30*(i-1)+30, meioI);
                                if(i!=c.length-1){
                                    if(c[i+1].equalsIgnoreCase("0")){
                                        g2d.drawLine(10+30*(i-1)+30,meioI,10+30*(i-1)+30, meio);
                                    }else{
                                        g2d.drawLine(10+30*(i-1)+30,meioI,10+30*(i-1)+30, meioS);
                                    }
                                }
                            }
                        }
                    }
                break;
                case 4:
                    int y=0;
                    for(int i=1; i<c.length; i++){
                        g2d.drawString(c[i],10+30*(i-1)+14,10);
                        if(c[i].equalsIgnoreCase("1")){
                            g2d.drawLine(10+30*(i-1),meio,10+30*(i-1)+30, meio);
                            if(i!=c.length-1){
                                if(c[i+1].equalsIgnoreCase("0")){
                                    if(y==0){
                                        g2d.drawLine(10+30*(i-1)+30,meio,10+30*(i-1)+30, meioS);
                                    }else{
                                        g2d.drawLine(10+30*(i-1)+30,meio,10+30*(i-1)+30, meioI);
                                    }
                                }
                            }
                        }else{
                            if(y==0){
                                y=1;
                                g2d.drawLine(10+30*(i-1),meioS,10+30*(i-1)+30, meioS);
                                if(i!=c.length-1){
                                    if(c[i+1].equalsIgnoreCase("1")){
                                        g2d.drawLine(10+30*(i-1)+30,meioS,10+30*(i-1)+30, meio);
                                    }else{
                                        g2d.drawLine(10+30*(i-1)+30,meioS,10+30*(i-1)+30, meioI);
                                    }
                                }
                            }else{
                                y=0;
                                g2d.drawLine(10+30*(i-1),meioI,10+30*(i-1)+30, meioI);
                                if(i!=c.length-1){
                                    if(c[i+1].equalsIgnoreCase("1")){
                                        g2d.drawLine(10+30*(i-1)+30,meioI,10+30*(i-1)+30, meio);
                                    }else{
                                        g2d.drawLine(10+30*(i-1)+30,meioI,10+30*(i-1)+30, meioS);
                                    }
                                }
                            }
                        }
                    }
                break;
                case 5:
                    for(int i=1; i<c.length; i++){
                        g2d.drawString(c[i],10+30*(i-1)+14,10);
                        if(c[i].equalsIgnoreCase("0")){
                            g2d.drawLine(10+30*(i-1), meioS, 10+30*(i-1)+15, meioS);
                            g2d.drawLine(10+30*(i-1)+15, meioS, 10+30*(i-1)+15, meioI);
                            g2d.drawLine(10+30*(i-1)+15, meioI, 10+30*(i-1)+30, meioI);
                            if(i!=c.length-1){
                                if(c[i+1].equalsIgnoreCase("0")){
                                    g2d.drawLine(10+30*(i-1)+30, meioI, 10+30*(i-1)+30, meioS);
                                }
                            }
                        }else{
                            g2d.drawLine(10+30*(i-1), meioI, 10+30*(i-1)+15, meioI);
                            g2d.drawLine(10+30*(i-1)+15, meioI, 10+30*(i-1)+15, meioS);
                            g2d.drawLine(10+30*(i-1)+15, meioS, 10+30*(i-1)+30, meioS);
                            if(i!=c.length-1){
                                if(c[i+1].equalsIgnoreCase("1")){
                                    g2d.drawLine(10+30*(i-1)+30, meioS, 10+30*(i-1)+30, meioI);
                                }
                            }
                        }
                    }
                break;
                case 6:
                    int w=0;
                    for(int i=1; i<c.length; i++){
                        g2d.drawString(c[i],10+30*(i-1)+14,10);
                        if(c[i].equalsIgnoreCase("0")){
                            if(w==0){
                                g2d.drawLine(10+30*(i-1), meioS, 10+30*(i-1), meioI);
                                g2d.drawLine(10+30*(i-1), meioI, 10+30*(i-1)+15, meioI);
                                g2d.drawLine(10+30*(i-1)+15, meioI, 10+30*(i-1)+15, meioS);
                                g2d.drawLine(10+30*(i-1)+15, meioS, 10+30*(i-1)+30, meioS);
                            }else{
                                g2d.drawLine(10+30*(i-1), meioI, 10+30*(i-1), meioS);
                                g2d.drawLine(10+30*(i-1), meioS, 10+30*(i-1)+15, meioS);
                                g2d.drawLine(10+30*(i-1)+15, meioS, 10+30*(i-1)+15, meioI);
                                g2d.drawLine(10+30*(i-1)+15, meioI, 10+30*(i-1)+30, meioI);
                            }
                        }else{
                            if(w==0){
                                g2d.drawLine(10+30*(i-1), meioS, 10+30*(i-1)+15, meioS);
                                g2d.drawLine(10+30*(i-1)+15, meioS, 10+30*(i-1)+15, meioI);
                                g2d.drawLine(10+30*(i-1)+15, meioI, 10+30*(i-1)+30, meioI);
                                w=1;
                            }else{
                                g2d.drawLine(10+30*(i-1), meioI, 10+30*(i-1)+15, meioI);
                                g2d.drawLine(10+30*(i-1)+15, meioI, 10+30*(i-1)+15, meioS);
                                g2d.drawLine(10+30*(i-1)+15, meioS, 10+30*(i-1)+30, meioS);
                                w=0;
                            }
                        }
                    }
                break;
            }    
        }*/
 
    }
 
    
}
