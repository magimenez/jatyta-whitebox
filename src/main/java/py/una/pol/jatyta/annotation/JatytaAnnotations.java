/**
 * 
 */
package py.una.pol.jatyta.annotation;

import java.lang.annotation.*;

/**
 * @author Beto
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface JatytaAnnotations {
	boolean itemscope() default false;

	String itemtype() default "";

	String itemid() default "";

	String itemprop() default "";

	String itemref() default "";
}