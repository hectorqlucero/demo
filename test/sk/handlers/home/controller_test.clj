(ns sk.handlers.home.controller-test
  (:require
   [clojure.test :refer :all]
   [sk.handlers.home.controller :refer :all]
   [sk.handlers.home.model :as model]
   [sk.handlers.home.view :as view]
   [sk.layout :as layout]
   [noir.session :as session]
   [noir.util.crypt :as crypt]
   [sk.models.util :refer [get-session-id]])) ;; Import get-session-id

(deftest test-main
  (testing "main function should return the correct application layout"
    (with-redefs [get-session-id (fn [] 1)
                  layout/application (fn [title ok js content] {:title title :ok ok :js js :content content})]
      (let [response (main nil)]
        (is (= {:title "home"
                :ok 1
                :js nil
                :content nil}
               response))))
    (with-redefs [get-session-id (fn [] 0)
                  layout/application (fn [title ok js content] {:title title :ok ok :js js :content content})]
      (let [response (main nil)]
        (is (= {:title "home"
                :ok 0
                :js nil
                :content [:h2.text-primary "clic en Entrar al sitio!"]}
               response))))))

(deftest test-login
  (testing "login function should return the correct application layout"
    (with-redefs [get-session-id (fn [] 1)
                  view/main-view (fn [title] (str "Main View: " title))
                  layout/application (fn [title ok js content] {:title title :ok ok :js js :content content})]
      (let [response (login nil)]
        (is (= {:title "Entrar al sitio"
                :ok 1
                :js nil
                :content "Main View: Entrar al sitio"}
               response))))))

(deftest test-login-user
  (testing "login-user function should handle login correctly"
    (with-redefs [model/get-user (fn [_] [{:id 1 :password "hashed-password" :active "T"}])
                  crypt/compare (fn [password hashed] (= password "correct-password"))
                  session/put! (fn [_ _] nil)
                  noir.response/redirect (fn [url] {:redirect url})
                  layout/error-404 (fn [msg redirect] {:message msg :redirect redirect})]
      (let [response (login-user {:params {:username "user" :password "correct-password"}})]
        (is (= {:redirect "/"} response)))
      (let [response (login-user {:params {:username "user" :password "wrong-password"}})]
        (is (= {:message "Incorrect Username and or Password!" :redirect "/"} response)))
      (with-redefs [model/get-user (fn [_] [{:id 1 :password "hashed-password" :active "F"}])]
        (let [response (login-user {:params {:username "user" :password "correct-password"}})]
          (is (= {:message "User is not active!" :redirect "/"} response)))))))

(deftest test-change-password
  (testing "change-password function should return the correct application layout"
    (with-redefs [get-session-id (fn [] 1)
                  view/change-password-view (fn [title] (str "Change Password View: " title))
                  layout/application (fn [title ok js content] {:title title :ok ok :js js :content content})]
      (let [response (change-password nil)]
        (is (= {:title "Cambiar Contraseña"
                :ok 1
                :js nil
                :content "Change Password View: Cambiar Contraseña"}
               response))))))

(deftest test-logoff-user
  (testing "logoff-user function should clear session and redirect"
    (with-redefs [session/clear! (fn [] nil)
                  layout/error-404 (fn [msg redirect] {:message msg :redirect redirect})]
      (let [response (logoff-user nil)]
        (is (= {:message "Logoff successfully" :redirect "/"} response))))))