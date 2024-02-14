(ns sk.models.grid
  (:require [clojure.string :as st]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

;; Start build-grid
(defn grid-search
  [href search-placeholder search-button all-button]
  (list
   [:form {:method "post"
           :action (str href "/search")}
    (anti-forgery-field)
    [:div.text-right
     [:input {:type "text"
              :id "search"
              :name "search"
              :placeholder search-placeholder
              :style "margin-right:5px;"}]
     [:input.btn.btn-primary {:type "submit"
                              :value search-button
                              :style "margin-right:5px;"}]
     [:a.btn.btn-secondary {:role "button"
                            :href (str href)} all-button]]]))

(defn build-table-head
  [fields href search-placeholder search-button new-button all-button]
  (list
   [:tr.bg-light
    [:th {:colspan (str (+ (count fields) 2))} (grid-search href search-placeholder search-button all-button)]]
   [:tr.bg-secondary
    (map (fn [field]
           [:th (st/upper-case field)]) fields)
    [:th.text-center {:colspan "2"}
     [:a.btn.btn-primary {:role "button"
                          :style "width:100%"
                          :href (str href "/add")} new-button]]]))

(defn build-table-body
  [rows db-fields href edit-button delete-button]
  (list
   (map (partial (fn [row]
                   (list
                    [:tr
                     (map (fn [db-field] [:td (db-field row)]) db-fields)
                     [:td.text-center {:colspan "2"}
                      [:a.btn.btn-primary {:role "button"
                                           :style "width:45%;margin-right:10px;"
                                           :href (str href "/edit/" (:id row))} edit-button]
                      [:a.confirm.btn.btn-danger {:type "button"
                                                  :style "width:45%;"
                                                  :href (str href "/delete/" (:id row))} delete-button]]]))) rows)))

(defn build-grid
  [title rows fields db-fields href search-placeholder search-button all-button new-button edit-button delete-button]
  (list
   [:table.table.table-bordered.table-hover
    [:caption title]
    [:thead
     (build-table-head fields href search-placeholder search-button new-button all-button)]
    [:tbody (build-table-body rows db-fields href edit-button delete-button)]]))
;; End build-grid

;; Start build-dashboard
(defn dashboard-search
  [href search-placeholder search-button all-button]
  (list
   [:form {:method "post"
           :action (str href "/search")}
    (anti-forgery-field)
    [:div.text-right
     [:input {:type "text"
              :id "search"
              :name "search"
              :placeholder search-placeholder
              :style "margin-right:5px;"}]
     [:input.btn.btn-primary {:type "submit"
                              :value search-button
                              :style "margin-right:5px;"}]
     [:a.btn.btn-secondary {:role "button"
                            :href (str href)} all-button]]]))

(defn build-dashboard-head
  [fields href search-placeholder search-button all-button]
  (list
   [:tr.bg-light
    [:th {:colspan (str (count fields))} (dashboard-search href search-placeholder search-button all-button)]]
   [:tr.bg-secondary
    (map (fn [field]
           [:th (st/upper-case field)]) fields)]))

(defn build-dashboard-body
  [rows db-fields href]
  (list
   (map (partial (fn [row]
                   (list
                    [:tr
                     (map (fn [db-field] [:td (db-field row)]) db-fields)]))) rows)))

(defn build-dashboard
  [title rows fields db-fields href search-placeholder search-button all-button]
  (list
   [:table.table.table-bordered.table-hover
    [:caption title]
    [:thead
     (build-dashboard-head fields href search-placeholder search-button all-button)]
    [:tbody (build-dashboard-body rows db-fields href)]]))
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
