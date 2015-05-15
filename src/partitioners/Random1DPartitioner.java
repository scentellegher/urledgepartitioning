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
public class Random1DPartitioner {
    
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
        
        String line;
        Integer vertex;
        String[] tmp;
        String edge;
        int old_vertex=0;
        
        //select random partition
        Random rand = new Random();
        int i=rand.nextInt(K);
        
        BufferedReader br = new BufferedReader(new FileReader(input));
        while((line = br.readLine())!=null){
            tmp = line.split(" ");
            vertex = Integer.parseInt(tmp[1]);
            edge = tmp[1]+" "+tmp[2];
            // if the vertex is the same write in the current partition 
            if(old_vertex == vertex){
                files[i].write(edge+"\n");
            } else {
                //change partition and store edge
                i=rand.nextInt(K);
                files[i].write(edge+"\n");
                old_vertex = vertex;
            }
        }
        br.close();
        
        for(int j=0; j<K; j++){
            files[j].close();
        }

    }   
    
}