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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import utils.ValueComparator;

/**
 *
 * @author cent
 */
public class UrlPartitioner {

    public static void main(String[] args) throws IOException {
        // params: inputfile outputpath partitions
        String input = args[0];
        String outpath = args[1];
        int K = Integer.parseInt(args[2]);
        
        //open file writers for the partitions
        FileWriter [] files = new FileWriter[K];
        String name = "partitions/part_";
        for(int i=0; i<K; i++){
            files[i]= new FileWriter(new File(outpath + name + i));
        }
        
        // open input file
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line;
        Integer domain;
        String[] tmp;
        String edge;
        int old_domain=0;
        int dim = 0;
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        FileWriter fw = new FileWriter(outpath+"domains/0");
        
        //compute domain sizes and create a file for each domain
        while((line = br.readLine())!=null){
            tmp = line.split(" ");
            domain = Integer.parseInt(tmp[0]);
            edge = tmp[1]+" "+tmp[1];
            if(old_domain == domain){
                fw.write(edge+"\n");
                dim++;
            } else {
                map.put(old_domain, dim);
                fw.close();
                fw = new FileWriter(outpath+"domains/"+domain);
                fw.write(edge+"\n");
                old_domain = domain;
                dim = 1;
            }
        }
        map.put(old_domain, dim);
        fw.close();
        br.close();
        
        //sort domains in descending order by values (number of edges)
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<Integer, Integer> sortedDomains = new TreeMap<Integer, Integer>(bvc);
        sortedDomains.putAll(map);
        
        int min=0;
        int[] load = new int[K];        
        //partitioning
        for (Map.Entry<Integer, Integer> entry : sortedDomains.entrySet()) {
            //return the index of the partition with the smallest load
            min = selectMinLoad(load,K);
            //open file containing domain_i
            br= new BufferedReader(new FileReader(outpath+"domains/"+entry.getKey()));
            while((line = br.readLine())!=null){
                files[min].write(line+"\n");
            }
            br.close();
            //update load
            load[min] += entry.getValue();           
        }       
        
        // close file writers and print final load
        for(int j=0; j<K; j++){
            files[j].close();
            System.out.println("load["+j+"]="+ load[j]);
        }
        
    }
    
    public static int selectMinLoad(int [] load, int K){
        int minload = Integer.MAX_VALUE;
        int min=-1;
        for(int i=0; i<K; i++){
            if(load[i]< minload){
                minload = load[i];
                min = i;
            }
        }
        return min;
    }
    
}
