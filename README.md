# KaosValidation
Method-based extension to Bean Validation, like @Observes for validation.

## Usage
To use KaosValidation add the @KaosValidation annotation to any CDI bean.

```java
@KaosValidation
public class MyBean {
    private String value;

    public String getValue() {
        return value;
    }
    
    public String setValue(String value) {
        this.value = value;
    }
}
```

Now you can write a validation method in any other CDI bean by using the @Validates annotation on a parameter.

```java
public class MyValidation {
    boolean validateValue(@Validates MyBean myBean) {
        return myBean.getValue() != null;
    }
}
```

Your validation methods will participate in the regular bean validation process.

```java
public class MyApp {
    @Inject
    Validator validator;
    
    public void run() {
        MyBean myBean = new MyBean();
        
        Set<ConstraintViolation<MyBean>> violations = validator.validate(myBean);
        
        System.out.println(violations.size()); // Should print 1
    }
}
```
