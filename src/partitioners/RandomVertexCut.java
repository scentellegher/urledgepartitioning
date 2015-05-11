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
import java.util.Random;

/**
 *
 * @author cent
 */
public class RandomVertexCut {
    
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
        String[] tmp;
        Random rand = new Random();
       
        while((line = br.readLine())!=null){
            tmp = line.split(" "); // domain v1 v2
            files[rand.nextInt(K)].write(tmp[1]+" "+tmp[2]+"\n");
        }
        
        for(int j=0; j<K; j++){
            files[j].close();
        }
        br.close();
    }   
    
}