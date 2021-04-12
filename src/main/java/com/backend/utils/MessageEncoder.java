package com.backend.utils;

import java.util.Date;

import javax.json.Json;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.backend.model.Message;

public class MessageEncoder implements Encoder.Text<Message> {
	
	@Override
	public void init(EndpointConfig config) {
		// No initiations needed
	}

	@Override
	public void destroy() {
		// No object closures to take care of
	}

	@Override
	public String encode(Message message) {
		return Json.createObjectBuilder()
				.add("Sender",message.getSender())
				.add("MessageBody",message.getText())
				.add("Time",new Date().toString())
				.build()
				.toString();
	
	}

}
