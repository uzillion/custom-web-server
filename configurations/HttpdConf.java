package configurations;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Uzair
 */
public class HttpdConf extends ConfigurationReader {
  
  private HashMap<String, ArrayList> configList;
  
  public HttpdConf(String path) {
    configList = new HashMap<String, ArrayList>();
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
            tokens = new StringTokenizer(line, " ", false);
            key = tokens.nextToken();
            while(tokens.hasMoreTokens()) {
              if(!configList.containsKey(key)) 
                  configList.put(key, new ArrayList());
                configList.get(key).add(tokens.nextToken());
              }
            }
          }
        line = contentsBuffer.readLine();
      }
    }
    catch (IOException ex) {
      System.out.println("Error reading httpd.conf file.");
    }
  }
  
  public HashMap getList() {
    return configList;
  }
}
