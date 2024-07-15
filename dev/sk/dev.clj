(ns sk.dev
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [sk.migrations :refer [config]]
            [sk.core :as core]))

(defn -main []
  (jetty/run-jetty (wrap-reload #'core/app) {:port (:port config)}))
