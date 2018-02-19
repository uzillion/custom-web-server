package response;


public class ResponseStatus {
  int status_code;
  String status_phrase;

  public ResponseStatus() {
    status_code = 200;
    status_phrase = "OK";
  }
  
  /**
   * OK
   */
  public void statusCode200() {
    status_code = 200;
    status_phrase = "OK";
  }
  
  /**
   * Created
   */
  public void statusCode201() {
    status_code = 201;
    status_phrase = "Created";
  }
  
  /**
   * No Content
   */
  public void statusCode204() {
    status_code = 204;
    status_phrase = "No Contnet";
  }
  
  /**
   * Not Modified
   */
  public void statusCode304() {
    status_code = 304;
    status_phrase = "Not Modified";
  }
  
  /**
   * Bad Request
   */
  public void statusCode400() {
    status_code = 400;
    status_phrase = "Bad Request";
  }
  
  /**
   * Unauthorized
   */
  public void statusCode401() {
    status_code = 401;
    status_phrase = "Unauthorized";
  }
  
  /**
   * Forbidden
   */
  public void statusCode403() {
    status_code = 403;
    status_phrase = "Forbidden";
  }
  
  /**
   * Not Found
   */
  public void statusCode404() {
    status_code = 404;
    status_phrase = "Not Found";
  } 
  
  /**
   * Internal Server Error
   */
  public void statusCode500() {
    status_code = 500;
    status_phrase = "Internal Server Error";
  }
  
  public int getStatusCode() {
    return status_code;
  }
  
  public String getStatusPhrase() {
    return status_phrase;
  }
  
}
