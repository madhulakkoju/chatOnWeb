package com.backend.utils;

import java.io.StringReader;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.backend.model.Message;

public class MessageDecoder implements Decoder.Text<Message>
{

	@Override
	public void init(EndpointConfig config) {
		//No required initiations needed
	}

	@Override
	public void destroy() {
		//No objects to close 
	}

	@Override
	public Message decode(String textMessage) throws DecodeException {
		JsonReader reader = Json.createReader(new StringReader(textMessage));
		JsonObject obj = reader.readObject();
		reader.close();
		return new Message(obj.getString("Sender"),obj.getString("MessageBody"),new Date());
		
	}

	@Override
	public boolean willDecode(String s) {
		return true;
	}

}
