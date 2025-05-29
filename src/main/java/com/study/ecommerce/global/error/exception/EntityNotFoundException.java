package com.study.ecommerce.global.error.exception;

import com.study.ecommerce.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message) {
<<<<<<< HEAD
        super(message, ErrorCode.RESOURCE_NOT_FOUND);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(ErrorCode.RESOURCE_NOT_FOUND);
=======
      super(message, ErrorCode.RESOURCE_NOT_FOUND);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
      super(ErrorCode.RESOURCE_NOT_FOUND);
>>>>>>> 27f6a88c6ed98b2aa47aec1d7ff81dbadd5a2c78
    }
}
