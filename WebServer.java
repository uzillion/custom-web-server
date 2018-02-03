
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
    
    public static void main(String args[]) throws MalformedURLException {
      HttpdConf httpd_conf = new HttpdConf("/conf/httpd.conf");
    }
}
