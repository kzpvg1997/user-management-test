package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidatorGateway, String> {
    @Override
    public void initialize(PasswordValidatorGateway constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;

        // Validar largo
        if (password.length() < 8 || password.length() > 12) {
            return false;
        }

        int upperCount = 0;
        int digitCount = 0;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upperCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            }
        }
        return upperCount == 1 && digitCount == 2;
    }
}

