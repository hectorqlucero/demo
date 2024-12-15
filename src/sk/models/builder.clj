(ns sk.models.builder
  (:require [clojure.java.io :as io]
            [clojure.string :as st]
            [hiccup.core :refer [html]]
            [sk.models.crud :refer [db get-table-describe Save]]))

(defn create-path [path]
  (.mkdir (io/file path)))

(def security-comments-1
  "Only <strong>los administrators </strong> can access this option!!!")

(def security-comments-2
  "Only <strong>system administrators </strong> can access this option!!!")

(defn process-security [security]
  (cond
    (= security 1) (str "(if\n"
                        "(or\n"
                        "(= (user-level) \"A\")\n"
                        "(= (user-level) \"S\"))\n"
                        "(application title ok js content)\n"
                        "(application title ok nil \"" security-comments-1 "\"))")
    (= security 2) (str "(if (= (user-level) \"S\")\n"
                        "(application title ok js content)\n"
                        "(application title ok nil \"" security-comments-2 "\"))")
    (= security 3) (str "(application title ok js content)")))

;; Start build-grid
(defn build-grid-controller
  [options]
  (let [folder (:folder options)
        titulo (:title options)
        tabla (:table options)
        root (:root options)
        link (:link options)
        security (:secure options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".controller\n"
     "(:require [sk.layout :refer [application error-404]]\n"
     "[sk.models.util :refer [get-session-id user-level]]\n"
     "[sk.models.crud :refer [build-form-save build-form-delete]]\n"
     "[" ns-root ".model :refer [get-" tabla " get-" tabla "-id]]\n"
     "[" ns-root ".view :refer [" folder "-view " folder "-edit-view " folder "-add-view " folder "-modal-script]]))\n\n"
     "(defn " folder "[_]\n"
     "(let [title \"" titulo "\"\n"
     "ok (get-session-id)\n"
     "js nil\n"
     "rows (get-" folder ")\n"
     "content (" folder "-view title rows)]\n"
     (process-security security) "))\n\n"
     "(defn " folder "-edit\n"
     "[id]\n"
     "(let [title \"Modificar " folder "\"\n"
     "ok (get-session-id)\n"
     "js (" folder "-modal-script)\n"
     "row (get-" folder "-id  id)\n"
     "rows (get-" folder ")\n"
     "content (" folder "-edit-view title row rows)]\n"
     "(application title ok js content)))\n\n"
     "(defn " folder "-save\n"
     "[{params :params}]\n"
     "(let [table \"" tabla "\"\n"
     "result (build-form-save params table)]\n"
     "(if (= result true)\n"
     "(error-404 \"Record se processo correctamente!\" \"" link "\")\n"
     "(error-404 \"No se pudo procesar el record!\" \"" link "\"))))\n\n"
     "(defn " folder "-add\n"
     "[_]\n"
     "(let [title \"Crear nuevo " tabla "\"\n"
     "ok (get-session-id)\n"
     "js (" folder "-modal-script)\n"
     "row nil\n"
     "rows (get-" folder ")\n"
     "content (" folder "-add-view title row rows)]\n"
     "(application title ok js content)))\n\n"
     "(defn " folder "-delete\n"
     "[id]\n"
     "(let [table \"" tabla "\"\n"
     "result (build-form-delete table id)]\n"
     "(if (= result true)\n"
     "(error-404 \"Record se processo correctamente!\" \"" link "\")\n"
     "(error-404 \"No se pudo procesar el record!\" \"" link "\"))))\n\n")))

(defn build-grid-model
  [options]
  (let [folder (:folder options)
        tabla (:table options)
        root (:root options)
        link (:link options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)
        data (get-table-describe tabla)
        cols (rest data)]
    (str
     "(ns " ns-root ".model\n"
     "(:require [sk.models.crud :refer [Query db]]\n"
     "[clojure.string :as st]))\n\n"
     "(def get-" folder "-sql\n"
     "(str\n"
     "\"\n"
     "SELECT *\n"
     "FROM " tabla "\n"
     "\"\n"
     "))\n\n"
     "(defn get-" folder "\n"
     "[]\n"
     "(Query db get-" folder "-sql))\n\n"
     "(def get-" folder "-id-sql\n"
     "(str\n"
     "\"\n"
     "SELECT *\n"
     "FROM " tabla "\n"
     "WHERE id = ?\n"
     "\"\n"
     "))\n\n"
     "(defn get-" folder "-id\n"
     "[id]\n"
     "(first (Query db [get-" folder "-id-sql id])))\n\n")))

(defn build-grid-form-col
  [col]
  (let [tipo (:type col)
        field (:field col)]
    (cond
      (= tipo "text")
      (str
       "(build-textarea {:label \"" (st/upper-case field) "\"\n"
       ":id \"" field "\"\n"
       ":name \"" field "\"\n"
       ":rows \"3\"\n"
       ":placeholder \"" field " aqui...\"\n"
       ":required false\n"
       ":value (:" field " row)})\n")
      (= tipo "date")
      (str
       "(build-field {:label \"" (st/upper-case field) "\"\n"
       ":type \"date\"\n"
       ":id \"" field "\"\n"
       ":name \"" field "\"\n"
       ":required false\n"
       ":value (:" field " row)})\n")
      :else
      (str
       "(build-field {:label \"" (st/upper-case field) "\"\n"
       ":type \"text\"\n"
       ":id \"" field "\"\n"
       ":name \"" field "\"\n"
       ":placeholder \"" field " aqui...\"\n"
       ":required false\n"
       ":value (:" field " row)})\n"))))

(defn build-grid-view
  [options]
  (let [folder (:folder options)
        tabla (:table options)
        root (:root options)
        link (:link options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)
        data (get-table-describe tabla)
        cols (rest data)]
    (str
     "(ns " ns-root ".view\n"
     "(:require [ring.util.anti-forgery :refer [anti-forgery-field]]\n"
     "[sk.models.form :refer [form build-hidden-field build-field build-select build-radio build-modal-buttons build-textarea]]\n"
     "[sk.models.grid :refer [build-grid build-modal modal-script]]))\n\n"
     "(defn " folder "-view\n"
     "[title rows]\n"
     "(let [labels [" (apply str (map (fn [col] (str " " "\"" (st/upper-case (:field col)) "\"")) cols)) "]\n"
     "db-fields [" (apply str (map (fn [col] (str " " (keyword (:field col)))) cols)) "]\n"
     "fields (zipmap db-fields labels)\n"
     "table-id \"" folder "_table\"\n"
     "args {:new true :edit true :delete true}\n"
     "href \"" link "\"]\n"
     "(build-grid title rows table-id fields href args)))\n\n"
     "(defn build-" folder "-fields\n"
     "[row]\n"
     "(list\n"
     "(build-hidden-field {:id \"id\"\n"
     ":name \"id\"\n"
     ":value (:id row)})\n"
     (apply str (map build-grid-form-col cols))
     "))\n\n"
     "(defn build-" folder "-form\n"
     "[title row]\n"
     "(let [fields (build-" folder "-fields row)\n"
     "href \"" link "/save\"\n"
     "buttons (build-modal-buttons)]\n"
     "(form href fields buttons)))\n\n"
     "(defn build-" folder "-modal\n"
     "[title row]\n"
     "(build-modal title row (build-" folder "-form title row)))\n\n"
     "(defn " folder "-edit-view\n"
     "[title row rows]\n"
     "(list\n"
     "(" folder "-view \"" folder " Manteniento\" rows)\n"
     "(build-" folder "-modal title row)))\n\n"
     "(defn " folder "-add-view\n"
     "[title row rows]\n"
     "(list\n"
     "(" folder "-view \"" folder " Mantenimiento\" rows)\n"
     "(build-" folder "-modal title row)))\n\n"
     "(defn " folder "-modal-script\n"
     "[]\n"
     "(modal-script))\n")))

(defn build-grid-skeleton
  "secure: 1=s/a, 2=s, 3=all"
  [options]
  (let [folder (:folder options)
        root (:root options)
        path (str root folder)]
    (create-path path)
    (spit (str path "/controller.clj") (build-grid-controller options))
    (spit (str path "/model.clj") (build-grid-model options))
    (spit (str path "/view.clj") (build-grid-view options))))

(defn build-grid
  [table]
  (build-grid-skeleton
   {:folder table
    :title (st/capitalize table)
    :table table
    :secure 1
    :link (str "/admin/" table)
    :root "src/sk/handlers/admin/"})
  (println (str "Codigo generado en: src/sk/handlers/admin/" table)))
;; End build-grid

;; Start build-dashboard
(defn build-dashboard-controller
  [options]
  (let [folder (:folder options)
        titulo (:title options)
        tabla (:table options)
        root (:root options)
        link (:link options)
        security (:secure options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)]
    (str
     "(ns " ns-root ".controller\n"
     "(:require [sk.layout :refer [application]]\n"
     "[sk.models.util :refer [get-session-id]]\n"
     "[" ns-root ".model :refer [get-" tabla "]]\n"
     "[" ns-root ".view :refer [" tabla "-view]]))\n\n"
     "(defn " folder "[_]\n"
     "(let [title \"" titulo "\"\n"
     "ok (get-session-id)\n"
     "js nil\n"
     "rows (get-" folder ")\n"
     "content (" folder "-view title rows)]\n"
     (process-security security) "))\n\n")))

(defn build-dashboard-model
  [options]
  (let [folder (:folder options)
        tabla (:table options)
        root (:root options)
        link (:link options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)
        data (get-table-describe tabla)
        cols (rest data)]
    (str
     "(ns " ns-root ".model\n"
     "(:require [sk.models.crud :refer [Query db]]\n"
     "[clojure.string :as st]))\n\n"
     "(def get-" folder "-sql\n"
     "(str\n"
     "\"\n"
     "SELECT *\n"
     "FROM " tabla "\n"
     "\"\n"
     "))\n\n"
     "(defn get-" folder "\n"
     "[]\n"
     "(Query db get-" folder "-sql))\n\n"
     "(def get-" folder "-id-sql\n"
     "(str\n"
     "\"\n"
     "SELECT *\n"
     "FROM " tabla "\n"
     "WHERE id = ?\n"
     "\"\n"
     "))\n\n"
     "(defn get-" folder "-id\n"
     "[id]\n"
     "(first (Query db [get-" folder "-id-sql id])))\n\n")))

(defn build-dashboard-view
  [options]
  (let [folder (:folder options)
        tabla (:table options)
        root (:root options)
        link (:link options)
        ns-root (subs (str (st/replace root #"/" ".") folder) 4)
        data (get-table-describe tabla)
        cols (rest data)]
    (str
     "(ns " ns-root ".view\n"
     "(:require [sk.models.grid :refer [build-dashboard]]))\n\n"
     "(defn " folder "-view\n"
     "[title rows]\n"
     "(let [table-id \"" folder "_table\"\n"
     "labels [" (apply str (map (fn [col] (str " " "\"" (st/upper-case (:field col)) "\"")) cols)) "]\n"
     "db-fields [" (apply str (map (fn [col] (str " " (keyword (:field col)))) cols)) "]\n"
     "fields (zipmap db-fields labels)]\n"
     "(build-dashboard title rows table-id fields)))\n")))

(defn build-dashboard-skeleton
  "secure: 1=s/a, 2=s, 3=all"
  [options]
  (let [folder (:folder options)
        root (:root options)
        path (str root folder)]
    (create-path path)
    (spit (str path "/controller.clj") (build-dashboard-controller options))
    (spit (str path "/model.clj") (build-dashboard-model options))
    (spit (str path "/view.clj") (build-dashboard-view options))))

(defn build-dashboard
  [table]
  (build-dashboard-skeleton
   {:folder table
    :title (st/capitalize table)
    :table table
    :secure 3
    :link (str "/" table)
    :root "src/sk/handlers/"})
  (println (str "Codigo generado en: src/sk/handlers/" table)))
;; End build-dashboard

(comment
  (build-grid "contactos"))
