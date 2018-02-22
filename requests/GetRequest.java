
package requests;

import java.awt.image.BufferedImage;
import response.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import response.ResponseStatus;


public class GetRequest extends Request {
  
  Response response;
  int content_length;
  String type;
  ResponseStatus status;
  OutputStream response_stream;
  HashMap<String, String> request_headers;
  HashMap<String, String> response_headers;
  String absPath;
  String body;
  ByteArrayOutputStream imageByteStream; 
  
  public GetRequest(String absPath, HashMap<String, String> headers, String type, ResponseStatus status, OutputStream responseStream) {
    this.response_stream = responseStream;
    request_headers = headers;
    this.type = type;
    this.status = status;
    this.absPath = absPath;
    response_headers = new HashMap<>();
  }
  
  @Override
  public Response createResponse() {
    
    if(request_headers.containsKey("Last-Modified"))
      checkModifiedStatus(absPath, request_headers.get("Last-Modified"));
        
    if(type.contains("image")) {
      try {
        content_length = loadImage(absPath);
      } catch (IOException ex) {
        System.err.println("Failed to load image.");
        status.statusCode500();
      }
      loadGeneralHeaders();
      return new Response(status, response_headers, imageByteStream, response_stream);
    } else {
      try {
        content_length = loadResponseContent(absPath);
      } catch (IOException ex) {
        System.err.println("Failed to load rosource contents.");
        status.statusCode500();
      }
      loadGeneralHeaders();
      return new Response(status, response_headers, body, response_stream);
    }
  }
  
  private void checkModifiedStatus(String absPath, String lastModifiedDate) {
    String modifiedDate = getDate(new File(absPath).lastModified());
    if(modifiedDate.equals(lastModifiedDate))
      status.statusCode304();
    else
      response_headers.put("Last-Modified", modifiedDate);
  }
  
  String getDate(long milliSeconds) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliSeconds);
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(calendar.getTime());
  }
  
  int loadImage(String path) throws IOException {

    BufferedImage image = ImageIO.read(new File(path));
    imageByteStream = new ByteArrayOutputStream();
   
    ImageIO.write(image, "jpg", imageByteStream);
         
    return imageByteStream.size();
  }


  private int loadResponseContent(String path) throws FileNotFoundException, IOException {
    int content_length = 0;
    String read_content = "";
    int c;
    FileReader reader = new FileReader(path);
    
      while ((c = reader.read()) != -1) {
        content_length++;
        read_content += (char)c;
      }
    body = read_content;

    reader.close();
    return content_length;
  }
  
  @Override
  public int getContentLength() {
    return content_length;
  }

  private void loadGeneralHeaders() {
     response_headers.put("Content-Type", type);
     response_headers.put("Content-Length", Integer.toString(getContentLength()));
  }
}