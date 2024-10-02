(ns sk.handlers.admin.categorias.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.handlers.admin.categorias.model :refer [carreras-options]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn categorias-view
  [title rows]
  (let [labels ["CARRERA" "CATEGORIA"]
        db-fields [:carrera_id_formatted :descripcion]
        fields (zipmap db-fields labels)
        table-id "categorias_table"
        args {:new true :edit true :delete true}
        href "/admin/categorias"]
    (build-grid title rows table-id fields href args)))

(defn build-categorias-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-select {:label "Carrera"
                  :id "carrera_id"
                  :name "carrera_id"
                  :required true
                  :error "La carrera es requerida..."
                  :value (:carrera_id row)
                  :options (carreras-options)})
   (build-field {:label "Categoria"
                 :type "text"
                 :id "descripcion"
                 :name "descripcion"
                 :placeholder "descripcion aqui..."
                 :required true
                 :value (:descripcion row)})))

(defn build-categorias-form
  [title row]
  (let [fields (build-categorias-fields row)
        href "/admin/categorias/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-categorias-modal
  [title row]
  (build-modal title row (build-categorias-form title row)))

(defn categorias-edit-view
  [title row rows]
  (list
   (categorias-view "categorias Manteniento" rows)
   (build-categorias-modal title row)))

(defn categorias-add-view
  [title row rows]
  (list
   (categorias-view "categorias Mantenimiento" rows)
   (build-categorias-modal title row)))

(defn categorias-modal-script
  []
  (modal-script))
