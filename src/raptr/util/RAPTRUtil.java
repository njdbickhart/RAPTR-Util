/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raptr.util;

import DivetToReadName.DivetSearch;
import GetCmdOpt.SimpleModeCmdLineParser;

/**
 *
 * @author desktop
 */
public class RAPTRUtil {
    private static final String version = "0.0.1";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimpleModeCmdLineParser cmd = PrepareCMDOptions();        
        cmd.GetAndCheckMode(args);
        
        switch(cmd.CurrentMode){
            case "divetsearch":
                DivetSearch divet = new DivetSearch(cmd);
                divet.run();
                break;
            default :
                System.err.println("Error! Unrecognized Mode!");
                System.err.println(cmd.GetUsage());
                System.exit(0);
                break;
        }
    }

    private static SimpleModeCmdLineParser PrepareCMDOptions() {
        String nl = System.lineSeparator();
        SimpleModeCmdLineParser cmd = new SimpleModeCmdLineParser("RAPTR-SV\tA tool to cluster split and paired end reads" + nl
                + "Version: " + version + nl
            + "Usage: java -jar RAPTR-Util.jar [mode] [mode specific options]" + nl
                + "Modes:" + nl
                + "\tdivetsearch\tIdentifies divet reads from an input BAM file" + nl ,
                "cluster",
                "preprocess"
        );
        cmd.AddMode("divetsearch", 
                "RAPTR-Util divetsearch mode" + nl +
                "Usage: java -jar RAPTR-Util.jar divetsearch [-d divet read name (separated by commas, or file list) -b bam file] (optional: -o output file)" + nl
                + "\t-d\tOne or more divet hash names (separated by commas) OR a file containing newline separated divet hash names" + nl
                + "\t-b\tThe BAM file that should contain the reads" + nl
                + "\t-o\tAn output file to print the results [optional; default is to print to console]" + nl,
                "d:b:o:", 
                "db", 
                "dbo", 
                "divet", "bam", "output");
        return cmd;
    }
    
}
