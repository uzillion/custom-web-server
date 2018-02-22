package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;


public class Logger {
  
  String logPath;
  String rootPath;
  
  public Logger(String path) throws IOException {
    logPath = path;
    logPath = logPath.replace("\"", "");
    File logFile = new File(logPath);
    if(!logFile.exists())
      logFile.createNewFile();
  }
  
  
  public void writeLog(String log) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(logPath, true));
    writer.write(log);
    writer.write("\r\n");
    writer.close();
  }
}
