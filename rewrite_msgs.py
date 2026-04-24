import sys

msg = sys.stdin.read()

replacements = [
    ("refactor: arregla violacion40 de regla2 (Eliminacion de comentarios redundantes en UserRepositoryMySQL)", "refactor: aplica Regla 10 de Guia2 (Eliminar comentarios redundantes) en UserRepositoryMySQL"),
    ("merge: fix/regla2/violacion40", "merge: Regla 10 de Guia2 (comentarios redundantes) en UserRepositoryMySQL"),
    ("refactor: arregla violacion39 de regla2 (Eliminacion de saveWithFields en UserRepositoryMySQL)", "refactor: aplica Regla 5 de Guia2 (Pocos parametros primitivos) - elimina saveWithFields en UserRepositoryMySQL"),
    ("merge: fix/regla2/violacion39", "merge: Regla 5 de Guia2 (pocos parametros) en UserRepositoryMySQL"),
    ("refactor: arregla violacion38 de regla1 (Wildcard imports en UserRepositoryMySQL)", "refactor: aplica Regla 4 de Guia1 (No imports wildcard) en UserRepositoryMySQL"),
    ("merge: fix/regla1/violacion38", "merge: Regla 4 de Guia1 (imports wildcard) en UserRepositoryMySQL"),
    ("refactor: arregla violacion36 y 37 de regla1 (Naming y Hardcoded en ConsoleIO)", "refactor: aplica Regla 4 y 10 de Guia1 (Naming y textos hardcoded) en ConsoleIO"),
    ("merge: fix/regla1/violacion36-37", "merge: Regla 4 y 10 de Guia1 (naming y hardcoded) en ConsoleIO"),
    ("refactor: arregla violacion21 de regla2 (CQS en UpdateUserService)", "refactor: aplica Regla 8 de Guia2 (CQS - retorno void) en UpdateUserService"),
    ("merge: fix/regla2/violacion21-cqs", "merge: Regla 8 de Guia2 (CQS) en UpdateUserService"),
    ("refactor: arregla violacion25 de regla1 (Eliminacion de UserValidationUtils)", "refactor: aplica Regla 13 de Guia2 (Evitar clases Utils) - elimina UserValidationUtils"),
    ("merge: fix/regla1/violacion25", "merge: Regla 13 de Guia2 (clases Utils) - elimina UserValidationUtils"),
    ("refactor: arregla violacion24 de regla1 (Inconsistencia semantica en UserApplicationMapper)", "refactor: aplica Regla 21 y 24 de Guia2 (codigo de error y consistencia semantica) en UserApplicationMapper"),
    ("merge: fix/regla1/violacion24", "merge: Regla 21 y 24 de Guia2 (semantica) en UserApplicationMapper"),
    ("refactor: arregla violacion23 de regla1 (SRP en EmailNotificationService)", "refactor: aplica Regla 3 de Guia2 (un nivel de abstraccion por funcion) en EmailNotificationService"),
    ("merge: fix/regla1/violacion23", "merge: Regla 3 de Guia2 (abstraccion) en EmailNotificationService"),
    ("refactor: arregla violacion22 de regla1 (SRP en LoginService)", "refactor: aplica Regla 1 y 14 de Guia2 (SRP y Ley de Demeter) en LoginService"),
    ("merge: fix/regla1/violacion22", "merge: Regla 1 y 14 de Guia2 (SRP, Demeter) en LoginService"),
    ("refactor: arregla violacion21 de regla1 (SRP en UpdateUserService)", "refactor: aplica Regla 6 y 7 de Guia2 (boolean flag y efectos ocultos) en UpdateUserService"),
    ("merge: fix/regla1/violacion21", "merge: Regla 6 y 7 de Guia2 (boolean flag) en UpdateUserService"),
    ("refactor: arregla violacion12 de regla1 (Objects.requireNonNull en EmailDestinationModel)", "refactor: aplica Regla 4 de Guia1 (== null -> requireNonNull) en EmailDestinationModel"),
    ("merge: fix/regla1/violacion12", "merge: Regla 4 de Guia1 (requireNonNull) en EmailDestinationModel"),
    ("refactor: arregla violacion11 de regla1 (Objects.requireNonNull en UserPassword)", "refactor: aplica Regla 4 de Guia1 (== null -> requireNonNull) en UserPassword"),
    ("merge: fix/regla1/violacion11", "merge: Regla 4 de Guia1 (requireNonNull) en UserPassword"),
    ("refactor: arregla violacion10 de regla1 (Objects.requireNonNull en UserName)", "refactor: aplica Regla 4 de Guia1 (== null -> requireNonNull) en UserName"),
    ("merge: fix/regla1/violacion10", "merge: Regla 4 de Guia1 (requireNonNull) en UserName"),
    ("refactor: arregla violacion9 de regla1 (Objects.requireNonNull en UserId)", "refactor: aplica Regla 4 de Guia1 (== null -> requireNonNull) en UserId"),
    ("merge: fix/regla1/violacion9", "merge: Regla 4 de Guia1 (requireNonNull) en UserId"),
    ("refactor: arregla violacion8 de regla1 (elimina log PII en dominio)", "refactor: aplica Regla 6 de Guia1 (no logging PII en dominio) en UserEmail"),
    ("merge: fix/regla1/violacion8", "merge: Regla 6 de Guia1 (PII logging) en UserEmail"),
    ("refactor: arregla violacion7 de regla2 (UserResponse como record)", "refactor: aplica Regla 2 de Guia1 (DTO de respuesta inmutable como record) en UserResponse"),
    ("merge: fix/regla2/violacion7", "merge: Regla 2 de Guia1 (DTO inmutable) en UserResponse"),
    ("refactor: arregla violacion6 de regla2 (inmutabilidad en UserModel)", "refactor: aplica Regla 2 de Guia1 (modelo de dominio inmutable con @Value) en UserModel"),
    ("merge: fix/regla2/violacion6", "merge: Regla 2 de Guia1 (inmutabilidad) en UserModel"),
    ("refactor: arregla violacion5 de regla1 (uso de mapper en login)", "refactor: aplica Regla 1 de Guia1 (arquitectura hexagonal - mapper en login) en UserController"),
    ("merge: fix/regla1/violacion5", "merge: Regla 1 de Guia1 (mapper en login) en UserController"),
    ("refactor: arregla violacion4 de regla1 (uso de mapper en deleteUser)", "refactor: aplica Regla 1 de Guia1 (arquitectura hexagonal - mapper en deleteUser) en UserController"),
    ("merge: fix/regla1/violacion4", "merge: Regla 1 de Guia1 (mapper en deleteUser) en UserController"),
    ("refactor: arregla violacion3 de regla1 (uso de mapper en entrypoint)", "refactor: aplica Regla 1 de Guia1 (arquitectura hexagonal - mapper en createUser) en UserController"),
    ("refactor: arregla violacion1 de regla1 (acoplamiento a infraestructura en dominio)", "refactor: aplica Regla 1 de Guia1 (arquitectura hexagonal - desacopla dominio de infraestructura) en UserModel"),
    ("refactor: arregla violacion13 de regla1 (constantes para magic numbers en UserPassword)", "refactor: aplica Regla 10 de Guia1 (magic numbers a constantes) en UserPassword"),
    ("merge: fix/regla1/violacion13", "merge: Regla 10 de Guia1 (magic numbers) en UserPassword"),
    ("refactor: arregla violacion14 de regla1 (constante para magic number en UserName)", "refactor: aplica Regla 10 de Guia1 (magic number a constante) en UserName"),
    ("merge: fix/regla1/violacion14", "merge: Regla 10 de Guia1 (magic number) en UserName"),
]

for old, new in replacements:
    msg = msg.replace(old, new)

sys.stdout.write(msg)
