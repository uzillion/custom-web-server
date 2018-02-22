/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.io.*;
import java.util.HashMap;
import response.Response;
import response.ResponseStatus;

public class PostRequest extends Request {
  
  String body;
  private HashMap<String, String> request_headers;
  private String absPath;
  private HashMap<String, String> response_headers;
  OutputStream response_stream;
  ResponseStatus status;
    
  public PostRequest(String absPath, HashMap<String, String> headers, String body, ResponseStatus status, OutputStream responseStream) {
    this.response_stream = responseStream;
    this.request_headers = headers;
    this.body = body;
    this.status = status;
    this.absPath = absPath;
    this.response_headers = new HashMap<>();
  }
  
  @Override
  public Response createResponse(boolean isScriptAliased) {
    
    try {
      postData();
    } catch (IOException ex) {
      System.err.println("Error posting data.");
      status.statusCode500();
    } finally {
      return new Response(status, response_headers, response_stream, isScriptAliased);
    }
  }
  
  private void postData() throws IOException {
    BufferedWriter writer = writer = new BufferedWriter(new FileWriter(absPath));
    writer.write(body);
    writer.close();
  }
  
  @Override
  public int getContentLength() {
    return body.getBytes().length;
  } 
}
