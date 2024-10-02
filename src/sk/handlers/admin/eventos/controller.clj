(ns sk.handlers.admin.eventos.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.eventos.model :refer [get-eventos get-eventos-id]]
            [sk.handlers.admin.eventos.view :refer [eventos-view eventos-edit-view eventos-add-view eventos-modal-script]]))

(defn eventos [_]
  (let [title "Eventos"
        ok (get-session-id)
        js nil
        rows (get-eventos)
        content (eventos-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn eventos-edit
  [id]
  (let [title "Modificar eventos"
        ok (get-session-id)
        js (eventos-modal-script)
        row (get-eventos-id  id)
        rows (get-eventos)
        content (eventos-edit-view title row rows)]
    (application title ok js content)))

(defn eventos-save
  [{params :params}]
  (let [table "eventos"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/eventos")
      (error-404 "No se pudo procesar el record!" "/admin/eventos"))))

(defn eventos-add
  [_]
  (let [title "Crear nuevo eventos"
        ok (get-session-id)
        js (eventos-modal-script)
        row nil
        rows (get-eventos)
        content (eventos-add-view title row rows)]
    (application title ok js content)))

(defn eventos-delete
  [id]
  (let [table "eventos"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/eventos")
      (error-404 "No se pudo procesar el record!" "/admin/eventos"))))
