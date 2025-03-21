(ns sk.handlers.users.controller
  (:require
   [sk.handlers.users.model :refer [get-users]]
   [sk.handlers.users.view :refer [users-view]]
   [sk.layout :refer [application]]
   [sk.models.util :refer [get-session-id]]))

(defn users
  [_]
  (let [title "Dashboard"
        ok (get-session-id)
        js nil
        rows (get-users)
        content (users-view title rows)]
    (application title ok js content)))
