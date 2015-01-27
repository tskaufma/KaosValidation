package ca.tskaufma.kaos.cdi.validation;

public interface KaosValidationResult {
	
	public boolean isReturnTypeVoid();
	
	public Object getReturnValue();
	
	public boolean hasViolations();

}
