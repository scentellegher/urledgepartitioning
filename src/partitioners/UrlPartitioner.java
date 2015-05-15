/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package partitioners;

import gnu.trove.map.hash.THashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        long start, end;
        DateFormat df = new SimpleDateFormat("mm:ss:SSS");
        
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
        int old_domain=0;
        int dim = 0;
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        
        System.out.println("------------------------");
        System.out.println("Start computing domain sizes...");
        start = System.currentTimeMillis();
        
        //compute domain sizes
        while((line = br.readLine())!=null){
            tmp = line.split(" ");
            domain = Integer.parseInt(tmp[0]);
            if(old_domain == domain){
                dim++;
            } else {
                map.put(old_domain, dim);
                old_domain = domain;
                dim = 1;
            }
        }
        map.put(old_domain, dim);
        
        System.out.println("End computing domain sizes!");
        end = System.currentTimeMillis();
        System.out.println("time:"+ df.format(new Date(end - start)));
        System.out.println("Number of domains: "+map.size());
        System.out.println("------------------------");
        br.close();
        
        //sort domains in descending order by values (number of edges)
        System.out.println("Start domain sorting...");
        start = System.currentTimeMillis();
        
        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<Integer, Integer> sortedDomains = new TreeMap<Integer, Integer>(bvc);
        sortedDomains.putAll(map);
        
        System.out.println("End domain sorting!");
        end = System.currentTimeMillis();
        System.out.println("time:"+ df.format(new Date(end - start)));
        System.out.println("------------------------");
        
        int min=0;
        int[] load = new int[K];
        Map<Integer, Integer> dom2part = new THashMap<Integer, Integer>();
        
        //greedy assignment -> bigger domains to lowest load partitions
        System.out.println("Start greedy assignment...");
        start = System.currentTimeMillis();
        
        for (Map.Entry<Integer, Integer> entry : sortedDomains.entrySet()) {
            //return the index of the partition with the smallest load
            min = selectMinLoad(load,K);
            //add the domain->partition assignment
            dom2part.put(entry.getKey(), min);
            //update load
            load[min] += entry.getValue();           
        }       
        
        System.out.println("size="+dom2part.size());
        System.out.println("End greedy assignment...");
        end = System.currentTimeMillis();
        System.out.println("time:"+ df.format(new Date(end - start)));
        System.out.println("------------------------");
       
        String edge;
        // write the partitions
        System.out.println("Dump partitions...");
        start = System.currentTimeMillis();
        
        br = new BufferedReader(new FileReader(input));
        while((line = br.readLine())!=null){
            tmp = line.split(" ");
            domain = Integer.parseInt(tmp[0]);
            edge = tmp[1]+" "+tmp[2];
            files[dom2part.get(domain)].write(edge+"\n");
        }
        br.close();
        
        System.out.println("End dump!...");
        end = System.currentTimeMillis();
        System.out.println("time:"+ df.format(new Date(end - start)));
        System.out.println("------------------------");
        
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
