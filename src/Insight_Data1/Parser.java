/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Insight_data1;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author aparnas
 */
public class Parser {
     final int FALSE = 0;
     final int TRUE = 1;
     final int NULL = 2;
     final int STRING = 3;
     final int NUMBER = 4;
     final int CURLY_OPEN = 5;
     final int CURLY_CLOSE = 6;
     final int SQUARE_OPEN = 7;
     final int SQUARE_CLOSE = 8;
     final int COMMA = 9;
     final int COLON = 10;
     final int NONE = 11;
     int index;
     public boolean unicode_exist;
    
     public boolean getUniExistence() {
         return unicode_exist;
     }
    
     public Object parse(String jsonstring) {
       // System.out.println(jsonstring);
        if (jsonstring == null) {
            return null;
        } else {
            char[] charArray = jsonstring.toCharArray();
            index = 0;  
            //System.out.println("Calling parse value");
            
            Object obj = parseValue(charArray);
            return obj;
        }
    }
    
    
    
    public  ArrayList parseArray(char[] charArray) {
        ArrayList array = new ArrayList();
        int token;
        boolean complete = false;
        
        //extract the character '['
        nextToken(charArray);
        
        while (!complete) {
          token = tokenlookup(charArray, index);
          if (token == NONE ) {
              return null;
          } else if (token == COMMA) {
              nextToken(charArray);
          } else if (token == SQUARE_CLOSE) {
              nextToken(charArray);
              break;
          }else {
              Object obj = parseValue(charArray);
              array.add(obj);
          }
        }
        return array;
    }
    public  HashMap parseObject(char[] charArray) {
       // System.out.println("Inside object");
        HashMap hash = new HashMap();
        int token;
        boolean complete = false;
        
        //extract the opening curly braces
        nextToken(charArray);
        
        while (!complete) {
            token = tokenlookup(charArray, index);
            if (token == NONE) {
                return null;
            } else if (token == COMMA){
                //extract the comma and move to next character
                nextToken(charArray);
            } else if (token == CURLY_CLOSE) {
                //extract the curly close and move the index to next token and return the current table
                nextToken(charArray);
                return hash;
            } else {
                // extract string and value and put it into the hashmap
                
                // check for string
                String string = parseString(charArray);
                //System.out.println(string);
                // check for : and extract that character
                token = nextToken(charArray);
                 if (token != COLON) {
                     System.out.println("Token found , format valid");
                     return null;
                 }
                // check for value
                Object value = parseValue(charArray);
                //System.out.println(value);
                hash.put(string, value);
            }
        }
        return hash;        
    }
    
    public  Object parseValue(char[] charArray) {
        switch(tokenlookup(charArray, index)) {
            case STRING:
                return parseString(charArray);
            case NUMBER:
                return parseNumber(charArray);
            case CURLY_OPEN:
                //System.out.println("seraching object");
                return parseObject(charArray);
            case SQUARE_OPEN:
                return parseArray(charArray);
            case TRUE:
                nextToken(charArray);
                return true;
            case FALSE:
                nextToken(charArray);
                return false;
            case NULL:
                nextToken(charArray);
                return null;
            case NONE:
                break;
        }
        return null;
    }
    public  double parseNumber(char[] charArray) {
        double value;
         skipWhiteSpace(charArray);
         int lastindex = getlastIndexOf(charArray, index);
         int charlength = lastindex-index+1;
              
         value = Double.parseDouble(new String(charArray, index, charlength));
         index = lastindex+1;
         return value;
    }
    
    public int getlastIndexOf(char[] charArray, int local_index) {
        for(local_index = index; local_index<charArray.length; local_index++) {
           if ("0123456789-.eE".indexOf(charArray[local_index]) == -1) {
               break;
           }
        }
        return local_index-1;
    }
    public String parseString(char[] charArray) {
        StringBuilder string = new StringBuilder();
        char c;
        boolean complete = false;
        
        //skip the white space before the string
        skipWhiteSpace(charArray);
        
        //Extracting the open quotes characeter '"'
        c = charArray[index++];
        
        while (!complete) {
            
            if (index == charArray.length) {
                break;
            }
            c = charArray[index++];
            if (c == '"')  {
               complete = true;
               break;
            } else if (c=='\\') { 
                  // Looking for special characters in the string
                   if (index == charArray.length) {
                       break;
                    }
                    c = charArray[index++];
                    if (c=='"') {
                        string.append('"');
                    } else if (c=='\'') {
                        string.append('\'');
                    } else if (c=='\\') {
                        string.append('\\');
                    } else if (c=='/') {
                        string.append('/');
                    } else if (c=='n') {
                        string.append(' ');
                    } else if (c=='b') {
                        string.append('\b');
                    } else if (c=='f') {
                        string.append(' ');
                    } else if (c=='r') {
                        string.append(' ');
                    } else if (c=='t') {
                        string.append(' ');
                    } else if (c=='u') {
                        int length = charArray.length - index;
                        if (length >= 4) {
                            
                         // Skipping the next four character for unicode.
                            string.append('\\');
                            string.append('u');
                            for (int i=index;i<index+4;i++)
                               string.append(charArray[i]);
                         index = index+4;                     
                        } else {
                            break;
                             }
                    }
                } else {
                     string.append(c);
                }
           }
        if (!complete) {
            return null;
        }
        return string.toString();
    }
        

    
    public  int tokenlookup(char[] charArray, int index) {       
       // Skip the white spaces if any present
        skipWhiteSpace(charArray);  
        if (index == charArray.length) {
            return NONE;
        }
        char c = charArray[index];
        index++;
        switch(c) {
            case '{':
                return CURLY_OPEN;
            case '}':
                return CURLY_CLOSE;
            case '[':
                return SQUARE_OPEN;
            case ']':
                return SQUARE_CLOSE;
            case ':':
                return COLON;
            case ',':
                return COMMA;
            case '"':
                return STRING;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '-':
                //fall through
                return NUMBER;           
        }
        index--;
        int length = charArray.length - index;
        
        //check for false
        if (length >= 5) {
            if (charArray[index] == 'f' &&
                    charArray[index+1] == 'a' &&
                    charArray[index+2] == 'l' &&
                    charArray[index+3] == 's' &&
                    charArray[index+4] == 'e') {
                index += 5;
                return FALSE;
            }
        }
        
        // check for true
         if (length >= 4) {
            if (charArray[index] == 't' &&
                    charArray[index+1] == 'r' &&
                    charArray[index+2] == 'u' &&
                    charArray[index+3] == 'e' ) {
                index += 4;
                return TRUE;
            }
        }
         // check for null
         if (length >= 4) {
            if (charArray[index] == 'n' &&
                    charArray[index+1] == 'u' &&
                    charArray[index+2] == 'l' &&
                    charArray[index+3] == 'l' ) {
                index += 4;
                return NULL;
            }
        }
         
         // else return none
         return NONE;
    }
    public  int nextToken(char[] charArray) {
        // Skip the white spaces if any present
        skipWhiteSpace(charArray);  
        if (index == charArray.length) {
            return NONE;
        }
        char c = charArray[index];
        index++;
        switch(c) {
            case '{':
                return CURLY_OPEN;
            case '}':
                return CURLY_CLOSE;
            case '[':
                return SQUARE_OPEN;
            case ']':
                return SQUARE_CLOSE;
            case ':':
                return COLON;
            case ',':
                return COMMA;
            case '"':
                return STRING;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '-':
                //fall through
                return NUMBER;           
        }
        index--;
        int length = charArray.length - index;
        
        //check for false
        if (length >= 5) {
            if (charArray[index] == 'f' &&
                    charArray[index+1] == 'a' &&
                    charArray[index+2] == 'l' &&
                    charArray[index+3] == 's' &&
                    charArray[index+4] == 'e') {
                index += 5;
                return FALSE;
            }
        }
        
        // check for true
         if (length >= 4) {
            if (charArray[index] == 't' &&
                    charArray[index+1] == 'r' &&
                    charArray[index+2] == 'u' &&
                    charArray[index+3] == 'e' ) {
                index += 4;
                return TRUE;
            }
        }
         // check for null
         if (length >= 4) {
            if (charArray[index] == 'n' &&
                    charArray[index+1] == 'u' &&
                    charArray[index+2] == 'l' &&
                    charArray[index+3] == 'l' ) {
                index += 4;
                return NULL;
            }
        }
         
         // else return none
         return NONE;
    }
    
    public  void skipWhiteSpace(char[] charArray) {
        for(;index<charArray.length;index++) {
            if (" \n\t\r".indexOf(charArray[index]) == -1) {
                break;
            }
        }
        
    }
    
     public String CleanText(String S) {
        String Snew;
        ArrayList<String> substring = new ArrayList<String>();
        unicode_exist = false;
        for (int i =0; i<S.length();i++) {            
            if (S.charAt(i) == '\\') {
                i++;
                if(S.charAt(i) == 'u') {
               substring.add(S.substring(i-1, i+5));               
               //Snew = S.replace(S.substring(i-1, i+5), "");               
                }               
            }            
           }
        
        
            Snew = S;
            for (int i=0;i<substring.size();i++) {
                unicode_exist = true;
                Snew = Snew.replace(substring.get(i), "");
                
            }
             
              
        return Snew;
        }
   
}
        
    
