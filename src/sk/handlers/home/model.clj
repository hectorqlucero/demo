(ns sk.handlers.home.model
  (:require [sk.models.crud :refer [Query Update db]]))

(defn get-user
  [username]
  (Query db ["SELECT * FROM users WHERE username=?" username]))

(defn get-users
  []
  (Query db ["SELECT * FROM users"]))

(defn update-password
  [username password]
  (let [where-clause ["username = ?" username]
        result (Update db :users {:password password} where-clause)]
    result))

(comment
  (get-user "sistema@gmail.com")
  (get-users))
