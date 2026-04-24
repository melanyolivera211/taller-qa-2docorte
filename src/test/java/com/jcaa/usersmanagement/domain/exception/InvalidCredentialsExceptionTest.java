package com.jcaa.usersmanagement.domain.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InvalidCredentialsException")
class InvalidCredentialsExceptionTest {

  @Test
  @DisplayName("Las factories deben producir mensajes distintos y no vacíos")
  void shouldProduceDistinctNonBlankMessagesForEachAuthFailureScenario() {
    // Act
    final String wrongPassMsg =
        InvalidCredentialsException.becausePasswordIsWrong().getMessage();
    final String inactiveUserMsg =
        InvalidCredentialsException.becauseUserIsInactive().getMessage();

    // Assert
    assertAll(
        "mensajes de autenticación fallida",
        () ->
            assertFalse(
                wrongPassMsg.isBlank(), "mensaje de contraseña incorrecta no debe estar vacío"),
        () ->
            assertFalse(
                inactiveUserMsg.isBlank(), "mensaje de usuario inactivo no debe estar vacío"),
        () ->
            assertNotEquals(
                wrongPassMsg,
                inactiveUserMsg,
                "cada escenario debe tener su propio mensaje para identificar la causa"));
  }
}
