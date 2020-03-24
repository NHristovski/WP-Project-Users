package hristovski.nikola.users.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordsMatchValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface PasswordsMatch {

    String message() default "Password and Confirm password fields do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
