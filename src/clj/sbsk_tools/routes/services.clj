(ns sbsk-tools.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [net.cgrand.enlive-html :as e])
  (:import (java.net URL)))

;; Do some fancy caching and something
(defonce event-page-data
         (e/html-resource (URL. "http://nor.service.ianseo.net/General/CompetitionList.php?Lang=en")))

(defn map-line [line]
  "Converts a line into a more reasonable struct"
  (let [content (:content line)]
    {
     :event-id        (first (:content (first (:content (nth content 1)))))
     :organizer-short (first (:content (first (:content (nth content 3)))))
     :date            (.trim (first (:content (nth content 2)))) ;; TODO: Parse to date?
     :organizer-full  (second (:content (nth content 3)))   ;; TODO: Remove first 3 chars and trim()
     :competition     (first (:content (nth content 4)))
     :comments        (first (:content (nth content 5)))
     }
    ))

(defn fetch-event-page []
  "Retrieves all events from the Norwegian event list."
  (map map-line (e/select event-page-data [:tr.status1])))

(defn scrape-event-data [page-data]
  page-data)

(defapi service-routes
        {:swagger {:ui   "/swagger-ui"
                   :spec "/swagger.json"
                   :data {:info {:version     "1.0.0"
                                 :title       "SBSK Tools API"
                                 :description "Various tools used by Sverresborg Bueskyttere Archery club"}}}}

        (context "/api/events" []
                 :tags ["Events"]
                 (GET "/events" []
                      :summary "Returns all events in Norway for the current year"
                      (ok (scrape-event-data (fetch-event-page))))))