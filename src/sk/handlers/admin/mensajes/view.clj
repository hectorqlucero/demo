(ns sk.handlers.admin.mensajes.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.handlers.admin.mensajes.model :refer [carreras-options]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn mensajes-view
  [title rows]
  (let [labels ["CARRERA" "ACTIVA?"]
        db-fields [:carrera_id_formatted :activa]
        fields (zipmap db-fields labels)
        table-id "mensajes_table"
        args {:new true :edit true :delete true}
        href "/admin/mensajes"]
    (build-grid title rows table-id fields href args)))

(defn build-mensajes-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-select {:label "Carrera"
                  :id "carrera_id"
                  :name "carrera_id"
                  :required true
                  :value (:carrera_id row)
                  :options (carreras-options)})
   (build-textarea {:label "Registrar Mensaje"
                    :id "registrar_mensaje"
                    :name "registrar_mensaje"
                    :rows "6"
                    :placeholder "registrar_mensaje aqui..."
                    :required false
                    :value (:registrar_mensaje row)})
   (build-textarea {:label "Correo Mensaje"
                    :id "correo_mensaje"
                    :name "correo_mensaje"
                    :rows "18"
                    :placeholder "correo_mensaje aqui..."
                    :required false
                    :value (:correo_mensaje row)})))

(defn build-mensajes-form
  [title row]
  (let [fields (build-mensajes-fields row)
        href "/admin/mensajes/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-mensajes-modal
  [title row]
  (build-modal title row (build-mensajes-form title row)))

(defn mensajes-edit-view
  [title row rows]
  (list
   (mensajes-view "mensajes Manteniento" rows)
   (build-mensajes-modal title row)))

(defn mensajes-add-view
  [title row rows]
  (list
   (mensajes-view "mensajes Mantenimiento" rows)
   (build-mensajes-modal title row)))

(defn mensajes-modal-script
  []
  (modal-script))
