package ca.tskaufma.kaos.cdi.validation;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import ca.tskaufma.kaos.cdi.validation.impl.KaosValidationMethodImpl;

public class KaosValidationExtension implements Extension {
	
	private List<KaosValidationMethod> validationMethods = new ArrayList<KaosValidationMethod>();
	
	void processAnnotatedType(@Observes ProcessAnnotatedType<?> pat, BeanManager beanManager) {
		AnnotatedType<?> at = pat.getAnnotatedType();
		
		// Check if any method defined as Validates method
		for (AnnotatedMethod<?> method : at.getMethods()) {
			for (AnnotatedParameter<?> param : method.getParameters()) {
				if (param.isAnnotationPresent(Validates.class)) {
					KaosValidationMethodImpl validationMethod = new KaosValidationMethodImpl(method, param, beanManager);
					System.out.println("Found validation method, check annotations: " + method.getAnnotations());
					if (method.isAnnotationPresent(ValidationOptions.class)) {
						System.out.println("PAT: Found ValidationOptions annotation");
						ValidationOptions validationOptions = method.getAnnotation(ValidationOptions.class);
						System.out.println("\tmessage: " + validationOptions.message());
						System.out.println("\tproperty: " + validationOptions.property());
						validationMethod.addOptions(validationOptions.message(), validationOptions.property());
					}
					validationMethods.add(validationMethod);
					continue;
				}
			}
		}
	}

	public List<KaosValidationMethod> getValidationMethods() {
		return validationMethods;
	}

}
