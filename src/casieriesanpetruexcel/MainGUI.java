/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package casieriesanpetruexcel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;
import java.util.Date; 
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author liviu
 */
public class MainGUI {
    public JPanel  mainPanel;
    public JFrame  mainFrame;
    public JButton importBtn, parseBtn;
    public JLabel  srcLabel;
    
    public String fileChosen = "";
    
    JDatePickerImpl datePicker;
    
    public JComboBox operatori;
    
    
    public MainGUI(){
        mainFrame = new JFrame("Import CSV");
       
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //mainPanel = new JPanel(new GridBagLayout()); // arangeable components in panel
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setBackground(Color.WHITE);
        
        importBtn = new JButton("Alege Fisier");
        importBtn.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent e){
                JButton open = new JButton();
                JFileChooser fc = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("CSV file", "csv");
                fc.setFileFilter(filter);
                fc.setCurrentDirectory(new java.io.File("d:/Work/CasierieSanpetruExcel"));
                fc.setDialogTitle("Alege CSV");
                if(fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    if (fc.getFileFilter() instanceof FileNameExtensionFilter) { // check that is csv
                        //do sth on choose
                        fileChosen = file.getAbsolutePath();
                        srcLabel.setText(fileChosen);
                        parseBtn.setEnabled(true);
                    }
                }
               
                
            }
            
        });
        
        srcLabel = new JLabel("Nici un fisier ales ...");
        
        parseBtn = new JButton("Parse CSV");
        parseBtn.setEnabled(false);
        parseBtn.setBounds(10, 10, 80, 20);
        parseBtn.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent e){
                //System.out.println("File Chose is: " + fileChose);
                ReadWrite rw = new ReadWrite();
                
                rw.read(fileChosen, datePicker.getJFormattedTextField().getText(), operatori.getSelectedItem().toString());
             
                
            }
            
        });
        
        
        
        mainPanel.add(importBtn);//, c);

        mainPanel.add(srcLabel);
        
        mainPanel.add(parseBtn);
        
        JLabel lblFileLink = new JLabel("Deschide fisier");

        // To indicate the the link is clickable
        lblFileLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        mainPanel.add(lblFileLink);

        String[] strOperatori = {"<", "=", ">"};
        
        operatori = new JComboBox(strOperatori);
        operatori.setSelectedIndex(2);
        operatori.setBounds(250,350,50,30);
        mainPanel.add(operatori);
        
        UtilDateModel model=new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setBounds(280,350,120,30);
        mainPanel.add(datePicker);
   
        mainFrame.setVisible(true);
        
        mainFrame.add(mainPanel);
    }
}
