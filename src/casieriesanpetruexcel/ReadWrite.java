/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package casieriesanpetruexcel;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.*;
import static oracle.jrockit.jfr.events.Bits.intValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author liviu
 */
public class ReadWrite {
    
    Integer sold = 0;
    Integer soldAnterior = 0;
    String year;
    
    private final String rootDir = "./assets/";
    
    FileInputStream input_document;
    XSSFWorkbook templateWorkbook;
    
    public ReadWrite(){
        
    }
    
    public void read(String file, String date, String operator){
        String currentDate = "";
                
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();
                
        try {
            Scanner scanner = new Scanner(new File(file));
            Integer row = 0;
            while (scanner.hasNext()) {
                List<String> line = CSVHelper.parseLine(scanner.nextLine());
                String valoare = line.get(5);
                String descriere = line.get(2);
                Date selectedDate, rowDate;
  
                if(line.get(0).equals("Casierie Sampetru")) {
                   
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        selectedDate = sdf.parse(date);

                        rowDate = sdf.parse(line.get(1));
                        
                        if((operator.equals("=") && rowDate.equals(selectedDate)) ||
                           (operator.equals("<") && (rowDate.before(selectedDate) || rowDate.equals(selectedDate))) ||
                           (operator.equals(">") && (rowDate.after(selectedDate) || rowDate.equals(selectedDate)))
                           ){

                           if(!line.get(1).equals(currentDate)){
                                
                                if(!currentDate.equals("")){
                                    
                                    if( row == 0 ){
                                        input_document = new FileInputStream(new File(this.rootDir + "proces_verbal_sanpetru_template.xlsx"));
                                        templateWorkbook = new XSSFWorkbook(input_document); 
                                        
                                        String [] dateParts = currentDate.split("-");
                                        year = dateParts[0];
                                    }
                                    row++;
                                    this.writeSheetXlsXPoi(currentDate ,values, descriptions); 
                                } 
                                currentDate = line.get(1);

                            }
                            
                            values.add(valoare);
                            descriptions.add(descriere);
                            
                            this.sold+= intValue(Double.parseDouble(valoare));
                        }
                        
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    
                }
            }
            
            // if it comes to the last date, it will not go to
            //  the next iteration to write the data in the file
            //  so we check that if data is not empty after the iteration
            //  it means it has to be written
            if(!values.isEmpty()){// last set of data
                //System.out.println(" OUT OF LOOP Current date: [" + currentDate + "]");
                //System.out.println("values: " + values.toString());
                this.writeSheetXlsXPoi(currentDate ,values, descriptions);            
                input_document.close();

                //Open FileOutputStream to write updates
                FileOutputStream output_file = new FileOutputStream(new File(this.rootDir + "output/proces_verbal_sanpetru_" + year + ".xlsx"));          
                templateWorkbook.removeSheetAt(2);
                templateWorkbook.removeSheetAt(1);
                templateWorkbook.removeSheetAt(0);
                //write changes
                templateWorkbook.write(output_file);           
                //close the stream
                output_file.close(); 
            }
            
            scanner.close();
        } catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
        }
    }

    
    private void writeSheetXlsXPoi(String date, ArrayList<String> values, ArrayList<String> descriptions){
        try {
            // Read excel sheet that needs to be updated
            XSSFSheet currentWorksheet = templateWorkbook.cloneSheet(0);
            templateWorkbook.setSheetName(templateWorkbook.getSheetIndex(currentWorksheet), date);

//            // declare a Cell object            
            Cell cellSoldAnterior = currentWorksheet.getRow(4).getCell(3);
            Cell cellSold = currentWorksheet.getRow(28).getCell(2);
            Cell cellDate = currentWorksheet.getRow(0).getCell(4);
            
            //changing date pattern
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(date);
             
                sdf.applyPattern("dd.MM.yyyy");
                String romanianDate = sdf.format(d);
                
                cellDate.setCellValue("Data: " + romanianDate);
                //System.out.println("Date: " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            Integer dataSize = values.size();
            //System.out.println("Data size is: [" + dataSize.toString() + "]");
            for (int i = 0; i < 20; i++) {

                if(i < dataSize){
                    Cell cellIn = currentWorksheet.getRow(i+7).getCell(2);
                    Cell cellOut = currentWorksheet.getRow(i+7).getCell(3);
                    Cell cellDesc = currentWorksheet.getRow(i+7).getCell(4);
                
                    Double valoare = Double.parseDouble(values.get(i));
                    if(valoare > 0){
                        cellIn.setCellValue(valoare);
                    } else {
                        cellOut.setCellValue(Math.abs(valoare));
                    }
                    cellDesc.setCellValue(descriptions.get(i));
                    //System.out.println("valoare: " + valoare);
                }
            }
           //System.out.println("SOLD: " + sold);
            this.soldAnterior = sold;
            for(int i = 0; i < dataSize; i++){
                Double valoare = Double.parseDouble(values.get(i));
                this.soldAnterior -= intValue(valoare);
            }
            cellSoldAnterior.setCellValue(this.soldAnterior);
            
            cellSold.setCellValue(sold);
            
            values.clear();
            descriptions.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      
}