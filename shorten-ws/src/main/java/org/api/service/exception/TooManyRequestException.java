package org.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
  code = HttpStatus.TOO_MANY_REQUESTS,
  reason = "Too many requests received, please wait a minute before you retry"
)
public class TooManyRequestException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 1L;
}
