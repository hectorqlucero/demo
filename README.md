# demo
Una libreria que genera una pagina web full stack

## Requisitos antes de Instalar
1. Java sdk instalado
2. MySQL instalado y configurado con una contraseña
3. Leiningen instalado. https://leiningen.org
4. Tu editor de preferencia que este configurado para codificar en clojure.
5. Si usas vim puedes bajar la configuracion para clojure.
    1. Dotfiles para vim aqui: https://github.com/hectorqlucero/vim
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


## Ejemplo - Crear una pagina de contactos
`git clone https://github.com/hectorqlucero/demo.git`

`mv demo contactos`

`cd contactos`

`rm -rf .git`

`git init`

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

`Crear una base de datos en tu cliente de MySQL contactos`

`Modificar defproject.clj aqui abajo`
```
Cambiar: :description "Demo" a :description "Contactos"
Cambiar: :uberjar-name "demo.jar" a :uberjar-name "contactos.jar"

(defproject sk "0.1.0"
  :description "Demo" ; Change me
  :url "http://example.com/FIXME" ; Change me
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.3"]
                 [org.clojure/data.csv "1.1.0"]
                 [compojure "1.7.1"]
                 [hiccup "1.0.5"]
                 [lib-noir "0.9.9"]
                 [com.draines/postal "2.0.5"]
                 [cheshire "5.13.0"]
                 [clj-pdf "2.6.8"]
                 [ondrs/barcode "0.1.0"]
                 [pdfkit-clj "0.1.7"]
                 [cljfmt "0.9.2"]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.15.2"]
                 [date-clj "1.0.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.clojure/data.codec "0.2.0"]
                 [mysql/mysql-connector-java "8.0.33"]
                 [ragtime "0.8.1"]
                 [ring/ring-core "1.12.2"]]
  :main ^:skip-aot sk.core
  :aot [sk.core]
  :plugins [[lein-ancient "0.7.0"]
            [lein-pprint "1.3.2"]]
  :uberjar-name "demo.jar" ; Change me
  :target-path "target/%s"
  :ring {:handler sk.core
         :auto-reload? true
         :auto-refresh? false}
  :resources-paths ["shared" "resources"]
  :aliases {"migrate" ["run" "-m" "sk.migrations/migrate"]
            "rollback" ["run" "-m" "sk.migrations/rollback"]
            "database" ["run" "-m" "sk.models.cdb/database"]
            "grid" ["run" "-m" "sk.models.builder/build-grid"]
            "dashboard" ["run" "-m" "sk.models.builder/build-dashboard"]}
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:source-paths ["src" "dev"]
                   :main sk.dev}})
```
`Renombrar resources/private/config_example.clj a config.clj`

```
Supongamos la contraseña de mi base de datos es: secreto
Cambiar :db-name "//localhost:3306/contactos?characterEncoding=UTF-8&serverTimezone=America/Los_Angeles"
Cambiar :database-url "mysql://localhost:3306/contactos?user=root&password=secreto&serverTimezone=America/Los_Angeles
Cambiar :db-pwd: "secreto"
Cambiar :site-name "Contactos"
Cambiar :company-name "Mi compañia"
Cambiar :uploads "./uploads/contactos/"

;; Replace all "xxxxx" with your configuration
{:db-protocol  "mysql"
 :db-name      "//localhost:3306/xxxxx?characterEncoding=UTF-8&serverTimezone=America/Los_Angeles" ; Change me
 :database-url "mysql://localhost:3306/xxxxx?user=root&password=xxxxx&serverTimezone=America/Los_Angeles" ; Change me
 :db-user      "root"
 :db-pwd       "xxxxx" ; Change me
 :db-class     "com.mysql.cj.jdbc.Driver"
 :email-host   "xxxxx" ; Optional
 :email-user   "xxxxx" ; Optional
 :email-pwd    "xxxxx" ; Optional
 :port         3000
 :tz           "US/Pacific"
 :site-name    "xxxxx" ; Change me
 :company-name "xxxxx" ; Change me
 :uploads      "./uploads/xxxxx/" ; Change me
 :base-url     "http://0.0.0.0:3000/"
 :img-url      "https://0.0.0.0/uploads/"
 :path         "/uploads/"}
```

`Abrir una terminal en folder del proyecto, Ejecutar: lein with-profile dev run`

`Abrir otra terminal en el folder del proyecto, Ejecutar: lein repl`

`Abrir otra terminal en el folder del proyecto, Ejecutar: lein migrate y despues alli mismo lein database`

## Ejecutar la pagina
http://localhost:3000
Cuando corra la pagina sin errores:
1. clic **Entrar al sitio**
2. En el campo Email:  **sistema@gmail.com**
3. En el campo Contraseña: **sistema**
4. Hacer clic en el boton Ingresar al sitio
    1. La pagina tiene un menu bootstrap5 con un Logo, Dashboard, Administrar y Salir [System User]

Si haces clic en Dashboard desplegara un reporte de usuarios.
Si haces clic en Administrar veras un menu con una opcion **Usuarios**, Si haces clic en **Usuarios**, veras un data crud grid para mantenimiento de usuarios.
Si haces clic en **Salir [System User]** te sacara de la pagina y tu sesion se destruira.

La pagina esta lista ahora para agregarle nuestro sistema de contactos...

## Ahora hay que crear nuestar pagina de contactos
Crear en resources/migrations/ -> resources/migrations/002-contactos.down.sql con el contenido:
```
drop table contactos;
```

Crear en resources/migrations/ - resources/migrations/002-contactos.up.sql con el contenido:
```
create table contactos (
  id int unsigned not null auto_increment primary key,
  nombre varchar(255),
  paterno varchar(255),
  materno varchar(255)
);
```
Ir a la terminal donde hiciste la primara migracion si esta habierta o habrir una nueva terminal en el folder del proyecto y ejecutar:
1. Crear la migracion de contactos: `lein migrate`
2. Crear un data grid de contactos: `lein grid contactos`
    1. Te dara un mensaje: Codigo generado en: src/sk/handlers/admin/contactos
        1. `controller.clj`
        2. `model.clj`
        3. `view.clj`
**Paginas generadas**:
Formatear la pagina src/sk/handlers/admin/contactos/**controller.clj**, la pagina se vera asi:
```
(ns sk.handlers.admin.contactos.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.contactos.model :refer [get-contactos get-contactos-id]]
            [sk.handlers.admin.contactos.view :refer [contactos-view contactos-edit-view contactos-add-view contactos-modal-script]]))

(defn contactos [_]
  (let [title "Contactos"
        ok (get-session-id)
        js nil
        rows (get-contactos)
        content (contactos-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn contactos-edit
  [id]
  (let [title "Modificar contactos"
        ok (get-session-id)
        js (contactos-modal-script)
        row (get-contactos-id  id)
        rows (get-contactos)
        content (contactos-edit-view title row rows)]
    (application title ok js content)))

(defn contactos-save
  [{params :params}]
  (let [table "contactos"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/contactos")
      (error-404 "No se pudo procesar el record!" "/admin/contactos"))))

(defn contactos-add
  [_]
  (let [title "Crear nuevo contactos"
        ok (get-session-id)
        js (contactos-modal-script)
        row nil
        rows (get-contactos)
        content (contactos-add-view title row rows)]
    (application title ok js content)))

(defn contactos-delete
  [id]
  (let [table "contactos"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/contactos")
      (error-404 "No se pudo procesar el record!" "/admin/contactos"))))
```
Formatear la pagina src/sk/handlers/admin/contactos/**model.clj**, la pagina se vera asi:
```
(ns sk.handlers.admin.contactos.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-contactos-sql
  (str
   "
SELECT *
FROM contactos
"))

(defn get-contactos
  []
  (Query db get-contactos-sql))

(def get-contactos-id-sql
  (str
   "
SELECT *
FROM contactos
WHERE id = ?
"))

(defn get-contactos-id
  [id]
  (first (Query db [get-contactos-id-sql id])))
```
Formatear la pagina src/sk/handlers/admin/contactos/**view.clj**, la pagina se vera asi:
```
(ns sk.handlers.admin.contactos.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn contactos-view
  [title rows]
  (let [labels ["NOMBRE" "PATERNO" "MATERNO"]
        db-fields [:nombre :paterno :materno]
        fields (zipmap db-fields labels)
        table-id "contactos_table"
        args {:new true :edit true :delete true}
        href "/admin/contactos"]
    (build-grid title rows table-id fields href args)))

(defn build-contactos-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "NOMBRE"
                 :type "text"
                 :id "nombre"
                 :name "nombre"
                 :placeholder "nombre aqui..."
                 :required false
                 :value (:nombre row)})
   (build-field {:label "PATERNO"
                 :type "text"
                 :id "paterno"
                 :name "paterno"
                 :placeholder "paterno aqui..."
                 :required false
                 :value (:paterno row)})
   (build-field {:label "MATERNO"
                 :type "text"
                 :id "materno"
                 :name "materno"
                 :placeholder "materno aqui..."
                 :required false
                 :value (:materno row)})))

(defn build-contactos-form
  [title row]
  (let [fields (build-contactos-fields row)
        href "/admin/contactos/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-contactos-modal
  [title row]
  (build-modal title row (build-contactos-form title row)))

(defn contactos-edit-view
  [title row rows]
  (list
   (contactos-view "contactos Manteniento" rows)
   (build-contactos-modal title row)))

(defn contactos-add-view
  [title row rows]
  (list
   (contactos-view "contactos Mantenimiento" rows)
   (build-contactos-modal title row)))

(defn contactos-modal-script
  []
  (modal-script))
```

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
