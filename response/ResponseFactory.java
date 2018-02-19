package response;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import Server.Worker;
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
  
//      public void reply(HashMap<String, String> request_line, HashMap<String, String> headers, String body, String absPath, List<String> records, String SC) throws IOException{
//        // response format: HTTP_VERSION STATUS_CODE REASON_PHRASE  HTTP_HEADERS body
//                PrintWriter out = new PrintWriter( Worker.client_socket.getOutputStream(), true );
//
//        SC = generate_SC(SC);
//        System.out.println(request_line.get("version"));
//        System.out.println(SC);
//        System.out.println(reasonphrase);
//        System.out.println(headers.toString());   // header is empty?
//        if(!records.isEmpty()){
//            for (String s: records){
//                out.println(s);
//            }
//        }
//    }
  
      
//    public String generate_SC(String sc){
//        if(sc.equals("200")){
//            reasonphrase = "complete successfully";
//            return "200 OK";
//        } else if(sc.equals("500")){
//            reasonphrase = "The server either does not recognize the request method, or it lacks the ability to fulfill the request";
//            return "500 Internal Server Error";
//        } else if(sc.equals("204")){
//            reasonphrase = "successfully delete the target file";
//            return "204 No Content";
//        } else if(sc.equals("201")){
//            reasonphrase = "successfully create the target file";
//            return "201 Created";
//        } else
//            return "unknown status code";
//    }
    
}
