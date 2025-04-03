(ns sk.handlers.admin.users.controller-test
  (:require
   [clojure.test :refer :all]
   [sk.handlers.admin.users.controller :refer [users users-edit users-save users-add users-delete]]
   [sk.handlers.admin.users.model :as model]
   [sk.handlers.admin.users.view :as view]
   [sk.layout :as layout]
   [sk.models.crud :as crud]
   [sk.models.util :as util]))

(deftest test-users
  (testing "users function should return the correct application layout"
    (with-redefs [util/get-session-id (fn [] 1)
                  model/get-users (fn [] [{:id 1 :name "John"}])
                  view/users-view (fn [title rows] (str "View: " title " Rows: " rows))
                  layout/application (fn [title ok js content] {:title title :ok ok :js js :content content})]
      (let [response (users nil)]
        (is (= {:title "Mantenimiento de Usuarios"
                :ok 1
                :js nil
                :content "View: Mantenimiento de Usuarios Rows: [{:id 1, :name \"John\"}]"}
               response))))))

(deftest test-users-edit
  (testing "users-edit function should return the correct application layout"
    (with-redefs [util/get-session-id (fn [] 1)
                  model/get-user (fn [id] {:id id :name "John"})
                  model/get-users (fn [] [{:id 1 :name "John"}])
                  view/users-edit-view (fn [title row rows] (str "Edit View: " title " Row: " row " Rows: " rows))
                  view/users-modal-script (fn [] "Modal Script")
                  layout/application (fn [title ok js content] {:title title :ok ok :js js :content content})]
      (let [response (users-edit 1)]
        (is (= {:title "Modificar Usuario"
                :ok 1
                :js "Modal Script"
                :content "Edit View: Modificar Usuario Row: {:id 1, :name \"John\"} Rows: [{:id 1, :name \"John\"}]"}
               response))))))

(deftest test-users-save
  (testing "users-save function should handle save results correctly"
    (with-redefs [crud/build-form-save (fn [_ _] true)
                  layout/error-404 (fn [msg redirect] {:message msg :redirect redirect})]
      (let [response (users-save {:params {:id 1 :name "John"}})]
        (is (= {:message "Record se processo correctamente!" :redirect "/admin/users"} response))))
    (with-redefs [crud/build-form-save (fn [_ _] false)
                  layout/error-404 (fn [msg redirect] {:message msg :redirect redirect})]
      (let [response (users-save {:params {:id 1 :name "John"}})]
        (is (= {:message "No se pudo procesar el record!" :redirect "/admin/users"} response))))))

(deftest test-users-add
  (testing "users-add function should return the correct application layout"
    (with-redefs [util/get-session-id (fn [] 1)
                  model/get-users (fn [] [{:id 1 :name "John"}])
                  view/users-add-view (fn [title row rows] (str "Add View: " title " Row: " row " Rows: " rows))
                  view/users-modal-script (fn [] "Modal Script")
                  layout/application (fn [title ok js content] {:title title :ok ok :js js :content content})]
      (let [response (users-add {:params {:id nil :name nil}})]
        (is (= {:title "Nuevo Usuario"
                :ok 1
                :js "Modal Script"
                :content "Add View: Nuevo Usuario Row:  Rows: [{:id 1, :name \"John\"}]"}
               response))))))

(deftest test-users-delete
  (testing "users-delete function should handle delete results correctly"
    (with-redefs [crud/build-form-delete (fn [_ _] true)
                  layout/error-404 (fn [msg redirect] {:message msg :redirect redirect})]
      (let [response (users-delete 1)]
        (is (= {:message "Record se processo correctamente!" :redirect "/admin/users"} response))))
    (with-redefs [crud/build-form-delete (fn [_ _] false)
                  layout/error-404 (fn [msg redirect] {:message msg :redirect redirect})]
      (let [response (users-delete 1)]
        (is (= {:message "No se pudo processar el record!" :redirect "/admin/users"} response))))))