(ns sk.handlers.users.controller
  (:require [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id]]
            [sk.handlers.admin.users.model :refer [get-users
                                                   get-users-search]]
            [sk.handlers.users.view :refer [users-view]]))

(defn users
  [_]
  (let [title "Dashboard"
        ok (get-session-id)
        js nil
        rows (get-users)
        content (users-view title rows)]
    (application title ok js content)))

(defn users-search
  [{params :params}]
  (let [title "Dashboard"
        ok (get-session-id)
        js nil
        search-string (:search params)
        rows (get-users-search search-string)
        content (users-view title rows)]
    (application title ok js content)))
