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
(def shown-events (r/atom ()))

(defn generate-shown-events [all-events shown-events]
  (reset! shown-events (filter event-valid-according-to-view-config (filter event-in-filter-list @events))))

(defn form-filters []
  [:div.row.form-filters
   [:label
    [:input#only-future {:type           "checkbox"
                         :defaultChecked (:only-future? @view-configuration)
                         :on-change      (fn [ev]
                                           (swap! view-configuration assoc :only-future? (true? (-> ev .-target .-checked)))
                                           (generate-shown-events @events shown-events))}]
    "Vis kun kommende stevner"]])

(defn events-page []
  (GET "/api/events" {:handler (fn [incoming]
                                 (reset! events incoming)
                                 (generate-shown-events @events shown-events))})
  (fn []
    [:div.container
     (form-filters)
     [:div.row
      (if (empty? @shown-events)
        [:div.col-md-12 "Laster stevner..."]
        (for [ev @shown-events]
          ^{:key (:event-id ev)}
          [:div.col-md-12
           [:div.row
            [:div.col-md-12
             [:h4 (str (:date ev) " - " (:competition ev) " - " (:organizer-full ev))]]]
           [:div.row.last-in-item
            [:div.col-md-3
             [:span (str (:date ev))]]
            [:div.col-md-3
             [:span (:competition ev)]]
            [:div.col-md-6
             [:span (:comments ev)]]]]))]]))
