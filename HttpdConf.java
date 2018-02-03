
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
  
  HashMap<String, ArrayList> configList;
  
  public HttpdConf(String path) {
    parse(loadFile(path));
  }

  public void parse(BufferedReader contentsBuffer) {
    try {
      String line;
      line = contentsBuffer.readLine();
      StringTokenizer tokens;
      while(line != null) {
        if(line.length() > 0) {
          if(line.charAt(0) != '#') {
//            tokens = new StringTokenizer(line, " ", false);
            System.out.println(line);
          }
        }
        line = contentsBuffer.readLine();
      }
    } catch (IOException ex) {
      Logger.getLogger(HttpdConf.class.getName()).log(Level.SEVERE, null, ex);
    }
  }    
}
