package configurations;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HttpdConf extends ConfigurationReader {
  
  private final HashMap<String, ArrayList> configList;
  
  public HttpdConf(String path) throws IOException {
    configList = new HashMap<>();
    path = getClass().getResource(path).getPath();
    parse(loadFile(path));
  }

  @Override
  void parse(BufferedReader contentsBuffer) {
    try {
      String line, key;
      line = contentsBuffer.readLine();
      String[] tokens;
      while(line != null) {
        if(line.length() > 0) {
          if(line.charAt(0) != '#') {
            tokens = line.split(" ", 3);
            key = tokens[0];
            for(int i=1 ; i<tokens.length; i++) {
              if(!configList.containsKey(key)) 
                  configList.put(key, new ArrayList());
                configList.get(key).add(tokens[i]);
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
  
  public HashMap<String, ArrayList> getList() {
    return configList;
  }
}
