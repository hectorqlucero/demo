(ns sk.core
  (:require [compojure.core :refer [defroutes routes]]
            [compojure.route :as route]
            [noir.response :refer [redirect]]
            [noir.session :as session]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [sk.migrations :refer [config]]
            [sk.models.crud :refer [KEY]]
            [sk.routes.proutes :refer [proutes]]
            [sk.routes.routes :refer [open-routes]])
  (:gen-class))

(defn wrap-login [hdlr]
  (fn  [req]
    (try
      (if (nil? (session/get :user_id)) (redirect "home/login") (hdlr req))
      (catch Exception _
        {:status 400 :body "Unable to process your request!"}))))

(defn wrap-exception-handling [hdlr]
  (fn [req]
    (try
      (hdlr req)
      (catch Exception _
        {:status 400 :body "Invalid data"}))))

(defn wrap-public-routes [open-routes]
  (fn [pub-routes]
    (open-routes pub-routes)))

(defn wrap-private-routes [proutes]
  (fn [priv-routes]
    (proutes priv-routes)))

(defroutes app-routes
  (route/resources "/")
  (route/files (:path config) {:root (:uploads config)})
  (wrap-public-routes open-routes)
  (wrap-login (wrap-private-routes proutes))
  (route/not-found "Not Found"))

(def app
  (-> (routes #'app-routes)
      (wrap-exception-handling)
      (wrap-multipart-params)
      (wrap-session)
      (session/wrap-noir-session*)
      (wrap-defaults (-> site-defaults
                         (assoc-in [:security :anty-forgery] true)
                         (assoc-in [:session :store] (cookie-store {:key KEY}))
                         (assoc-in [:session :cookie-attrs] {:max-age 28800})
                         (assoc-in [:session :cookie-name] "LS")))))

(defn -main
  []
  (jetty/run-jetty app {:port (:port config)}))

(comment
  (:port config))
