/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurations;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 *  
 */
public class MimeTypes extends ConfigurationReader {

  HashMap<String, String> types;
  
  public MimeTypes(String path) {
    types = new HashMap<String, String>();
    parse(loadFile(path));
  }
  
  @Override
  void parse(BufferedReader contentsBuffer) {
    try {
      String line, extension, type;
      line = contentsBuffer.readLine();
      StringTokenizer tokens;
      while(line != null) {
        if(line.length() > 0) {
          if(line.charAt(0) != '#') {
            tokens = new StringTokenizer(line, "\t ", false);
            type = tokens.nextToken();
            while(tokens.hasMoreTokens()) {
              extension = tokens.nextToken();
              types.put(extension, type);
            }
          }
        }
        line = contentsBuffer.readLine();
      }
    } catch (IOException ex) {
      System.out.println("Error reading mime.types file.");
    }
  }
  
  public HashMap getTypes() {
    return types;
  }
  
  // Returns the suitable type for the given extension.
  public void dump() {
    for(Map.Entry<String, String> type : types.entrySet()) {
      System.out.println(type.getKey()+"\t->\t"+type.getValue());
    }
  }
  
  
}
