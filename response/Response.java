package response;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class Response {
  
  ResponseFactory factory;
  ArrayList<String> response_content;
  Socket client;
  byte[] size;
  ByteArrayOutputStream imageStream;
  int content_length;

  public Response(ArrayList<String> response_content, int content_length, String type, ResponseStatus status, Socket client) {
    this.client = client;
    factory = new ResponseFactory();
    this.content_length = content_length;
    this.response_content = factory.create(response_content, content_length, type, status);
  }
  
  public Response(ByteArrayOutputStream stream, String type, ResponseStatus status, Socket client) {
    this.client = client;
    imageStream = stream;
    this.response_content = new ArrayList<>();
    factory = new ResponseFactory();
    this.content_length = stream.size();
    this.response_content = factory.create(response_content, content_length, type, status);
  }
 
  public void respond(String type) throws IOException {
      OutputStream outputStream = client.getOutputStream();

      PrintWriter out = new PrintWriter( outputStream, true );
    if(!response_content.isEmpty()){
      for (String line: response_content) {
        out.println(line);
      }
    }
    if(type.contains("image")) {
      imageStream.writeTo(outputStream);
      imageStream.close();
    }
  }
  
  public int getContentLength() {
    return content_length;
  }

  
}
