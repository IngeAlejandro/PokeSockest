package proyectosockets;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Client extends javax.swing.JFrame {

    private static final int PORT = 5000;
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private String P;
    private int health;
    private int otherHealth;

    public Client() throws Exception {
        initComponents();
        socket = new Socket("127.0.0.1", PORT);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        health = 150;
        otherHealth = 150;
        setBars();
    }

    public void play() throws Exception {
        String response;
        try (socket) {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                P = response.substring(8);
                this.setTitle("POKEMÓN: " + P);
                if ("PIKACHU".equals(P)) {
                    URL url = this.getClass().getResource("pikachu.gif");
                    Icon icon = new ImageIcon(url);
                    selfIcon.setIcon(icon);
                    selfName.setText("PIKACHU");
                    url = this.getClass().getResource("charmander.gif");
                    icon = new ImageIcon(url);
                    otherIcon.setIcon(icon);
                    otherName.setText("CHARMANDER");
                    this.repaint();
                } else if ("CHARMANDER".equals(P)) {
                    URL url = this.getClass().getResource("charmanderB.gif");
                    Icon icon = new ImageIcon(url);
                    selfIcon.setIcon(icon);
                    selfName.setText("CHARMANDER");
                    url = this.getClass().getResource("pikachuF.gif");
                    icon = new ImageIcon(url);
                    otherIcon.setIcon(icon);
                    otherName.setText("PIKACHU");
                    jButton2.setText("Flamethrower");
                    this.repaint();
                    jButton1.setEnabled(false);
                    jButton2.setEnabled(false);
                    jButton3.setEnabled(false);
                }
            }

            while (true) {
                response = in.readLine();
                System.out.println("INPUT " + response);
                if (response.startsWith("DMG")) {
                    log.append("Es turno de tu oponente\n");
                    log.setCaretPosition(log.getDocument().getLength());
                    setBars();
                } else if (response.startsWith("ATK")) {
                    int dmg = Integer.parseInt(response.substring(4));
                    health -= dmg;
                    log.append(P + " ha recibido " + dmg + " de daño!\nEs tu turno\n");
                    log.setCaretPosition(log.getDocument().getLength());
                    selfH.setText(health + "/150");
                    setBars();
                    jButton1.setEnabled(true);
                    jButton2.setEnabled(true);
                    jButton3.setEnabled(true);
                } else if (response.startsWith("VICTORY")) {
                    log.append("Has ganado! \n");
                    log.setCaretPosition(log.getDocument().getLength());
                    URL url = this.getClass().getResource("pokeball.gif");
                    Icon icon = new ImageIcon(url);
                    otherIcon.setIcon(icon);
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    log.append("Oh no! Has perdido\n");
                    log.setCaretPosition(log.getDocument().getLength());
                    URL url = this.getClass().getResource("pokeball.gif");
                    Icon icon = new ImageIcon(url);
                    selfIcon.setIcon(icon);
                    break;
                } else if (response.startsWith("LOG")) {
                    log.append(response.substring(4) + "\n");
                    log.setCaretPosition(log.getDocument().getLength());
                }
            }
            out.println("QUIT");
        }
    }

    private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(this,
                "¿Jugar Otra Vez?",
                "¡Átrapalos Ya!",
                JOptionPane.YES_NO_OPTION);
        this.dispose();
        return response == JOptionPane.YES_OPTION;
    }

    public int calculo(int dmg) {
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        int r = (int) (Math.random() * 5);
        switch (r) {
            case 0:
                log.append("Es súper efectivo!\n");
                log.setCaretPosition(log.getDocument().getLength());
                out.println(P + " " + dmg * 2);
                otherHealth -= dmg * 2;
                otherH.setText(otherHealth + "/150");
                return dmg * 2;
            case 1:
                log.append("Ha fallado!\n");
                log.setCaretPosition(log.getDocument().getLength());
                out.println(P + " " + dmg * 0);
                otherHealth -= dmg * 0;
                otherH.setText(otherHealth + "/150");
                return dmg * 0;
            default:
                out.println(P + " " + dmg);
                otherHealth -= dmg;
                otherH.setText(otherHealth + "/150");
                return dmg;
        }
    }

    private void setBars() {
        String bar = "";
        for (int i = 0; i < otherHealth / 10; i++) {
            bar += "■";
        }
        for (int i = bar.length(); i < 15; i++) {
            bar += " ";
        }
        if (otherHealth <= 150 && otherHealth >= 100) {
            otherBar.setForeground(Color.GREEN);
        }
        if (otherHealth <= 100 && otherHealth >= 50) {
            otherBar.setForeground(Color.YELLOW);
        } else if (otherHealth <= 50) {
            otherBar.setForeground(Color.RED);
        }
        otherBar.setText(bar);

        bar = "";
        for (int i = 0; i < health / 10; i++) {
            bar += "■";
        }
        for (int i = bar.length(); i < 15; i++) {
            bar += " ";
        }
        if (health <= 150 && health >= 100) {
            selfBar.setForeground(Color.GREEN);
        }
        if (health <= 100 && health >= 50) {
            selfBar.setForeground(Color.YELLOW);
        } else if (health <= 50) {
            selfBar.setForeground(Color.RED);
        }
        selfBar.setText(bar);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        selfIcon = new javax.swing.JLabel();
        otherIcon = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        log = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        otherBar = new javax.swing.JTextField();
        otherName = new javax.swing.JLabel();
        otherH = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        selfName = new javax.swing.JLabel();
        selfH = new javax.swing.JLabel();
        selfBar = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RockPaperScissors_Client");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        selfIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selfIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectosockets/pikachu.gif"))); // NOI18N
        selfIcon.setToolTipText("");

        otherIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        otherIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectosockets/charmander.gif"))); // NOI18N
        otherIcon.setToolTipText("");

        log.setEditable(false);
        log.setBackground(new java.awt.Color(204, 204, 204));
        log.setColumns(20);
        log.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        log.setRows(5);
        log.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane1.setViewportView(log);

        jPanel2.setBackground(new java.awt.Color(255, 204, 153));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setMinimumSize(new java.awt.Dimension(134, 148));

        otherBar.setEditable(false);
        otherBar.setBackground(new java.awt.Color(204, 204, 204));
        otherBar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        otherBar.setForeground(new java.awt.Color(102, 204, 0));
        otherBar.setText("■■■■■■■■■■■■■■■");
        otherBar.setAutoscrolls(false);
        otherBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherBarActionPerformed(evt);
            }
        });

        otherName.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        otherName.setText("Pikachu");

        otherH.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        otherH.setText("150/150");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(otherBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(otherName)
                    .addComponent(otherH, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(otherName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(otherBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(otherH)
                .addGap(54, 54, 54))
        );

        jPanel3.setBackground(new java.awt.Color(255, 204, 153));
        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setMinimumSize(new java.awt.Dimension(134, 94));

        selfName.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        selfName.setText("Pikachu");

        selfH.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        selfH.setText("150/150");

        selfBar.setEditable(false);
        selfBar.setBackground(new java.awt.Color(204, 204, 204));
        selfBar.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        selfBar.setForeground(new java.awt.Color(102, 204, 0));
        selfBar.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        selfBar.setText("■■■■■■■■■■■■■■■");
        selfBar.setAutoscrolls(false);
        selfBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selfBarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(selfH)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(selfBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(selfName)))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selfName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selfBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selfH))
        );

        jButton1.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        jButton1.setText("Quick Attack");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        jButton2.setText("Thunder Shock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Consolas", 1, 12)); // NOI18N
        jButton3.setText("Growl");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(otherIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(selfIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(otherIcon, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(selfIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        log.append(P + " ha golpeado por " + calculo(10) + " de daño\n");
        log.setCaretPosition(log.getDocument().getLength());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void otherBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_otherBarActionPerformed

    private void selfBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selfBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selfBarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        log.append(P + " ha golpeado por " + calculo(30) + " de daño\n");
        log.setCaretPosition(log.getDocument().getLength());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        log.append(P + " ha golpeado por " + calculo(20) + " de daño\n");
        log.setCaretPosition(log.getDocument().getLength());
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) throws Exception {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        while (true) {
            Client c = new Client();
            c.setVisible(true);
            c.play();
            if (!c.wantsToPlayAgain()) {
                break;
            }
        }
        /* Create and display the form */
    }
    //</editor-fold>


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea log;
    private javax.swing.JTextField otherBar;
    private javax.swing.JLabel otherH;
    private javax.swing.JLabel otherIcon;
    private javax.swing.JLabel otherName;
    private javax.swing.JTextField selfBar;
    private javax.swing.JLabel selfH;
    private javax.swing.JLabel selfIcon;
    private javax.swing.JLabel selfName;
    // End of variables declaration//GEN-END:variables
}
