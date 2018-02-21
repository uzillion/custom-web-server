package core;

import requests.PostRequest;
import requests.PutRequest;
import requests.GetRequest;
import requests.DeleteRequest;
import requests.Request;
import response.ResponseFactory;
import response.ResponseStatus;
import configurations.Htaccess;
import configurations.Htpassword;
import configurations.HttpdConf;
import configurations.MimeTypes;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;


public class Worker extends Thread {
 
  private final HashMap<String, String> request_line;
  private final HashMap<String, String> headers;
  private final InputStream client_stream;
  private final HttpdConf httpd_configs;
  private final MimeTypes mimeTypes;
  public Socket client_socket;
  String absPath;
  Htaccess htaccess;
  Htpassword password;
  Request request;
  ResponseStatus status;
  ResponseFactory rf;
  Resource resource;
  Logger logger;
  String body;
  String date;
  
  private final boolean DUMP = false; 

  
  public Worker(Socket client_socket, HttpdConf httpd_configs, MimeTypes mimeTypes) throws IOException {
    this.client_socket = client_socket;
    request_line = new HashMap<>();
    headers = new HashMap<>();
    body = "";
    status = new ResponseStatus();
    logger = new Logger((String) httpd_configs.getList().get("LogFile").get(0));
    this.client_stream = client_socket.getInputStream();
    this.httpd_configs = httpd_configs;
    this.mimeTypes = mimeTypes;
    
  }
  
  @Override
  public void run() {
    try {
      parse(client_stream);
      resource = new Resource(httpd_configs.getList(), request_line.get("URI"));
      absPath = resource.resolveAddresses();
      String accessFilePath = getAccessFilePath(absPath);
      File authFile = new File(accessFilePath);
      if(authFile.exists()) {
//        System.out.println("Access file exists");
        boolean authorized;
        authorized = checkAuthorization(authFile);
        if(authorized) {
          handleRequest(request_line.get("verb"), absPath);
        }
        
      } else {
        handleRequest(request_line.get("verb"), absPath);
      }      
      
    } catch (Exception e) {
      e.printStackTrace();
      status.statusCode500();
    } finally {
      try {
        client_stream.close();
        client_socket.close();
      } catch (IOException ex) {
      }
      System.out.println(logBuilder());
      try {
        logger.writeLog(logBuilder());
      } catch (IOException ex) {
        java.util.logging.Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
      }
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
    
//    requestBuffer.close();
    
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
      status.statusCode400();

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
    if(!check & !request_line.get("verb").equals("PUT")){
        status.statusCode404();
    }
  }
  
  private void runscript() throws IOException, InterruptedException{
      ProcessBuilder processBuilder;
      processBuilder = new ProcessBuilder(absPath);
//      exportEnvVariables(builder);
      String scriptParent = new File(absPath).getParent();
      System.out.println(scriptParent);
      File outputFile = new File(scriptParent+"/scriptOutput.txt");
      outputFile.createNewFile();
      processBuilder.redirectOutput(outputFile);
      Process process = processBuilder.start();
      
//      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//      StringBuilder builder = new StringBuilder();
//      String line = null;
//      while ( (line = reader.readLine()) != null) {
//        builder.append(line);
//        builder.append(System.getProperty("line.separator"));
//      }
//      String result = builder.toString();
      process.waitFor();
//      if(body.length() > 0)
//        pipeBodyToScript(process);
  }

  private String getAccessFilePath(String absPath) {
    
    String parentPath = new File(absPath).getParent();
    parentPath = parentPath.replace("\"", "") + "/";
    
    String accessFileName = (String) httpd_configs.getList().get("AccessFileName").get(0);
    accessFileName  = accessFileName.replace("\"", "");
    
    return parentPath + accessFileName;
  }

  private boolean checkAuthorization(File authFile) throws IOException {
    boolean authorized = false;
    htaccess = new Htaccess(authFile.getPath());
    if(headers.containsKey("Authorization")) {
      password = new Htpassword(htaccess.getPasswordFileNmae());
      authorized = password.isAuthorized(headers.get("Authorization"));
      if(!authorized) {
        status.statusCode403();
      }            
    } else {
      status.statusCode401();
    }
    return authorized;
  }

  private void handleRequest(String verb, String absPath) throws IOException, InterruptedException {
    File file = new File(absPath);
    if(file.exists()) {
      if(resource.isScriptAliased) {
        runscript();
      } else {
        switch(verb) {
          case "GET":
            request = new GetRequest(absPath, headers, getType(absPath), status, client_socket);
            break;
          case "DELETE":
            request = new DeleteRequest(request_line, headers, body, absPath);
            break;
          case "POST":
            request = new PostRequest(request_line, headers, body, absPath);
            break;
          case "PUT":
            request = new PutRequest(request_line, headers, body, absPath);
            break;
          default:
            status.statusCode400();
            
        }
      }
    } else {
      status.statusCode404();
    }
  }
  
  String getType(String path) {
    String[] tokens = path.split("\\.");
    String extension = tokens[tokens.length-1];
    return mimeTypes.getType(extension);
  }

  private void exportEnvVariables(ProcessBuilder process) {
    for(Map.Entry<String, String> entry : headers.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
    }
  }

  private void pipeBodyToScript(Process process) throws IOException {
    OutputStream process_stdin = process.getOutputStream();
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process_stdin));
    writer.write(body);
    writer.newLine();
    writer.flush();
    writer.close();
  }

  private String logBuilder() {
    String log_entry = "";
    log_entry += client_socket.getRemoteSocketAddress()+" ";
    if(password != null)
      log_entry += password.getUser()+" ";
    else
      log_entry += "- ";
    log_entry += "["+getLogTime()+"] ";
    log_entry += "\""+request_line.get("verb")+" ";
    log_entry += request_line.get("URI")+" ";
    if(request_line.get("version") != null)
      log_entry += request_line.get("version")+"\" ";
    else
      log_entry += "\" ";
    log_entry += status.getStatusCode()+" ";
    if(request != null)
      log_entry += request.getContentLength();
    else
      log_entry += "0 ";
    return log_entry;
  }
  
  private String getLogTime() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(calendar.getTime());
  }
}
