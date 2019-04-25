package sdp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Subhadeep Sen
 * @HandlerMethod This annotation can be used to define a method which is
 *                declared inside a class annotated with @DefineController as http
 *                handler method. This method will be invoked depending upon the
 *                handler name and http method. It accepts two parameters, value
 *                and method.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandlerMethod {
	String value() default "";

	String method() default "GET";
}
