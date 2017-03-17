/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package casieriesanpetruexcel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 *
 * @author liviu
 */
public class MainGUI extends JPanel{
    public JPanel  mainPanel;
    public JFrame  mainFrame;
    public JButton importBtn, parseBtn;
    public JLabel  srcLabel;
    
    public String fileChosen = "";
    
    JDatePickerImpl datePicker;
    
    public JComboBox operatori;
    
    public JTextField soldStart;
    
    GridBagConstraints gbc = new GridBagConstraints();
    
    public MainGUI() {
//        mainFrame = new JFrame("Import CSV");
//       
//        mainFrame.setSize(600, 400);
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        
//        mainPanel = new JPanel(new GridBagLayout()); // arangeable components in panel
////        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        mainPanel.setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        // ROW 1
        JLabel lblSoldStart = new JLabel("Sold Inceput");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblSoldStart, gbc);

        soldStart = new JTextField();
        soldStart.setPreferredSize(new Dimension(80,20));
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(soldStart, gbc);
        
        
        // ROW 2
        importBtn = new JButton("Alege Fisier");
        importBtn.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent e){
                JButton open = new JButton();
                JFileChooser fc = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("CSV file", "csv");
                fc.setFileFilter(filter);
                fc.setCurrentDirectory(new java.io.File("./"));
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
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(importBtn, gbc);
        
        srcLabel = new JLabel("Nici un fisier ales ...");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(srcLabel, gbc);
 
        JLabel lblFileLink = new JLabel("Deschide fisier");

        // To indicate the the link is clickable
        lblFileLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridx = 2;
        gbc.gridy = 2;
        add(lblFileLink, gbc);

        // ROW 3
        String[] strOperatori = {"<", "=", ">"};
        
        operatori = new JComboBox(strOperatori);
        operatori.setSelectedIndex(2);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(operatori, gbc);
        
        UtilDateModel model=new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(datePicker, gbc);
   
        parseBtn = new JButton("Parse CSV");
        parseBtn.setEnabled(false);
        parseBtn.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent e){
                //System.out.println("File Chose is: " + fileChose);
                ReadWrite rw = new ReadWrite();
                if (!soldStart.getText().equals("")) { 
                    rw.sold = Integer.valueOf(soldStart.getText());
                }
            
                rw.read(fileChosen, datePicker.getJFormattedTextField().getText(), operatori.getSelectedItem().toString());               
            }
            
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(parseBtn, gbc); 
        
    }
}
