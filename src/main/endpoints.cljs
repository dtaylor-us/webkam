(ns endpoints
  (:require-macros [cljs.core.async.macros :refer [alt! go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async
             :as    async
             :refer [<! put! chan close! timeout]]
            [clojure.pprint :refer [pprint]]
            [taoensso.timbre :as log]
            [com.rpl.specter :as s]
            [cljs.nodejs :as nodejs]
            [bidi.bidi :refer [path-for]]
            [fast-twitch.nav :refer [cached-routes]]
            [fast-twitch.os :as os]
            [fast-twitch.web-api :as web]
            [ui.components.core :as c]
            [ui.pages.core :as p]
            [ui.templates.core :as t]
            [services.core :as svc :refer [<get-document
                                           <get-document-and-related
                                           <list-content
                                           <list-topics]]))

(defn home
  [req]
  (go
    (alt!
      (apply <list-topics (svc/topics))
      ([data]
       (web/send :html
                 [t/default-template-ui
                  {:title   "Welcome to Kamestery!"
                   :content [p/home data]}]))
      (timeout 8000)
      (do
        (log/debug "ERROR:::")
        (web/send :html
                  [t/default-template-ui
                   {:title   "Welcome to Kamestery!"
                    :content [p/home []]}])))
    ))

;; user
(defn user-login
  [req]
  (let [{:keys [csrf-token]} req
        data {:csrf-token csrf-token}]
    (web/send :html
              [t/default-template-ui
               {:title   "User Login"
                :content [p/login data]}])))


(defn authenticate [req]
  (let [{:keys [body]} req]
    (do
      (log/debug body)
      (svc/authenticate body)
      (web/redirect :home))))

(defn user-register
  [req]
  (let [{:keys [csrf-token]} req
        data {:csrf-token csrf-token}]
    (web/send :html
              [t/default-template-ui
               {:title   "User Registration"
                :content [p/register data]}])))

(defn enroll [req]
  (let [{:keys [body csrf-token]} req]
    (do
      (svc/enroll body)
      (web/redirect :login))))

;; content
(defn document [req]
  (go
   (let [title   (-> req :route-params :title)
         topic   (-> req :route-params :topic)]
     (alt!
       (<get-document-and-related topic title)
      ([resp]
        (do (log/debug "RESPONSE::::" resp)
          (web/send :html
                    [t/default-template-ui
                     {:title title
                      :content [p/document resp]}])))
      (timeout 2000)
      (do
        (log/debug "ERROR:::")
        (web/send :html
                  [t/default-template-ui
                   {:title title
                    :content [p/home []]}]))))))

(defn list-content [req]
  (go
   (let [topic   (-> req :route-params :topic)]
     (alt!
      (<list-content topic)
      ([resp]
        (do
          (log/debug "RESONSE::::" resp)
          (web/send :html
                    [t/default-template-ui
                     {:title topic
                      :content [p/content resp]}])))
      (timeout 2000)
      (do
        (log/debug "ERROR:::")
        (web/send :html
                  [t/default-template-ui
                   {:title "List of Content"
                    :content [p/home {}]}]))))))

;; Application LifeCycle
(defn app-start
  [req]
  (web/send "Started"))

(defn check-health
  [req]
  (web/send "Healthy!"))

(defn app-stop
  [req]
  (web/send "Stopping..."))


