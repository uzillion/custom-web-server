/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.awt.image.BufferedImage;
import response.Response;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import response.ResponseStatus;


public class GetRequest extends Request {
  
  Response response;
  ArrayList<String> response_content;
  int content_length;
  String type;
  ResponseStatus status;
  Socket client;
  
  public GetRequest(String absPath, HashMap<String, String> headers, String type, ResponseStatus status, Socket socket) throws IOException, InterruptedException{
    System.out.println(absPath);
    response_content = new ArrayList<>();
    client = socket;
    this.type = type;
    this.status = status;
    if(headers.containsKey("Last-Modified"))
      checkModifiedStatus(absPath, headers.get("Last-Modified"));
    if(type.contains("image")) {
      loadImage(absPath);
    } else {
      content_length = loadResponseContent(absPath);
//      System.out.println(content_length);
      response = new Response(response_content, content_length, type, status, client);
    }
    response.respond(type);
  }

  private int loadResponseContent(String path) throws FileNotFoundException, IOException {
    int content_length = 0;
    String body = "";
    int c;
    FileReader reader = new FileReader(path);
    while ((c = reader.read()) != -1) {
      content_length++;
      body += (char)c;
    }
    response_content.add(body);

    reader.close();
    return content_length;
  }
  
  private int getSize(String string) throws UnsupportedEncodingException {
    byte[] bytes = string.getBytes("UTF-8");
    return bytes.length;
  }
  
  @Override
  public int getContentLength() {
    return response.getContentLength();
  }
  
  void loadImage(String path) throws IOException, InterruptedException {

    BufferedImage image = ImageIO.read(new File(path));

    ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
    ImageIO.write(image, "jpg", byteArrayStream);
    
    response = new Response(byteArrayStream, type, status, client);

  }
  
  String getDate(long milliSeconds) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliSeconds);
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(calendar.getTime());
  }

  private void checkModifiedStatus(String absPath, String lastModifiedDate) {
    String modifiedDate = getDate(new File(absPath).lastModified());
    if(modifiedDate.equals(lastModifiedDate))
      status.statusCode304();
  }
}
