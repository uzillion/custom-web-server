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
import java.util.StringTokenizer;

/**
 *
 * @author Uzair
 */
public class MimeTypes extends ConfigurationReader {

  HashMap<String, ArrayList> types;
  
  public MimeTypes(String path) {
    types = new HashMap<String, ArrayList>();
    parse(loadFile(path));
  }
  
  @Override
  void parse(BufferedReader contentsBuffer) {
    try {
      String line, key;
      line = contentsBuffer.readLine();
      StringTokenizer tokens;
      while(line != null) {
        if(line.length() > 0) {
          if(line.charAt(0) != '#') {
            tokens = new StringTokenizer(line, "\t ", false);
            key = tokens.nextToken();
            types.put(key, new ArrayList());
            while(tokens.hasMoreTokens()) {
              types.get(key).add(tokens.nextToken());
            }
          }
        }
        line = contentsBuffer.readLine();
      }
    } catch (IOException ex) {
      System.out.println("Error reading mime.types file.");
    }
  }
  
  public ArrayList getExtensions(String type) {
    if(types.containsKey(type))
      return types.get(type);
    else
      return null;
  }
  
  // Returns the suitable type for the given extension.
  public String getType(String extension) {
    return "NOT SUPPORTED YET";
  }
  
}
