(ns sk.handlers.admin.users.model
  (:require [sk.models.crud :refer [Query db]]
            [clojure.string :as st]))

;; Start get-users
(def get-users-sql
  (str
   "
    SELECT *,
    DATE_FORMAT(dob,'%d de %M %Y','es_ES') as dob_formatted,
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
    DATE_FORMAT(dob,'%Y-%m-%d') as dob_formatted
    FROM users
    WHERE id = ?
    ORDER BY lastname,firstname
    "))

(defn get-user
  [id]
  (first (Query db [get-user-sql id])))
;; End get-user

;; Start search-user
(defn get-users-search-sql
  [search]
  (str
   "
  SELECT *,
   CASE 
   WHEN level = \"U\" THEN \"Usuario\"
   WHEN level = \"A\" THEN \"Administrador\"
   ELSE \"Sistema\"
   END level_formatted,
   CASE
   WHEN active = \"T\" THEN \"Activo\"
   ELSE \"Inactivo\"
   END active_formatted
  FROM users
  WHERE
  "
   "lastname LIKE '%" search "%' "
   "OR LOWER(firstname) LIKE '%" search "%' "
   "OR LOWER(cell) LIKE '%" search "%' "
   "OR LOWER(phone) LIKE '%" search "%' "
   "OR LOWER(email) LIKE '%" search "%' "
   "OR LOWER(level) LIKE '%" search "%' "
   "OR LOWER(active) LIKE '%" search "%'"))

(defn get-users-search
  [search]
  (Query db (get-users-search-sql (st/lower-case search))))
;; End search-user

(comment
  (get-user 2)
  (get-users))
