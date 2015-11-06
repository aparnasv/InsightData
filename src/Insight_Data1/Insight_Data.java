package Insight_data1;

//import java.io.*;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author aparnas
 */
public class Insight_Data {
     /**
     * @param args the command line arguments
     */ 
    
      static BufferedWriter writer, writedegree;
      static hashtagGraph Graph;
      static BufferedReader br;
      
    public static void main(String[] args) {
        // TODO code application logic here
        try {         
      
        String tweet, text,output, tag, timestamp = null;        
        HashMap h;
        boolean flag, tweet_arrived = false; 
        br = new BufferedReader(new FileReader("../../tweet_input/tweets.txt"));
        writer = new BufferedWriter(new FileWriter("../../tweet_output/ft1.txt", true));
        writedegree = new BufferedWriter(new FileWriter("../../tweet_output/ft2.txt", true));
        Parser parser = new Parser();                
        Graph = new hashtagGraph();
        tweet = null;
        int tot_unicode = 0;
        
             
      /*
       * The below piece of code write the text from the tweet in the format
       * <contents of "text" field> (timestamp: <contents of "created_at" field>)
       * into the file ft1.txt.
       */   
        while ((tweet = br.readLine()) != null) {  
            System.out.println(tweet);
            h = (HashMap)parser.parse(tweet); 
            if (h == null)
                continue;
             text = (String)h.get("text"); 
             if (text == null)
                 continue;
             text = parser.CleanText(text); 
             System.out.println(text);
             if (parser.unicode_exist == true) {               
                tot_unicode++;
            }
            timestamp = (String)h.get("created_at"); 
            if (text != null && timestamp != null) {
            writer.write(text+" "+"(timestamp: "+timestamp+")");
            writer.newLine();
            writer.flush(); 
            }          
            createUpdateGraph(text, timestamp);
            Graph.deleteIsolatedVertex();
            float degree = Graph.Averagedegree(); 
            String round_deg = String.format("%.2f", degree);  
            System.out.println(round_deg);
            writedegree.write(round_deg);
            writedegree.newLine();
            writedegree.flush();           
        }
        System.out.println(tot_unicode);
        writer.newLine();
        writer.write(tot_unicode+" tweets contained unicode.");
        writer.flush();
        
         
       
        }catch(Exception e){
            System.out.println(e);
        } finally {
            try {
            if (writer != null) {
                writer.close();
            }
            if (writedegree != null) {
                writedegree.close();
            }
            if (br != null) {
                br.close();
            }
            }catch(Exception e) {
                
            }
        }
        
       
     }
    
      
    
    /*
     * This function reads the text field, extract the hash tag and create a hashtagGraph
     * calculate the average degree and update the output in ft2.txt file.
     */
     public static void createUpdateGraph(String tweet, String timestamp) {
         try {             
             
             if (tweet != null && timestamp != null) {
             String[] result = tweet.split(" ");
              List<String> tags = new ArrayList<String>();
              
                              
               //Adding vertex or tags to the graph
             for (int j=0; j<result.length;j++) {
                if (! result[j].isEmpty()) {
                 if (result[j].charAt(0)== '#') {
                    tags.add(result[j]);
                    //System.out.println(result[j]);
                    if (Graph.CheckKey(result[j]) == false) {                          
                        Graph.AddNode(result[j]);            
                    }                         
                  }
                }
             }
                // Adding edge between the nodes             
             for(int j=0;j<tags.size()-1;j++) {
                 //System.out.println(tags.get(j));
                 for(int k=j+1;k<tags.size();k++) {
                     Graph.AddEdge(tags.get(j), tags.get(k), timestamp);
                 }
             }           
             }
             
            }catch(Exception e){
              System.out.println(e);
        }
            
    }
}
