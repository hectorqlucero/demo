(ns sk.models.grid
  (:require [clojure.string :as st]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

;; start build-gid
(defn build-grid-head
  [href fields & args]
  (let [args (first args)
        new (:new args)]
    (list
     [:thead.table-light
      [:tr
       (map (fn [field]
              (list
               [:th {:data-sortable "true"
                     :data-field (key field)} (st/upper-case (val field))]))

            fields)
       [:th.text-center [:a.btn.btn-primary {:role "button"
                                             :class (str "btn btn-primary" (when (= new false) " disabled"))
                                             :href (str href "/add")} "Nuevo Record"]]]])))

(defn build-grid-body
  [rows href fields & args]
  (let [args (first args)
        edit (:edit args)
        delete (:delete args)]
    (list
     [:tbody
      (map (partial (fn [row]
                      [:tr
                       (map (fn [field]
                              [:td ((key field) row)]) fields)
                       [:td.text-nowrap.text-center {:style "width:15%;"}
                        [:a {:role "button"
                             :class (str "btn btn-primary" (when (= edit false) " disabled"))
                             :style "margin:1px;"
                             :href (str href "/edit/" (:id row))} "Editar"]
                        [:a {:role "button"
                             :class (str "confirm btn btn-danger" (when (= delete false) " disabled"))
                             :style "margin:1px;"
                             :href (str href "/delete/" (:id row))} "Borrar"]]])) rows)])))

(defn build-grid
  [title rows table-id fields href & args]
  (list
   [:div.table-responsive
    [:table.table.table-sm {:id table-id
                            :data-locale "es-MX"
                            :data-toggle "table"
                            :data-show-columns "true"
                            :data-show-toggle "true"
                            :data-show-print "false"
                            :data-search "true"
                            :data-pagination "true"
                            :data-key-events "true"}
     [:caption title]
     (if (seq args)
       (build-grid-head href fields (first args))
       (build-grid-head href fields))
     (if (seq args)
       (build-grid-body rows href fields (first args))
       (build-grid-body rows href fields))]]))
;; End build-grid

;; start build-dashboard
(defn build-dashboard-head
  [fields]
  (list
   [:thead.table-light
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
   [:div.table-responsive
    [:table.table.table-sm {:id table-id
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
     (build-dashboard-body rows fields)]]))
;; End build-dashboard

;; Start build-modal
(defn build-modal
  [title row form]
  (list
   [:div.modal.fade {:id "exampleModal"
                     :tabindex "-1"
                     :aria-labelledby "exampleModalLabel"
                     :aria-hidden "true"}
    [:div.modal-dialog
     [:div.modal-content
      [:div.modal-header
       [:h1.modal-title.fs-5 {:id "exampleModalLabel"} title]
       [:button.btn-close {:type "button"
                           :data-bs-dismiss "modal"
                           :aria-label "Close"}]]

      [:div.modal-body
       [:span form]]]]]))
;; End build-modal

(defn modal-script
  []
  [:script
   "
   const myModal = new bootstrap.Modal(document.getElementById('exampleModal'), {
    keyboard: false
   })

   myModal.show();
   "])
;; End build-modal
