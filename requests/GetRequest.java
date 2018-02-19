/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import response.Response;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import response.ResponseStatus;


public class GetRequest extends Request {
  
  Response response;
  ArrayList<String> response_content;
  int content_length;
  
  public GetRequest(String absPath, String type, ResponseStatus status) throws IOException{
    response_content = new ArrayList<>();
    content_length = loadResponseContent(absPath);
    response = new Response(response_content, content_length, type, status);
    response.respond();
  }

  private int loadResponseContent(String path) throws FileNotFoundException, IOException {
    int content_length = 0;
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line;
    while ((line = reader.readLine()) != null) {
      content_length += getSize(line);
      response_content.add(line);
    }
    reader.close();
    return content_length;
  }
  
  private int getSize(String string) throws UnsupportedEncodingException {
    byte[] bytes = string.getBytes("UTF-8");
    return bytes.length;
  }
  
  public int getContentLength() {
    return content_length;
  }
}
