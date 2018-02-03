
import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Uzair
 */
public abstract class ConfigurationReader {
  
  BufferedReader loadFile(String path) {
    return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));
  }
  
  abstract void parse(BufferedReader fileContentBuffer);
  
}
