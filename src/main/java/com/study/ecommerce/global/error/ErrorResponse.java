package com.study.ecommerce.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
<<<<<<< HEAD
import org.springframework.validation.FieldError;
=======
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
<<<<<<< HEAD
=======

>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private int status;
    private String code;
    private List<FieldError> errors;

    public ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = errors;
    }

    public ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.errors = new ArrayList<>();
    }

<<<<<<< HEAD
    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
=======
    public static ErrorResponse of(final ErrorCode code,final BindingResult bindingResult) {
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

<<<<<<< HEAD
    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
=======
    public static ErrorResponse of(MethodArgumentTypeMismatchException e){
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
<<<<<<< HEAD
    public static class FieldError {
=======
    public static class FieldError{
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
        private String field;
        private String value;
        private String reason;

<<<<<<< HEAD
        public FieldError(final String field, final String value, final String reason) {
=======
        public FieldError(final String field, final String value, final String reason){
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

<<<<<<< HEAD
        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        public static List<FieldError> of(final BindingResult bindingResult) {
=======
        public static List<FieldError> of(final String field, final String value, final String reason){
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field,value,reason));
            return fieldErrors;
        }

        public static List<FieldError> of(final BindingResult bindingResult){
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    ))
                    .toList();
        }
    }
<<<<<<< HEAD
=======

>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
}
