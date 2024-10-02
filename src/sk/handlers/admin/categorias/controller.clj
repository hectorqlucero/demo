(ns sk.handlers.admin.categorias.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.models.util :refer [get-session-id user-level]]
            [sk.models.crud :refer [build-form-save build-form-delete]]
            [sk.handlers.admin.categorias.model :refer [get-categorias get-categorias-id]]
            [sk.handlers.admin.categorias.view :refer [categorias-view categorias-edit-view categorias-add-view categorias-modal-script]]))

(defn categorias [_]
  (let [title "Categorias"
        ok (get-session-id)
        js nil
        rows (get-categorias)
        content (categorias-view title rows)]
    (if
     (or
      (= (user-level) "A")
      (= (user-level) "S"))
      (application title ok js content)
      (application title ok nil "Only <strong>los administrators </strong> can access this option!!!"))))

(defn categorias-edit
  [id]
  (let [title "Modificar categorias"
        ok (get-session-id)
        js (categorias-modal-script)
        row (get-categorias-id  id)
        rows (get-categorias)
        content (categorias-edit-view title row rows)]
    (application title ok js content)))

(defn categorias-save
  [{params :params}]
  (let [table "categorias"
        result (build-form-save params table)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/categorias")
      (error-404 "No se pudo procesar el record!" "/admin/categorias"))))

(defn categorias-add
  [_]
  (let [title "Crear nuevo categorias"
        ok (get-session-id)
        js (categorias-modal-script)
        row nil
        rows (get-categorias)
        content (categorias-add-view title row rows)]
    (application title ok js content)))

(defn categorias-delete
  [id]
  (let [table "categorias"
        result (build-form-delete table id)]
    (if (= result true)
      (error-404 "Record se processo correctamente!" "/admin/categorias")
      (error-404 "No se pudo procesar el record!" "/admin/categorias"))))
