package org.api.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
  code = HttpStatus.INTERNAL_SERVER_ERROR,
  reason = "Server is on Maintainence, please try later."
)
public class MaintainenceException extends RuntimeException {

  /** */
  private static final long serialVersionUID = 1L;
}
