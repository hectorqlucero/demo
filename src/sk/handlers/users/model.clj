(ns sk.handlers.users.model
  (:require
   [sk.models.crud :refer [db Query]]))

(defn get-users
  []
  (Query db "select * from users_view"))

(comment
  (get-users))
