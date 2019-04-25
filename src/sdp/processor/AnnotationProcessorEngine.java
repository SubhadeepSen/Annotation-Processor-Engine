package sdp.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sdp.annotation.AutoInject;
import sdp.annotation.DefineComponent;
import sdp.annotation.DefineController;
import sdp.annotation.HandlerMethod;

/**
 * The AnnotationProcessorEngine processes the annotations, creates the objects
 * of the annotated classes, injects the dependencies to the fields, and invoke
 * the handler method by mapping the handler name, http method and injecting the
 * required arguments.
 * 
 * @author Subhadeep Sen
 */
@SuppressWarnings("rawtypes")
public class AnnotationProcessorEngine {
	private AnnotationProcessorConfig annotationProcessorConfig;

	public AnnotationProcessorEngine(AnnotationProcessorConfig annotationProcessorConfig) {
		this.annotationProcessorConfig = annotationProcessorConfig;
	}

	/**
	 * starts the processing engine
	 * 
	 * @return Object
	 */
	public Object process() {
		Object returnObject = null;
		// retrieving the package name
		String packageName = annotationProcessorConfig.getPackageName();

		// creating the object of AnnotatedClassLoader
		AnnotatedClassLoader annotatedClassLoader = new AnnotatedClassLoader();
		try {
			/*
			 * creating the object of ClassLoader which is required to invoke getClasses()
			 * method
			 */
			ClassLoader classLoader = this.getClass().getClassLoader();
			/*
			 * invoking getClasses() method with classLoader and packageName and retrieving
			 * the list of available classes
			 */
			List<Class> availableClasses = annotatedClassLoader.getClasses(classLoader, packageName);
			/*
			 * calling annotatedClassProcessor() private method for getting the objects of
			 * available classes which are annotated as a container Map.
			 */
			Map<String, Object> annotatedClassObjects = annotatedClassProcessor(availableClasses);
			/*
			 * checking the Map whether it is empty or not. If not empty then proceed
			 * further
			 */
			if (!annotatedClassObjects.isEmpty()) {
				/*
				 * adding external objects to the map which will be used for dependency
				 * injection method invocation [method arguments]
				 */
				addExternalObjects(annotatedClassObjects);
				/* processing the annotated fields and injecting the dependencies */
				annotatedFieldProcessor(annotatedClassObjects);
				/*
				 * processing the annotated methods and invoking them depending upon the handler
				 * name and http method
				 */
				returnObject = annotatedMethodProcessor(annotatedClassObjects);
			} else {
				/* if the map is empty and no annotated class has been found */
				System.out.println("No annotated class has been found in : " + packageName);
			}
		} catch (Exception e) {
			System.out.println("[ProcessorEngine] Unable to process : " + e.getMessage());
		}
		return returnObject;
	}

	/**
	 * adds external objects to the container
	 * 
	 * @param annotatedClassObjects
	 */
	private void addExternalObjects(Map<String, Object> annotatedClassObjects) {
		/*
		 * if the list of objects in annotationProcessor object is not null, then
		 * iterating through the list and adding the objects in the
		 * annotatedClassObjects Map with key as the fully qualified class name and
		 * value as the object.
		 */
		if (null != annotationProcessorConfig.getObjects()) {
			for (Object object : annotationProcessorConfig.getObjects()) {
				annotatedClassObjects.put(object.getClass().getName(), object);
			}
		}
	}

	/**
	 * creates a container of objects of all the annotated classes
	 * 
	 * @param availableClasses
	 * @return Map
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private Map<String, Object> annotatedClassProcessor(List<Class> availableClasses) throws Exception {
		// storing the objects of all the annotated classes in annotatedClassObjects Map
		Map<String, Object> annotatedClassObjects = new HashMap<>();
		for (Class availableClass : availableClasses) {
			/*
			 * checking whether the class is annotated with @Controller / @Component
			 * annotation or not
			 */
			if (availableClass.isAnnotationPresent(DefineController.class)
					|| availableClass.isAnnotationPresent(DefineComponent.class)) {
				// Key[sdp.controller.Message] --> Value[sdp.controller.Message@4520ebad]
				annotatedClassObjects.put(availableClass.getName(),
						Class.forName(availableClass.getName()).newInstance());
			}
		}
		return annotatedClassObjects;
	}

	/**
	 * inject the dependencies to the annotated fields
	 * 
	 * @param annotatedClassObjects
	 * @throws IllegalAccessException
	 */
	private void annotatedFieldProcessor(Map<String, Object> annotatedClassObjects) throws IllegalAccessException {
		Object object = null;
		Field[] declaredFields = null;
		/*
		 * injecting all the required dependencies to all the annotated fields whose
		 * objects are already there in the Map
		 */
		for (String className : annotatedClassObjects.keySet()) {
			object = annotatedClassObjects.get(className);
			declaredFields = object.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				// checking whether the class is annotated with @Autowired annotation or not
				if (field.isAnnotationPresent(AutoInject.class)) {
					field.setAccessible(true);
					// injecting the dependency
					field.set(object, annotatedClassObjects.get(field.getType().getName()));
					field.setAccessible(false);
				}
			}
		}
	}

	/**
	 * invokes the annotated method which matched the handler name and http method
	 * 
	 * @param annotatedClassObjects
	 * @return Object
	 * @throws Exception
	 */
	private Object annotatedMethodProcessor(Map<String, Object> annotatedClassObjects) throws Exception {
		String handlerName = annotationProcessorConfig.getHandlerName();
		String httpMethod = annotationProcessorConfig.getHttpMethod();

		Object object = null;
		Object returnObject = null;
		Method[] declaredMethods = null;
		String annotationValue = "";
		String annotationMethod = "";
		for (String className : annotatedClassObjects.keySet()) {
			object = annotatedClassObjects.get(className);
			declaredMethods = object.getClass().getDeclaredMethods();
			for (Method method : declaredMethods) {
				/*
				 * checking whether the class is annotated with @RequestMapping annotation or
				 * not
				 */
				if (method.isAnnotationPresent(HandlerMethod.class)) {
					// retrieving the handler name from value
					annotationValue = method.getAnnotation(HandlerMethod.class).value();
					// retrieving the http method name from method
					annotationMethod = method.getAnnotation(HandlerMethod.class).method();
					// checking for matching with the handler name and http method
					if (annotationValue.equals(handlerName) && annotationMethod.equals(httpMethod)) {
						// invoking the method by passing the required argument varargs
						returnObject = method.invoke(object, prepareArguments(method, annotatedClassObjects));
						System.out.println("Executing [" + httpMethod + " " + handlerName + "]: "
								+ method.invoke(object, prepareArguments(method, annotatedClassObjects)));
					}
				}
			}
		}
		return returnObject;
	}

	/**
	 * Create the argument array for invoking a method on an object, if there in no
	 * argument then returns empty array with size 0
	 * 
	 * @param method
	 * @param annotatedClassObjects
	 * @return Object[]
	 */
	private Object[] prepareArguments(Method method, Map<String, Object> annotatedClassObjects) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		Object arguments[] = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			arguments[i] = annotatedClassObjects.get(parameterTypes[i].getName());
		}
		return arguments;
	}
}
