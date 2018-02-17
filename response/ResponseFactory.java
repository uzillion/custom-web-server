package response;

import java.util.HashMap;
import java.util.List;

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
    public String reasonphrase;
    
    public void reply(HashMap<String, String> request_line, HashMap<String, String> headers, String body, String absPath, List<String> records, String SC){
        // response format: HTTP_VERSION STATUS_CODE REASON_PHRASE  HTTP_HEADERS body
        SC = generate_SC(SC);
        System.out.println(request_line.get("version"));
        System.out.println(SC);
        System.out.println(reasonphrase);
        System.out.println(headers.toString());   // header is empty?
        if(!records.isEmpty()){
            for (String s: records){
                System.out.println(s);
            }
        }
        
        
        
        
        
    }
    
    public String generate_SC(String sc){
        if(sc.equals("200")){
            reasonphrase = "complete successfully";
            return "200 OK";
        } else if(sc.equals("500")){
            reasonphrase = "The server either does not recognize the request method, or it lacks the ability to fulfill the request";
            return "500 Internal Server Error";
        } else if(sc.equals("204")){
            reasonphrase = "successfully delete the target file";
            return "204 No Content";
        } else if(sc.equals("201")){
            reasonphrase = "successfully create the target file";
            return "201 Created";
        } else
            return "unknown status code";
    }
    
}
