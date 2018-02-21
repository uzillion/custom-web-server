package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;


public class Logger {
  
  String logPath;
  String rootPath;
  
  public Logger(String path) {
    logPath = path;
    File logFile = new File(path);
    if(!logFile.exists())
      createLogFile(path);
  }
  
  
  public void writeLog(String log) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(logPath, true));
    writer.write(log);
    writer.write("\r\n");
    writer.close();
  }

  private void createLogFile(String path) {
    
  }
}
