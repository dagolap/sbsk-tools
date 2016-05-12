(ns sbsk-tools.pages.events
  (:require [reagent.core :as r]
            [sbsk-tools.events.filters :as eventfilters]
            [sbsk-tools.events.facts :as facts]
            [cljs-time.core :as time]
            [cljs-time.coerce :as timec]
            [ajax.core :refer [GET POST]]))

(def view-configuration (r/atom {
                                 :only-future?    true
                                 :only-midtnorge? true
                                 :filtered-clubs  (:clubs (:midtnorge facts/club-facts))}))

(defn event-in-filter-list [ev]
  (if (:only-midtnorge? @view-configuration)
    (eventfilters/organizer-in-list? ev (:filtered-clubs @view-configuration))
    true))


(defn event-valid-according-to-view-config [ev]
  (if (:only-future? @view-configuration)
    (eventfilters/date-in-future? ev)
    true))

(def events (r/atom ()))
(def shown-events (r/atom ()))

(defn generate-shown-events [all-events shown-events]
  (reset! shown-events (filter event-valid-according-to-view-config (filter event-in-filter-list @events))))


(defn date-to-string [date]
  (let [parsed-date (timec/from-date date)]
    (str (time/day parsed-date) ". " (facts/month-to-name-lower (time/month parsed-date)))
    ))


(defn form-filters []
  [:div
   [:div.row.form-filters
    [:div.col-md-12
     [:div.checkbox.checkbox-success
      [:input#only-future {:type           "checkbox"
                           :defaultChecked (:only-future? @view-configuration)
                           :on-change      (fn [ev]
                                             (swap! view-configuration assoc :only-future? (true? (-> ev .-target .-checked)))
                                             (generate-shown-events @events shown-events))}]
      [:label {:for "only-future"} "Kun fremtidige stevner"]]
     [:div.checkbox.checkbox-success
      [:input#only-midtnorge {:type           "checkbox"
                              :defaultChecked (:only-midtnorge? @view-configuration)
                              :on-change      (fn [ev]
                                                (swap! view-configuration assoc :only-midtnorge? (true? (-> ev .-target .-checked)))
                                                (generate-shown-events @events shown-events))}]
      [:label {:for "only-midtnorge"} "Kun stevner i midtnorge"]
      ]]]])


(defn event-item [ev]
  [:li
   [:div.timeline-badge.success [:i (facts/month-to-name (time/month (timec/from-date (:date ev))))]]
   [:div.timeline-panel
    [:div.timeline-heading
     [:h4.timeline-title (str (date-to-string (:date ev)) " - " (:organizer-full ev))]]
    [:div.timeline-body
     [:div
      [:div (:competition ev)]
      [:div (:comments ev)]]
     [:span.pull-right.status
      (if (eventfilters/has-invitation? ev)
        [:a.btn.btn-success {:href (:statuslink ev)} "Invitasjon"]
        nil)]]]])

(defn event-list []
  (if (empty? @shown-events)
    [:div.container
     [:div.row
      [:div.col-md-12 "Laster stevner..."]]]

    [:div.container
     [:ul.timeline
      (for [ev @shown-events]
        ^{:key (:event-id ev)}
        [event-item ev])]]))

(defn events-page []
  (GET "/api/events" {:handler (fn [incoming]
                                 (reset! events incoming)
                                 (generate-shown-events @events shown-events))})
  (fn []
    [:div
     [:div.container
      (form-filters)]
     [event-list]]))
