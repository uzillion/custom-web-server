/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import response.*;


/**
 *
 *  
 */
public class GetRequest extends Request {
  public GetRequest(HashMap<String, String> request_line, HashMap<String, String> headers, String body, String absPath){
    List<String> records = new ArrayList<String>();
    try{
        BufferedReader reader = new BufferedReader(new FileReader(absPath));
        String line;
        while ((line = reader.readLine()) != null)
        {
          records.add(line);
        }
        reader.close();
        
        // send records to response
        Response response = new Response();
        String SC = "200";
        response.call(request_line, headers, body, absPath, records, SC);
        
    } catch (Exception e){
        System.out.println("Exception occurred trying to read " + absPath);
        String SC = "500";
        Response response = new Response();
        response.call(request_line, headers, body, absPath, records, SC);
    }
      
  }
}
