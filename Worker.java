
import configurations.HttpdConf;
import configurations.MimeTypes;
import response.Response;
import response.Error;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 * @author Uzair
 */
public class Worker extends Thread {
 
  private final HashMap<String, String> request_line;
  private final HashMap<String, String> headers;
  private final InputStream client_stream;
  Resource resource;
  private final HttpdConf httpd_configs;
  private final MimeTypes mimeTypes;
  String body;
  
  private final boolean DUMP = false; 

  
  public Worker(InputStream client_stream, HttpdConf httpd_configs, MimeTypes mimeTypes) {
    request_line = new HashMap<>();
    headers = new HashMap<>();
    body = "";

    this.client_stream = client_stream;
    this.httpd_configs = httpd_configs;
    this.mimeTypes = mimeTypes;
    
  }
  
  @Override
  public void run() {
    try {
      parse(client_stream);
      resource = new Resource(httpd_configs.getList());
    } catch (IOException ex) {
      Error.internalError();
    }
  }
  
   private void parse(InputStream client_stream) throws IOException {
    BufferedReader request = new BufferedReader(new InputStreamReader(client_stream));
    String[] request_line_tokens;
    
    while(true) {
      if(request.ready()) {
        break;
      }
    }
    
    parseRequestLine(request);
    parseHeaders(request);
    if(headers.containsKey("content-length"))
      parseBody(request);
    
    if(DUMP) {
      System.out.println();
      System.out.println(this.getName()+":");
      System.out.println(request_line.toString());
      System.out.println(headers.toString());
      System.out.println(body);
      System.out.println();
    }
    
  }
  
  private void parseRequestLine(BufferedReader request) throws IOException {
    String [] request_line_tokens = request.readLine().split(" ");

    String request_verb = "";
    String URI = "";
    String version = "";
    
     
    int request_line_length = request_line_tokens.length;
      
    if(request_line_length != 0 && request_line_length <= 3) {
      request_verb = request_line_tokens[0];
      if(request_line_length == 3) {
        URI = request_line_tokens[1];
        version = request_line_tokens[2];
      } else {
        URI = request_line_tokens[1];
      }
    }
    else
      Error.badRequest();

    request_line.put("verb", request_verb);
    request_line.put("URI", URI);
    request_line.put("version", version);
  }

  private void parseHeaders(BufferedReader request) throws IOException {
    String header = "";
    header = request.readLine();
    while(header != null && header.length() != 0) {
      String[] keyValuePair = header.split(": ");
      headers.put(keyValuePair[0], keyValuePair[1]);
      header = request.readLine();
    }
  }

  private void parseBody(BufferedReader request) throws IOException {
    int c, read = 0;
    int content_length = Integer.parseInt(headers.get("content-length"));
    while((c=request.read()) != -1) {
      body += (char)c;
      read++;
      if(read == content_length)
        break;
    } 
  }
}
