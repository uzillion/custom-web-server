package response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  
 */
public class ResponseFactory {
    final String PROTOCOL = "HTTP/1.1";
    final String SERVER_NAME = "Server of HardWork";

  ArrayList create(ArrayList<String> response_content, int content_length, String type, ResponseStatus status) {
    String status_line = PROTOCOL + " " + status.getStatusCode() + " " + status.getStatusPhrase();
    String headers = generateHeaders(content_length, type);
    response_content.add(0, headers);
    response_content.add(0, status_line);
    return response_content;
  }
  
  
  private String generateHeaders(int content_length, String type) {

    String headers = "";
    headers += "Content-Type: "+type+"\r\n";
    headers += "Content-Length: "+content_length+"\r\n";
    headers += "Date: "+getHttpTime()+"\r\n";
    headers += "Server: "+SERVER_NAME+"\r\n";
    return headers;
  }
    
  String getHttpTime() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(calendar.getTime());
  }
   
}
