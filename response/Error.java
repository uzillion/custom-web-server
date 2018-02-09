package response;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Uzair and Jizhou
 */
public class Error {
  
  public static void internalError() {
    
  }

  public static void badRequest() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  public static void SC400() {
    System.out.println("400, bad request");  
    throw new UnsupportedOperationException("400, bad request."); // error 400, bad request, the request has to contain 3 key-value pairs
  }
  
}
