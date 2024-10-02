(ns sk.handlers.registered.view
  (:require [hiccup.page :refer [html5]]
            [sk.migrations :refer [config]]
            [pdfkit-clj.core :refer [as-stream gen-pdf]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.util :refer [parse-int
                                    zpl
                                    seconds->duration]]
            [sk.handlers.registro.model :refer [get-active-carreras]]
            [sk.handlers.registered.model
             :refer [get-active-carrera-name
                     get-registered
                     get-oregistered
                     get-register-row
                     get-carreras
                     create-barcode]]))

;; Start registrados
(defn build-body [row]
  (let [href (str "/display/registered/" (:id row))]
    [:tr
     [:td (:descripcion row)]
     [:td [:a.btn.btn-primary {:role "button"
                               :href href} "Registrados"]]]))

(defn registrados-view []
  (list
   [:table.table.table-secondary.table-hover {:style "width:100%;height:auto;"}
    [:caption.table-info "Seleccione la carrera o paseo al cual desea ver los registrados"]
    [:thead.table-info
     [:tr
      [:th {:field "descripcion" :width "50"} "Carrera/Paseo"]
      [:th {:width "50"} "Procesar"]]]
    [:tbody
     (map build-body (get-active-carreras))]]))
;; End registrados

;; Start oregistrados
(defn obuild-body [row]
  (let [href (str "/display/oregistered/" (:id row))]
    [:tr
     [:td (:descripcion row)]
     [:td [:a.btn.btn-primary {:role "button"
                               :href href} "Registrados"]]]))

(defn oregistrados-view []
  (list
   [:table.table.table-secondary.table-hover {:style "width:100%;height:auto;"}
    [:caption.table-info "Seleccione la carrera o paseo al cual desea ver los registrados"]
    [:thead.table-info
     [:tr
      [:th {:field "descripcion" :width "50"} "Carrera/Paseo"]
      [:th {:width "50"} "Procesar"]]]
    [:tbody
     (map obuild-body (get-active-carreras))]]))
;; End oregistrados

(def cnt (atom 0))

;; Start registered-view
(defn my-body [row]
  (let [button-path (str "'" "/imprimir/registered/" (:id row) "'")
        comando (str "location.href = " button-path)]
    [:tr
     [:td (swap! cnt inc)]
     [:td (:id row)]
     [:td (:nombre row)]
     [:td (:telefono row)]
     [:td (:email row)]
     [:td (:categoria row)]
     [:td [:input#numero {:name "numero_asignado"
                          :type "textbox"
                          :label ""
                          :prompt "No asignado aqui..."
                          :value (:numero_asignado row)
                          :onblur (str "postValue(" (:id row) ", this.value)")}]]
     [:td [:button.btn.btn-outline-primary {:type "button"
                                            :onclick comando
                                            :target "_blank"} "Registro"]]]))

(defn registered-view [carrera_id]
  (let [rows (get-oregistered carrera_id)
        cnt (reset! cnt 0)]
    [:div.container
     [:center
      [:h2 "CORREDORES REGISTRADOS"]
      [:h3 (get-active-carrera-name carrera_id)]]
     [:table.table.table-striped.table-hover.table-bordered
      [:thead.table-primary
       [:tr
        [:th "#"]
        [:th "ID"]
        [:th "Nombre"]
        [:th "Telefono"]
        [:th "Email"]
        [:th "Categoria"]
        [:th "No Asignado"]
        [:th "Imprimir"]]]
      [:tbody (map my-body rows)]]]))
;; End registered-view

;; Start oregistered-view
(defn omy-body [row]
  (let [segundos (parse-int (:tiempo row))
        tiempo (if-not (nil? segundos) (seconds->duration segundos) nil)]
    [:tr
     [:td (swap! cnt inc)]
     [:td (:id row)]
     [:td (:nombre row)]
     [:td (:categoria row)]
     [:td tiempo]]))

(defn oregistered-view [carrera_id]
  (let [rows (get-oregistered carrera_id)
        cnt (reset! cnt 0)]
    [:div.container
     [:center
      [:h2 "CORREDORES REGISTRADOS"]
      [:h3 (get-active-carrera-name carrera_id)]]
     [:table.table.table-striped.table-hover.table-bordered
      [:thead.table-primary
       [:tr
        [:th "#"]
        [:th "ID"]
        [:th "Nombre"]
        [:th "Categoria"]
        [:th "Tiempo"]]]
      [:tbody (map omy-body rows)]]]))
;; End oregistered-view

(def table-style
  "border:1px solid black;border-padding:0;")

(defn build-html [id]
  (let [row (get-register-row id)]
    (html5
     [:div
      [:img {:src (create-barcode id)}]
      [:center [:h2 [:strong (:carrera row)]]]
      [:center [:h3 [:strong "FORMATO DE REGISTRO"]]] [:span {:style "float:right;margin-left:10px;"} [:strong "identificador: " (:id row)]]
      [:span "DATOS PERSONALES:"] [:span {:style "float:right;"} [:strong "Fecha: " (:date row)]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Nombre: "] (str (:nombre row) "&nbsp;" (:apell_paterno row) "&nbsp;" (:apell_materno row))]]
        [:tr
         [:td {:style table-style} [:strong "Categoria: "] (:categoria row)]
         [:td {:colspan 2
               :style table-style} [:strong  "Telefono: "] (:telefono row)]]
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Email: "] (:email row)]]
        [:tr
         [:td {:colspan 3
               :style table-style} [:strong "Dirección: "] (:direccion row)]]]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr
         [:td {:style table-style} [:strong "Fecha de Registro: "] [:span {:style "float:right;"} [:strong "No: "] "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"]]]
        [:tr
         [:td {:style table-style} [:strong "Costo del Evento el día de registro $ : "]]]]]
      [:table {:style "margin-top:5px;border:1px solid black;border-radius:13px;border-spacing:0;padding:13px;font-size:0.9em;width:100%;"}
       [:tbody
        [:tr {:align "center"}
         [:td [:strong "Políticas y reglas del Evento"]]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p1 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p2 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p3 row)]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} "&nbsp;&nbsp;"]]
        [:tr {:align "justify"}
         [:td {:style "font-size:0.5em;"} (:p4 row)]]
        [:tr {:align "center"}
         [:td {:style "font-size:0.7em;"} [:strong (:d1 row)]]]
        [:tr {:align "center"}
         [:td {:style "font-size:0.7em;"} [:strong (:d2 row)]]]]]
      [:br]
      [:div {:style "width:100%;font-size:1em;font-weight:bold;text:center;"}
       [:center [:hr {:style "width:70%;"}]]
       [:center [:p [:strong "Firma y Aceptación del participante y/o tutor:"]]]]])))

;; Start update-number-view
(defn update-number-view [carrera_id]
  (let [row (get-carreras carrera_id)
        nombre (str (:nombre row) " " (:apell_paterno row) " " (:apell_materno row))]
    (list
     [:div.container {:title (str "Actualizar numero asignado de: " nombre)
                      :style "width:100%;max-width:400px;padding:30px 60px;"}
      [:form {:id "uform"
              :method "post"
              :action "/update/number"}
       (anti-forgery-field)
       [:input {:type "hidden"
                :name "id"
                :value carrera_id}]
       [:div {:style "margin-bottom:20px;"}
        [:input {:id "numero_asignado"
                 :name "numero_asignado"
                 :style "width:100%"
                 :prompt "Numero aqui..."
                 :data-options "label:'Numero:',required:true"}]]]

      [:div {:style "text-align:center;padding:5px 0"}
       [:a#submit.linkbutton.btn.btn-primary {:href "javascript:void(0)"
                                              :onclick "submitForm()"
                                              :style "width:80px;"} "Procesar"]]])))

(defn update-number-script []
  [:script
   "
   function submitForm(){
    $('#uform').form('submit', {
      onSubmit: function() {
        if($(this).form('validate')) {
          $('a#submit').linkbutton('disable');
        }
        return $(this).form('validate');
      },
      success: function(result) {
        var json = JSON.parse(result);
        if(json.error) {
          alert(json.error);
          $('a#submit').linkbutton('enable');
        } else {
          alert('Procesado con existo!');
          window.location.href = '/';
        }
      }
    });
   }
   "])
;; End update-number-view

(defn registered-pdf [id]
  (let [html (build-html id)
        filename (str "registro_" id ".pdf")]
    {:headers {"Content-Type" "application/pdf"
               "Content-Disposition" (str "attachment;filename=" filename)
               "Cache-Control" "no-cache,no-store,max-age=0,must-revalidate,pre-check=0,post-check=0"}
     :body (as-stream (gen-pdf html
                               :margin {:top 20 :right 15 :bottom 50 :left 15}))}))

(defn registered-js []
  [:script
   (str "function postValue(id,no) {
         $.get('/update/registered/'+id+'/'+no, function(data) {
           var mensaje = JSON.parse(data);
           alert(mensaje.message);
         })
   }")])

(comment
  (create-barcode 175)
  (build-html 175)
  (get-registered 175)
  (get-oregistered 175)
  (registrados-view))
