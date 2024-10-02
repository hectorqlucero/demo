(ns sk.handlers.admin.mensajes.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.mensajes.model :refer [get-mensajes get-mensajes-id]]
            [sk.handlers.admin.mensajes.view :refer [mensajes-view mensajes-edit-view mensajes-add-view mensajes-modal-script]]))

(defn mensajes [_]
  (let [title "Mensajes"
        ok (get-session-id)
        js nil
        rows (get-mensajes)
        content (mensajes-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn mensajes-edit
  [id]
  (let [title "Modificar mensajes"
        ok (get-session-id)
        js (mensajes-modal-script)
        row (get-mensajes-id  id)
        rows (get-mensajes)
        content (mensajes-edit-view title row rows)]
    (application title ok js content)))

(defn mensajes-save
  [{params :params}]
  (let [table "mensajes"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/mensajes")
      (error-404 "No se pudo procesar el record!" "/admin/mensajes"))))

(defn mensajes-add
  [_]
  (let [title "Crear nuevo mensajes"
        ok (get-session-id)
        js (mensajes-modal-script)
        row nil
        rows (get-mensajes)
        content (mensajes-add-view title row rows)]
    (application title ok js content)))

(defn mensajes-delete
  [id]
  (let [table "mensajes"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/mensajes")
      (error-404 "No se pudo procesar el record!" "/admin/mensajes"))))
