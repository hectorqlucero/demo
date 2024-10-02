(ns sk.handlers.admin.eventos.model
  (:require [sk.models.crud :refer [Query db]]
            [sk.models.util :refer [current_year]]
            [sk.migrations :refer [config]]
            [clojure.string :as st]))

(def get-eventos-sql
  (str
   "
SELECT *
FROM eventos
WHERE YEAR(fecha) >= " (current_year) "
"))

(defn get-eventos
  []
  (let [trows (Query db get-eventos-sql)
        rows (map (fn [row]
                    (let [img-url (str (:img-url config) (:imagen row))
                          img-url (str "<img src=" \" img-url "\" width='95' height='71' alt='evento'>")]
                      (assoc row :imagen img-url))) trows)]
    rows))

(def get-eventos-id-sql
  (str
   "
SELECT *
FROM eventos
WHERE id = ?
"))

(defn get-eventos-id
  [id]
  (first (Query db [get-eventos-id-sql id])))

(comment
  (get-eventos)
  (current_year))
