package configurations;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 *  
 */
public abstract class ConfigurationReader {
  
//  InputStreamReader fileContents;
  FileReader fileContents;

  BufferedReader loadFile(String path) {
//      URL url = getClass().getResource(path);
//            System.out.println(url.getPath());

    try {
//            fileContents = new InputStreamReader(getClass().getResourceAsStream(path));
      fileContents = new FileReader(path);
    } catch (Exception e) {
//      System.out.println("Could not load: "+ url.getPath());
    }
    return new BufferedReader(fileContents);
  } 
  
  abstract void parse(BufferedReader fileContentBuffer);
  
}
