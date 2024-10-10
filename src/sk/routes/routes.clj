(ns sk.routes.routes
  (:require [compojure.core :refer [defroutes GET POST]]
            [sk.handlers.home.controller :as home-controller]
            [sk.handlers.eventos.controller :as eventos-dashboard]
            [sk.handlers.registro.controller :as registro-dashboard]
            [sk.handlers.registered.controller :as registered-dashboard]))

(defroutes open-routes
  (GET "/" params home-controller/main)
  (GET "/home/login" params home-controller/login)
  (POST "/home/login" params home-controller/login-user)
  (GET "/home/logoff" params home-controller/logoff-user)
  (GET "/change/password" params home-controller/change-password)
  (POST "/change/password" req [] (home-controller/process-password req))

  (GET "/eventos/list" request [] (eventos-dashboard/eventos request))
  (GET "/eventos/list/:year/:month" [year month] (eventos-dashboard/display-eventos year month))

  ;; Start registro
  (GET "/registro" request [] (registro-dashboard/registro request))
  (GET "/registrar/:carrera_id" [carrera_id] (registro-dashboard/registrar carrera_id))
  (POST "/registrar/save" request [] (registro-dashboard/registrar-save request))
  ;; End registro

  ;; Start display registered
  (GET "/display/oregistered" req [] (registered-dashboard/oregistrados req))
  (GET "/display/oregistered/:carrera_id" [carrera_id] (registered-dashboard/oregistered carrera_id))
  (GET "/update/number/:carrera_id" [carrera_id] (registered-dashboard/update-number carrera_id))
  (POST "/update/number" req [] (registered-dashboard/!update-number req))
  (GET "/imprimir/cert" req [] (registered-dashboard/imprimir-cert req))
  (POST "/imprimir/cert" req [] (registered-dashboard/imprimir-cert-download req))
  ;; End display registered
  )
