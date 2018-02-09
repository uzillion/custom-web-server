
import java.util.HashMap;

/**
 *
 * @author Uzair and Jizhou
 */
public class Resource {
  
  HashMap configList;
  
  public Resource(HashMap configlist) {
    
    this.configList = configList;
    System.out.println(configlist.get("Alias"));
//    System.out.println(configlist.get("Alias").get(1));
  }  
}
