/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import response.Response;

/**
 *  update the contents of a file
 *  
 */
public class PostRequest extends Request {
    public PostRequest(HashMap<String, String> request_line, HashMap<String, String> headers, String body, String absPath){
        List<String> records = new ArrayList<String>();
        String SC;
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(absPath));
            writer.write(body);
            writer.close();
            Response response = new Response();
            SC = "200";
            response.call(request_line, headers, body, absPath, records, SC);
        
        } catch (Exception e){
            System.out.println("Exception occurred trying to read " + absPath);
            SC = "500";
            Response response = new Response();
            response.call(request_line, headers, body, absPath, records, SC);
        }
        
        
    }
    
}
