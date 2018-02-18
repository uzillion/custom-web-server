
import configurations.Htaccess;
import configurations.Htpassword;
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
  Htaccess htaccess;
  Htpassword password;
  ResponseError error;
  Resource resource;
  String body;
  
  private final boolean DUMP = false; 

  
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
    String absPath;
    String parentPath;
    String accessFileName;
    try {
      parse(client_stream);
      resource = new Resource(httpd_configs.getList(), request_line.get("URI"));
      absPath = resource.resolveAddresses();
      parentPath = new File(absPath).getParent();
      parentPath = parentPath.replace("\"", "") + "/";
      accessFileName = (String) httpd_configs.getList().get("AccessFileName").get(0);
      accessFileName  = accessFileName.replace("\"", "");
      File authFile = new File(parentPath + accessFileName);
      System.out.println(authFile.getPath());
      if(authFile.exists()) {
        System.out.println("Access file exists");
        htaccess = new Htaccess(authFile.getPath());
        if(headers.containsKey("Authorization")) {
          boolean authorized;
          password = new Htpassword(htaccess.getPasswordFileNmae());
          authorized = password.isAuthorized(headers.get("Authorization"));
          if(authorized) {
            //Auth Error 403
          }
            
        } else {
          // Auth error 401
        }
      } else {
        File file = new File(absPath);
        if(file.exists()) {
          if(resource.isScriptAliased) {
            // run script
          }
        } else {
          // Internal Error
        }
      }
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
}
