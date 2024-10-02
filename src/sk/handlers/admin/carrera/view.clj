(ns sk.handlers.admin.carrera.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]
            [sk.models.grid :refer [build-grid build-modal modal-script]]))

(defn carrera-view
  [title rows]
  (let [labels ["DESCRIPCION" "ACTIVA"]
        db-fields [:descripcion :activa]
        fields (zipmap db-fields labels)
        table-id "carrera_table"
        args {:new true :edit true :delete true}
        href "/admin/carrera"]
    (build-grid title rows table-id fields href args)))

(defn build-carrera-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "Descripción"
                 :type "text"
                 :id "descripcion"
                 :name "descripcion"
                 :placeholder "descripcion aqui..."
                 :required false
                 :value (:descripcion row)})
   (build-textarea {:label "Parrafo #1"
                    :id "p1"
                    :name "p1"
                    :rows "6"
                    :placeholder "p1 aqui..."
                    :required false
                    :value (:p1 row)})
   (build-textarea {:label "Parrafor #2"
                    :id "p2"
                    :name "p2"
                    :rows "6"
                    :placeholder "Parrafo aqui..."
                    :required false
                    :value (:p2 row)})
   (build-textarea {:label "Parrafo #3"
                    :id "p3"
                    :name "p3"
                    :rows "6"
                    :placeholder "Parrafo aqui..."
                    :required false
                    :value (:p3 row)})
   (build-textarea {:label "Parrafo #4"
                    :id "p4"
                    :name "p4"
                    :rows "6"
                    :placeholder "Parrafo aqui..."
                    :required false
                    :value (:p4 row)})
   (build-textarea {:label "Nota"
                    :id "d1"
                    :name "d1"
                    :rows "3"
                    :placeholder "Nota aqui..."
                    :required false
                    :value (:d1 row)})
   (build-textarea {:label "Link"
                    :id "d2"
                    :name "d2"
                    :rows "3"
                    :placeholder "link aqui..."
                    :required false
                    :value (:d2 row)})
   (build-radio {:label "ACTIVA"
                 :name "activa"
                 :value (:activa row)
                 :options [{:id "activaS"
                            :label "Si"
                            :value "S"}
                           {:id "activaN"
                            :label "No"
                            :value "N"}]})))

(defn build-carrera-form
  [title row]
  (let [fields (build-carrera-fields row)
        href "/admin/carrera/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))

(defn build-carrera-modal
  [title row]
  (build-modal title row (build-carrera-form title row)))

(defn carrera-edit-view
  [title row rows]
  (list
   (carrera-view "carrera Manteniento" rows)
   (build-carrera-modal title row)))

(defn carrera-add-view
  [title row rows]
  (list
   (carrera-view "carrera Mantenimiento" rows)
   (build-carrera-modal title row)))

(defn carrera-modal-script
  []
  (modal-script))
