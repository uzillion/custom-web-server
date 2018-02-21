package configurations;


import java.io.BufferedReader;
import java.io.FileReader;

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
