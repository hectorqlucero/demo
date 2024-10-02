(ns sk.handlers.admin.carrera.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.carrera.model :refer [get-carrera get-carrera-id]]
            [sk.handlers.admin.carrera.view :refer [carrera-view carrera-edit-view carrera-add-view carrera-modal-script]]))

(defn carrera [_]
  (let [title "Carrera"
        ok (get-session-id)
        js nil
        rows (get-carrera)
        content (carrera-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn carrera-edit
  [id]
  (let [title "Modificar carrera"
        ok (get-session-id)
        js (carrera-modal-script)
        row (get-carrera-id  id)
        rows (get-carrera)
        content (carrera-edit-view title row rows)]
    (application title ok js content)))

(defn carrera-save
  [{params :params}]
  (let [table "carrera"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/carrera")
      (error-404 "No se pudo procesar el record!" "/admin/carrera"))))

(defn carrera-add
  [_]
  (let [title "Crear nuevo carrera"
        ok (get-session-id)
        js (carrera-modal-script)
        row nil
        rows (get-carrera)
        content (carrera-add-view title row rows)]
    (application title ok js content)))

(defn carrera-delete
  [id]
  (let [table "carrera"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/carrera")
      (error-404 "No se pudo procesar el record!" "/admin/carrera"))))
