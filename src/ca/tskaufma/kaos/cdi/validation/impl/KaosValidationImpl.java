package ca.tskaufma.kaos.cdi.validation.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import ca.tskaufma.kaos.cdi.validation.KaosValidation;
import ca.tskaufma.kaos.cdi.validation.KaosValidationExtension;
import ca.tskaufma.kaos.cdi.validation.KaosValidationMethod;
import ca.tskaufma.kaos.cdi.validation.KaosValidationResult;

public class KaosValidationImpl implements
		ConstraintValidator<KaosValidation, Object> {

	@Inject
	KaosValidationExtension kve;

	@Inject
	BeanManager manager;

	public void initialize(KaosValidation validation) {
	}

	public boolean isValid(Object obj, final ConstraintValidatorContext context) {

		context.disableDefaultConstraintViolation();
		int violations = 0;
		/*
		 * Somehow call all validates methods!
		 */
		// System.out.println("Kaos Validation: " + kve.getValidationMethods());

		List<KaosValidationMethod> validatorList = kve
				.getValidationMethods();
		for (KaosValidationMethod validator : validatorList) {
			if (!validator.canValidate(obj.getClass())) {
				continue;
			}
			try {
				KaosValidationResult result = validator.validate(obj, context);

				if (result.isReturnTypeVoid()) {
					if (result.hasViolations())
						violations++;
				} else {
					Object retVal = result.getReturnValue();
					if (retVal != null) {
						if (handleValidateResult(retVal, context, validator)) {
							violations++;
						}
					}
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return violations == 0;
	}

	private boolean handleValidateResult(Object result,
			ConstraintValidatorContext context,
			KaosValidationMethod validator) {
		if (result instanceof Boolean) {
			if (((Boolean) result).booleanValue() == false) {
				String message = validator.getValidationMessage();

				ConstraintViolationBuilder builder = context
						.buildConstraintViolationWithTemplate(message != null ? message
								: validator.toString());

				if (validator.getValidationPropery() != null) {
					builder.addPropertyNode(validator.getValidationPropery());
				}

				builder.addConstraintViolation();
				return true;
			}
		} else {
			context.buildConstraintViolationWithTemplate(result.toString())
					.addConstraintViolation();
			return true;
		}

		/* If not violation created, return false */
		return false;
	}
}
