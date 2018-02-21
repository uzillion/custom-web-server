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

/**
 *  update the contents of a file
 *  
 */
public class PostRequest extends Request {
    String body;
  
    public PostRequest(HashMap<String, String> request_line, HashMap<String, String> headers, String body, String absPath) {
        this.body = body;
        List<String> records = new ArrayList<String>();
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(absPath));
            writer.write(body);
            writer.close();
        
        } catch (Exception e){
            System.out.println("Exception occurred trying to read " + absPath);
        }
        
        
    }

  @Override
  public int getContentLength() {
    return body.getBytes().length;
  }
    
}
