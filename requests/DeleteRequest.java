/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.util.HashMap;
import response.Response;
import java.io.*;
import response.ResponseStatus;

public class DeleteRequest extends Request {
  Response response;
  HashMap<String, String> response_headers;
  OutputStream response_stream;
  int content_length;
  ResponseStatus status;
  String absPath;
    
  public DeleteRequest(String absPath, HashMap<String, String> headers, ResponseStatus status, OutputStream responseStream) {
    this.status = status;
    this.status.statusCode204();
    this.absPath = absPath;
    response_stream = responseStream;
    response_headers = new HashMap<>();
  }
  
  @Override
  public Response createResponse(boolean isScriptAliased) {
    deleteFile();
    return new Response(status, response_headers, response_stream, isScriptAliased);
  }

  private void deleteFile() {
    File file = new File(absPath);
    file.delete();
  }
  
  @Override
  public int getContentLength() {
    return 0;
  }
}
