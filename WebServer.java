
import configurations.HttpdConf;
import configurations.MimeTypes;
import java.net.MalformedURLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Uzair
 */
public class WebServer {
    
    public static void main(String args[]) {
      
      HttpdConf httpd_configs = new HttpdConf("/conf/httpd.conf");
      MimeTypes mimeTypes = new MimeTypes("/conf/mime.types");
    }
}
