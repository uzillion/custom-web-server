/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
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
  private String body;
  
  public PutRequest(String absPath, HashMap<String, String> headers, String body, ResponseStatus status, OutputStream responseStream) {
    this.body = body;
    this.status = status;
    this.status.statusCode201();
    this.absPath = absPath;
    this.request_headers = headers;
    response_headers = new HashMap<>();
    response_stream = responseStream;
  }
  
  @Override
  public Response createResponse(boolean isScriptAliased) {
    try {
      createFile();
      putData();
    } catch (IOException ex) {
      System.err.println("Error creating file.");
      status.statusCode500();
    } finally {
      return new Response(status, response_headers, response_stream, isScriptAliased);
    }
  }

  private void createFile() throws IOException {
    File file = new File(absPath);
    if(file.exists()) {
      file.delete();
    }
    file.createNewFile();
  }
  
  private void putData() throws IOException {
    BufferedWriter writer = writer = new BufferedWriter(new FileWriter(absPath));
    writer.write(body);
    writer.close();
  }
  
  @Override
  public int getContentLength() {
    return 0;
  }
}
