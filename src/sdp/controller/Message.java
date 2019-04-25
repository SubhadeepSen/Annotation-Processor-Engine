package sdp.controller;

import sdp.annotation.DefineComponent;

@DefineComponent
public class Message {

	public String getMessage() {
		return createMessage();
	}

	private String createMessage() {
		return "A message from Message class annotated with @Component";
	}
}
