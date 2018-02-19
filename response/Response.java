package response;


import Server.Worker;
import java.io.IOException;
import java.io.PrintWriter;
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
  
  ResponseFactory factory;
  ArrayList<String> response_content;

  public Response(ArrayList<String> response_content, int content_length, String type, ResponseStatus status) {
    factory = new ResponseFactory();
    this.response_content = factory.create(response_content, content_length, type, status);
  }
 
  public void respond() throws IOException {
//    System.out.println("Starting to respond");
    PrintWriter out = new PrintWriter( Worker.client_socket.getOutputStream(), true );
    if(!response_content.isEmpty()){
      for (String line: response_content) {
        out.println(line);
//        System.out.println(line);
      }
    }
//    out.close();
  }
  
}
