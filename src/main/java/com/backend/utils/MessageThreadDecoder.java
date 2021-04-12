package com.backend.utils;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.backend.model.MessageThread;

public class MessageThreadDecoder implements Decoder.Text<MessageThread> {

	@Override
	public void init(EndpointConfig config) {
		// No initiations
	}

	@Override
	public void destroy() {
		// No closures
	}

	@Override
	public MessageThread decode(String s) throws DecodeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean willDecode(String s) {
		return true;
	}

}
