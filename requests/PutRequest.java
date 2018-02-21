/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import response.Response;

/**
 *
 *  
 */
public class PutRequest extends Request {
  public PutRequest(HashMap<String, String> request_line, HashMap<String, String> headers, String body, String absPath) throws IOException{
      List<String> records = new ArrayList<String>();
//      Response response = new Response();
      String SC;
      try{
        File f = new File(absPath);
        f.createNewFile();
        SC = "201";
//        response.respond(request_line, headers, body, absPath, records, SC);
        
      } catch (Exception e){
        System.out.println("Exception occurred trying to create " + absPath);
        SC = "500";
//        response.respond(request_line, headers, body, absPath, records, SC);
      }
      
      
      
  }

  @Override
  public int getContentLength() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
