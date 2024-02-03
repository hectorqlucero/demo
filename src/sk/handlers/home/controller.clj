(ns sk.handlers.home.controller
  (:require [sk.layout :refer [application error-404]]
            [sk.handlers.home.view :refer [main-view]]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [noir.util.crypt :as crypt]
            [sk.handlers.home.model :refer [get-user get-users]]
            [sk.models.util :refer [get-session-id]]))

(defn main
  [_]
  (let [title "home"
        ok (get-session-id)
        js nil
        content [:h2.text-primary "Click on login to continue..."]]
    (application title ok js content)))

(defn login
  [_]
  (let [title "Login"
        ok (get-session-id)
        js nil
        content (main-view title)]
    (application title ok js content)))

(defn login-user
  [{params :params}]
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
      (error-404 "User is not active!" "/"))))

(defn logoff-user
  [_]
  (session/clear!)
  (error-404 "Logoff successfully" "/"))

(comment
  (:username (first (get-users))))
