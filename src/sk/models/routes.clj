(ns sk.models.routes
  (:require
   [clojure.string :as str]))

(defn build-grid-defroutes
  "Genera rutas especificas para el grid para una tabla"
  [table]
  (let [data (str
              "  (GET \"/admin/" table "\" params [] (" table "-controller/" table " params))\n"
              "  (GET \"/admin/" table "/edit/:id\" [id] (" table "-controller/" table "-edit id))\n"
              "  (POST \"/admin/" table "/save\" params [] (" table "-controller/" table "-save params))\n"
              "  (GET \"/admin/" table "/add\" params [] (" table "-controller/" table "-add params))\n"
              "  (GET \"/admin/" table "/delete/:id\" [id] (" table "-controller/" table "-delete id))")]
    data))

(defn build-defroutes
  "Genera rutas especificas para un dashboard para una tabla"
  [table]
  (let [data (str
              "  (GET \"/" table "\" params [] (" table "-dashboard/" table " params))")]
    data))

(defn build-defroutes-reporte
  "Genera rutas especificas para un dashboard para una tabla"
  [table]
  (let [data (str
              "  (GET \"/reportes/" table "\" params [] (" table "-reporte/" table " params))")]
    data))

(defn build-grid-require
  "Genera archivos requeridos para un grid para una tabla"
  [table]
  (let [data (str "   [sk.handlers.admin." table ".controller :as " table "-controller]")]
    data))

(defn build-reporte-require
  "Genera archivos requeridos para un reporte para un controller"
  [table]
  (let [data (str "   [sk.handlers.reportes." table ".controller :as " table "-reporte]")]
    data))

(defn build-require
  "Genera archivos requeridos para un dashboard para una tabla"
  [table]
  (let [data (str "   [sk.handlers." table ".controller :as " table "-dashboard]")]
    data))

(defn insert-lines-after-search [file-path lines-to-insert search-str]
  (let [file-contents (slurp file-path)
        lines (str/split-lines file-contents)
        found-index (first (keep-indexed (fn [idx line]
                                           (when (str/includes? line search-str) idx))
                                         lines))
        insert-at (if found-index (inc found-index) (count lines))
        lines-before (take insert-at lines)
        lines-after (drop insert-at lines)
        new-lines (concat lines-before lines-to-insert lines-after)
        new-file-contents (str/join "\n" new-lines)]
    (spit file-path new-file-contents)))

(defn process-grid
  "Actualiza proutes.clj"
  [table]
  (insert-lines-after-search "src/sk/routes/proutes.clj"
                             [(build-grid-require table)]
                             "(:require")
  (insert-lines-after-search "src/sk/routes/proutes.clj"
                             [(build-grid-defroutes table)]
                             "(defroutes proutes"))
(defn process-dashboard
  "Actualiza proutes.clj"
  [table]
  (insert-lines-after-search "src/sk/routes/proutes.clj"
                             [(build-require table)]
                             "(:require")
  (insert-lines-after-search "src/sk/routes/proutes.clj"
                             [(build-defroutes table)]
                             "(defroutes proutes"))

(defn process-reporte
  "Actualiza proutes.clj"
  [table]
  (insert-lines-after-search "src/sk/routes/proutes.clj"
                             [(build-reporte-require table)]
                             "(:require")
  (insert-lines-after-search "src/sk/routes/proutes.clj"
                             [(build-defroutes-reporte table)]
                             "(defroutes proutes"))
(comment
  (build-defroutes-reporte "users")
  (build-reporte-require "users")
  (process-reporte "users")
  (process-dashboard "amigos")
  (process-grid "amigos"))
