package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
public interface UpdateUserUseCase {
  void execute(@NotNull @Valid UpdateUserCommand command);
}
