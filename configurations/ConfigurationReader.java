package configurations;


import java.io.BufferedReader;
import java.io.InputStreamReader;

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
  
  InputStreamReader fileContents;

  BufferedReader loadFile(String path) {
    try {
      fileContents = new InputStreamReader(getClass().getResourceAsStream(path));
    } catch(Exception e) {
      System.out.println("Configuration file at "+path+" could not be loaded.");
    }
    return new BufferedReader(fileContents);
  } 
  
  abstract void parse(BufferedReader fileContentBuffer);
  
}
