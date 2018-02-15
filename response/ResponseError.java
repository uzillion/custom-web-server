package response;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  
 */
public class ResponseError {
  
  public void internalError() {
    
  }

  public void badRequest() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  public void SC400() {
    System.out.println("400, bad request");  
    throw new UnsupportedOperationException("400, bad request."); // error 400, bad request, the request has to contain 3 key-value pairs
  } 
  
  public void SC404() {
    System.out.println("404, file not exist");  
    throw new UnsupportedOperationException("404, file not exist"); // error 400, bad request, the request has to contain 3 key-value pairs
  } 
  
  public void SC200() {
    System.out.println("200, OK");  
  } 
  
  public void SC500() {
    System.out.println("500, Internal Server Error");  
    throw new UnsupportedOperationException("500, Internal Server Error"); // error 400, bad request, the request has to contain 3 key-value pairs
  } 
  
}
