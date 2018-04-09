import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import spark.*;

public class Example {
  // Find your Account Sid and Token at twilio.com/user/account
  public static final String ACCOUNT_SID = "AC4b014f6935e1abb4f51fe47dc576119c";
  public static final String AUTH_TOKEN = "edab778b6cc76c28a99f0862e333b360";

  public static void main(String[] args) {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message message = Message.creator(new PhoneNumber("+15015291761"),
        new PhoneNumber("+15017256278"), 
        "This is the ship that made the Kessel Run in fourteen parsecs?").create();
    
    System.out.println(message.getSid());
  }
}