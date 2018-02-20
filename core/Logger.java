package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;


public class Logger {
  
  String logPath;
  String rootPath;
  
  public Logger(String path) {
    logPath = path;
  }
  
//  public Logger(String path, boolean create) {
//    if(create) 
//      setLogFile(path);
//    else
//      this(path);
//  }
  
//  private void setLogFile(String path) {
//    try {
//      BufferedWriter writer = new BufferedWriter(new FileWriter("../conf/httpd.conf", true));
//      writer.write("LogFile \""+)
//    } catch (IOException ex) {
//      System.err.println("Error accessing httpd.conf");
//    }
//    
//  }
  
  public void writeLog(String log) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(logPath, true));
    writer.write(log);
    writer.write("\r\n");
    writer.close();
  }
}
