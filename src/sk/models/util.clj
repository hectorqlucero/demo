(ns sk.models.util
  (:require [noir.session :as session]
            [clj-jwt.core :refer [jwt str->jwt to-str verify]]
            [clj-time.coerce :as c]
            [clj-time.format :as f]
            [clj-time.core :as t]
            [clojure.string :as st]
            [sk.migrations :refer [config]]
            [sk.models.crud :refer [Query db]]))

(def tz (t/time-zone-for-id (:tz config)))

(def internal-date-parser (f/formatter tz "YYYY-MM-dd" "MM/dd/YYYY"))

(def external-date-parser (f/formatter tz "MM/dd/YYYY" "YYYY-MM-dd"))

(def external-ld-parser (f/formatter tz "E M d Y" "YYYY-MM-dd"))

(def external-dt-parser (f/formatter tz "E M d Y  h:m a" "YYYY-MM-dd"))

(def internal-time-parser (f/formatter tz "HH:mm:ss" "H:k:s"))

(def external-time-parser (f/formatter tz "hh:mm:ss a" "H:k:s"))

(defn get-base-url [request]
  (str (subs (str (:scheme request)) 1) "://" (:server-name request) ":" (:server-port request)))

(defn get-reset-url [request token]
  (str (get-base-url request) "/reset_password/" token))

;; Start jwt token
(defn create-token
  "Creates jwt token with 10 minutes expiration time"
  [k]
  (let [data {:iss k
              :exp (t/plus (t/now) (t/minutes 10))
              :iat (t/now)}]
    (-> data jwt to-str)))

(defn decode-token
  "Decodes jwt token"
  [t]
  (-> t str->jwt :claims))

(defn verify-token
  "Verifies that token is good"
  [t]
  (-> t str->jwt verify))

(defn check-token
  "Checks if token verifes and it's not expired, returns id or nil"
  [t]
  (let [token (decode-token t)
        exp (:exp token)
        cexp (c/to-epoch (t/now))
        token-id (:iss token)]
    (if (and (verify-token t) (> exp cexp))
      token-id
      nil)))
;; End jwt token

(defn parse-int
  "Attempt to convert to integer or on error return nil or itself if it's already an integer"
  [s]
  (try
    (Integer. s)
    (catch Exception _ (if (integer? s) s nil))))

(defn current_date
  "Get current date formatted MM/dd/YYYY"
  []
  (f/unparse external-date-parser (t/now)))

(defn current_date_long
  "Get current date formatted Sat 11 12 2016"
  []
  (f/unparse external-ld-parser (t/now)))

(defn current_date_time
  "Get current date formatted Sat 11 12 2016 at 18:20 PM"
  []
  (f/unparse external-dt-parser (t/now)))

(defn current_time
  "Get simple date formatted time h:m:s a"
  []
  (f/unparse external-time-parser (t/now)))

(defn current_time_internal
  "Get current time formatted H:k:s"
  []
  (f/unparse internal-time-parser (t/now)))

(defn previous_year []
  (t/year (t/minus (t/now) (t/years 1))))

(defn current_year
  "GEt simple date formatted year"
  []
  (t/year (t/from-time-zone (t/now) tz)))

(defn current_month
  "Get simple date formatted month :MM"
  []
  (t/month (t/from-time-zone (t/now) tz)))

(defn current_day
  "Get Simple Date formatted :dd"
  []
  (t/day (t/from-time-zone (t/now) tz)))

(defn get_date_external
  "Convert date instance to external MM/dd/YYYY"
  [d]
  (f/unparse external-date-parser d))

(defn get-month-name [month]
  (cond
    (= month 1) "Enero"
    (= month 2) "Febrero"
    (= month 3) "Marzo"
    (= month 4) "Abril"
    (= month 5) "Mayo"
    (= month 6) "Junio"
    (= month 7) "Julio"
    (= month 8) "Agosto 4"
    (= month 9) "Septiembre"
    (= month 10) "Octubre"
    (= month 11) "Noviembre"
    (= month 12) "Diciembre"))

(defn get-session-id []
  (try
    (if (session/get :user_id) (session/get :user_id) 0)
    (catch Exception e (.getMessage e))))

(defn user-level []
  (let [id   (get-session-id)
        type (if (nil? id)
               nil
               (:level (first (Query db ["select level from users where id = ?" id]))))]
    type))

(defn user-email []
  (let [id    (get-session-id)
        email (if (nil? id)
                nil
                (:username (first (Query db ["select username from users where id = ?" id]))))]
    email))

(defn user-name []
  (let [id (get-session-id)
        username (if (nil? id)
                   nil
                   (:name (first (Query db ["select CONCAT(firstname,' ',lastname) as name from users where id = ?" id]))))]
    username))

(defn zpl
  "n=number,c=zeropad number"
  [n c]
  (loop [s (str n)]
    (if (< (.length s) c)
      (recur (str "0" s))
      s)))

(defn zpr
  "n=number,c=zeropad number"
  [n c]
  (loop [s (str n)]
    (if (< (.length s) c)
      (recur (str s "0"))
      s)))

(defn build-field [options]
  [:div {:style "margin-bottom:10px;"}
   [:input options]])

(defn build-button [options]
  [:div {:style "text-align:center;padding:5px 0"}
   (if (list? options)
     (for [option options]
       [:a option])
     [:a options])])

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
    (st/join ", "
             (filter #(not (st/blank? %))
                     (conj []
                           (when (> wk 0) (str wk " semana"))
                           (when (> d 0) (str d " dia"))
                           (when (> hr 0) (str hr " hr"))
                           (when (> min 0) (str min " min"))
                           (when (> sec 0) (str sec " seg")))))))
;; End format seconds->duration

(comment
  (seconds->duration 992932))
