(ns sk.handlers.eventos.controller
  (:require [sk.layout :refer [application]]
            [sk.models.util :refer [get-session-id
                                    parse-int
                                    current_year
                                    get-month-name
                                    zpl]]
            [sk.migrations :refer [config]]
            [sk.handlers.eventos.model :refer [get-eventos]]
            [sk.handlers.eventos.view :refer [eventos-view
                                              eventos-scripts
                                              display-eventos-view
                                              display-eventos-scripts]]))

(defn eventos [_]
  (let [title "Eventos"
        ok (get-session-id)
        js (eventos-scripts)
        content (eventos-view title (current_year))]
    (application title ok js content)))

(defn display-eventos [year month]
  (let [title   (get-month-name (parse-int month))
        ok      (get-session-id)
        js      (display-eventos-scripts year month)
        rows    (get-eventos year month)
        rows    (map #(assoc % :day (zpl (% :day) 2)) rows)
        content (display-eventos-view title year month rows (str (config :path) "eventos/"))]
    (application title ok js content)))

(comment
  (get-eventos 2024 10)
  (eventos {}))
