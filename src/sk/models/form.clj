(ns sk.models.form
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn login-form
  [title href]
  (list
   [:div.container.border.w-25.bg-secondary
    [:legend title]
    [:form {:method "POST"
            :action href}
     (anti-forgery-field)
     [:div.form-group
      [:label {:for "username"} "Email:"]
      [:input.form-control {:id "username"
                            :name "username"
                            :type "email"
                            :required "true"
                            :oninvalid "this.setCustomValidity('Email es requerido...')"
                            :oninput "this.setCustomValidity('')"
                            :placeholder "Email aqui..."}]]
     [:div.form-group
      [:label {:for "password"} "Contraseña:"]
      [:input.form-control {:id "password"
                            :name "password"
                            :required "true"
                            :oninvalid "this.setCustomValidity('La contraseña es requerida...')"
                            :oninput "this.setCustomValidity('')"
                            :placeholder "Contraseña aqui..."
                            :type "Password"}]]
     [:input.btn.btn-primary {:type "submit"
                              :value "Ingresar al sitio"
                              :style "margin:2px;"}]]]))
;; Start form
(defn build-hidden-field
  "args:type,id,name,value"
  [args]
  [:input {:type "hidden"
           :id (:id args)
           :name (:name args)
           :value (:value args)}])

(defn build-field
  "args:label,type,id,name,placeholder,required,error,value"
  [args]
  (list
   [:div.form-group
    [:label {:for (:name args)} (:label args)]
    [:input.form-control {:type (:type args)
                          :id (:id args)
                          :name (:name args)
                          :placeholder (:placeholder args)
                          :required (:required args)
                          :oninvalid (str "this.setCustomValidity('" (:error args) "')")
                          :oninput "this.setCustomValidity('')"
                          :value (:value args)}]]))

(defn build-textarea
  "args:label,id,name,placeholder,required,error,value"
  [args]
  (list
   [:div.form-group
    [:label {:for (:name args)} (:label args)]
    [:textarea.form-control {:id (:id args)
                             :name (:name args)
                             :rows (:rows args)
                             :placeholder (:placeholder args)
                             :required (:required args)
                             :oninvalid (str "this.setCustomValidity('" (:error args) "')")
                             :oninput "this.setCustomValidity('')"} (:value args)]]))

(defn build-select
  "args:label,id,name,required,error"
  [args]
  (let [options (:options args)]
    (list
     [:div.form-group
      [:label {:for (:name args)} (:label args)]
      [:select.form-control {:id (:id args)
                             :name (:name args)
                             :required (:required args)
                             :oninvalid (str "this.setCustomValidity('" (:error args) "')")
                             :oninput "this.setCustomValidity('')"}
       (map (partial (fn [option]
                       (list
                        [:option {:value (:option option)
                                  :selected (if (= (:value args) (:option option)) true false)} (:label option)]))) (:options args))]])))

(defn build-radio
  [args]
  (let [options (:options args)]
    (list
     [:div.form-group
      [:label {:for "#"} (:label args)]
      (map (partial (fn [option]
                      (list
                       [:div.form-check
                        [:input.form-check-input {:type "radio"
                                                  :id (:id option)
                                                  :name (:name args)
                                                  :value (:value option)
                                                  :checked (if (= (:value args) (:value option)) true false)}
                         [:label {:for (:id option)} (:label option)]]]))) options)])))

(defn build-primary-input-button
  "args: type,value"
  [args]
  [:input.btn.btn-primary {:type (:type args)
                           :style "padding:5px;margin:5px;"
                           :value (:value args)}])

(defn build-secondary-input-button
  "args: type,value"
  [args]
  [:input.btn.btn-secondary {:type (:type args)
                             :style "padding:5px;margin:5px;"
                             :value (:value args)}])

(defn build-primary-anchor-button
  "args: label,href"
  [args]
  [:a.btn.btn-primary {:type "button"
                       :href (:href (:href args))} (:label args)])

(defn build-secondary-anchor-button
  "args: label,href"
  [args]
  [:a.btn.btn-secondary {:type "button"
                         :href (:href (:href args))} (:label args)])

(defn build-modal-buttons
  []
  (list
   [:input.btn.btn-primary {:type "submit"
                            :style "padding:5px;margin:5px;"
                            :value "Processar"}]
   [:button.btn.btn-secondary {:type "button"
                               :data-dismiss "modal"} "Cancelar"]))
(defn form
  [href fields buttons]
  (list
   [:div.container.border.bg-secondary
    [:form {:method "POST"
            :action href}
     (anti-forgery-field)
     fields
     buttons]]))
;; End form

(comment
  "Ejemplo de uso: value=mysql data"
  (build-radio {:label "Status"
                :name "active"
                :value "T"
                :options [{:id "activeT"
                           :label "Active"
                           :value "T"}
                          {:id "activeF"
                           :label "Inactive"
                           :value "F"}]})
  (build-select {:label "Level:"
                 :id "level"
                 :name "level"
                 :value "U"
                 :required "true"
                 :error "El nivel es requerido..."
                 :options [{:option ""
                            :label "Seleccionar nivel..."}
                           {:option "U"
                            :label "User"}
                           {:option "A"
                            :label "Admin"}
                           {:option "S"
                            :label "System"}]})
  (build-hidden-field {:id "id"
                       :name "id"
                       :value "El valor de la base de datos"})
  (build-textarea {:label "Comentarios"
                   :id "comentarios"
                   :name "comentarios"
                   :rows "3"
                   :placeholder "Comentarios aqui..."
                   :required "true"
                   :error "El comentario es requerido!"
                   :value "El perro loco de Mexicali!"})
  (let [args {:label "Apellido:"
              :type "text"
              :id "lastname"
              :name "lastname"
              :placeholder "Apellido paterno aqui..."
              :required "true"
              :error "El apellido paterno es requerido"
              :value "Valor de base de datos si aplica"}]
    (build-field args)))
