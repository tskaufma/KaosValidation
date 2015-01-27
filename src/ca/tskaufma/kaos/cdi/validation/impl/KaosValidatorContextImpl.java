package ca.tskaufma.kaos.cdi.validation.impl;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import ca.tskaufma.kaos.cdi.validation.KaosValidationResult;
import ca.tskaufma.kaos.cdi.validation.KaosValidatorContext;

public class KaosValidatorContextImpl implements KaosValidatorContext {

	private ConstraintValidatorContext context;

	private int violationCount = 0;

	private Object returnValue;

	private boolean isVoid;

	public KaosValidatorContextImpl(ConstraintValidatorContext context) {
		this.context = context;
	}

	@Override
	public ConstraintViolationBuilder reject(String template) {
		violationCount++;
		return context.buildConstraintViolationWithTemplate(template);
	}

	@Override
	public void reject(String template, String property) {
		violationCount++;
		context.buildConstraintViolationWithTemplate(template).addPropertyNode(
				property).addConstraintViolation();
	}

	public int getViolationCount() {
		return violationCount;
	}

	public KaosValidationResult createResult(Object returnValue,
			boolean isVoid) {
		System.out.println("Return value = " + returnValue + ", isVoid = "
				+ isVoid);
		return new KaosValidationResultImpl(violationCount > 0, returnValue, isVoid);
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public boolean isReturnTypeVoid() {
		return isVoid;
	}

}
