/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import core.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import response.Response;
import java.io.*;
import java.net.Socket;
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
  public Response createResponse() {
    deleteFile();
    return new Response(status, response_headers, response_stream);
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
