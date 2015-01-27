package ca.tskaufma.kaos.cdi.validation;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidatorContext;

public interface KaosValidationMethod {

	public abstract KaosValidationResult validate(Object object,
			ConstraintValidatorContext context)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException;

	public abstract boolean canValidate(Class<?> class1);

	public abstract String getValidationMessage();

	public abstract String getValidationPropery();

}