package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValidatorGateway {
    String message() default "the password don´t have the correct format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
