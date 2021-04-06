package com.backend.utils;

import java.io.StringReader;
import java.time.LocalTime;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.codehaus.jackson.map.ObjectMapper;

import com.backend.model.Message;

public class MessageDecoder implements Decoder.Text<Message>
{

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message decode(String textMessage) throws DecodeException {
		// TODO Auto-generated method stub
		System.out.println(textMessage);
		JsonObject obj =Json.createReader(new StringReader(textMessage)).readObject();
		
		Message message = new Message(obj.getString("Sender"),obj.getString("MessageBody"),new Date());
		
		return message;
	}

	@Override
	public boolean willDecode(String s) {
		// TODO Auto-generated method stub
		return true;
	}

}
