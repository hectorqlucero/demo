(ns sk.handlers.registro.model
  (:require [sk.models.crud :refer [Query db]]))

(defn get-active-carreras []
  (let [sql "select * from carrera where activa = 'S'"
        rows (Query db sql)]
    rows))

(defn get-active-carrera-name [carrera_id]
  (:descripcion (first (Query db ["select descripcion from carrera where id = ?" carrera_id]))))

(defn registrar-mensaje [carrera_id]
  (:registrar_mensaje (first (Query db ["select registrar_mensaje from mensajes where carrera_id = ?" carrera_id]))))

(defn correo-mensaje [carrera_id]
  (:correo_mensaje (first (Query db ["select correo_mensaje from mensajes where carrera_id = ?" carrera_id]))))

(def registered-sql
  "
   select * 
   from carreras 
   where carrera_id = ?
   order by
   nombre,
   apell_paterno,
   apell_materno
   ")

(defn get-registered [carrera_id]
  (Query db [registered-sql carrera_id]))

(defn get-carrera-name [id]
  (:descripcion (first (Query db ["select descripcion from carrera where id = ?" id]))))

(defn get-active-carrera-name [carrera_id]
  (let [carrera-name (get-carrera-name carrera_id)]
    carrera-name))

(def register-row-sql
  "
   select
   carreras.id,
   carreras.nombre,
   carreras.apell_paterno,
   carreras.apell_materno,
   carreras.pais,
   carreras.ciudad,
   carreras.telefono,
   carreras.email,
   carreras.sexo,
   DATE_FORMAT(carreras.fecha_nacimiento, '%d/%m/%Y') as fecha_nacimiento,
   carreras.direccion,
   carreras.club,
   carreras.carrera_id,
   carreras.categoria_id,
   DATE_FORMAT(carreras.last_updated, '%d/%m/%Y') as date,
   carrera.p1,
   carrera.p2,
   carrera.p3,
   carrera.p4,
   carrera.d1,
   carrera.d2,
   carrera.descripcion as carrera,
   categorias.descripcion as categoria
   from carreras
   left join carrera on carreras.carrera_id = carrera.id
   left join categorias on carreras.categoria_id = categorias.id
   where carreras.id = ?
   ")

(defn get-register-row [carrera_id]
  (first (Query db [register-row-sql carrera_id])))

;; start categoria-options
(def categoria-options-sql
  "
  select id as value,
  descripcion as label
  from categorias
  where carrera_id = ?
  order by descripcion
  ")

(defn categoria-options
  [carrera-id]
  (let [options (Query db [categoria-options-sql carrera-id])]
    (list* {:label "Seleccionar categoria"
            :value ""} options)))
;; end categoria-options

(comment
  (categoria-options 2)
  (get-active-carreras)
  (get-active-carrera-name 2)
  (registrar-mensaje 1)
  (correo-mensaje 2)
  (get-carrera-name 1)
  (get-register-row 1))
