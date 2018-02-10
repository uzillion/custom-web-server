
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 *  
 */
public class Resource {
  
  private HashMap<String, ArrayList> configList;
  private String URI_alias;
  private String AbsolutePath;
  String[] URI_tokens;
  String URI;
  
  Resource(HashMap<String, ArrayList> list, String URI) {
    this.configList = list;
    AbsolutePath = "";
    this.URI = URI;
    this.URI_tokens = URI.split("/", 3);
    this.URI_alias = "/" + this.URI_tokens[1];
    if(this.URI_tokens.length > 2)
      this.URI_alias = this.URI_alias + "/";
  }

  void resolveAddresses() {
    String path;
    int alias_index;
    if((alias_index = checkAlias("Alias")) != -1) {
      path = getAbsPath("Alias", alias_index);
      // send path to appropriate class
      System.out.println("Alias found");
    }
    else if((alias_index = checkAlias("ScriptAlias")) != -1) {
      path = getAbsPath("ScriptAlias", alias_index);
      // send path to appropriate class
      System.out.println("ScriptAlias found");      
    }
    else {
      path = getAbsPath("DocumentRoot", 0);
    }
    
    System.out.println("Final output path: "+path);
      
  }
  
  private int checkAlias(String alias) {
    ArrayList alias_list = configList.get(alias);
    if(alias_list.contains(URI_alias))
      return alias_list.indexOf(URI_alias);
    
    return -1;
  }

  private String getAbsPath(String configKey, int alias_index) {
    String path;
    if(!configKey.equals("DocumentRoot")) {
      path = (String) configList.get(configKey).get(alias_index+1);
      path = path.substring(1, path.length()-1);
    }  
    else {
      path = (String) configList.get(configKey).get(alias_index);
      path = path.substring(1, path.length()-1); 
      System.out.println(path);
    }
    if(URI_tokens.length > 2)
        path = path + URI_tokens[2];
    
    File file = new File(path);
    if(file.isFile())
      return file.getAbsolutePath();
    else {
      path = path + "index.html";
      file = new File(path);
      return file.getAbsolutePath();
    }
  }
}
