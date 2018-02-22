package configurations;


import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Htpassword extends ConfigurationReader {
  
  private HashMap<String, String> passwords;
  Htaccess access;
  String requestingUser;

  public Htpassword( String filename ) throws IOException {
    filename = filename.replace("\"", "");
    this.passwords = new HashMap<String, String>();
    parse(loadFile(filename));
//    System.out.println("Passwords:" + passwords.toString() );

  }
  
  @Override
  void parse(BufferedReader fileContentBuffer) {
    try {
      String line;
      while((line = fileContentBuffer.readLine()) != null) {
        String[] tokens = line.split( ":" );
        
        if( tokens.length == 2 ) {
          passwords.put( tokens[ 0 ], tokens[ 1 ].replace( "{SHA}", "" ).trim() );
        }
      }
    } catch (IOException ex) {
      Logger.getLogger(Htpassword.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public boolean isAuthorized( String authInfo ) {
    // authInfo is provided in the header received from the client
    // as a Base64 encoded string.
    String credentials = new String(
      Base64.getDecoder().decode( authInfo ),
      Charset.forName( "UTF-8" )
    );

    // The string is the key:value pair username:password
    String[] tokens = credentials.split( ":" );
    requestingUser = tokens[0];
    if(passwords.containsKey(tokens[0])) {
      if(verifyPassword(tokens[0], tokens[1])) {
        return true;
      } else {
        return false;
      } 
    }
    return false;
  }

  private boolean verifyPassword( String username, String password ) {
    String encryptedPassword = encryptClearPassword(password);
//    System.out.println(encryptedPassword);
    if(passwords.get(username).equals(encryptedPassword))
      return true;
    
    return false;
  }

  private String encryptClearPassword( String password ) {
    // Encrypt the cleartext password (that was decoded from the Base64 String
    // provided by the client) using the SHA-1 encryption algorithm
    try {
      MessageDigest mDigest = MessageDigest.getInstance( "SHA-1" );
      byte[] result = mDigest.digest( password.getBytes() );

     return Base64.getEncoder().encodeToString( result );
    } catch( Exception e ) {
      return "";
    }
  }
  
  public String getUser() {
    return requestingUser;
  }
}

