(ns sk.handlers.admin.users.controller
  (:require [sk.layout :refer [application
                               error-404]]
            [sk.models.util :refer [get-session-id]]
            [sk.models.crud :refer [build-form-save
                                    build-form-delete]]
            [sk.handlers.admin.users.model :refer [get-users
                                                   get-user]]
            [sk.handlers.admin.users.view :refer [users-view
                                                  users-edit-view
                                                  users-add-view
                                                  users-modal-script]]))

(defn users
  [_]
  (let [title "Mantenimiento de Usuarios"
        ok (get-session-id)
        js nil
        rows (get-users)
        content (users-view title rows)]
    (application title ok js content)))

(defn users-edit
  [id]
  (let [title "Modificar Usuario"
        ok (get-session-id)
        js (users-modal-script)
        row (get-user id)
        rows (get-users)
        content (users-edit-view title row rows)]
    (application title ok js content)))

(defn users-save
  [{params :params}]
  (let [table "users"
        result (build-form-save params table)]
    (if (= result true)
      (error-404  "Record se processo correctamente!" "/admin/users")
      (error-404 "No se pudo procesar el record!" "/admin/users"))))

(defn users-add
  [_]
  (let [title "Nuevo Usuario"
        ok (get-session-id)
        js (users-modal-script)
        row nil
        rows (get-users)
        content (users-add-view title row rows)]
    (application title ok js content)))

(defn users-delete
  [id]
  (let [table "users"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/users")
      (error-404 "No se pudo processar el record!" "/admin/users"))))
