package response;


import java.util.ArrayList;
import java.util.Arrays;

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
  
}
