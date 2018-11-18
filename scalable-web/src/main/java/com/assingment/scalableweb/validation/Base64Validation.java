package com.assingment.scalableweb.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.assingment.scalableweb.validation.impl.Base64ValidationImpl;

/**
 * This is an annotation used to validate the String in Base 64 format.
 * 
 * @author <a href="mailto:saurabh.s.oza@gmail.com">Saurabh Oza</a>.
 *
 */
@Documented
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=Base64ValidationImpl.class)
public @interface Base64Validation {
    String message() default "Input String is not in a valid Base 64 JSON format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
