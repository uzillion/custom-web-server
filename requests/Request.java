package requests;

import java.io.IOException;
import java.util.HashMap;
import response.Response;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  
 */
public abstract class Request {
  
  public abstract Response createResponse();
  
  public abstract int getContentLength();
          
}
