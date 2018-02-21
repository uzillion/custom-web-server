package core;




import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import response.Response;
import response.ResponseStatus;

/**
 *
 *  
 */
public class Resource {
  
  private final HashMap<String, ArrayList> configList;
  private String URI_alias;
  private String AbsolutePath;
  boolean isScriptAliased;
  ResponseStatus error;
  String[] URI_tokens;
  String URI;
  String dirIndex;
  
  Resource(HashMap<String, ArrayList> list, String URI) {
    this.configList = list;
    AbsolutePath = "";
    isScriptAliased = false;
    this.URI = URI;
    dirIndex = (String) list.get("DirectoryIndex").get(0);
    dirIndex = dirIndex.replace("\"", "");
    
    // Breaking URI using "/" to get the first entry path
    this.URI_tokens = URI.split("/", 3);
    
    // Storing the entry path to check against aliases
    this.URI_alias = "/" + this.URI_tokens[1];
    
    // If alias or path is not end point, append "/" to the end 
    if(this.URI_tokens.length > 2)
      this.URI_alias = this.URI_alias + "/";
  }

  String resolveAddresses() {
    String path;
    int alias_index;
    // Checking if the alis exists in the config file
    if((alias_index = checkAlias("Alias")) != -1)
      path = getAbsPath("Alias", alias_index);
    
    // Else check if alias exists as a script alias
    else if((alias_index = checkAlias("ScriptAlias")) != -1) {
      path = getAbsPath("ScriptAlias", alias_index);
      isScriptAliased = true;
      System.out.println("Script Aliased");
    }
    // Else append documet root to unmodified URI
    else
      path = getAbsPath("DocumentRoot", 0);
    
      String scriptParent = new File(path).getParent();
      System.out.println(scriptParent);
//    System.out.println("Final output path: "+path);
    return path;
  }
  
  private int checkAlias(String alias) {
    ArrayList alias_list = configList.get(alias);
    if(alias_list.contains(URI_alias))
      return alias_list.indexOf(URI_alias);
    
    return -1;
  }

  // Gets absolute path of the directory by checking various conditions
  private String getAbsPath(String configKey, int alias_index) {
    String path = "";
    File file;
    if(!configKey.equals("DocumentRoot")) {
      // If alias exists, the resolved path will be stored in the next index
      path = (String) configList.get(configKey).get(alias_index+1);
      path = path.replace("\"", "");
      if(URI_tokens.length > 2)
        path = path + URI_tokens[2];
    }
    // else, if alias does not exist
    else {
      path = (String) configList.get(configKey).get(alias_index);
      path = path.replace("\"", ""); 
      // If alias was not end point, append the remainder of the path to the modified URI
      path = path.substring(0, path.length()-1);
      path = path + URI;
//      System.out.println(path);
    }
    

    file = new File(path);
    if(!file.isFile()) {
      if(!path.endsWith("/"))
        path += "/";
      path = path + dirIndex;
      file = new File(path);
    }
    
    return file.getAbsolutePath();
      
  }
}
