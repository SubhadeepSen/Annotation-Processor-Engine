package sdp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Subhadeep Sen
 * @DefineController This annotation specifies a class as a controller which
 *                   will be used to perform http operations by the defined
 *                   methods. The proper handler method must be annotated
 *                   with @HandlerMethod annotation and having the value for
 *                   handler and http method. It also creates the object of the
 *                   annotated class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface DefineController {

}
