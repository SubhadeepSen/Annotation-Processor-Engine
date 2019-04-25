package sdp.controller;

import sdp.annotation.AutoInject;
import sdp.annotation.DefineController;

@DefineController
public class MessageController {

	@AutoInject
	private Message message;
	
	public String getMessage() {
		return message.getMessage();
	}

}
