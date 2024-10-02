(ns sk.handlers.registro.controller
  (:require [sk.models.util :refer [get-session-id]]
            [sk.migrations :refer [config]]
            [sk.models.crud :refer [build-postvars
                                    crud-fix-id
                                    Query
                                    db
                                    Save]]
            [sk.handlers.registro.model :refer [get-active-carrera-name
                                                registrar-mensaje
                                                correo-mensaje]]
            [cheshire.core :refer [generate-string]]
            [sk.layout :refer [application
                               error-404]]
            [pdfkit-clj.core :refer [gen-pdf]]
            [sk.models.email :refer [host
                                     send-email]]
            [sk.handlers.registro.view :refer [registro-view
                                               registrar-view
                                               build-html
                                               ; registrar-view-scripts 
                                               ]]))

(defn registro [_]
  (let [title "Registro de Paseos/Carreras"
        ok (get-session-id)
        content (registro-view)]
    (application title ok nil content)))

(defn registrar
  [carrera-id]
  (let [title (get-active-carrera-name carrera-id)
        ok -1
        content (registrar-view title carrera-id)]
    (application title ok nil content)))

;; start registrar-save
(defn email-body
  "Crear el cuerpo del correo electronico"
  [params]
  (try
    (let [nombre (str (:nombre params) " " (:apell_paterno params) " " (:apell_materno params))
          carrera_id (:carrera_id params)
          domicilio (:direccion params)
          telefono (:telefono params)
          email (:email params)
          categoria_id (:categoria_id params)
          categoria (:descripcion (first (Query db ["select descripcion from categorias where id = ?" categoria_id])))
          carrera (:descripcion (first (Query db ["select * from carrera where id = ?" carrera_id])))
          subject (str "Nuevo Registro - " carrera)
          content (str "<strong>Hola Marco</strong>,</br></br>"
                       "Mis datos son los siguientes:</br></br>"
                       "<strong>Nombre:</strong> " nombre "</br></br>"
                       "<strong>Domicilio:</strong> " domicilio "</br></br>"
                       "<strong>Telefono:</strong> " telefono "</br></br>"
                       "<strong>Email:</strong> " email "</br></br>"
                       "<strong>Categoria:</strong> " categoria)
          body {:from "ciclismobc@fastmail.com"
                :to "marcopescador@hotmail.com"
                :cc "hectorqlucero@gmail.com"
                :subject subject
                :body [{:type "text/html;charset=utf-8"
                        :content content}]}]
      body)
    (catch Exception e (.getMessage e))))

(defn corredor-email-body
  "Crear el cuerpo del correo electronico de confirmacion at corredor"
  [params id]
  (try
    (let [nombre (str (:nombre params) " " (:apell_paterno params) " " (:apell_materno params))
          email (:email params)
          carrera_id (:carrera_id params)
          subject (str "Nuevo Registro - " (get-active-carrera-name carrera_id))
          content (str "<strong>Hola</strong> " nombre ",<br/>" (correo-mensaje carrera_id))
          body {:from "ciclismobc@fastmail.com"
                :to email
                :cd "hectorqlucero@gmail.com"
                :subject subject
                :body [{:type "text/html;charset=utf-8"
                        :content content}
                       {:type :inline
                        :content (:path (bean (gen-pdf (build-html id))))
                        :content-type "application/pdf"}]}]
      body)
    (catch Exception e (.getMessage e))))

(defn registrar-save
  [{params :params}]
  (let [table "carreras"
        id (crud-fix-id (:id params))
        carrera_id (:carrera_id params)
        email-body (email-body params)
        postvars (build-postvars table params)
        result (Save db (keyword table) postvars ["id = ?" id])
        corredor-email-body (corredor-email-body params (:generated_key (first result)))]
    (if (seq result)
      (do
        (future (send-email host email-body))
        (future (send-email host corredor-email-body))
        (error-404 "Registro exitoso! Puede cerrar pestaña" "<a href='#' onclick='close_window();return;false;>Continuar</a>"))
      (error-404 "Registro fallido! Puede cerrar pestaña" "<a href='#' onclick='close_window();return;false;>Continuar</a>"))))
;; end registrar-save

(comment
  (build-html 1)
  (registrar 4))
