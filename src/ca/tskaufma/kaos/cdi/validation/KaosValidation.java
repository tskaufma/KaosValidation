package ca.tskaufma.kaos.cdi.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ca.tskaufma.kaos.cdi.validation.impl.KaosValidationImpl;

@Target( { TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = KaosValidationImpl.class)
@Documented
public @interface KaosValidation {

	String message() default "KaosValidation error";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
