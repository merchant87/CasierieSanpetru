/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package casierieSanpetruProcesVerbal;

import java.awt.*;
import javax.swing.JFrame;


/**
 *
 * @author liviu
 */
public class CasierieSanpetruProcesVerbal {
    MainGUI mainGUI;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
      
        new CasierieSanpetruProcesVerbal();
    }
    
    public CasierieSanpetruProcesVerbal(){
        MainGUI mainGUI = new MainGUI();
        JFrame mainFrame = new JFrame("Import CSV");
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.add(mainGUI);
    }

}
