package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UserResponsePrinter {

  private static final String SEPARATOR = "-".repeat(52);
  private static final String ROW_FORMAT = "  %-10s : %s%n";

  private final ConsoleIO console;

  public void print(final UserResponse response) {
    console.println(SEPARATOR);
    console.printf(ROW_FORMAT, "ID",     response.id());
    console.printf(ROW_FORMAT, "Name",   response.name());
    console.printf(ROW_FORMAT, "Email",  response.email());
    console.printf(ROW_FORMAT, "Role",   response.role());
    // Clean Code - Regla 16: se llama al auxiliar que tiene la cadena if/else larga
    console.printf(ROW_FORMAT, "Status", getStatusLabel(response.status()));
    console.println(SEPARATOR);
  }

  public void printList(final List<UserResponse> users) {
    if (users.isEmpty()) {
      console.println("  No users found.");
      return;
    }
    console.printf("%n  Total: %d user(s)%n", users.size());
    users.forEach(this::print);
  }

  public void printSummary(final List<UserResponse> users) {
    Optional.ofNullable(users)
        .filter(list -> !list.isEmpty())
        .map(list -> list.stream()
            .reduce(
                new StringBuilder(),
                (summary, user) -> summary.append(String.format("  %s (%s)%n", user.name(), getStatusLabel(user.status()))),
                StringBuilder::append))
        .map(StringBuilder::toString)
        .ifPresentOrElse(console::println, () -> console.println("  No users found."));
  }

  private static String getStatusLabel(final String status) {
    if ("ACTIVE".equals(status)) {
      return "Activo";
    } else if ("INACTIVE".equals(status)) {
      return "Inactivo";
    } else if ("PENDING".equals(status)) {
      return "Pendiente de activacion";
    } else if ("BLOCKED".equals(status)) {
      return "Bloqueado";
    } else if ("DELETED".equals(status)) {
      return "Eliminado";
    } else {
      return "Estado desconocido";
    }
  }
}