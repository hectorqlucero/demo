(ns sk.handlers.home.model
  (:require [sk.models.crud :refer [Query db]]))

(defn get-user
  [username]
  (Query db ["SELECT * FROM users WHERE username=?" username]))

(defn get-users
  []
  (Query db ["SELECT * FROM users"]))

(comment
  (get-user "sistema@gmail.com")
  (get-users))
