(ns sk.handlers.admin.users.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

;; Start get-users
(def get-users-sql
  (str
   "
    SELECT *,
    DATE_FORMAT(dob,'%d/%m/%Y') as dob_formatted,
    CASE
    WHEN level = 'U' THEN 'Usuario'
    WHEN level = 'A' THEN 'Administrador'
    ELSE 'Sistema'
    END level_formatted,
    CASE
    WHEN active = 'T' THEN 'Activo'
    ELSE 'Inactivo'
    END active_formatted
    FROM users
    ORDER BY lastname,firstname
    "))

(defn get-users
  []
  (Query db get-users-sql))
;; End get-users

;; Start get-user
(def get-user-sql
  (str
   "
    SELECT *,
    DATE_FORMAT(dob,'%d/%m/%Y') as dob_formatted
    FROM users
    WHERE id = ?
    ORDER BY lastname,firstname
    "))

(defn get-user
  [id]
  (first (Query db [get-user-sql id])))
;; End get-user

(comment
  (get-user 2)
  (get-users))
