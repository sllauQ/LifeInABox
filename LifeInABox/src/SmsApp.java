import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

//import spark.Spark.*;

import com.sun.net.httpserver.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.InputStream;
import java.util.*;


public class SmsApp {
	public static final String ACCOUNT_SID = "AC4b014f6935e1abb4f51fe47dc576119c";
	public static final String AUTH_TOKEN = "edab778b6cc76c28a99f0862e333b360";
	
	@SuppressWarnings("restriction")
	public static void main(String[] args) throws Exception {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	    Message message = Message.creator(new PhoneNumber("+15015291761"),
	        new PhoneNumber("+15017256278"), 
	        "Twilio Server is now Online").create();
	    System.out.println(message.getSid());
		
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.createContext("/sms", new SmsHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
       
        System.out.println("app listening on 8000");
    }
	@SuppressWarnings("restriction")
	static class MyHandler implements HttpHandler {
	    @Override
	    public void handle(HttpExchange t) throws IOException {
	        String response = "This is the response";
	        t.sendResponseHeaders(200, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
	    }
	}
	@SuppressWarnings("restriction")
	static class SmsHandler implements HttpHandler {
		@Override
		public void handle (HttpExchange t) throws IOException {
			System.out.println("It worked!");
			InputStream iStream = t.getRequestBody();
			Scanner input = new Scanner(iStream);
			String chunk = input.nextLine();
			input.close();
			System.out.println(chunk);
			String message = getMessage(chunk);
			String sender = getSender(chunk);
			System.out.println(sender);
			System.out.println(message);
			Message response = Message.creator(new PhoneNumber(sender),
			        new PhoneNumber("+15017256278"), 
			        message).create();
			    System.out.println(response.getSid());
		}
	}
	static String getMessage(String chunk)
	{
		String tag = "Body=";
		String message = "";
		int size = tag.length();
		int count = 0;
		int lb = 0;
		while(count != -1 && count < chunk.length())
		{
			//System.out.println(chunk.substring(count, size + count));
			//System.out.println(tag);
			if(chunk.substring(count, size + count).equals(tag))
			{
				System.out.println("Success!");
				message = chunk.substring(count + size);
				lb = count + size;
				count = -1;
			}else
				count++;
		}
		count++;
		tag = "&FromCountry";
		size = tag.length();
		System.out.println("In between!");
		while(count != -1 && count < chunk.length())
		{
			//System.out.println(chunk.substring(count, size + count));
			//System.out.println(tag);
			if(chunk.substring(count, size + count).equals(tag))
			{
				System.out.println("Success!");
				//System.out.println(chunk.substring(lb, count));
				return chunk.substring(lb, count).replace("+", " ");
				
				
				//count = -1;
			}else
				count++;
		}
		System.out.println("Exiting");
		return message;
	}
	static String getSender(String chunk)
	{
		String tag = "From=%2B";
		String message = "";
		int size = tag.length();
		int count = 0;
		while(count != -1 && count < chunk.length())
		{
			//System.out.println(chunk.substring(count, size + count));
			//System.out.println(tag);
			if(chunk.substring(count, size + count).equals(tag))
			{
				System.out.println("Success!");
				message = chunk.substring(count + size, count + size + 11);
				return message;
			}else
				count++;
		}
		System.out.println("Exiting");
		return message;
	}
}

