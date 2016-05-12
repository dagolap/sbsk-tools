(ns sbsk-tools.events.filters
  (:require #?(:clj [clj-time.core :as time]
               :cljs [cljs-time.core :as time])))

(defn organizer-in-list [event filtered-organizers]
  (some #(= (:organizer-short event) %) filtered-organizers))

(defn date-in-future [event]
  (>= (:date event) (time/today-at-midnight)))