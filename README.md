# demo
Una libreria que genera una pagina web full stack

## Requisitos antes de Instalar
1. Java sdk instalado
2. MySQL instalado y configurado con una contraseña
3. Leiningen instalado. https://leiningen.org
4. Tu editor de preferencia que este configurado para codificar en clojure.
5. Si usas vim puedes bajar la configuracion para clojure.
    1. Dotfiles para vim aqui: https://githubcom/hectorqlucero/vim
    1. Necesitas instalar **vifm**
6. Recomiendo dbeaver-ce para administrar la base de datos.
    1. Lo puedes bajar aqui: https://dbeaver.io

## Instalación
1. En una terminal: git clone https://github.com/hectorqlucero/demo.git
2. Renombra el folder al nombre de tu gusto.
3. Configurar `project.clj`
4. Renombrar: `/resources/private/config_example.clj` a `/resources/private/config.clj`
5. Configurar: `/resources/private/config.clj`
6. Crear una base de datos con el mismo nombre de la configuracion **5.** Usando dbeaver o tu cliente de preferencia.
7. En el folder donde esta el codigo abrir una terminal y ejecutar: **lein run**
8. Abrir otra terminal en el folder donde esta el codigo y ejecutar: **lein repl**
9. Abrir otra terminal en el folder donde esta el codigo y ejecutar:
    1. **lein migrate** - Esto creara la tabla de **users**, lo puedes verificar en dbeaver.
    2. **lein database** - Esto creara tres usuarios temporaios.
        1. **Usuario**: user@gmail.com    **contraseña**: user
        2. **Usuario**: admin@gmail.com   **contraseña**: admin
        3. **Usuario**: sistema@gmail.com **contraseña**: sistema

## Usarlo cuando esta instalado
**En el browser de tu gusto**: http://localhost:3000 para correr la pagina

## Opciones
1. **lein migrate**: Crear una migracion a la base de datos
2. **lein database**: Crear usuarios temporarios
3. **lein rollback**: Regresar una migracion a la anterior.
4. **lein grid 'nombre de la tabla'**: Crear crud para una tabla ejemplo: **lein grid contactos** Nota: la tabla debe de existir en la base de datos.
    1. Crea un **data grid** con todas las funciones crud para una tabla.
        1. Crear un nuevo record
        2. Editar un record existente
        3. Borrar un record existente
5. **lein dashboard 'nombre de la tabla'**: Crear un 'dashboard' para una tabla ejemplo: **lein dashboard contactos** Nota: la tabla debe de existir en la base de datos.


## Ejemplo
`git clone https://github.com/hectorqlucero/demo.git`

`mv demo contactos`

`cd contactos`

`rm -rf .git`

$ ./tree-md .
# Arbol del proyecto

.
 * [CHANGELOG.md](./CHANGELOG.md)
 * [dev](./dev)
   * [sk](./dev/sk)
     * [dev.clj](./dev/sk/dev.clj)
 * [doc](./doc)
 * [LICENSE](/.LICENSE)
 * [project.clj](./project.clj)
 * [README.md](./README.md)
 * [resources](./resources)
   * [migrations](./resources/migrations)
     * [001-users.down.sql](./resources/migrations/001-users.down.sql)
     * [001-users.up.sql](./resources/migrations/001-users.up.sql)
   * [private](./resources/private)
     * [config_example.clj](./resources/private/config_example.clj)
   * [public](./resources/public)
     * [bootstrap5](./resources/public/bootstrap5)
     * [bootstrap-icons](./resources/public/bootstrap-icons)
     * [bootstrap-table-master](./resources/public/bootstrap-table-master)
     * [favicon.ico](./resources/public/favicon.ico)
     * [images](./resources/public/images)
     * [js](./resources/public/js)
 * [src](./src)
   * [sk](./src/sk)
     * [handlers](./src/sk/handlers)
       * [admin](./src/sk/handlers/admin)
         * [users](./src/sk/handlers/admin/users)
           * [controller.clj](./src/sk/handlers/admin/users/controller.clj)
           * [model.clj](./src/sk/handlers/admin/users/model.clj)
           * [view.clj](./src/sk/handlers/admin/users/view.clj)
       * [home](./src/sk/handlers/home)
         * [controller.clj](./src/sk/handlers/home/controller.clj)
         * [model.clj](./src/sk/handlers/home/model.clj)
         * [view.clj](./src/sk/handlers/home/view.clj)
       * [users](./src/sk/handlers/users)
         * [controller.clj](./src/sk/handlers/users/controller.clj)
         * [model.clj](./src/sk/handlers/users/model.clj)
         * [view.clj](./src/sk/handlers/users/view.clj)
     * [models](./src/sk/models)
     * [routes](./src/sk/routes)
     * [core.clj](./src/sk/core.clj)
     * [layout.clj](./src/sk/layout.clj)
     * [migrations.clj](./src/sk/migrations.clj)
 * [test](./test)

## License

Copyright © 2024 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
