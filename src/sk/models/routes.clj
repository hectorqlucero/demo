(ns sk.models.routes
  (:require [clojure.java.io :as io]
            [sk.models.crud :refer [Query db]]))

(def proutes-path
  (io/resource "proutes/"))

(def routes-source
  "src/sk/routes/")

(defn proutes-resource
  "Lee el contenido de un archivo del directorio resources/proutes/"
  [file]
  (slurp (str proutes-path file)))

(defn build-grid-defroutes
  "Genera rutas especificas para el grid para una tabla y se la agrega a defroutes.txt"
  [table]
  (let [data (str
              "  (GET \"/admin/" table "\" params [] (" table "-controller/" table " params))\n"
              "  (GET \"/admin/" table "/edit/:id\" [id] (" table "-controller/" table "-edit id))\n"
              "  (POST \"/admin/" table "/save\" params [] (" table "-controller/" table "-save params))\n"
              "  (GET \"/admin/" table "/add\" params [] (" table "-controller/" table "-add params))\n"
              "  (GET \"/admin/" table "/delete/:id\" [id] (" table "-controller/" table "-delete id))\n")]
    (spit (str proutes-path "defroutes.txt") data :append true)))

(defn build-defroutes
  "Genera rutas especificas para un dashboard para una tabla y le agrega a defroutes.txt"
  [table]
  (let [data (str
              "  (GET \"/admin/" table "\" params [] (" table "-controller/" table " params))\n"
              "  (GET \"/admin/" table "/edit/:id\" [id] (" table "-controller/" table "-edit id))\n"
              "  (POST \"/admin/" table "/save\" params [] (" table "-controller/" table "-save params))\n"
              "  (GET \"/admin/" table "/add\" params [] (" table "-controller/" table "-add params))\n"
              "  (GET \"/admin/" table "/delete/:id\" [id] (" table "-controller/" table "-delete id))\n"
              "  (GET \"/" table "\" params [] (" table "-dashboard/" table " params))\n")]
    (spit (str proutes-path "defroutes.txt") data :append true)))

(defn build-grid-require
  "Genera archivos requeridos para un grid para una tabla y le agrega a require.txt"
  [table]
  (let [data (str
              "    [sk.handlers.admin." table ".controller :as " table "-controller]\n")]
    (spit (str proutes-path "require.txt") data :append true)))

(defn build-require
  "Genera archivos requeridos para un dashboard para una tabla y le agrega a require.txt"
  [table]
  (let [data (str
              "    [sk.handlers.admin." table ".controller :as " table "-controller]\n"
              "    [sk.handlers." table ".controller :as " table "-dashboard]\n")]
    (spit (str proutes-path "require.txt") data :append true)))

(defn build-proutes
  "Crea el namespace proutes completo combinando require y defroutes contenido"
  []
  (str
   "(ns sk.routes.proutes\n"
   "  (:require\n"
   "    [compojure.core :refer [defroutes GET POST]]\n"
   (proutes-resource "require.txt")
   "  )\n"
   ")\n\n"
   "(defroutes proutes\n"
   (proutes-resource "defroutes.txt")
   ")"))

(defn prune-tables
  "Remueve tablas que no necesitamos de la lista de tablas"
  [tables-vector]
  (remove #{"BASE TABLE" "ragtime_migrations"} tables-vector))

(defn fetch-tables
  "Regresa una lista de tablas de la base de datos y filtra las tablas que no necesitamos"
  []
  (let [data (Query db "show full tables where Table_Type = 'BASE TABLE'")
        tables (map (fn [row] (vals row)) data)]
    (prune-tables (vec (flatten tables)))))

(defn process-grid-routes
  "Procesa y genera rutas especificas para grids para todas las tablas"
  []
  (let [tables (fetch-tables)]
    (spit (str proutes-path "require.txt") "")      ;limpiar archivo para recrear rutas
    (spit (str proutes-path "defroutes.txt") "")    ;limpiar archivo para recrear rutas
    (doseq [table tables]                           ;usando doseq para crear side-effects no regresa una collection
      (build-grid-require table)
      (build-grid-defroutes table))
    (spit (str routes-source "proutes.clj") (build-proutes))))  ;escribir el archivo proutes

(defn process-dashboard-routes
  "Procesa y genera rutas especificas para dashboards para todas las tablas"
  []
  (let [tables (fetch-tables)]
    (spit (str proutes-path "require.txt") "")      ;limpiar archivo para recrear rutas
    (spit (str proutes-path "defroutes.txt") "")    ;limpiar archivo para recrear rutas
    (doseq [table tables]                           ;usando doseq para crear side-effects no regresa una collection
      (build-require table)
      (build-defroutes table))
    (spit (str routes-source "proutes.clj") (build-proutes))))  ;escribir el archivo proutes

(comment
  (process-grid-routes)
  (process-dashboard-routes)
  (proutes-resource "require.txt")
  (proutes-resource "defroutes.txt"))