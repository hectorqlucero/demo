(ns sk.handlers.admin.categorias.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-categorias-sql
  (str
   "
SELECT categorias.*,
carrera.descripcion as carrera_id_formatted
FROM categorias
JOIN carrera on carrera.id = categorias.carrera_id 
ORDER BY categorias.descripcion
"))

(defn get-categorias
  []
  (Query db get-categorias-sql))

(def get-categorias-id-sql
  (str
   "
SELECT *
FROM categorias
WHERE id = ?
"))

(defn carreras-options
  []
  (let [rows (Query db ["select id as value,CONCAT(descripcion,' - ',activa) as label from carrera order by descripcion"])]
    (list* {:id "" :label "Seleccionar carrera..."} rows)))

(defn get-categorias-id
  [id]
  (carreras-options)
  (first (Query db [get-categorias-id-sql id])))

(comment
  (get-categorias))
