/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package partitioners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author cent
 */
public class RRPartitioner {
    
    public static void main(String[] args) throws IOException {
        // params: inputfile outputpath partitions
        String input = args[0];
        String outpath = args[1];
        int K = Integer.parseInt(args[2]);
        FileWriter [] files = new FileWriter[K];
        String name = "part_";
        for(int i=0; i<K; i++){
            files[i]= new FileWriter(new File(outpath + name + i));
        }
        
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line;
        int i = 0;
        while((line = br.readLine())!=null){
            files[i%K].write(line+"\n");
            i++;
        }
        
        for(int j=0; j<K; j++){
            files[j].close();
        }
        br.close();
    }   
    
}