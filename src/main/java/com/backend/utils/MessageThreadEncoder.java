package com.backend.utils;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.backend.model.Message;
import com.backend.model.MessageThread;

public class MessageThreadEncoder implements Encoder.Text<MessageThread> {

	@Override
	public void init(EndpointConfig config) {
		// No initiations	
	}

	@Override
	public void destroy() {
		// No Closures
	}

	@Override
	public String encode(MessageThread thread) throws EncodeException {	
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
		return encodedMessages.toString();
	}

}
