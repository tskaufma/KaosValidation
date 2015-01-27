package ca.tskaufma.kaos.cdi.validation;

import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

public interface KaosValidatorContext {

	/**
	 * Creates a ConstraintViolationBuilder which allows custom property setting
	 * for the Constraint Violation using the provided template. Calling this
	 * method indicates a violation exists. Caller is responsible to call
	 * {@link ConstraintViolationBuilder#addConstraintViolation()}, not doing so
	 * will cause an exception to be thrown.
	 * 
	 * @param template
	 *            template for the Constraint Violation
	 * @return ConstraintViolationBuilder to customize the Constraint Violation
	 */
	public ConstraintViolationBuilder reject(String template);

	/**
	 * Creates a Constraint Violation for the given property using the provided
	 * template.
	 * 
	 * @param template
	 *            template for the Constraint Violation
	 * @param property
	 *            the property the Constraint Violation relates to, may be
	 *            <code>null</code> for class-level violation
	 */
	public void reject(String template, String property);

}
