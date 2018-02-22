package requests;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
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
  
  public abstract Response createResponse(boolean isScriptAliased);
  
  public abstract int getContentLength();
  
  protected String getDate(long milliSeconds) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliSeconds);
    SimpleDateFormat dateFormat = new SimpleDateFormat(
        "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    return dateFormat.format(calendar.getTime());
  }
          
}
