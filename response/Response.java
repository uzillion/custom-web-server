package response;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  
 */
public class Response {
  
  static final ArrayList<String> REQUEST_VERBS = new ArrayList(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE"));

  public static void badRequest() {
    
  }
  
  public void call(HashMap<String, String> request_line, HashMap<String, String> headers, String body, String absPath, List<String> records, String SC){
      ResponseFactory rf = new ResponseFactory();
      rf.reply(request_line, headers, body, absPath, records, SC);
  }
  
}
