(ns sbsk-tools.routes.events
  (:require [net.cgrand.enlive-html :as e]
            [clojure.string :as str]
            [clj-time.core :as time]
            [clj-time.format :as timef])
  (:import (java.net URL)))

(defn month-name-to-number [month-name]
  (case month-name
    "jan." 1
    "feb." 2
    "mars" 3
    "april" 4
    "mai" 5
    "juni" 6
    "juli" 7
    "aug." 8
    "sep." 9
    "okt." 10
    "nov." 11
    "des." 12))

;; Do some fancy caching and something
(defonce event-page-data
         (e/html-resource (URL. "http://nor.service.ianseo.net/General/CompetitionList.php?Lang=en")))

(defn retrieve-date [content]
  (.trim (first (:content (nth content 2)))))

(defn parse-to-date [date-parts]
  (.toDate (time/date-time (Integer/parseInt (nth date-parts 2)) (month-name-to-number (second date-parts)) (Integer/parseInt (first date-parts)))))
  ;(timef/unparse (timef/formatters :basic-date-time) (time/date-time (Integer/parseInt (nth date-parts 2)) (month-name-to-number (second date-parts)) (Integer/parseInt (first date-parts)))))

(defn map-line [line]
  "Converts a line into a more reasonable struct"
  (let [content (:content line)]
    {
     :event-id        (first (:content (first (:content (nth content 1)))))
     :organizer-short (first (:content (first (:content (nth content 3)))))
     :organizer-full  (str/trim (if (nil? (second (:content (nth content 3)))) "" (subs (second (:content (nth content 3))) 3)))
     :competition     (first (:content (nth content 4)))
     :date            (parse-to-date (str/split (retrieve-date content) #"\s"))
     :comments        (first (:content (nth content 5)))}))



(defn fetch-event-page []
  "Retrieves all events from the Norwegian event list."
  (map map-line (e/select event-page-data [:tr.status1])))

(defn scrape-event-data [page-data]
  page-data)