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
import java.net.Socket;
import response.ResponseStatus;

/**
 *
 *  
 */
public class DeleteRequest extends Request {
      Response response;
      ArrayList<String> response_content;
      int content_length;
      String type;
      ResponseStatus status;
      Socket client;
    
  public DeleteRequest(String absPath, HashMap<String, String> headers, String type, ResponseStatus status, Socket socket) throws IOException{
    File file = new File(absPath);
    response_content = new ArrayList<>();
    client = socket;
    this.type = type;
    this.status = status;
    
    if(file.delete()){
//        Response response = new Response();
        String SC = "204";
        List<String> records = new ArrayList<String>();
        status.statusCode204();
//        response.respond(request_line, headers, body, absPath, records, SC);
    } else{
        System.out.println("Exception occurred trying to delete " + absPath);
        String SC = "500";
        status.statusCode500();
//        Response response = new Response();
        List<String> records = new ArrayList<String>();
//        response.respond(request_line, headers, body, absPath, records, SC);
    }
    
    response = new Response(response_content, content_length, type, status, client);
    response.respond(type);
          
          

  }

  @Override
  public int getContentLength() {
 //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    return 0;
  }
  
}
