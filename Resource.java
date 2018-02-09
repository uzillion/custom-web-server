
import java.util.HashMap;

/**
 *
 * @author Uzair
 */
public class Resource {
  
  HashMap configList;
  
  public Resource(HashMap configlist) {
    
    this.configList = configList;
    System.out.println(configlist.get("Alias"));
    
  }  
}
