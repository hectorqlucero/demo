(ns test-util
  (:require
   [clojure.test :refer :all]
   [sk.models.util :refer [get-session-id user-level user-email user-name seconds->string image-link]]
   [sk.models.crud :refer [Query db]]
   [noir.session :as session]
   [clojure.core :refer [random-uuid]])) ;; Ensure random-uuid is imported

(deftest test-get-session-id
  (testing "get-session-id should return user_id from session or 0 if not present"
    (let [mock-session (atom {})]
      ;; Mock noir.session functions
      (with-redefs [session/get (fn [k] (@mock-session k))
                    session/put! (fn [k v] (swap! mock-session assoc k v))
                    session/remove! (fn [k] (swap! mock-session dissoc k))]
        ;; Test session behavior
        (session/put! :user_id 42)
        (is (= 42 (get-session-id)))
        (session/remove! :user_id)
        (is (= 0 (get-session-id)))))))

(deftest test-user-level
  (testing "user-level should return the user's level or nil if not found"
    (with-redefs [db (atom {}) ;; Mock db
                  Query (fn [_ _] [{:level "admin"}])]
      (is (= "admin" (user-level))))
    (with-redefs [db (atom {}) ;; Mock db
                  Query (fn [_ _] [])]
      (is (nil? (user-level))))))

(deftest test-user-email
  (testing "user-email should return the user's email or nil if not found"
    (with-redefs [db (atom {}) ;; Mock db
                  Query (fn [_ _] [{:username "test@example.com"}])]
      (is (= "test@example.com" (user-email))))
    (with-redefs [db (atom {}) ;; Mock db
                  Query (fn [_ _] [])]
      (is (nil? (user-email))))))

(deftest test-user-name
  (testing "user-name should return the user's full name or nil if not found"
    (with-redefs [db (atom {}) ;; Mock db
                  Query (fn [_ _] [{:name "John Doe"}])]
      (is (= "John Doe" (user-name))))
    (with-redefs [db (atom {}) ;; Mock db
                  Query (fn [_ _] [])]
      (is (nil? (user-name))))))

(deftest test-seconds->string
  (testing "seconds->string should convert seconds to a human-readable string"
    (is (= "1 day 1 hour 1 minute " (seconds->string 90061)))
    (is (= "0 days 0 hours 0 minutes " (seconds->string 0)))))

(deftest test-image-link
  (testing "image-link should generate an HTML img tag with the correct path"
    (with-redefs [db (atom {}) ;; Mock db
                  Query (fn [_ _] [{:path "/uploads/"}])
                  random-uuid (fn [] "1234-uuid")] ;; Mock random-uuid
      (is (= "<img src='/uploads/test.png?1234-uuid' alt='test.png' width=32 height=32>"
             (image-link "test.png"))))))