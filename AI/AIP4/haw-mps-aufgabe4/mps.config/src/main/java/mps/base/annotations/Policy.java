package mps.base.annotations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This annotation indicates that something is used as a policy according to the 
 * Domain Driven Design meta model
 *
 */
@Component
@Scope("prototype")
public @interface Policy {
	/**
	 * potential value that can be used to discriminate different policies
	 * @return the discriminator
	 */
	String value() default "";
}
