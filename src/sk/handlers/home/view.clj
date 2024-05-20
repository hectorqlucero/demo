(ns sk.handlers.home.view
  (:require [sk.models.form :refer [login-form password-form]]))

(defn main-view
  "This creates the login form and we are passing the title from the controller"
  [title]
  (let [href "/home/login"]
    (login-form title href)))

(defn change-password-view
  [title]
  (password-form title))
