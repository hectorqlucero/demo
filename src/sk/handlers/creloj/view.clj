(ns sk.handlers.creloj.view
  (:require [hiccup.page :refer [html5]]
            [sk.handlers.registro.model :refer [get-active-carreras]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [sk.models.form :refer [build-form
                                    build-select]]
            [sk.models.util :refer [build-field
                                    build-button
                                    parse-int
                                    current_time]]
            [clojure.string :as string]
            [sk.handlers.creloj.model
             :refer [get-active-carrera-name
                     get-oregistered
                     get-register-row
                     carreras-options]]))

;; Start format seconds->duration
(def seconds-in-minute 60)
(def seconds-in-hour (* 60 seconds-in-minute))
(def seconds-in-day (* 24 seconds-in-hour))
(def seconds-in-week (* 7 seconds-in-day))

(defn seconds->duration [seconds]
  (let [seconds (or seconds 0)
        weeks   ((juxt quot rem) seconds seconds-in-week)
        wk      (first weeks)
        days    ((juxt quot rem) (last weeks) seconds-in-day)
        d       (first days)
        hours   ((juxt quot rem) (last days) seconds-in-hour)
        hr      (first hours)
        min     (quot (last hours) seconds-in-minute)
        sec     (rem (last hours) seconds-in-minute)]
    (string/join ", "
                 (filter #(not (string/blank? %))
                         (conj []
                               (when (> wk 0) (str wk " semana"))
                               (when (> d 0) (str d " dia"))
                               (when (> hr 0) (str hr " hr"))
                               (when (> min 0) (str min " min"))
                               (when (> sec 0) (str sec " seg")))))))
;; End format seconds->duration

;; Start registrados-view
(defn build-body [row]
  (let [href (str "/display/creloj/" (:id row))
        sref (str "/display/salidas/" (:id row))
        lref (str "/display/llegadas/" (:id row))]
    [:tr
     [:td (:descripcion row)]
     [:td [:a.btn.btn-primary {:role "button"
                               :href href} "Contra Reloj"]]
     [:td [:a.btn.btn-primary {:role "button"
                               :href sref} "Salidas"]]
     [:td [:a.btn.btn-primary {:role "button"
                               :href lref} "Llegadas"]]]))
(defn registrados-view []
  (list
   [:table.table.table-secondary.table-hover {:style "width:100%;height:auto;"}
    [:caption.table-info "Seleccione la carrera o paseo al cual desea ver los corredores. Nota: Necesita tener un numero asignado!!!"]
    [:thead.table-info
     [:tr
      [:th {:field "descripcion" :width "50"} "Carrera/Paseo"]
      [:th {:width "20"} "Procesar"]
      [:th {:width "20"} "Procesar"]
      [:th {:width "20"} "Procesar"]]]
    [:tbody
     (map build-body (get-active-carreras))]]))
;; End registrados-view

;; Start tiempo-view
(defn my-body [row]
  (let [slink (str "salida(" (:id row) ")")
        dslink (str "salida_cambiar(" (:id row) ")")
        elink (str "llegada(" (:id row) ")")
        delink (str "llegada_cambiar(" (:id row) ")")
        segundos  (parse-int (:tiempo row))
        tiempo (if-not (nil? segundos) (seconds->duration segundos) nil)]
    [:tr
     [:td (:id row)]
     [:td (str (:nombre row) " " (:apell_paterno row) " " (:apell_materno row))]
     [:td (:categoria row)]
     [:td [:strong (:numero_asignado row)]]
     [:td (if (:salida row)
            [:input {:id (str "salida_" (:id row))
                     :type "textbox"
                     :value (:salida row)
                     :onchange dslink}]
            [:input {:type "checkbox"
                     :onclick slink}])]
     [:td (if (:llegada row)
            [:input {:id  (str "llegada_" (:id row))
                     :type "textbox"
                     :value (:llegada row)
                     :onchange delink}]
            [:input {:type "checkbox"
                     :onclick elink}])]
     [:td tiempo]]))

(defn creloj-view [carrera_id]
  (let [rows (get-oregistered carrera_id)]
    [:div.container
     [:center
      [:h2 "CORREDORES REGISTRADOS"]
      [:h3 (get-active-carrera-name carrera_id)]
      [:h4 [:a.btn.btn-primary {:role "button"
                                :href (str "/creloj/csv/" carrera_id)} "Exportar a hoja electronica"]]]
     [:table.table.table-striped.table-hover.table-bordered
      [:thead.table-primary
       [:tr
        [:th "ID"]
        [:th "CORREDOR"]
        [:th "CATEGORIA"]
        [:th "NUMERO ASIGNADO"]
        [:th "SALIDA"]
        [:th "LLEGADA"]
        [:th "TIEMPO"]]]
      [:tbody (map my-body rows)]]]))
;; End tiempo-view

;; Start limpiar
(defn limpiar-view [title]
  (build-form
   title
   ""
   (build-select
    {:label "Carrera"
     :id "id"
     :name "id"
     :value ""
     :options (carreras-options)})
   [:button.bnt.btn-primary {:id "submit"
                             :onclick "submitForm()"} "Limpiar"]))

(defn limpiar-script []
  [:script
   "
    function submitForm() {
        $('.fm').form('submit', {
            onSubmit:function() {
                if($(this).form('validate')) {
                  $('#submit').prop('disabled',true);
                  $('#submit').html('Procesando!');
                }
                return $(this).form('enableValidation').form('validate');
            },
            success: function(data) {
                try {
                    var dta = JSON.parse(data);
                    if(dta.hasOwnProperty('url')) {
                        alert(dta.url);
                        window.location.href = dta.url;
                    } else if(dta.hasOwnProperty('error')) {
                        alert(dta.error);
                        $('#submit').prop('disabled',true);
                        $('#submit').html('Limpiar');
                    }
                } catch(e) {
                    console.error('Invalid JSON');
                }
            }
        });
    }
   "])

;; End limpiar

;; Start creloj-scripts
(defn creloj-js [carrera_id]
  [:script
   "
    function salida(id) {
      $.get('/update/salida/'+id, function(data) {
        var dta = JSON.parse(data);
        alert(dta.message);
        window.location.href = '/display/creloj/'+" carrera_id ";
      })
    }

    function salida_cambiar(id) {
     let llave = '#salida_'+id;
     let valor = $(llave).val();
     $.get('/change/salida/'+id+'/'+valor, function(data) {
      var dta = JSON.parse(data);
      alert(dta.message);
      window.location.href = '/display/creloj/'+" carrera_id ";
     })
    }

    function llegada(id) {
      $.get('/update/llegada/'+id, function(data) {
        var dta = JSON.parse(data);
        alert(dta.message);
        window.location.href = '/display/creloj/'+" carrera_id ";
      })
    }

    function llegada_cambiar(id) {
     let llave = '#llegada_'+id;
     let valor = $(llave).val();
     $.get('/change/llegada/'+id+'/'+valor, function(data) {
      var dta = JSON.parse(data);
      alert(dta.message);
      window.location.href = '/display/creloj/'+" carrera_id ";
     })
    }
 "])
;; End crelog-scripts

;; Start salidas
(defn salidas-view [carrera_id]
  [:div.container
   [:div.row {:style "width:200px;border:1px; solid black;background:white;"}
    [:div.col-sm
     [:h1 "SALIDAS"]
     [:div#runningTime]
     [:hr]
     [:input#numero {:type "text" :placeholder "Numero corredor aqui"}]
     [:button.btn.btn-primary {:style "align:center;width:100%;margin-top:5px;margin-bottom:5px;"
                               :onclick "procesar()"} "Salir"]]]])

(defn salidas-js [carrera_id]
  [:script
   "
   function procesar() {
    let numero = $('#numero').val();
    $.ajax({
      method: 'GET',
      url: '/procesar/salidas/" carrera_id "/'+numero,
      success: function(result) {
        var json = JSON.parse(result);
        if(json.error) {
          alert(json.error);
        } else {
          alert(json.success);
        }
      }
    });
    $('#numero').val('');
   }

   $(document).ready(function() {
    setInterval(runningTime, 1000);
   });

   function runningTime() {
    $.ajax({
      url: '/table_ref/get-current-time',
      success: function(data) {
        $('#runningTime').html(data);
      },
    });
   }
   "])
;; End salidas

;; Start llegadas
(defn llegadas-view [carrera_id]
  [:div.container
   [:div.row {:style "width:200px;border:1px; solid black;background:white;"}
    [:div.col-sm
     [:h1 "LLEGADAS"]
     [:div#runningTime]
     [:hr]
     [:input#numero {:type "text" :placeholder "Numero corredor aqui"}]
     [:button.btn.btn-primary {:style "align:center;width:100%;margin-top:5px;margin-bottom:5px;"
                               :onclick "procesar_l()"} "LLegar"]]]])

(defn llegadas-js [carrera_id]
  [:script
   "
   function procesar_l() {
    let numero = $('#numero').val();
    $.ajax({
      method: 'GET',
      url: '/procesar/llegadas/" carrera_id "/'+numero,
      success: function(result) {
        var json = JSON.parse(result);
        if(json.error) {
          alert(json.error);
        } else {
          alert(json.success);
        }
      }
    });
    $('#numero').val('');
   }

   $(document).ready(function() {
    setInterval(runningTime, 1000);
   });

   function runningTime() {
    $.ajax({
      url: '/table_ref/get-current-time',
      success: function(data) {
        $('#runningTime').html(data);
      },
    });
   }
   "])
;; End llegadas

(comment
  (limpiar-view)
  (seconds->duration 7249)
  (get-registered 5)
  (creloj-view 5))
