
import configurations.Htaccess;
import configurations.HttpdConf;
import configurations.MimeTypes;
import response.Response;
import response.ResponseError;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 *  
 */
public class Worker extends Thread {
 
  private final HashMap<String, String> request_line;
  private final HashMap<String, String> headers;
  private final InputStream client_stream;
  private final HttpdConf httpd_configs;
  private final MimeTypes mimeTypes;
  String absPath;
  Htaccess htaccess;
  ResponseError error;
  Resource resource;
  String body;
  
  private final boolean DUMP = true; 

  
  public Worker(InputStream client_stream, HttpdConf httpd_configs, MimeTypes mimeTypes) {
    request_line = new HashMap<>();
    headers = new HashMap<>();
    body = "";
    error = new ResponseError();
    this.client_stream = client_stream;
    this.httpd_configs = httpd_configs;
    this.mimeTypes = mimeTypes;
    
  }
  
  @Override
  public void run() {
    String authPath;
    try {
      parse(client_stream);
      System.out.println("worker, place 4");
      resource = new Resource(httpd_configs.getList(), request_line.get("URI"));
      absPath = resource.resolveAddresses();
      System.out.println("worker, place 1");
      System.out.println(absPath);
      
      // check if file exists
      fileexist();

      // check if a script alias
      if(resource.sa){
        runscript();
      }
      

      authPath = (String) httpd_configs.getList().get("AccessFileName").get(0);
      authPath = authPath.replace("\"", "");
      File authFile = new File(authPath);
      if(authFile.exists())
        htaccess = new Htaccess(authPath);
      
    } catch (IOException ex) {
      error.internalError();
    }
  }
  
   private void parse(InputStream client_stream) throws IOException {
    BufferedReader requestBuffer = new BufferedReader(new InputStreamReader(client_stream));
    String[] request_line_tokens;
    
    while(true) {
      if(requestBuffer.ready()) {
        break;
      }
    }
    
    parseRequestLine(requestBuffer);
    parseHeaders(requestBuffer);
    if(headers.containsKey("content-length"))
      parseBody(requestBuffer);
    
    if(DUMP) {
      System.out.println();
      System.out.println(this.getName()+":");
      System.out.println(request_line.toString());
      System.out.println(headers.toString());
      System.out.println(body);
      System.out.println();
    }
    
  }
  
  private void parseRequestLine(BufferedReader requestBuffer) throws IOException {
    String [] request_line_tokens = requestBuffer.readLine().split(" ");

    String request_verb = "";
    String URI = "";
    String version = "";
    
     
    int request_line_length = request_line_tokens.length;
    
    if(request_line_length != 3){
        error.SC400();
    }
    

    
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
      error.badRequest();

    request_line.put("verb", request_verb);
    request_line.put("URI", URI);
    request_line.put("version", version);
  }

  private void parseHeaders(BufferedReader requestBuffer) throws IOException {
    String header = "";
    header = requestBuffer.readLine();
    while(header != null && header.length() != 0) {
      String[] keyValuePair = header.split(": ");
      headers.put(keyValuePair[0], keyValuePair[1]);
      header = requestBuffer.readLine();
    }
  }

  private void parseBody(BufferedReader requestBuffer) throws IOException {
    int c, read = 0;
    int content_length = Integer.parseInt(headers.get("content-length"));
    while((c=requestBuffer.read()) != -1) {
      body += (char)c;
      read++;
      if(read == content_length)
        break;
    } 
  }
  
  public void fileexist(){
    System.out.println(absPath);
    File file = new File(absPath);
    boolean check = file.exists();
    if(!check){
        error.SC404();
    }
  }
  
  public void runscript() throws IOException{
      System.out.println("worker place 3");
      System.out.println(absPath);
      Process process;
      try{
        System.out.println("worker place 6");
        process = Runtime.getRuntime().exec(absPath);
        process.waitFor();
        System.out.println("worker place 7");
        System.out.println(process.exitValue());
        if(process.exitValue() == 0)
            error.SC200();
        else
            error.SC500();
      } catch(Exception e){
        System.out.println("worker place 5 exception e");
        error.SC500();
      }
  }
}
