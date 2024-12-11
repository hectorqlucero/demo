(ns sk.handlers.home.controller
  (:require
   [noir.response :refer [redirect]]
   [noir.session :as session]
   [noir.util.crypt :as crypt]
   [sk.models.crud :refer [safe-try]]
   [sk.handlers.home.model :refer [get-user get-users update-password]]
   [sk.handlers.home.view :refer [change-password-view main-view]]
   [sk.layout :refer [application error-404]]
   [sk.models.util :refer [get-session-id]]))

(defn main
  [_]
  (safe-try
   (let [title "home"
         ok (get-session-id)
         js nil
         content (if (> (get-session-id) 0)
                   nil
                   [:h2.text-primary "clic en Entrar al sitio!"])]
     (application title ok js content))))

(defn login
  [_]
  (safe-try
   (let [title "Entrar al sitio"
         ok (get-session-id)
         js nil
         content (main-view title)]
     (application title ok js content))))

(defn login-user
  [{params :params}]
  (safe-try
   (let [username (:username params)
         password (:password params)
         row (first (get-user username))
         active (:active row)]
     (if (= active "T")
       (if (crypt/compare password (:password row))
         (do
           (session/put! :user_id (:id row))
           (redirect "/"))
         (error-404 "Incorrect Username and or Password!" "/"))
       (error-404 "User is not active!" "/")))))

(defn change-password
  [_]
  (safe-try
   (let [title "Cambiar Contraseña"
         ok (get-session-id)
         js nil
         content (change-password-view title)]
     (application title ok js content))))

(defn process-password
  [{params :params}]
  (safe-try
   (let [username (:email params)
         password (crypt/encrypt (:password params))
         row (first (get-user username))
         active (:active row)]
     (if (= active "T")
       (if (seq (update-password username password))
         (error-404 "Su contraseña se cambio correctamente!" "/home/login")
         (error-404 "No se pudo cambiar su contraseña" "/home/login"))
       (error-404 "No se pudo cambiar su contraseña!" "/home/login")))))

(defn logoff-user
  [_]
  (safe-try
   (session/clear!))
  (error-404 "Logoff successfully" "/"))

(comment
  (:username (first (get-users))))
