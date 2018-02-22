package core;

import requests.*;
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
import response.Response;


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
  Response response;
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
      resource = new Resource(httpd_configs.getList(), request_line);
      absPath = resource.resolveAddresses();
      if(new File(absPath).exists() || request_line.get("verb").equals("PUT")) {
        String accessFilePath = getAccessFilePath(absPath);
        File authFile = new File(accessFilePath);
        if(authFile.exists()) {
          boolean authorized;
          authorized = checkAuthorization(authFile);
          if(authorized) {
            if(resource.isScriptAliased)
              runscript();
            else
              handleRequest(request_line.get("verb"));
          }
        } else {
          if(resource.isScriptAliased)
              runscript();
          else
              handleRequest(request_line.get("verb"));
        }
      } else {
        status.statusCode404();
      }
      
    } catch(IOException e) {
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
        System.err.println("Failed to output log to file");
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
    if(headers.containsKey("Content-Length"))
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
    int content_length = Integer.parseInt(headers.get("Content-Length"));
    while((c=requestBuffer.read()) != -1) {
      body += (char)c;
      read++;
      if(read == content_length)
        break;
    } 
  }
  
  private void runscript() {
    try {
      ProcessBuilder builder;
      builder = new ProcessBuilder(absPath);
      
      Map<String, String> env = builder.environment();
      for(Map.Entry<String, String> entry : headers.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        env.put(key, value);
      }
      redirectOutput(builder);
      Process process = builder.start();
      
      String verb = request_line.get("verb");
      if(verb.equals("PUT") || verb.equals("POST")) {
        writeToProcessSTDIN(process);
        
      }

      process.waitFor();
    }
    catch(Exception e) {
      System.err.println("Problem running script.");
      status.statusCode500();
    }
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

  private void handleRequest(String verb) throws IOException {
    switch(verb) {
      case "GET":
        request = new GetRequest(absPath, headers, getType(absPath), status, client_socket.getOutputStream());
        break;
      case "DELETE":
        request = new DeleteRequest(absPath, headers, status, client_socket.getOutputStream());
        break;
      case "POST":
        request = new PostRequest(absPath, headers, body, status, client_socket.getOutputStream());
        break;
      case "PUT":
        request = new PutRequest(absPath, headers, status, client_socket.getOutputStream());
        break;
      case "HEAD":
        request = new HeadRequest(absPath, headers, getType(absPath), status, client_socket.getOutputStream());
        break;
      default:
        status.statusCode400();
        return;
    }
    
    response = request.createResponse();
    response.respond();
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

  private void redirectOutput(ProcessBuilder processBuilder) throws IOException, InterruptedException {
    
      String scriptParent = new File(absPath).getParent();
      File outputFile = new File(scriptParent+"/scriptOutput.txt");
      outputFile.createNewFile();
      processBuilder.redirectOutput(outputFile);
      absPath = outputFile.getAbsolutePath();
      handleRequest(body);
      
  }

  private void writeToProcessSTDIN(Process process) throws IOException, IOException {
    OutputStream stdin = process.getOutputStream();
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
    writer.write(body);
  }
}
