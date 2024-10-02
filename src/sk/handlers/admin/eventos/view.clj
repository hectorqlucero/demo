(ns sk.handlers.admin.eventos.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form
                                    build-hidden-field
                                    build-image-field
                                    build-image-field-script
                                    build-field
                                    build-select
                                    build-radio
                                    build-modal-buttons
                                    build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn eventos-view
  [title rows]
  (let [labels ["TITULO" "DETALLES" "LUGAR" "FECHA" "HORA" "ORGANIZA" "IMAGEN"]
        db-fields [:titulo :detalles :lugar :fecha :hora :organiza :imagen]
        fields (zipmap db-fields labels)
        table-id "eventos_table"
        args {:new true :edit true :delete true}
        href "/admin/eventos"]
    (build-grid title rows table-id fields href args)))

(defn build-eventos-fields
  [row]
  (list
   (build-image-field row)
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "TITULO"
                 :type "text"
                 :id "titulo"
                 :name "titulo"
                 :placeholder "titulo aqui..."
                 :required false
                 :value (:titulo row)})
   (build-textarea {:label "DETALLES"
                    :id "detalles"
                    :name "detalles"
                    :rows "6"
                    :placeholder "detalles aqui..."
                    :required false
                    :value (:detalles row)})
   (build-textarea {:label "LUGAR"
                    :id "lugar"
                    :name "lugar"
                    :rows "3"
                    :placeholder "lugar aqui..."
                    :required false
                    :value (:lugar row)})
   (build-field {:label "FECHA"
                 :type "date"
                 :id "fecha"
                 :name "fecha"
                 :required false
                 :value (:fecha row)})
   (build-field {:label "HORA"
                 :type "time"
                 :id "hora"
                 :name "hora"
                 :placeholder "hora aqui..."
                 :required false
                 :value (:hora row)})
   (build-field {:label "ORGANIZA"
                 :type "text"
                 :id "organiza"
                 :name "organiza"
                 :placeholder "organiza aqui..."
                 :required false
                 :value (:organiza row)})))

(defn build-eventos-form
  [title row]
  (let [fields (build-eventos-fields row)
        href "/admin/eventos/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-eventos-modal
  [title row]
  (build-modal title row (build-eventos-form title row)))

(defn eventos-edit-view
  [title row rows]
  (list
   (eventos-view "eventos Manteniento" rows)
   (build-eventos-modal title row)))

(defn eventos-add-view
  [title row rows]
  (list
   (eventos-view "eventos Mantenimiento" rows)
   (build-eventos-modal title row)))

(defn eventos-modal-script
  []
  (list
   (modal-script)
   (build-image-field-script)))
