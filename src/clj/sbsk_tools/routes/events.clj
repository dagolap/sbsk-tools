(ns sbsk-tools.routes.events
  (:require [net.cgrand.enlive-html :as e]
            [clojure.string :as str]
            [clj-time.core :as time])
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

;; TODO: Do some fancy caching and something
;; ----------------
;; Fetch web page

(defonce ianseo-base-url "http://nor.service.ianseo.net")
(defonce event-page-data
         (e/html-resource (URL. (str ianseo-base-url "/General/CompetitionList.php?Lang=en"))))

;; ----------------
;; Helper functions to retrieve correct data from a line
(defn retrieve-date [content]
  (.trim (first (:content (nth content 2)))))

(defn retrieve-organizer [content]
  (second (:content (nth content 3))))

(defn date-from-parts [date-parts]
  (.toDate (time/date-time (Integer/parseInt (nth date-parts 2)) (month-name-to-number (second date-parts)) (Integer/parseInt (first date-parts)))))

;; -----------------
;; Map a line to a reasonable data structure
(defn map-event [line]
  "Converts a line into a more reasonable struct"
  (let [content (:content line)]
    {
     :event-id        (first (:content (first (:content (nth content 1)))))
     :organizer-short (first (:content (first (:content (nth content 3)))))
     :organizer-full  (str/trim (if (nil? (retrieve-organizer content)) "" (subs (retrieve-organizer content) 3)))
     :competition     (first (:content (nth content 4)))
     :date            (date-from-parts (str/split (retrieve-date content) #"\s"))
     :comments        (first (:content (nth content 5)))
     :statuslink      (str ianseo-base-url (:href (:attrs (first (:content (nth content 0))))))}))

(defn map-all-events []
  "Retrieves all events from the Norwegian event list."
  (map map-event (e/select event-page-data [:tr.status1])))
