
import configurations.HttpdConf;
import configurations.MimeTypes;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Uzair
 */
public class WebServer {
    
    static final int DEFAULT_PORT = 3000;
    static final ArrayList<String> REQUEST_VERBS = new ArrayList(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE"));
    static Socket client_socket;
    static ServerSocket server_socket;
  
    public static void main(String args[]) throws IOException {
      
      HttpdConf httpd_configs = new HttpdConf("/conf/httpd.conf");
      MimeTypes mimeTypes = new MimeTypes("/conf/mime.types");
      
      client_socket = null;
      server_socket = new ServerSocket(DEFAULT_PORT);

      startServer();
    }
    
    private static void startServer() throws IOException {
      while(true) {
        if(client_socket == null)
          System.out.println("Server is listening...");
        client_socket = server_socket.accept();
        Thread responseThread = new Thread() {
          @Override
          public void run() {
            System.out.println(this.getName());
            try {
              parseRequest(client_socket);
            } catch (IOException ex) {
              Response.internalError();
            }
          }
        };
        
        responseThread.start();
      }
    }
    
    public static void parseRequest(Socket client_socket) throws IOException {
      InputStreamReader request_stream = new InputStreamReader(client_socket.getInputStream());
      BufferedReader request = new BufferedReader(request_stream);
      
      HashMap<String, String> headers = new HashMap<>();
      String[] request_line = {};
      while(true) {
        if(request.ready()) {
          request_line = request.readLine().split(" ");
          break;
        }
      }
      String URI = "";
      String version = "";
      String header = "";
      String body = "";
      int request_line_length = request_line.length;
      
      if(request_line_length != 0 && request_line_length <= 3) {
        String request_verb = request_line[0];
        if(request_line_length == 3) {
          URI = request_line[1];
          version = request_line[2];
        } else {
          URI = request_line[1];
        }
      }
      else
        Response.badRequest();
      
      header = request.readLine();
      while(header != null && header.length() != 0) {
        String[] keyValuePair = header.split(": ");
        headers.put(keyValuePair[0], keyValuePair[1]);
        header = request.readLine();
      }
//      
//      int c;
//      int content_length = Integer.parseInt(headers.get("content-length"));
//      System.out.println(request_stream.ready());
//      while((c=request_stream.read()) != -1 ) {
//        System.out.print(c);
//      }
      
      
      System.out.println("\""+request_line[0]+"\"\n\""+request_line[1]+"\"\n\""+request_line[2]+"\"");
      System.out.println(headers.toString());
//      System.out.println(body);
    }
}
