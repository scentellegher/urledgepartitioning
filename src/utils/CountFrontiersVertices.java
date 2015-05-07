/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author cent
 */
public class CountFrontiersVertices {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String partitions_path = args[0];
        //int nodes = args[1];
        
        File folder = new File(partitions_path);
        File[] list = folder.listFiles();
        int K = list.length;
        System.out.println(K+" partitions");
        
        int i =0;
        BufferedReader br;
        String line;
        String [] tmp;
        int v1, v2;
        //create a set for each partition
        Set<Integer> [] V = new HashSet[K];
        //for each partition
        for (File file : list) {
            if (file.isFile()) {
                //open file
                br= new BufferedReader(new FileReader(file.getAbsolutePath()));
                V[i]= new HashSet();
                System.out.println(file.getName());
                //extract the set of vertices in the partition
                while((line = br.readLine())!=null){
                    tmp = line.split(" ");
                    v1 = Integer.parseInt(tmp[0]);
                    v2 = Integer.parseInt(tmp[1]);
                    V[i].add(v1);
                    V[i].add(v2);
                }
                //System.out.println("size="+V[i]);
                i++;
                br.close();
            }
        }
        
        // map for the frontiers
        Map<Integer,Integer> m = new HashMap<Integer,Integer>();
        //for each set of vertices
        for(int k=0; k<K; k++){
            //add <v_id, count>
            for (Integer v : V[k]) {
                if(m.containsKey(v)){
                    m.put(v, m.get(v)+1);
                }else{
                    m.put(v, 1);
                }
            }
        }
        
        int count =0;
        //extract the size of the intersection
        for (Map.Entry<Integer, Integer> entry : m.entrySet()) {
            //if the count of a vertex is > 1 it means that the vertex is a frontier 
            if(entry.getValue()>1)
                count++;
        }
        System.out.println("frontier vertices= "+count);
    }
    
}
