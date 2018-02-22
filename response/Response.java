package response;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Response {
  
  ResponseFactory factory;
  ArrayList<String> response_content;
  Socket client;
  byte[] size;
  ByteArrayOutputStream imageByteStream;
  private OutputStream response_stream;
//  boolean isScriptAliased = ;
  

  public Response(ResponseStatus status, HashMap<String, String> response_headers, OutputStream response_stream, boolean isScriptAliased) {
    initializeData(response_stream);
    this.response_content = factory.create(status, response_headers, isScriptAliased);
  }

  public Response(ResponseStatus status, HashMap<String, String> response_headers, String body, OutputStream response_stream, boolean isScriptAliased) {
    initializeData(response_stream);
    this.response_content = factory.create(status, response_headers, body, isScriptAliased);
  }

  public Response(ResponseStatus status, HashMap<String, String> response_headers, ByteArrayOutputStream imageByteStream, OutputStream response_stream, boolean isScriptAliased) {
    initializeData(response_stream);
    this.imageByteStream = imageByteStream;
    this.response_content = factory.create(status, response_headers, isScriptAliased);
  }
  
  public Response(ResponseStatus status, OutputStream response_stream, boolean isScriptAliased) {
    initializeData(response_stream);
    HashMap<String, String> response_headers = new HashMap<>();
    this.response_content = factory.create(status, response_headers, isScriptAliased);
  }
  
  void initializeData(OutputStream response_stream) {
    this.response_stream = response_stream;
    this.imageByteStream = null;
    factory = new ResponseFactory();
  }
 
  public void respond() throws IOException {
    PrintWriter out = new PrintWriter( response_stream, true );
    if(!response_content.isEmpty()){
      for (String line: response_content) {
        out.println(line);
      }
    }
    if(imageByteStream != null) {
      imageByteStream.writeTo(response_stream);
      imageByteStream.close();
    }
    out.close();
  }
}
