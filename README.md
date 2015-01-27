# KaosValidation
Method-based extension to Bean Validation, like @Observes for validation.

## Usage
To use KaosValidation add the @KaosValidation annotation to any CDI bean.

    public class MyBean {
      private String value;
      
      public String getValue() {
        return value;
      }
    }

Now you can write a validation method in any other CDI bean.

Your validation methods will participate  in the regular bean validation process.
