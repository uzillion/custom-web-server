package response;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
    ArrayList<String> response_content = new ArrayList<>();
    String status_line;
  
  ArrayList<String> create(ResponseStatus status, HashMap<String, String> response_headers, boolean isScriptAliased) {
    status_line = PROTOCOL + " " + status.getStatusCode() + " " + status.getStatusPhrase();
    String headers = generateHeaders(response_headers, isScriptAliased);
    response_content.add(0, headers);
    response_content.add(0, status_line);
    return response_content;
  }
  
  ArrayList<String> create(ResponseStatus status, HashMap<String, String> response_headers, String body, boolean isScriptAliased) {
    response_content.add(0, body);
    return create(status, response_headers, isScriptAliased);
  }
  
  private String generateHeaders(HashMap<String, String> response_headers, boolean isScriptAliased) {

    String headers = "";
    headers = loadForwardedHeaders(response_headers);
    headers += "Date: "+getHttpTime()+"\r\n";
    headers += "Server: "+SERVER_NAME;
    if(!isScriptAliased) {
      headers += "\r\n";
    }  
    return headers;
  }
    
  protected String getHttpTime() {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(calendar.getTime());
  }

  private String loadForwardedHeaders(HashMap<String, String> response_headers) {
    String headers = "";
    for(Map.Entry<String, String> header : response_headers.entrySet()) {
      String key = header.getKey();
      String value = header.getValue();
      headers += key + ": "+value+"\r\n";
    }
    return headers;
  }
   
}
