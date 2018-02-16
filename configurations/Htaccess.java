/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configurations;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Htaccess extends ConfigurationReader {
  
  HashMap<String, String> auth_details;

  public Htaccess(String path) {
    auth_details = new HashMap<>();
    parse(loadFile(path));
    System.out.println(auth_details.toString());
  }

  @Override
  void parse(BufferedReader fileContentBuffer) {
    String tokens[], line;
    try {
      while((line = fileContentBuffer.readLine()) != null ) {
        tokens = line.split(" ", 2);
        auth_details.put(tokens[0], tokens[1]);
      }
    } catch (IOException ex) {
      Logger.getLogger(Htaccess.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public HashMap getAuth() {
    return auth_details;
  }
  
}
