package sdp.controller;

import sdp.annotation.AutoInject;
import sdp.annotation.DefineController;
import sdp.annotation.HandlerMethod;

@DefineController
public class ProcessController {

	@AutoInject
	private String processName;

	@AutoInject
	private MessageController messageController;

	@HandlerMethod(value = "/process", method = "GET")
	public String getProcessName() {
		return "processing controller";
	}

	@HandlerMethod(value = "/messageObject", method = "POST")
	public MessageController getController(String value) {
		if (value.equals("return"))
			return messageController;
		return null;
	}

	@HandlerMethod(value = "/messageController", method = "GET")
	public String getMessageFromMessageController() {
		return messageController.getMessage();
	}

	@HandlerMethod(value = "/message", method = "GET")
	public String getMessage(Message message) {
		return message.getMessage() + " from ProcessController";
	}
}
