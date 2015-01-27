package ca.tskaufma.kaos.cdi.validation.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.validation.ConstraintValidatorContext;

import ca.tskaufma.kaos.cdi.validation.KaosValidationMethod;
import ca.tskaufma.kaos.cdi.validation.KaosValidationResult;
import ca.tskaufma.kaos.cdi.validation.KaosValidatorContext;

public class KaosValidationMethodImpl implements KaosValidationMethod {

	private AnnotatedMethod<?> method;
	private AnnotatedParameter<?> param;
	private BeanManager beanManager;
	private Object methodBeanInstance;
	private List<InjectionPoint> methodParams;
	private Integer contextParamPosition;
	private String validationMessage;
	private String validationProperty;

	public KaosValidationMethodImpl(AnnotatedMethod<?> method,
			AnnotatedParameter<?> param, BeanManager beanManager) {
		this.method = method;
		this.param = param;
		this.beanManager = beanManager;
		this.contextParamPosition = null;
	}

	private void initializeValidationBean() {

		// Get any existing eligible bean based on the type of the Property
		// Resolver method containing class.
		Set<Bean<?>> beans = beanManager.getBeans(method.getJavaMember()
				.getDeclaringClass());
		final Bean<?> methodBean = beanManager.resolve(beans);
		CreationalContext<?> creationalContext = beanManager
				.createCreationalContext(methodBean);

		methodBeanInstance = beanManager.getReference(methodBean, method
				.getJavaMember().getDeclaringClass(), creationalContext);

		methodParams = new ArrayList<InjectionPoint>();

		// Create injection points for any additional Property Resolver
		// method parameters. They will be later injected by the container
		if (method.getParameters().size() > 1) {
			int currentIndex = 0;
			for (final AnnotatedParameter<?> parameter : method
					.getParameters()) {

				// System.out.println("\tParameter " + parameter.getPosition() +
				// " " + parameter.getBaseType() + "[" + parameter + "]");
				if (currentIndex++ == param.getPosition()) {
					// System.out.println("\t\tSkip");
					continue;
				}

				if (parameter.getBaseType().equals(KaosValidatorContext.class)) {
					System.out.println("Context requested");
					contextParamPosition = parameter.getPosition();
					continue;
				}

				methodParams.add(new InjectionPoint() {

					public Type getType() {
						return parameter.getBaseType();
					}

					public Set<Annotation> getQualifiers() {
						Set<Annotation> qualifiers = new HashSet<Annotation>();
						for (Annotation annotation : parameter.getAnnotations()) {
							if (beanManager.isQualifier(annotation
									.annotationType())) {
								qualifiers.add(annotation);
							}
						}
						return qualifiers;
					}

					public Bean<?> getBean() {
						return methodBean;
					}

					public Member getMember() {
						return parameter.getDeclaringCallable().getJavaMember();
					}

					public Annotated getAnnotated() {
						return parameter;
					}

					public boolean isDelegate() {
						return false;
					}

					public boolean isTransient() {
						return false;
					}

				});
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.tskaufma.kaos.cdi.validation.IKaosValidationMethod#validate(T,
	 * javax.validation.ConstraintValidatorContext)
	 */
	public KaosValidationResult validate(Object object,
			ConstraintValidatorContext context)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (methodBeanInstance == null) {
			initializeValidationBean();
		}

		KaosValidatorContextImpl kvContext = new KaosValidatorContextImpl(
				context);

		CreationalContext<?> ctx = beanManager.createCreationalContext(null);
		List<Object> parameters = new ArrayList<Object>();

		for (InjectionPoint parameter : methodParams) {
			parameters.add(beanManager.getInjectableReference(parameter, ctx));
		}

		if (contextParamPosition != null) {
			if (contextParamPosition < param.getPosition()) {
				parameters.add(contextParamPosition, kvContext);
				parameters.add(param.getPosition(), object);
			} else {
				parameters.add(param.getPosition(), object);
				parameters.add(contextParamPosition, kvContext);
			}
		} else {
			parameters.add(param.getPosition(), object);
		}

		// System.out.println("validate: parameters " + parameters);
		Object returnValue = method.getJavaMember().invoke(methodBeanInstance,
				parameters.toArray());

		System.out.println(method.getJavaMember().getReturnType().getName());

		return kvContext.createResult(returnValue, method.getJavaMember()
				.getReturnType().getName().equals("void"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.tskaufma.kaos.cdi.validation.IKaosValidationMethod#canValidate(java
	 * .lang.Class)
	 */
	public boolean canValidate(Class<?> class1) {
		return ((Class<?>) param.getBaseType()).isAssignableFrom(class1);
	}

	@Override
	public String toString() {
		return "KoasValidationMethod [method=" + method + "]";
	}

	public void addOptions(String message, String property) {
		this.validationMessage = message;
		this.validationProperty = property;
	}

	@Override
	public String getValidationMessage() {
		return validationMessage;
	}

	@Override
	public String getValidationPropery() {
		return validationProperty;
	}

}
