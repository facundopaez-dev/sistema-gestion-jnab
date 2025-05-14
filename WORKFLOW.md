# 🛠 Flujo de trabajo general
El objetivo de este flujo de trabajo es mantener la rama `main` del repositorio remoto siempre estable y asegurarnos de que todos podamos trabajar sin sobrescribir el código de otros.

## Guía rápida
1. Crear un issue en GitHub por cada feature.
2. Actualizar la rama `main` antes de comenzar a trabajar.
3. Crear una rama específica para cada feature.
4. Realizar commits localmente en la rama correspondiente.
5. Subir la rama al repositorio remoto.
6. Crear un Pull Request (PR) en GitHub.
7. Realizar el merge en la rama `main` en GitHub.
8. Usar nombres descriptivos y consistentes.

---

### 1. Crear un issue en GitHub por cada feature
Antes de comenzar a desarrollar una nueva funcionalidad, **se debe crear un issue en GitHub** describiendo claramente la tarea a realizar. Esta issue servirá para documentar el trabajo, facilitar la asignación y permitir su cierre automático al integrar el código.

---

### 2. Actualizar la rama `main` antes de comenzar a trabajar
Siempre se debe trabajar sobre la última versión de `main`. Por lo tanto, antes de implementar una funcionalidad nueva, retormar una rama antigua o resolver conflictos, se deben ejecutar los siguientes comandos en el repositorio local:

```
git checkout main
git pull origin main
```

**Nota:** En caso de ser necesario, ejecutar `git merge` o `git rebase` para asegurarse de tener los últimos cambios del repositorio remoto.

---

### 3. Crear una rama específica para cada feature
El nombre de la rama debe seguir la convención: `feature/nombre-funcionalidad`. Esto facilita la organización y el seguimiento del desarrollo. Por ejemplo:

```
git checkout -b feature/pantalla-agenda
```

---

### 4. Realizar commits localmente en la rama correspondiente
- Se recomienda redactar el mensaje del commit en **modo imperativo**, ya que describe **lo que hace el commit al aplicarse**, no lo que hizo el programador. Esta práctica está establecida en la sección _5.2 Distributed Git - Contributing to a Project_ del libro **Pro Git**.

  ✔️ Correcto (mensaje en modo imperativo):
  ```
  git commit -m "feat: implementa pantalla de agenda"
  ```

  ❌ Incorrecto:
  ```
  git commit -m "feat: implementé pantalla de agenda"
  ```

- También se recomienda seguir la especificación [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/), la cual define los siguientes tipos de commit más comunes:
  - `feat`: agregar una nueva funcionalidad
  - `fix`: corregir un error
  - `docs`: actualizar documentación
  - `style`: aplicar cambios de formato
  - `refactor`: reestructurar código sin cambiar funcionalidad
  - `test`: agregar o modificar pruebas
  - `chore`: tareas de mantenimiento

- Opcional, pero recomendable: al final del mensaje de un commit, se puede incluir entre paréntesis # seguido del número del issue relacionado, ya que contribuye a la trazabilidad. Por ejemplo:

  ```
  git commit -m "fix: corrige validación de correo (#42)"
  ```

> ⚠️ **Importante:** No se debe incluir `closes #<número-issue>` en el mensaje de un commit. Ese enlace se declara en la **descripción del pull request**, como se indica en el paso 6.

---

### 5. Subir la rama al repositorio remoto
Luego de realizar un commit en la nueva rama, esta debe subirse al repositorio remoto con:

```
git push origin <nombre-rama>
```

---

### 6. Crear un Pull Request (PR) en GitHub
- El título del pull request debe reflejar el cambio realizado, por ejemplo, la funcionalidad implementada. Si está relacionado con un issue, se recomienda incluir al final del título el número del issue utilizando el formato `(#<número-issue>)`, ya que esto mejora la trazabilidad. Por ejemplo: `feat: implementa modelo de datos del simposio (#4)`.
- En la descripción del PR, debe incluirse `closes #<número-issue>` si el cambio está relacionado con un issue. Esto permitirá que GitHub cierre automáticamente el issue al hacer el merge.
- El propietario del PR **debe asignarse** como responsable del pull request.
- El propietario del PR puede aprobar su propio pull request. Esta configuración busca agilizar el flujo de trabajo del equipo sin imponer rigidez innecesaria.

---

### 7. Realizar el merge en la rama `main` en GitHub
- Fusionar (_merge_ en inglés) el pull request en la rama `main`.
- Una vez realizado el merge, se **recomienda eliminar la rama asociada al cambio** (por ejemplo, una funcionalidad implementada o un archivo de documentación modificado, entre otros), ya que esto ayuda a evitar confusiones, mejora la organización y fomenta un flujo de desarrollo más ordenado.

#### Cómo eliminar una rama:
```
# Una vez realizado el merge en la rama main en GitHub, cambiar a la rama main en el repositorio local y
# actualizarla con los últimos cambios del repositorio remoto
git checkout main
git pull origin main

# Eliminar la rama localmente
git branch -D <nombre-rama> # Elimina la rama local junto con los cambios en ella

# Eliminar rama del repositorio remoto
git push origin --delete <nombre-rama>
```

> ⚠️ **Advertencia sobre la eliminación de una rama:** Si los cambios de una rama no han sido fusionados, se perderán al eliminarla.

---

### 8. Usar nombres descriptivos y consistentes
Mantener nombres descriptivos y consistentes en el issue, la rama y el pull request mejora la organización y la trazabilidad del trabajo. Por ejemplo, si la funcionalidad es una pantalla de agenda y el issue relacionado es el número 1, se recomienda utilizar:
- **Título del issue**: `Pantalla de agenda`
- **Nombre de la rama**: `feature/pantalla-agenda`
- **Mensaje del commit**: `feat: implementa pantalla de agenda (#1)`
- **Título del pull request**: `feat: implementa pantalla de agenda (#1)`
- **Descripción del pull request**: `closes #1`