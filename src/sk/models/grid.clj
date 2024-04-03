(ns sk.models.grid
  (:require [clojure.string :as st]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

;; start build-gid
(defn build-grid-head
  [href fields]
  (list
   [:thead
    [:tr
     (map (fn [field]
            (list
             [:th {:data-sortable "true"
                   :data-field (key field)} (st/upper-case (val field))]))

          fields)
     [:th.text-center [:a.btn.btn-primary {:role "button"
                                           :href (str href "/add")} "Nuevo Record"]]]]))

(defn build-grid-body
  [rows href fields]
  (list
   [:tbody
    (map (partial (fn [row]
                    [:tr
                     (map (fn [field]
                            [:td ((key field) row)]) fields)
                     [:td.text-center
                      [:a.btn.btn-primary {:role "button"
                                           :style "margin:1px;"
                                           :href (str href "/edit/" (:id row))} "Editar"]
                      [:a.confirm.btn.btn-danger {:role "button"
                                                  :style "margin:1px;"
                                                  :href (str href "/delete/" (:id row))} "Borrar"]]])) rows)]))

(defn build-grid
  [title rows table-id fields href]
  (list
   [:table.table {:id table-id
                  :data-locale "es-MX"
                  :data-toggle "table"
                  :data-show-columns "true"
                  :data-show-toggle "true"
                  :data-show-print "false"
                  :data-search "true"
                  :data-pagination "true"
                  :data-key-events "true"}
    [:caption title]
    (build-grid-head href fields)
    (build-grid-body rows href fields)]))
;; End build-grid

;; start build-dashboard
(defn build-dashboard-head
  [fields]
  (list
   [:thead
    [:tr
     (map (fn [field]
            (list
             [:th {:data-sortable "true"
                   :data-field (key field)} (st/upper-case (val field))]))

          fields)]]))

(defn build-dashboard-body
  [rows fields]
  (list
   [:tbody
    (map (partial (fn [row]
                    [:tr
                     (map (fn [field]
                            [:td ((key field) row)]) fields)])) rows)]))

(defn build-dashboard
  [title rows table-id fields]
  (list
   [:table.table {:id table-id
                  :data-locale "es-MX"
                  :data-toggle "table"
                  :data-show-columns "true"
                  :data-show-toggle "true"
                  :data-show-print "true"
                  :data-search "true"
                  :data-pagination "true"
                  :data-key-events "true"}
    [:caption title]
    (build-dashboard-head fields)
    (build-dashboard-body rows fields)]))
;; End build-dashboard

;; Start build-modal
(defn build-modal
  [title row form]
  (list
   [:div.modal.fade {:id "myModal"
                     :tabindex "-1"
                     :role "dialog"
                     :aria-labelledby "myModatlTitle"
                     :aria-hidden "true"}
    [:div.modal-dialog {:role "document"}
     [:div.modal-content
      [:div.modal-header
       [:h5.modal-title {:id "myModalTitle"} title]
       [:button.close {:type "button"
                       :data-dismiss "modal"
                       :aria-label "Close"}
        [:span {:aria-hidden "true"} "&times;"]]]
      [:div.modal-body
       [:span form]]]]]))

(defn modal-script
  []
  [:script
   "
   $('#myModal').modal('show');
   "])
;; End build-modal
