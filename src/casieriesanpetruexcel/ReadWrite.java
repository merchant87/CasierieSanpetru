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
    
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    
    private final String rootDir = "D:/Work/CasierieSanpetruExcel/";
    
    public ReadWrite(){
        
    }
    
    public void read(String file, String date, String operator){
        String currentDate = "";
                
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();
                
        try{
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNext()) {
                List<String> line = parseLine(scanner.nextLine());
                String valoare = line.get(5);
                String descriere = line.get(2);
                Date selectedDate, rowDate;
                
                
                
                if(line.get(0).equals("Casierie Sampetru")) {
                    
                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-d-yyyy");
                        selectedDate = sdf.parse(date);

                        rowDate = sdf.parse(line.get(1));
                        
                        
                        if(operator.equals("=") && rowDate.equals(selectedDate) ||
                           operator.equals("<") && (rowDate.before(selectedDate) || rowDate.equals(selectedDate)) ||
                           operator.equals(">") && (rowDate.after(selectedDate) || rowDate.equals(selectedDate))
                           ){
                           if(!line.get(1).equals(currentDate)){
                                
                                if(!currentDate.equals("")){
                                    //System.out.println(" in LOOP Current date: [" + currentDate + "]");
                                    this.writeXlsXPoi(currentDate ,values, descriptions);
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
                this.writeXlsXPoi(currentDate ,values, descriptions);
            }
            
            scanner.close();
        } catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
        }
    }

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }
    
    
    
    private void writeXlsXPoi(String date, ArrayList<String> values, ArrayList<String> descriptions){
        
        
        try {
            
            //Read Excel document first
            FileInputStream input_document = new FileInputStream(new File(this.rootDir + "proces_verbal_sanpetru_template.xlsx"));
            // convert it into a POI object
            XSSFWorkbook my_xlsx_workbook = new XSSFWorkbook(input_document); 
            // Read excel sheet that needs to be updated
            XSSFSheet my_worksheet = my_xlsx_workbook.getSheetAt(0); 
            // declare a Cell object            
            Cell cellSoldAnterior = my_worksheet.getRow(4).getCell(3);
            Cell cellSold = my_worksheet.getRow(28).getCell(2);
            Cell cellDate = my_worksheet.getRow(0).getCell(4);
            
            String exportDate = "";
            
            //changing date pattern
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-d-yyyy");
                Date d = sdf.parse(date);
                sdf.applyPattern("dd.MM.yyyy");
                String romanianDate = sdf.format(d);

                sdf.applyPattern("yyyy-MM-dd");
                exportDate = sdf.format(d);
                
                cellDate.setCellValue("Data: " + romanianDate);
            
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
                    
            Integer dataSize = values.size();
            //System.out.println("Data size is: [" + dataSize.toString() + "]");
            
            for (int i = 0; i < 20; i++) {

                if(i < dataSize){
                    Cell cellIn = my_worksheet.getRow(i+7).getCell(2);
                    Cell cellOut = my_worksheet.getRow(i+7).getCell(3);
                    Cell cellDesc = my_worksheet.getRow(i+7).getCell(4);
                
                    Double valoare = Double.parseDouble(values.get(i));
                    if(valoare > 0){
                        cellIn.setCellValue(valoare);
                        //this.soldAnterior -= intValue(Math.abs(valoare));
                    } else {
                        cellOut.setCellValue(Math.abs(valoare));
                        //this.soldAnterior += intValue(Math.abs(valoare));
                    }
                    cellDesc.setCellValue(descriptions.get(i));
                    //System.out.println("valoare: " + valoare);
                }
            }
            
            this.soldAnterior = this.sold;
            for(int i = 0; i < dataSize; i++){
                Double valoare = Double.parseDouble(values.get(i));
                this.soldAnterior -= intValue(valoare);
            }
            cellSoldAnterior.setCellValue(this.soldAnterior);
            
            cellSold.setCellValue(this.sold);
            
            values.clear();
            descriptions.clear();            
            //important to close InputStream
            input_document.close();
            //Open FileOutputStream to write updates
            FileOutputStream output_file =new FileOutputStream(new File(this.rootDir + "output/proces_verbal_sanpetru_" + exportDate + ".xlsx"));
            //write changes
            my_xlsx_workbook.write(output_file);
            //close the stream
            output_file.close(); 

            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  
}