package implementacaobd;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class PainelScroll extends JFrame{
    private static JComboBox<String> jComboBox1;
    private static JLabel jLabel1;
    private static Painel jPanel1;
    private static JFrame frame;
    private static JScrollPane jScrollPane1;
    private String codigo;
    private JFrame esse = this;

    public PainelScroll(String codigo){
        frame = new JFrame();
        this.codigo = codigo;
                jPanel1 = new Painel(1, codigo);
                jLabel1 = new javax.swing.JLabel();
                jComboBox1 = new javax.swing.JComboBox<>();
                jScrollPane1 = new javax.swing.JScrollPane();

                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setTitle("Codificador de Linha - Elton, Lorenzo");
                frame.setVisible(true);

                jLabel1.setText("Código de Linha:");

                jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"NRZ-L", "NRZ-I", "AMI", "Pseudoternário", "Manchester", "Mancherter Diferencial"}));
                jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBox1ActionPerformed(evt);
                    }
                });
                

                jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                jScrollPane1.setViewportView(jPanel1);
                jScrollPane1.setMaximumSize(new Dimension(711, 175));
                jScrollPane1.setPreferredSize(new Dimension(codigo.length()*30+20, 175));
                
                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, codigo.length()*30+20, Short.MAX_VALUE)
                );
                jPanel1Layout.setVerticalGroup(
                        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 135, Short.MAX_VALUE)
                );
                jPanel1.setBackground(new java.awt.Color(250, 250, 250));
                jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
                

                GroupLayout layout = new GroupLayout(frame.getContentPane());
                frame.getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1)
                                .addContainerGap())
                );

                frame.pack();
                frame.setLocationRelativeTo(null);
    }
    
    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
                String opcao = jComboBox1.getItemAt(jComboBox1.getSelectedIndex());
                switch (opcao) {
                    case "NRZ-L":
                        jPanel1.desenhar(1, codigo);
                        break;
                    case "NRZ-I":
                        jPanel1.desenhar(2, codigo);
                        break;
                    case "AMI":
                        jPanel1.desenhar(3, codigo);
                        break;
                    case "Pseudoternário":
                        jPanel1.desenhar(4, codigo);
                        break;
                    case "Manchester":
                        jPanel1.desenhar(5, codigo);
                        break;
                    case "Mancherter Diferencial":
                        jPanel1.desenhar(6, codigo);
                        break;
                }
    }             

}