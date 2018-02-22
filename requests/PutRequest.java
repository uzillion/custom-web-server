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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import response.Response;
import response.ResponseStatus;

/**
 *
 *  
 */
public class PutRequest extends Request {

  private String absPath;
  private ResponseStatus status;
  private HashMap<String, String> request_headers;
  private HashMap<String, String> response_headers;
  private OutputStream response_stream;
  
  public PutRequest(String absPath, HashMap<String, String> headers, ResponseStatus status, OutputStream responseStream) {
    this.status = status;
    this.status.statusCode201();
    this.absPath = absPath;
    this.request_headers = headers;
    response_headers = new HashMap<>();
    response_stream = responseStream;
  }
  
  @Override
  public Response createResponse() {
    try {
      createFile();
    } catch (IOException ex) {
      System.err.println("Error creating file.");
      status.statusCode500();
    }
    return new Response(status, response_headers, response_stream);
  }

  private void createFile() throws IOException {
    File file = new File(absPath);
    if(file.exists()) {
      file.delete();
    }
    file.createNewFile();
  }
  
  @Override
  public int getContentLength() {
    return 0;
  }
}
