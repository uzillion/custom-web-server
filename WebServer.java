
import configurations.HttpdConf;
import configurations.MimeTypes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  
 */
public class WebServer {
  
  static final int DEFAULT_PORT = 3000;
  static Socket client_socket;
  static ServerSocket server_socket;
  static HttpdConf httpd_configs;
  static MimeTypes mimeTypes;
  
  public static void main(String args[]) throws IOException {
      
    httpd_configs = new HttpdConf("/conf/httpd.conf");
    mimeTypes = new MimeTypes("/conf/mime.types");
      
    client_socket = null;
    server_socket = new ServerSocket(DEFAULT_PORT);

    startServer();
  }
    
  private static void startServer() throws IOException {
    while(true) {
      if(client_socket == null)
        System.out.println("Server is listening...");
      client_socket = server_socket.accept();
      Thread responseThread = new Worker(client_socket.getInputStream(), httpd_configs, mimeTypes);
      
      responseThread.start();
    }
  }
}
