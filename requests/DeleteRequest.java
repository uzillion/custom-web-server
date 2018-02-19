/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import response.Response;
import java.io.*;

/**
 *
 *  
 */
public class DeleteRequest extends Request {
  public DeleteRequest(HashMap<String, String> request_line, HashMap<String, String> headers, String body, String absPath) throws IOException{
    File file = new File(absPath);
    if(file.delete()){
//        Response response = new Response();
        String SC = "204";
        List<String> records = new ArrayList<String>();
//        response.respond(request_line, headers, body, absPath, records, SC);
    } else{
        System.out.println("Exception occurred trying to delete " + absPath);
        String SC = "500";
//        Response response = new Response();
        List<String> records = new ArrayList<String>();
//        response.respond(request_line, headers, body, absPath, records, SC);
    }
          
          

  }

  @Override
  public int getContentLength() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
