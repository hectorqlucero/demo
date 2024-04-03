(ns sk.handlers.users.view
  (:require [sk.models.grid :refer [build-dashboard]]))

(defn users-view
  [title rows]
  (let [table-id "users_table"
        labels ["apellido paterno"
                "nombre"
                "usuario"
                "fecha de nacimiento"
                "celular"
                "nivel"
                "status"]
        db-fields [:lastname
                   :firstname
                   :username
                   :dob_formatted
                   :cell
                   :level_formatted
                   :active_formatted]
        fields (zipmap db-fields labels)]
    (build-dashboard title rows table-id fields)))

(comment
  (users-view "Usuarios" nil))
