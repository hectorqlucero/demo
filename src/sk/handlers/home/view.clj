(ns sk.handlers.home.view
  (:require [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn main-view
  "This creates the login form and we are passing the title from the controller"
  [title]
  (list
   [:form {:method "POST"}
    (anti-forgery-field)
    [:div.form-group
     [:label {:for "username"} "Username(email):"]
     [:input.form-control {:id "username"
                           :name "username"
                           :type "text"
                           :placeholder "Enter your email here please..."}]]
    [:div.form-group
     [:label {:for "password"} "Password"]
     [:input.form-control {:id "password"
                           :name "password"
                           :placeholder "Enter password here..."
                           :type "password"}]
     [:button.btn.btn-primary {:type "submit"} "Submit"]]]))
