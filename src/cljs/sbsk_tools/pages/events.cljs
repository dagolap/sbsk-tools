(ns sbsk-tools.pages.events
  (:require [reagent.core :as r]
            [cljs-time.core :as time]
            [cljs-time.format :as timef]
            [ajax.core :refer [GET POST]]))

(def view-configuration (r/atom {
                                 :only-future?   true
                                 :filtered-clubs ["Geita" "Sverr" "Tustn" "Yrjar" "Stoks" "VIGRA"]}))

(defn event-in-filter-list [ev]
  (some #(= (:organizer-short ev) %) (:filtered-clubs @view-configuration)))

(defn event-valid-according-to-view-config [ev]
(if (:only-future? @view-configuration)
  (>= (:date ev) (time/today-at-midnight))
  true))

(def events (r/atom ()))

(defn events-page []
  (GET "/api/events" {:handler (fn [incoming] (reset! events incoming))})
  (fn []
    [:div.container
     [:div.row
      (if (empty? @events)
        [:div.col-md-12 "Laster stevner..."]
        (for [ev (filter event-valid-according-to-view-config (filter event-in-filter-list @events))]
          ^{:key (:event-id ev)}
          [:div.col-md-12
           [:div.row
            [:div.col-md-12
             [:h4 (str (:date ev) " - " (:competition ev) " - " (:organizer-full ev))]]]
           [:div.row.last-in-item
            [:div.col-md-3
             [:span (str (:date ev))]
             ]
            [:div.col-md-3
             [:span (:competition ev)]]
            [:div.col-md-6
             [:span (:comments ev)]]]]))]]))
