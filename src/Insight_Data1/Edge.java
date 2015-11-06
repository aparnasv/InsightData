/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Insight_data1;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
/**
 *
 * @author aparnas
 */
public class Edge {
    String tag;
    Date timestamp;
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    public Edge(String hashtag, String created_time) {
        try {
        tag = hashtag;
        timestamp = dateFormat.parse(created_time);  
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public String getTag() {
        return tag;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String time) {
        try {
        timestamp = dateFormat.parse(time);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
