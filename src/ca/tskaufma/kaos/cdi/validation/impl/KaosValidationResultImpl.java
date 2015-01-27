package ca.tskaufma.kaos.cdi.validation.impl;

import ca.tskaufma.kaos.cdi.validation.KaosValidationResult;

public class KaosValidationResultImpl implements KaosValidationResult {

	private boolean hasViolations;
	private Object returnValue;
	private boolean isVoid;

	public KaosValidationResultImpl(boolean hasViolations, Object returnValue,
			boolean isVoid) {
		this.hasViolations = hasViolations;
		this.returnValue = returnValue;
		this.isVoid = isVoid;
	}

	@Override
	public Object getReturnValue() {
		return returnValue;
	}

	@Override
	public boolean hasViolations() {
		return hasViolations;
	}

	@Override
	public boolean isReturnTypeVoid() {
		return isVoid;
	}

}
