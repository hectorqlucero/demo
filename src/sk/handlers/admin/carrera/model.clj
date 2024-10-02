(ns sk.handlers.admin.carrera.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

(def get-carrera-sql
  (str
   "
SELECT *
FROM carrera
"))

(defn get-carrera
  []
  (Query db get-carrera-sql))

(def get-carrera-id-sql
  (str
   "
SELECT *
FROM carrera
WHERE id = ?
"))

(defn get-carrera-id
  [id]
  (first (Query db [get-carrera-id-sql id])))
