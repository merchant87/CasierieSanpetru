/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package casieriesanpetruexcel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author liviu
 */
public class SwingGUI extends javax.swing.JFrame {
    /*
    private JPanel  mainPanel;
    private JFrame  mainFrame;
    private JButton importBtn;
    private JLabel  srcLabel;
    
    private JFileChooser fileChooser;
    */

    /**
     * Creates new form MainGUI
     */
    public SwingGUI() {
        initComponents();
        /*
        mainFrame = new JFrame("Import CSV");
        mainFrame.setVisible(true);
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mainPanel = new JPanel(new GridBagLayout()); // arangeable components in panel
        mainPanel.setBackground(Color.WHITE);
        
        importBtn = new JButton("Alege Fisier");
        
        srcLabel = new JLabel("Nici un fisier ales ...");
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10 , 10);
        c.gridx = 0;
        c.gridy = 1;
        
        mainPanel.add(importBtn, c);

        mainPanel.add(srcLabel);
        
        mainFrame.add(mainPanel);
        */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importBtn = new javax.swing.JButton();
        srcLabel = new javax.swing.JLabel();
        outputText = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Import CSV");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        importBtn.setText("Alege Fisier");
        getContentPane().add(importBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        srcLabel.setText("Nici un fisier ales ..");
        getContentPane().add(srcLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 15, 270, -1));
        getContentPane().add(outputText, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 330, 50));

        jSeparator1.setPreferredSize(new java.awt.Dimension(400, 10));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    /*
    public static void main(String args[]) {
        // Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
    /*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SwingGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SwingGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SwingGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SwingGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    */
        //</editor-fold>

        
        
        // Create and display the form 
        //java.awt.EventQueue.invokeLater(new Runnable() {
           // public void run() {
          //      new SwingGUI().setVisible(true);
           // }
       // });
    //}
    //*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton importBtn;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField outputText;
    private javax.swing.JLabel srcLabel;
    // End of variables declaration//GEN-END:variables
}
