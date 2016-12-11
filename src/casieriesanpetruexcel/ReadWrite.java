/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package casieriesanpetruexcel;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JLabel;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.xmlbeans.XmlException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.util.*;
import static oracle.jrockit.jfr.events.Bits.intValue;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variables;
/**
 *
 * @author liviu
 */
public class ReadWrite {
    
    Integer sold = 0;
    Integer soldAnterior = 0;
    
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    
    public ReadWrite(){
        
    }
    
    public void read(String file, String date){
        String currentDate = "";
                
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();
                
        try{
            Scanner scanner = new Scanner(new File(file));
            while (scanner.hasNext()) {
                List<String> line = parseLine(scanner.nextLine());
                String valoare = line.get(5);
                String descriere = line.get(2);
 
                if(line.get(0).equals("Casierie Sampetru")) {
                        
                    if(!line.get(1).equals(currentDate)){
                        // changed the day; write te date in the header
                        //System.out.println("Current date: [" + currentDate + "]");
                        
                        if(!currentDate.equals("")){
                            this.writeDocPoi(currentDate ,values, descriptions);
                        }
                        currentDate = line.get(1);

                    }
                    //System.out.println(valoare);
                    this.sold+= intValue(Double.parseDouble(valoare));
                    values.add(valoare);
                    descriptions.add(descriere);
                        
                }
            }
            
            // if it comes to the last date, it will not go to
            //  the next iteration to write the data in the file
            //  so we check that if data is not empty after the iteration
            //  it means it has to be written
            if(!values.isEmpty()){// last set of data
                this.writeDocPoi(currentDate ,values, descriptions);
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
    
    public void writeDocxPoi(String date, ArrayList<String> values, ArrayList<String> descriptions){
        String rootDir = "D:/Work/CasierieSanpetruExcel/";
        
        try{
            
            XWPFDocument tmpl = new XWPFDocument(OPCPackage.open(rootDir + "proces_verbal_sanpetru_template3.docx")); 
            
            tmpl = replaceTextDocx(tmpl, "$sold_anterior", this.soldAnterior.toString());
            
            Integer dataSize = values.size();
            //System.out.println("Data size is: [" + dataSize.toString() + "]");
            
            //for (int i = 0; i <= 19; i++) {
            for (int i = 19; i >= 0; i--) {// not to replace "$in1" in "$in19"
                //System.out.println("i=["+i+"]");
                if(i < dataSize){
                    Double valoare = Double.parseDouble(values.get(i));
                    if(valoare > 0){
                        //System.out.println("i=["+i+"] , replacing [$in"+i+"] with [" + values.get(i) + "]");
                        tmpl = replaceTextDocx(tmpl, "$in"+i, values.get(i));
                        tmpl = replaceTextDocx(tmpl, "$out"+i, "");
                    } else {
                        //System.out.println("i=["+i+"] , replacing [$out"+i+"] with [" + valoare + "]"); 
                        tmpl = replaceTextDocx(tmpl, "$out"+i, String.valueOf(Math.abs(valoare)));
                        tmpl = replaceTextDocx(tmpl, "$in"+i, "");
                    }
                    //System.out.println("i=["+i+"] , replacing with [" + descriptions.get(i) + "]");
                    tmpl = replaceTextDocx(tmpl, "$det"+i, descriptions.get(i));
                        
                } else { // fill until the end of table with blanks
                    //System.out.println("i=["+i+"] , replacing [$in"+i+"] with blanks");
                    //System.out.println("i=["+i+"] , replacing [$out"+i+"] with blanks");
                    //System.out.println("i=["+i+"] , replacing [$det"+i+"] with blanks");
                    tmpl = replaceTextDocx(tmpl, "$in"+i, "");
                    tmpl = replaceTextDocx(tmpl, "$out"+i, "");
                    tmpl = replaceTextDocx(tmpl, "$det"+i, "");
                }
            }
            //System.out.println("----------------------------");
            tmpl = replaceTextDocx(tmpl, "$sold", this.sold.toString());
            
            //tmpl = replaceTextDocx(tmpl, "$out0", "abcd");
            this.soldAnterior = this.sold;
            
            FileOutputStream output = new FileOutputStream(rootDir + "proces_verbal_sanpetru_" + date + ".docx");
            tmpl.write(output);
            output.close();
            
            values.clear();
            descriptions.clear();
            
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public XWPFDocument replaceTextDocx(XWPFDocument doc, String findText, String replaceText){
        
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains(findText)) {
                        text = text.replace(findText, replaceText);
                        r.setText(text, 0);
                    }
                }
            }
        }
        for (XWPFTable tbl : doc.getTables()) {
           for (XWPFTableRow row : tbl.getRows()) {
              for (XWPFTableCell cell : row.getTableCells()) {
                 for (XWPFParagraph p : cell.getParagraphs()) {
                    for (XWPFRun r : p.getRuns()) {
                      String text = r.getText(0);
                      //System.out.println(text);
                      if (text.contains(findText)) {
                        text = text.replace(findText, replaceText);
                        r.setText(text);
                      }
                    }
                 }
              }
           }
        }    
         
        
        return doc;
    }
        
    public void writeDocPoi(String date, ArrayList<String> values, ArrayList<String> descriptions){
        //DateFormat df = new SimpleDateFormat("yyyyMMdd");
        //String sdt = df.format(new Date());
        
        String rootDir = "D:/Work/CasierieSanpetruExcel/";
        // load template
        try{
            FileInputStream input = new FileInputStream(rootDir+"proces_verbal_sanpetru_template3.doc");
            // class used to extract content
            HWPFDocument tmpl = new HWPFDocument(input);
            
            //System.out.println(date);
            tmpl = replaceText(tmpl, "$sold_anterior", this.soldAnterior.toString());
            
            Integer dataSize = values.size();
            
            //System.out.println("Data size is: [" + dataSize.toString() + "]");
            //for (int i = 0; i <= 19; i++) {
            for (int i = 19; i >= 0; i--) {// not to replace "$in1" in "$in19"
                //System.out.println("i=["+i+"]");
                if(i < dataSize){
                    Double valoare = Double.parseDouble(values.get(i));
                    if(valoare > 0){
                        tmpl = replaceText(tmpl, "$in"+i, values.get(i));
                        tmpl = replaceText(tmpl, "$out"+i, "");
                    } else {
                        tmpl = replaceText(tmpl, "$out"+i, String.valueOf(Math.abs(valoare)));
                        tmpl = replaceText(tmpl, "$in"+i, "");
                    }
                    System.out.println("i=["+i+"] , replacing with [" + descriptions.get(i) + "]");
                    tmpl = replaceText(tmpl, "$det"+i, descriptions.get(i));
                        
                } else { // fill until the end of table with blanks
                    System.out.println("i=["+i+"] , replacing [$in"+i+"] with blanks");
                    System.out.println("i=["+i+"] , replacing [$out"+i+"] with blanks");
                    System.out.println("i=["+i+"] , replacing [$det"+i+"] with blanks");
                    tmpl = replaceText(tmpl, "$in"+i, "");
                    tmpl = replaceText(tmpl, "$out"+i, "");
                    tmpl = replaceText(tmpl, "$det"+i, "");
                }
            }
            //System.out.println("----------------------------");
            tmpl = replaceText(tmpl, "$sold", this.sold.toString());
            
            //tmpl = replaceText(tmpl, "$out0", "abcd");
            this.soldAnterior = this.sold;
            
            FileOutputStream output = new FileOutputStream(rootDir+"proces_verbal_sanpetru_"+date+".doc");
            tmpl.write(output);
            output.close();
            
            values.clear();
            descriptions.clear();
            
        }catch (Exception e) {
            e.printStackTrace();
        } 
        

    }
    
    private static HWPFDocument replaceText(HWPFDocument doc, String findText, String replaceText){
        Range r1 = doc.getRange(); 

        for (int i = 0; i < r1.numSections(); ++i ) { 
            Section s = r1.getSection(i); 
            
            for (int x = 0; x < s.numParagraphs(); x++) { 
                Paragraph p = s.getParagraph(x); 
                for (int z = 0; z < p.numCharacterRuns(); z++) { 
                    CharacterRun run = p.getCharacterRun(z); 
                    String text = run.text();
                    if(text.contains(findText)) {
                        run.replaceText(findText, replaceText);
                    } 
                }
            }
        } 
        return doc;
    }
    
    
    private WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
            WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
            return template;
    }
    
    public void replaceDoc4j(){
        String rootDir = "D:/Work/CasierieSanpetruExcel/";
        
        // create new instance of docx template
        Docx docx = new Docx(rootDir + "proces_verbal_sanpetru_template2.docx");

        // set the variable pattern. In this example the pattern is as follows: #{variableName}
        docx.setVariablePattern(new VariablePattern("#{", "}"));

        // read docx content as simple text
        String content = docx.readTextContent();

        // and display it
        //System.out.println(content); 

        // find all variables satisfying the pattern #{...}
        List<String> findVariables = docx.findVariables();

        // and display it
        for (String var : findVariables) {
                //System.out.println("VARIABLE => " + var);
        }

        // prepare map of variables for template
        Variables var = new Variables();
        
        var.addTextVariable(new TextVariable("#{in0}", "Lukasz"));
        var.addTextVariable(new TextVariable("#{out0}", "Lukaszaa"));
        var.addTextVariable(new TextVariable("#{det0}", "Lukasbbz"));

        // fill template by given map of variables
        docx.fillTemplate(var);

        // save filled document
        docx.save(rootDir + "output.docx");
    }
}

