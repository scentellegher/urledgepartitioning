/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
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
        Set<Integer> [] V = new THashSet[K];
        //for each partition
        for (File file : list) {
            if (file.isFile()) {
                //open file
                br= new BufferedReader(new FileReader(file.getAbsolutePath()));
                V[i]= new THashSet();
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
        
        System.out.println("Start map for frontiers");
        // map for the frontiers
        Map<Integer,Integer> m = new THashMap<Integer,Integer>();
        //for each set of vertices
        for(int k=0; k<K; k++){
            //add <v_id, count>
            for (Integer v : V[k]) {
                if(m.containsKey(v)){
                    m.put(v, m.get(v)+1);
                }else{
                    m.put(v, 1);
                }
                if(m.size()%1000000==0)
                    System.out.println("size="+m.size());
            }
        }
        
        System.out.println("Find intersection");
        int count =0;
        int countReplica= 0;
        //extract the size of the intersection
        for (Map.Entry<Integer, Integer> entry : m.entrySet()) {
            countReplica+=entry.getValue();
            //if the count of a vertex is > 1 it means that the vertex is a frontier 
            if(entry.getValue()>1)
                count++;
            
        }
        System.out.println("frontier vertices= "+count);
        System.out.println("countReplica="+countReplica+" |V|="+m.size());
        System.out.println("number of replicas per node= "+ (double) countReplica / m.size());
    }
    
}
