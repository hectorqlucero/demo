(ns sk.handlers.admin.mensajes.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-mensajes-sql
  (str
   "
SELECT mensajes.*,
carrera.descripcion as carrera_id_formatted,
CASE
   WHEN carrera.activa = 'S' THEN 'Si'
   WHEN carrera.activa = 'N' THEN 'No'
END AS activa
FROM mensajes
JOIN carrera on carrera.id = mensajes.carrera_id
ORDER BY carrera.descripcion
"))

(defn get-mensajes
  []
  (Query db get-mensajes-sql))

(def get-mensajes-id-sql
  (str
   "
SELECT *
FROM mensajes
WHERE id = ?
"))

(defn get-mensajes-id
  [id]
  (first (Query db [get-mensajes-id-sql id])))

(defn carreras-options
  []
  (let [rows (Query db ["select id as value,CONCAT(descripcion,' - ',activa) as label from carrera order by descripcion"])]
    (list* {:id "" :label "Seleccionar carrera..."} rows)))

(comment
  (get-mensajes))
