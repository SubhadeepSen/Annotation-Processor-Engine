package sdp.main;

import sdp.processor.AnnotationProcessor;
import sdp.processor.AnnotationProcessorConfig;

public class AnnotationProcessorMain {
	public static void main(String[] args) {
		String handlerName = "/process"; // /messageObject, /messageController, /message
		String httpMethod = "GET"; // POST

		AnnotationProcessorConfig annotationProcessorConfig = new AnnotationProcessorConfig();
		annotationProcessorConfig.setPackageName("sdp.controller");
		annotationProcessorConfig.setHandlerName(handlerName);
		annotationProcessorConfig.setHttpMethod(httpMethod);
		
		AnnotationProcessor annotationProcessor = new AnnotationProcessor(annotationProcessorConfig);
		//annotationProcessor.process();
		System.out.println(annotationProcessor.process());
	}
}
