(ns sk.handlers.users.view
  (:require [sk.models.grid :refer [build-dashboard]]))

(defn users-view
  [title rows]
  (let [fields ["APELLIDO PATERNO" "NOMBRE" "USUARIO" "FECHA DE NACIMIENTO" "CELULAR" "NIVEL" "STATUS"]
        db-fields [:lastname :firstname :username :dob_formatted :cell :level_formatted :active_formatted]
        href "/users"
        search-placeholder "Buscar aqui..."
        search-button "Buscar"
        all-button "Todos"]
    (build-dashboard title rows fields db-fields href search-placeholder search-button all-button)))

(comment
  (users-view "Usuarios" nil))
