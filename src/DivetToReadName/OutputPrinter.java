/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DivetToReadName;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import net.sf.samtools.SAMRecord;

/**
 *
 * @author desktop
 */
public class OutputPrinter {
    private boolean outputPrint; 
    private BufferedWriter output = null;
    
    public OutputPrinter(boolean outputPrint, Path out) throws IOException{
        this.outputPrint = outputPrint;
        if(outputPrint){
            output = Files.newBufferedWriter(out, Charset.defaultCharset());
        }
    }
    
    public void PrintOut(SAMRecord sam, Long read){
        String record = sam.getSAMString();
        
        if(outputPrint){
            try{
                output.write(record + "\tXH:Z:" + read + System.lineSeparator());
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }else{
            System.out.println(record + "\tXH:Z:" + read);
        }
    }
}
