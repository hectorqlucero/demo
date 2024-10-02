(ns sk.handlers.creloj.controller
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as st]
            [sk.handlers.creloj.view
             :refer [registrados-view
                     creloj-view creloj-js
                     seconds->duration
                     limpiar-view
                     limpiar-script
                     salidas-view
                     salidas-js
                     llegadas-view
                     llegadas-js]]
            [sk.handlers.creloj.model :refer [get-oregistered
                                              limpiar
                                              get-carreras-row
                                              get-lector
                                              process-lector]]
            [sk.models.crud :refer [Update db]]
            [sk.models.util :refer [current_time_internal
                                    parse-int]]
            [cheshire.core :refer [generate-string]]
            [sk.layout
             :refer [application error-404]]
            [sk.models.util :refer [get-session-id]]))

(defn registrados [_]
  (let [title "Corredores Registrados"
        ok (get-session-id)
        content (registrados-view)]
    (application title ok nil content)))

(defn salidas [carrera_id]
  (let [title "SALIDAS DE CORREDORES"
        ok (get-session-id)
        js (salidas-js carrera_id)
        content (salidas-view carrera_id)]
    (application title ok js content)))

(defn llegadas [carrera_id]
  (let [title "LLEGADAS DE CORREDORES"
        ok (get-session-id)
        js (llegadas-js carrera_id)
        content (llegadas-view carrera_id)]
    (application title ok js content)))

(defn contra-reloj [carrera_id]
  (let [title "CORREDORES REGISTRADOS"
        ok (get-session-id)
        js (creloj-js carrera_id)
        content (creloj-view carrera_id)]
    (application title ok js content)))

(defn contra-reloj-salida [id]
  (let [postvars {:salida (current_time_internal)}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el tiempo de salida!"}))))

(defn contra-reloj-llegada [id]
  (let [postvars {:llegada (current_time_internal)}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el tiempo de llegada!"}))))

(defn contra-reloj-salida-cambiar [id v]
  (let [postvars {:salida v}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el tiempo de salida!"}))))

(defn contra-reloj-llegada-cambiar [id v]
  (let [postvars {:llegada v}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (seq result)
      (generate-string {:message "Procesado correctamente!"})
      (generate-string {:message "No se pudo asignar el tiempo de llegada!"}))))

(defn remove-non-printable-characters [x]
  (if-not (nil? x)
    (st/replace x #"[\p{C}&&^(\S)]" "")
    x))

(defn process-csv [row]
  (let [segundos (parse-int (:tiempo row))
        tiempo (if-not (nil? segundos) (seconds->duration segundos) nil)]
    [(remove-non-printable-characters (:nombre row))
     (remove-non-printable-characters (:apell_paterno row))
     (remove-non-printable-characters (:apell_materno row))
     (or (:categoria row) "  ")
     (or (:numero_asignado row) "  ")
     (or (:hora_salida row) "  ")
     (or (:hora_llegada row) "  ")
     (or tiempo " ")]))

(defn generate-csv [carrera_id]
  (let [rows (get-oregistered carrera_id)
        heads ["NOMBRE" "PATERNO" "MATERNO" "CATEGORIA" "NUMERO ASIGNADO" "SALIDA" "LLEGADA" "TIEMPO"]
        data (vec (cons heads (map process-csv rows)))
        filename (str "contra_reloj_" carrera_id ".csv")]
    (with-open [writer (io/writer filename)]
      (csv/write-csv writer data))
    {:status 200
     :headers {"Content-Type" "text/csv"
               "Content-Disposition" (str "attachment; filename=" filename)}
     :body (slurp filename)}))

(defn limpiar-form [_]
  (let [title "Limpiar tiempos"
        ok (get-session-id)
        js (limpiar-script)
        content (limpiar-view title)]
    (application title ok js content)))

(defn limpiar-tiempos [{params :params}]
  (let [carrera-id (:id params)
        result (limpiar carrera-id)]
    (if result
      (error-404 "Tiempos limpiados!" "/")
      (error-404 "Incapaz de nulificar campos!" "/"))))

(defn procesar-salidas [carrera_id numero_asignado]
  (let [row (get-carreras-row carrera_id numero_asignado)
        id (:id row)
        postvars {:salida (current_time_internal)}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (> (first (seq result)) 0)
      (generate-string {:success "Procesado correctamente!"})
      (generate-string {:error "No se pudo procesar, o numero asignado incorrecto!"}))))

(defn procesar-llegadas [carrera_id numero_asignado]
  (let [row (get-carreras-row carrera_id numero_asignado)
        id (:id row)
        postvars {:llegada (current_time_internal)}
        result (Update db :carreras postvars ["id = ?" id])]
    (if (> (first (seq result)) 0)
      (generate-string {:success "Procesado correctamente!"})
      (generate-string {:error "No se pudo procesar, o numero asignado incorrecto!"}))))

(defn lector-carreras
  [_]
  (let [result (doall (process-lector (get-lector)))]
    (if (seq result)
      (error-404 "Procesado con exito!" "/display/creloj")
      (error-404 "No se pudo procesar!" "/display/creloj"))))

(comment
  (limpiar-view "Limpiar tiempos")
  (remove-non-printable-characters nil)
  (get-oregistered 5)
  (generate-csv 5))
