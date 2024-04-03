(ns sk.handlers.admin.users.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [form
                                    build-hidden-field
                                    build-field
                                    build-select
                                    build-radio
                                    build-modal-buttons]]
            [sk.models.grid :refer [build-grid
                                    build-modal
                                    modal-script]]))

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
        fields (zipmap db-fields labels)
        href "/admin/users"]
    (build-grid title rows table-id fields href)))

;; Start users-form
(defn build-users-fields
  [row]
  (list
   (build-hidden-field {:id "id"
                        :name "id"
                        :value (:id row)})
   (build-field {:label "Apellido Paterno:"
                 :type "text"
                 :id "lastname"
                 :name "lastname"
                 :required "true"
                 :error "El apellido paterno es un campo requerido..."
                 :placeholder "Apellido paterno aqui..."
                 :value (:lastname row)})
   (build-field {:label "Nombre:"
                 :type "text"
                 :id "firstname"
                 :name "firstname"
                 :required "true"
                 :error "El nombre es un campo requrerido..."
                 :placeholder "El nombre aqui..."
                 :value (:firstname row)})
   (build-field {:label "Usuario:"
                 :type "email"
                 :id "username"
                 :name "username"
                 :required "true"
                 :error "El usuario es un campo requerido..."
                 :placeholder "El email del usuario aqui..."
                 :value (:username row)})
   (build-field {:label "Fecha de nacimiento:"
                 :type "date"
                 :id "dob"
                 :name "dob"
                 :required "false"
                 :error nil
                 :value (:dob row)})
   (build-field {:label "Celular:"
                 :type "text"
                 :id "cell"
                 :name "cell"
                 :required "false"
                 :error nil
                 :value (:cell row)})
   (build-select {:label "Nivel de Usuario:"
                  :id "level"
                  :name "level"
                  :required "true"
                  :error "El nivel es un campo requerido..."
                  :value (:level row)
                  :options [{:option ""
                             :label "Seleccionar nivel..."}
                            {:option "U"
                             :label "Usuario"}
                            {:option "A"
                             :label "Administrador"}
                            {:option "S"
                             :label "Sistema"}]})
   (build-radio {:label "Estado:"
                 :name "active"
                 :value (:active row)
                 :options [{:id "activeT"
                            :label "Activo"
                            :value "T"}
                           {:id "activeF"
                            :label "Inactivo"
                            :value "F"}]})))

(defn build-users-form
  [title row]
  (let [fields (build-users-fields row)
        href "/admin/users/save"
        buttons (build-modal-buttons)]
    (form href fields buttons)))
;; End users-form

(defn build-users-modal
  [title row]
  (build-modal title row (build-users-form title row)))

(defn users-edit-view
  [title row rows]
  (list
   (users-view "Users Maintenance" rows)
   (build-users-modal title row)))

(defn users-add-view
  [title row rows]
  (list
   (users-view "Users Maintenance" rows)
   (build-users-modal title row)))

(defn users-modal-script
  []
  (modal-script))
