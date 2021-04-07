package com.backend.utils;

import java.io.IOException;
import java.util.Date;

import javax.json.Json;
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
		
		StringBuilder encodedMessages = new StringBuilder("[");
		for(Message m: thread.getMessages() )
		{
			encodedMessages.append(Json.createObjectBuilder()
					.add("Sender",m.getSender())
					.add("MessageBody",m.getText())
					.build()
					.toString()+", ");
		}
				
		encodedMessages.append(Json.createObjectBuilder()
				.add("Sender",thread.getParticipant())
				.add("replied",thread.isReplied())
				.build()
				.toString());
		encodedMessages.append("]");
		System.out.println(encodedMessages);
		return encodedMessages.toString();
	}

}
