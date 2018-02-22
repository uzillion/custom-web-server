






import core.Worker;
import configurations.HttpdConf;
import configurations.MimeTypes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class WebServer {
  
  static int port;
  public static Socket client_socket;
  static ServerSocket server_socket;
  static HttpdConf httpd_configs;
  static MimeTypes mimeTypes;
  static boolean start = false;
  
  public static void main(String args[]) throws IOException {
      
    httpd_configs = new HttpdConf("/conf/httpd.conf");
    mimeTypes = new MimeTypes("/conf/mime.types");
    port =   Integer.parseInt((String) httpd_configs.getList().get("Listen").get(0));
    client_socket = null;
    server_socket = new ServerSocket(port);

    start();
  }
    
  private static void start() throws IOException {
    while(true) {
      if(!start) {
        System.out.println("Server started...");
        start = true;
      }
      client_socket = server_socket.accept();
      Thread responseThread = new Worker(client_socket, httpd_configs, mimeTypes);
      responseThread.start();
      client_socket = null;
    }
  }
}
