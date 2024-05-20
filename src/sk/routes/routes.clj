(ns sk.routes.routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [sk.handlers.home.controller :as home-controller]))

(defroutes open-routes
  (GET "/" params home-controller/main)
  (GET "/home/login" params home-controller/login)
  (POST "/home/login" params home-controller/login-user)
  (GET "/home/logoff" params home-controller/logoff-user)
  (GET "/change/password" params home-controller/change-password)
  (POST "/change/password" req [] (home-controller/process-password req)))
