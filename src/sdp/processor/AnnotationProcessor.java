package sdp.processor;

/**
 * AnnotationProcessor is an entry point to start the processing. It needs an
 * object of AnnotationProcessorConfig class where package name is compulsory to
 * start the processing.
 * 
 * @author Subhadeep Sen
 *
 */
public class AnnotationProcessor {

	private AnnotationProcessorConfig annotationProcessorConfig;

	public AnnotationProcessor(AnnotationProcessorConfig annotationProcessorConfig) {
		this.annotationProcessorConfig = annotationProcessorConfig;
	}

	/**
	 * starts the processing
	 * 
	 * @return Object
	 */
	public Object process() {
		String packageName = annotationProcessorConfig.getPackageName();
		if (null == packageName || packageName.isEmpty()) {
			return null;
		}
		return new AnnotationProcessorEngine(annotationProcessorConfig).process();
	}
}
