package com.backend.utils;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.backend.model.Message;
import com.backend.model.MessageThread;

public class MessageThreadEncoder implements Encoder.Text<MessageThread> {

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(MessageThread thread) throws EncodeException {
		// TODO Auto-generated method stub
		thread.addMessage(new Message("fsdf","dfsadfgddf"));
		thread.addMessage(new Message("fsdfgsdfg","dfdfgdfgdsadf"));
		thread.addMessage(new Message("fdfgsdf","dfsadfgdfgf"));
		thread.addMessage(new Message("fsdfdgf","dfdfgdfgdfgdfgf"));
		ObjectMapper mapper = new ObjectMapper();
		
		String encoded="[";
		
		for(Message m: thread.getMessages() )
		{
			try {
				
				System.out.println(mapper.writeValueAsString(m));
				encoded += mapper.writeValueAsString(m);
				
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		encoded += "]";
		System.out.println(encoded);
		return encoded;
	}

}
