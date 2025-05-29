package com.study.ecommerce.global.error.exception;

import com.study.ecommerce.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

<<<<<<< HEAD
=======

>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
<<<<<<< HEAD

=======
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
}
