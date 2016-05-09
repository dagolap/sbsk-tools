(ns sbsk-tools.routes.events
  (:require [net.cgrand.enlive-html :as e]
            [clojure.string :as str]
            [clj-time.core :as time])
  (:import (java.net URL)))

(defonce ianseo-base-url "http://nor.service.ianseo.net")

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


;; ----------------
;; Fetch web page
(def event-page-data
  (let [latest-source (e/html-resource (URL. (str ianseo-base-url "/General/CompetitionList.php?Lang=en")))]
  {:competition-source                  latest-source
   :competitions                        (map map-event (e/select latest-source [:tr.status1]))
   :competition-source-cached-timestamp (time/now)}))

(defn get-page-data []
  (when (> (time/in-millis (time/interval (:competition-source-cached-timestamp event-page-data) (time/now))) 600000)
    (let [latest-source (e/html-resource (URL. (str ianseo-base-url "/General/CompetitionList.php?Lang=en")))]
    (def event-page-data (assoc event-page-data :competition-source latest-source
                                                :competitions (map map-event (e/select latest-source [:tr.status1]))
                                                :competition-source-cached-timestamp (time/now)))))
  (:competitions event-page-data))


;; -------------------
;; Used by web service endpoints
(defn map-all-events []
  "Retrieves all events from the Norwegian event list."
  (get-page-data))

(defn latest-competition-cache []
  "Returns latest cache time for competition page"
  (.toDate (:competition-source-cached-timestamp event-page-data)))