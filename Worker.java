
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
 * @author Uzair and Jizhou
 */
public class Worker extends Thread {
 
  private final HashMap<String, String> request_line;
  private final HashMap<String, String> headers;
  private final InputStream client_stream;
  Resource resource;
  private final HttpdConf httpd_configs;
  private final MimeTypes mimeTypes;
  String body;
  
  private final boolean DUMP = true; 

  
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
      
    if(request_line_length == 3) {
      request_verb = request_line_tokens[0];
      
      // check for URI alias
      if(request_line_tokens[1].substring(0,4).equals("/ab/")){
          // needs update
          URI =  "/Users/YJZ/Documents/SEMESTER/2018Spring/CSC867 Internet Application Design and Development/web server/web-server-uzair-jizhou/public_html/ab1/ab2/" + request_line_tokens[1].substring(4);
      }
      else if(request_line_tokens[1].substring(0,11).equals("/~traciely/")){
          URI =  "/Users/YJZ/Documents/SEMESTER/2018Spring/CSC867 Internet Application Design and Development/web server/web-server-uzair-jizhou/public_html/" + request_line_tokens[1].substring(11);
      }
      else if(request_line_tokens[1].substring(0,9).equals("/cgi-bin/")){
          URI =  "/Users/YJZ/Documents/SEMESTER/2018Spring/CSC867 Internet Application Design and Development/web server/web-server-uzair-jizhou/public_html/cgi-bin/" + request_line_tokens[1].substring(9);
      }
      else
          URI = request_line_tokens[1];
      version = request_line_tokens[2];
    }
    else
      Error.SC400();   // if the request length is not 3, 400 error

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
