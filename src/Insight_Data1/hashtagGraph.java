/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Insight_data1;

/**
 *
 * @author aparnas
 */
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class hashtagGraph {
    
    private Map<String, List<Edge>> AdjacencyList = null;
    private Date oldest_timestamp = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    
    
    public void AddNode(String hash) {
     if (AdjacencyList == null) {  
        AdjacencyList = new HashMap<String, List<Edge>>(); 
     }    
     System.out.println("adding node"+hash);
     if (hash == null)
         return;
     hash = hash.toLowerCase();
     AdjacencyList.put(hash, new LinkedList<Edge>());
    }
    
    
   public void AddEdge(String tag1, String tag2, String timestamp) {
        List<Edge> list1, list2;
         if (tag1 == null || tag2 == null)
             return;
        tag1 = tag1.toLowerCase();
        tag2 = tag2.toLowerCase();
        list1 = AdjacencyList.get(tag1);
        list2 = AdjacencyList.get(tag2);
        Edge  E, Eold;
        boolean exists = false;
        Iterator Ilist;
       
        
      try {
          if (! list1.isEmpty()) {
         Ilist= list1.iterator();
         while (Ilist.hasNext()) {           
           Eold = (Edge)Ilist.next();
           if (Eold.getTag().equalsIgnoreCase(tag2)) {
               exists = true;
               Eold.setTimestamp(timestamp);
           }
          }
          }
          if (!list2.isEmpty()) {
         Ilist = list2.iterator();
         while (Ilist.hasNext()) {           
           Eold = (Edge)Ilist.next();
           if (Eold.getTag().equalsIgnoreCase(tag1)) {
               exists = true;
               Eold.setTimestamp(timestamp);
           }
          }
          }
        if (!exists) {
            E= new Edge(tag2, timestamp);           
            list1.add(E);
         }
        if(!exists) {
            E = new Edge(tag1, timestamp);           
            list2.add(E);
        }
        
        
        /* Check if there is no edge in the graph, then this is the first edge added to the graph
         * So the oldest_timestamp = timestamp
         */
        
        if (oldest_timestamp == null) {
            oldest_timestamp = dateFormat.parse(timestamp);            
        }
        
        /*
         *  After adding this new edge to the graph, we need to update the 
         * graph, if the (oldest_timestmap - timestamp) > 60000 milliseconds
         */
        
        
        if (IsGraphUpdationReqd(oldest_timestamp, timestamp)) {
           UpdateGraph(timestamp);
           oldest_timestamp = updateOldestTimestamp();           
       }
      }catch(Exception e) {
          System.out.println(e);
      }
    }
    
   public boolean IsGraphUpdationReqd(Date date_old, String new_time) {
     Date date_new;
     try {     
     date_new = dateFormat.parse(new_time);
     if (date_new.getTime() - date_old.getTime() > 60000) {
         return true;
     }
     }catch(Exception e) {
         System.out.println(e);
     }
      return false;       
   }
   
   public Date updateOldestTimestamp() {
       Date min = null, current;
       Iterator Ilist;
       try {
           for (List<Edge> l : AdjacencyList.values()) {
           Ilist = l.iterator();
           while(Ilist.hasNext()) {
               current = ((Edge)Ilist.next()).timestamp;
               
               if (min == null) {
                   min = current;
               }
               if (min.compareTo(current) > 0) {
                   min = current;
               }               
           }
       }}catch(Exception e) {
           System.out.println(e);
       }
    return min;
   }
   
   public void UpdateGraph(String timestamp) {
       try {
       Date date = dateFormat.parse(timestamp);
       List<Edge> l;
       String key;      
       
           for(Map.Entry<String, List<Edge>> entry : AdjacencyList.entrySet()) {
           key = entry.getKey();
           l = entry.getValue();
           checkEdgeTimestamp(l, date, key);
           }          
          
        }catch(Exception e) {             
            System.out.println(e);
       }
   }
  
   public void checkEdgeTimestamp(List<Edge> l, Date date, String key) {
       Iterator Ilist = l.iterator();
       Edge E;
       long millisec;
       boolean change = false;
       try {
       while (Ilist.hasNext()) {           
           E = (Edge)Ilist.next();
          millisec = date.getTime() - E.timestamp.getTime();
          if (millisec > 60000) {
              Ilist.remove();
              change = true;
          }
       }
       if (change) {
       AdjacencyList.put(key, l);
       }
       }catch(Exception e) {
           System.out.println(e);
       }
   }
    public float Averagedegree() {
        int num_vertice ;
        Set keyset ;
        Iterator I ; 
        int edge_size=0;
        if (AdjacencyList == null) {
            return 0;
        }
        num_vertice = AdjacencyList.size();
        keyset = AdjacencyList.keySet();
        I = keyset.iterator();
        while (I.hasNext()) {
           edge_size+= (AdjacencyList.get(I.next()).size());
        }
        float degree=(float)edge_size/num_vertice;        
        return degree;
    }
    
    public void deleteIsolatedVertex() {
        Set keyset; 
        Iterator I; 
        List<Edge> l;
        String key;
        List<String> empty_node = new ArrayList<String>();
        try {
            if (AdjacencyList != null)  {           
            keyset = AdjacencyList.keySet();
            I = keyset.iterator();
            while (I.hasNext()) {
             key = (String)I.next();
             l = AdjacencyList.get(key);
             if (l.size() == 0) {
                 empty_node.add(key);                               
             }
            }
            if (!empty_node.isEmpty()) {
                for (String empty_node1 : empty_node) {
                    AdjacencyList.remove(empty_node1);
                }
            }
            if (AdjacencyList.isEmpty()) {
                AdjacencyList = null;
            }
        }
        } catch(Exception e) {           
            System.out.println(e);
        }
    }
    
    
    public boolean CheckKey(String tag) { 
        if (tag == null)
            return true;
        tag = tag.toLowerCase();
        if (AdjacencyList == null) {
            return false;
        }
        else if(AdjacencyList.containsKey(tag) == true) {
           return true;
        }  
       else {
            return false;
           
        }
    }     
    
}
