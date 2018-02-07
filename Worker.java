
import response.Response;
import response.Error;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Uzair
 */
public class Worker extends Thread {
 
  private final HashMap<String, String> request_line = new HashMap<>();
  private final HashMap<String, String> headers = new HashMap<>();
  private final InputStream client_stream;
  
  public Worker(InputStream client_stream) {
    this.client_stream = client_stream;
  }
  
  @Override
  public void run() {
    System.out.println(this.getName());
    try {
      parse(client_stream);
    } catch (IOException ex) {
      Error.internalError();
    }
  }
  
   private void parse(InputStream client_stream) throws IOException {
    BufferedReader request = new BufferedReader(new InputStreamReader(client_stream));
    String URI = "";
    String version = "";
    String header = "";
    String body = "";
    
    String[] request_line_array = parseRequest(request);
    request_line.put("verb", request_line_array[0]);
    request_line.put("URI", request_line_array[1]);
    request_line.put("version", request_line_array[2]);
    
    header = request.readLine();
    while(header != null && header.length() != 0) {
      String[] keyValuePair = header.split(": ");
      headers.put(keyValuePair[0], keyValuePair[1]);
      header = request.readLine();
    }
      
    int c, read = 0;
    int content_length = Integer.parseInt(headers.get("content-length"));
    while((c=request.read()) != -1) {
      body += (char)c;
      read++;
      if(read == content_length)
        break;
    } 
      
  }
  
  private static String[] parseRequest(BufferedReader request) throws IOException {
    String[] request_line = {};
    String request_verb = "";
    String URI = "";
    String version = "";
    while(true) {
      if(request.ready()) {
        request_line = request.readLine().split(" ");
        break;
      }
    }
     
    int request_line_length = request_line.length;
      
    if(request_line_length != 0 && request_line_length <= 3) {
      request_verb = request_line[0];
      if(request_line_length == 3) {
        URI = request_line[1];
        version = request_line[2];
      } else {
        URI = request_line[1];
      }
    }
    else
      Response.badRequest();

    request_line[0] = request_verb;
    request_line[1] = URI;
    request_line[2] = version;
    return request_line;
  }
}
