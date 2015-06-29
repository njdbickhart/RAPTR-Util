/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DivetToReadName;

import GetCmdOpt.SimpleModeCmdLineParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecordIterator;

/**
 *
 * @author desktop
 */
public class DivetSearch {
    private Path bamFile;
    private boolean outputFile = false;
    private Path outFile = null;
    private OutputPrinter printer; 
    private Set<Long> divetSearch;
    private final ReadNameUtility rn = new ReadNameUtility();
    
    public DivetSearch(SimpleModeCmdLineParser cmd){        
        this.bamFile = Paths.get(cmd.GetValue("bam"));
        
        // Check to see if the bam exists
        if(!Files.isReadable(bamFile)){
            System.err.println("Error! Could not read bam file: " + bamFile.toString());
            System.exit(-1);
        }
        
        // Input checking
        String divet = cmd.GetValue(cmd.GetValue("divet"));
        Path dfile = Paths.get(divet);
        boolean isFile = Files.isReadable(dfile);
        if(StrUtils.NumericCheck.isNumeric(divet) || divet.matches(".+,.+") && !isFile){
            // Input is numeric or contains commas
            divetSearch = Arrays.asList(divet.split(",")).stream()
                    .map(Long::valueOf)
                    .collect(Collectors.toCollection(HashSet::new));
        }else if(isFile){
            // We need to read in all of the values from a file
            divetSearch = new HashSet<>();
            try(BufferedReader input = Files.newBufferedReader(dfile, Charset.defaultCharset())){
                String line;
                while((line = input.readLine()) != null){
                    line = line.trim();
                    divetSearch.add(Long.valueOf(line));
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }else{
            System.err.println("Error! Could not identify divet content!");
            System.exit(-1);
        }
        
        // Output check
        if(cmd.HasOpt("output")){
            outputFile = true;
            outFile = Paths.get(cmd.GetValue("output"));
            try {
                printer = new OutputPrinter(outputFile, outFile);
            } catch (IOException ex) {
                Logger.getLogger(DivetSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            try {
                printer = new OutputPrinter(outputFile, outFile);
            } catch (IOException ex) {
                Logger.getLogger(DivetSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void run(){
        SAMFileReader sam = new SAMFileReader(this.bamFile.toFile());
        sam.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
        
        SAMRecordIterator samItr = sam.iterator();
        
        samItr.forEachRemaining((s) -> {
            Long hash = rn.ReadHash(s.getReadName());
            if(divetSearch.contains(hash)){
                printer.PrintOut(s, hash);
            }
        });
    }
    
    
}
