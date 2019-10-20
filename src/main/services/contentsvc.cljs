(ns services.contentsvc
  (:require-macros [cljs.core.async.macros :refer [alt! go]])
  (:require [taoensso.timbre :as log]
            [cljs-http.client :as http]
            [graphql-query.core :refer [graphql-query]]
            [cljs.core.async
             :as    async
             :refer [<! put! chan close! timeout pipeline-async to-chan]]
            [fast-twitch.web-api :as web]
            [services.urls :as urls]
            ["slugify" :as slugify]))

(defn get-topics []
  ["history" "education" "language" "africa"])

(defn <get-document
  "[topic title] Retrieve document by topic and title."
  [topic title]
  (log/debug "Retrieving Content from Topic: " topic " with Title: " title)
  (let [{:keys [url namespace]} (urls/url-config)
        query-str (graphql-query {:queries
                                  [[:getdocument
                                    {:dockey {:Topic (str namespace ":##:" (slugify topic))
                                              :DocumentID (str namespace ":##:"
                                                               (slugify topic) ":##:" (slugify title))}}
                                    [:Topic :DocumentID :UserID :Identifier :Slug
                                     :Publish :Tags :FiltreVisuel :Niveau :Score
                                     :Title :Version :Body :CreatedAt :UpdatedAt]]]
                                  })]
    (log/debug "Query: " query-str)
    (go
      (-> (http/post
           url
           {:json-params
            {:query query-str}})
          <!
          :body
          :data
          :getdocument))
    ))

(defn <get-document-and-related
  "[topic title] Retrieve document and related by topic and title."
  [topic title]
  (log/debug "Retrieving Content from Topic: " topic " with Title: " title)
  (let [{:keys [url namespace]} (urls/url-config)
        query-str (graphql-query {:queries
                                  [{:query/alias :doc
                                    :query/data
                                    [:getdocument
                                     {:dockey {:Topic (str namespace ":##:" (slugify topic))
                                               :DocumentID (str namespace ":##:"
                                                                (slugify topic) ":##:" (slugify title))}}
                                     [:Topic :DocumentID :UserID :Identifier :Slug
                                      :Publish :Tags :FiltreVisuel :Niveau :Score
                                      :Title :Version :Body :CreatedAt :UpdatedAt]]}
                                   {:query/alias :related
                                    :query/data
                                    [:querydocument
                                     {:docquery {:Name (str namespace ":##:" (slugify topic))}}
                                     [:Topic :DocumentID :UserID :Identifier :Slug
                                      :Publish :Tags :FiltreVisuel :Niveau :Score
                                      :Title :Version :Body :CreatedAt :UpdatedAt]]}]
                                  })]
    (log/debug "Query: " query-str)
    (go
      (-> (http/post
           url
           {:json-params
            {:query query-str}})
          <!
          :body
          :data))
    ))

(defn <list-content
  [topic]
  (log/debug "Listing Content from Topic: " topic)
  (let [{:keys [url namespace]} (urls/url-config)
        query-str (graphql-query {:queries
                                  [[:querydocument
                                    {:docquery {:Name (str namespace ":##:" (slugify topic))}}
                                    [:Topic :DocumentID :UserID :Identifier :Slug
                                     :Publish :Tags :FiltreVisuel :Niveau :Score
                                     :Title :Version :Body :CreatedAt :UpdatedAt]]]
                                  })]
    (log/debug "Query: " query-str)
    (go
      (-> (http/post
           url
           {:json-params
            {:query query-str}})
          <!
          :body
          :data
          :querydocument))))

(defn <list-topics [& topics]
  (log/debug "Listing Content from Topics: " topics)
  (let [{:keys [url namespace]} (urls/url-config)
        query-str (graphql-query {:queries
                                  (into []
                                        (map #(hash-map
                                               :query/alias (keyword %)
                                               :query/data
                                               [:querydocument
                                                {:docquery {:Name (str namespace ":##:" (slugify %))}}
                                                [:Topic :DocumentID :UserID :Identifier :Slug
                                                 :Publish :Tags :FiltreVisuel :Niveau :Score
                                                 :Title :Version :Body :CreatedAt :UpdatedAt]]) topics))
                                  })]
    (log/debug "Query: " query-str)
    (go
      (-> (http/post
           url
           {:json-params
            {:query query-str}})
          <!
          :body
          :data))))


