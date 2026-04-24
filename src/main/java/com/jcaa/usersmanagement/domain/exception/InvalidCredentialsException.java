package com.jcaa.usersmanagement.domain.exception;

public final class InvalidCredentialsException extends DomainException {

  private static final String WRONG_PASSWORD_MESSAGE = "The password provided is incorrect.";
  private static final String INACTIVE_USER_MESSAGE = "The user is not active in the system.";

  private InvalidCredentialsException(final String message) {
    super(message);
  }

  public static InvalidCredentialsException becausePasswordIsWrong() {
    return new InvalidCredentialsException(WRONG_PASSWORD_MESSAGE);
  }

  public static InvalidCredentialsException becauseUserIsInactive() {
    return new InvalidCredentialsException(INACTIVE_USER_MESSAGE);
  }
}
