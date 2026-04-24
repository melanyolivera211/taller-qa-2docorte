package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.in.CreateUserUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteUserUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllUsersUseCase;
import com.jcaa.usersmanagement.application.port.in.GetUserByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.LoginRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UserController")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  private static final String BCRYPT_HASH =
      "$2a$12$abcdefghijklmnopqrstabcdefghijklmnñopqrstuvwxyzabcdefgh";

  @Mock private CreateUserUseCase createUserUseCase;
  @Mock private UpdateUserUseCase updateUserUseCase;
  @Mock private DeleteUserUseCase deleteUserUseCase;
  @Mock private GetUserByIdUseCase getUserByIdUseCase;
  @Mock private GetAllUsersUseCase getAllUsersUseCase;
  @Mock private LoginUseCase loginUseCase;

  private UserController controller;

  private static UserModel buildUser(
      final String id,
      final String name,
      final String email,
      final UserRole role,
      final UserStatus status) {
    return new UserModel(
        new UserId(id),
        new UserName(name),
        new UserEmail(email),
        UserPassword.fromHash(BCRYPT_HASH),
        role,
        status);
  }

  @BeforeEach
  void setUp() {
    controller =
        new UserController(
            createUserUseCase,
            updateUserUseCase,
            deleteUserUseCase,
            getUserByIdUseCase,
            getAllUsersUseCase,
            loginUseCase);
  }

  @Test
  @DisplayName("listAllUsers() retorna una lista mapeada de UserResponse")
  void listAllUsers_returnsMappedResponseList_whenUsersExist() {
    // Arrange
    final UserModel user =
        buildUser("u-001", "Alice Smith", "alice@example.com", UserRole.ADMIN, UserStatus.ACTIVE);
    when(getAllUsersUseCase.execute()).thenReturn(List.of(user));

    // Act
    final List<UserResponse> result = controller.listAllUsers();

    // Assert
    assertAll(
        "single-user list mapping",
        () -> assertEquals(1, result.size()),
        () -> assertEquals("u-001", result.get(0).id()),
        () -> assertEquals("Alice Smith", result.get(0).name()),
        () -> assertEquals("alice@example.com", result.get(0).email()),
        () -> assertEquals("ADMIN", result.get(0).role()),
        () -> assertEquals("ACTIVE", result.get(0).status()));
    verify(getAllUsersUseCase).execute();
  }

  @Test
  @DisplayName("listAllUsers() retorna lista vacía cuando no hay usuarios")
  void listAllUsers_returnsEmptyList_whenNoUsersExist() {
    // Arrange
    when(getAllUsersUseCase.execute()).thenReturn(List.of());

    // Act
    final List<UserResponse> result = controller.listAllUsers();

    // Assert
    assertTrue(result.isEmpty());
    verify(getAllUsersUseCase).execute();
  }

  @Test
  @DisplayName("findUserById() retorna el usuario mapeado")
  void findUserById_returnsMappedResponse_whenUserExists() {
    // Arrange
    final UserModel user =
        buildUser("u-002", "Bob Jones", "bob@example.com", UserRole.MEMBER, UserStatus.ACTIVE);
    when(getUserByIdUseCase.execute(new GetUserByIdQuery("u-002"))).thenReturn(user);

    // Act
    final UserResponse result = controller.findUserById("u-002");

    // Assert
    assertAll(
        "findUserById response mapping",
        () -> assertEquals("u-002", result.id()),
        () -> assertEquals("Bob Jones", result.name()),
        () -> assertEquals("bob@example.com", result.email()),
        () -> assertEquals("MEMBER", result.role()),
        () -> assertEquals("ACTIVE", result.status()));
  }

  @Test
  @DisplayName("findUserById() propaga UserNotFoundException")
  void findUserById_propagatesUserNotFoundException_whenUserDoesNotExist() {
    // Arrange
    when(getUserByIdUseCase.execute(new GetUserByIdQuery("u-999")))
        .thenThrow(UserNotFoundException.becauseIdWasNotFound("u-999"));

    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> controller.findUserById("u-999"));
  }

  @Test
  @DisplayName("createUser() delega correctamente y retorna el usuario mapeado")
  void createUser_delegatesCorrectCommandAndReturnsMappedResponse_whenCreationSucceeds() {
    // Arrange
    final CreateUserRequest request =
        new CreateUserRequest("u-003", "Carol White", "carol@example.com", "Pass1234", "REVIEWER");
    final UserModel createdUser =
        buildUser(
            "u-003", "Carol White", "carol@example.com", UserRole.REVIEWER, UserStatus.PENDING);
    final ArgumentCaptor<CreateUserCommand> captor =
        ArgumentCaptor.forClass(CreateUserCommand.class);
    when(createUserUseCase.execute(captor.capture())).thenReturn(createdUser);

    // Act
    final UserResponse result = controller.createUser(request);

    // Assert
    assertAll(
        "createUser command delegation and response mapping",
        () -> assertEquals("u-003", captor.getValue().id()),
        () -> assertEquals("Carol White", captor.getValue().name()),
        () -> assertEquals("carol@example.com", captor.getValue().email()),
        () -> assertEquals("Pass1234", captor.getValue().password()),
        () -> assertEquals("REVIEWER", captor.getValue().role()),
        () -> assertEquals("u-003", result.id()),
        () -> assertEquals("PENDING", result.status()));
  }

  @Test
  @DisplayName("updateUser() delega UpdateUserCommand")
  void updateUser_delegatesCorrectCommand_whenUpdateSucceeds() {
    // Arrange
    final UpdateUserRequest request =
        new UpdateUserRequest(
            "u-005", "Eve Martinez", "eve@example.com", "NewPass9!", "ADMIN", "ACTIVE");
    final ArgumentCaptor<UpdateUserCommand> captor =
        ArgumentCaptor.forClass(UpdateUserCommand.class);
    doNothing().when(updateUserUseCase).execute(captor.capture());

    // Act
    controller.updateUser(request);

    // Assert
    assertAll(
        "updateUser command delegation",
        () -> assertEquals("u-005", captor.getValue().id()),
        () -> assertEquals("Eve Martinez", captor.getValue().name()),
        () -> assertEquals("eve@example.com", captor.getValue().email()),
        () -> assertEquals("NewPass9!", captor.getValue().password()),
        () -> assertEquals("ADMIN", captor.getValue().role()),
        () -> assertEquals("ACTIVE", captor.getValue().status()));
  }

  @Test
  @DisplayName("deleteUser() delega DeleteUserCommand")
  void deleteUser_delegatesDeleteCommandWithCorrectId() {
    // Arrange
    final ArgumentCaptor<DeleteUserCommand> captor =
        ArgumentCaptor.forClass(DeleteUserCommand.class);
    doNothing().when(deleteUserUseCase).execute(captor.capture());

    // Act
    controller.deleteUser("u-006");

    // Assert
    assertEquals("u-006", captor.getValue().id());
  }

  @Test
  @DisplayName("login() delega LoginCommand y retorna UserResponse")
  void login_delegatesCorrectCommandAndReturnsMappedResponse_whenCredentialsAreValid() {
    // Arrange
    final LoginRequest request = new LoginRequest("frank@example.com", "Pass1234!");
    final UserModel loggedUser =
        buildUser("u-007", "Frank Green", "frank@example.com", UserRole.MEMBER, UserStatus.ACTIVE);
    final ArgumentCaptor<LoginCommand> captor = ArgumentCaptor.forClass(LoginCommand.class);
    when(loginUseCase.execute(captor.capture())).thenReturn(loggedUser);

    // Act
    final UserResponse result = controller.login(request);

    // Assert
    assertAll(
        "login command delegation and response mapping",
        () -> assertEquals("frank@example.com", captor.getValue().email()),
        () -> assertEquals("Pass1234!",         captor.getValue().password()),
        () -> assertEquals("u-007",             result.id()),
        () -> assertEquals("frank@example.com", result.email()),
        () -> assertEquals("ACTIVE",            result.status()));
  }

  @Test
  @DisplayName("login() propaga InvalidCredentialsException")
  void login_propagatesInvalidCredentialsException_whenCredentialsAreInvalid() {
    // Arrange
    final LoginRequest request = new LoginRequest("frank@example.com", "WrongPass1");
    when(loginUseCase.execute(any()))
        .thenThrow(InvalidCredentialsException.becausePasswordIsWrong());

    // Act & Assert
    assertThrows(InvalidCredentialsException.class, () -> controller.login(request));
  }
}
