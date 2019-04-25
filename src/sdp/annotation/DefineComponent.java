package sdp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Subhadeep Sen
 * @DefineComponent This annotation can be used to create object internally of a
 *                  class which will then be used for dependency injection to
 *                  the fields which are annotated with @AutoInject annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface DefineComponent {

}
