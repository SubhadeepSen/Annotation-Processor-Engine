package sdp.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Subhadeep Sen
 * @AutoInject This annotation can be used on any field in a class to
 *             automatically inject the object of that type if any object is
 *             present otherwise it will be set to null. A class must be
 *             annotated with @DefineComponent annotation to get the object.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoInject {

}
