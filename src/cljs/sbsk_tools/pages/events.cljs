(ns sbsk-tools.pages.events
  (:require [reagent.core :as r]
            [cljs-time.core :as time]
            [cljs-time.coerce :as timec]
            [ajax.core :refer [GET POST]]))

(def view-configuration (r/atom {
                                 :only-future?    true
                                 :only-midtnorge? true
                                 :filtered-clubs  ["Bøfjo" "Geita" "HEGIL" "Harøy" "HITRA" "Hunde"
                                                   "GOMA" "Yrjar" "Levah" "Lyngs" "Malvi" "Molde"
                                                   "Roan" "SALSN" "Stoks" "Surna" "Sverr" "Tustn"
                                                   "Ulvun" "Verda" "Vestn" "VIGRA" "Volla" "Ålesu"]}))

(defn event-in-filter-list [ev]
  (if (:only-midtnorge? @view-configuration)
    (some #(= (:organizer-short ev) %) (:filtered-clubs @view-configuration))
    true))


(defn event-valid-according-to-view-config [ev]
  (if (:only-future? @view-configuration)
    (>= (:date ev) (time/today-at-midnight))
    true))

(def events (r/atom ()))
(def shown-events (r/atom ()))

(defn generate-shown-events [all-events shown-events]
  (reset! shown-events (filter event-valid-according-to-view-config (filter event-in-filter-list @events))))

(defn month-to-monthname [month]
  (case month
    1 "januar"
    2 "februar"
    3 "mars"
    4 "april"
    5 "mai"
    6 "juni"
    7 "juli"
    8 "august"
    9 "september"
    10 "oktober"
    11 "november"
    12 "desember"))

(defn date-to-string [date]
  (let [parsed-date (timec/from-date date)]
    (str (time/day parsed-date) ". " (month-to-monthname (time/month parsed-date)))
    ))

(defn has-invitation? [ev]
  (if (not (nil? (:statuslink ev)))
    (do (.log js/console (.indexOf (:statuslink ev) "Invitation.pdf")) (>= (.indexOf (:statuslink ev) "Invitation.pdf") 0))
    false))

(defn is-finished? [ev]
  (if (not (nil? (:statuslink ev)))
    (>= (.indexOf (:statuslink ev) "CompetitionDetail.php") 0)
    false))

(defn form-filters []
  [:div
   [:div.row.form-filters
    [:div.col-md-12
     [:label
      [:input#only-future {:type           "checkbox"
                           :defaultChecked (:only-future? @view-configuration)
                           :on-change      (fn [ev]
                                             (swap! view-configuration assoc :only-future? (true? (-> ev .-target .-checked)))
                                             (generate-shown-events @events shown-events))}]
      "Kun fremtidige stevner"]]]
   [:div.row.form-filters
    [:div.col-md-12
     [:label
      [:input#only-midtnorge {:type           "checkbox"
                              :defaultChecked (:only-midtnorge? @view-configuration)
                              :on-change      (fn [ev]
                                                (swap! view-configuration assoc :only-midtnorge? (true? (-> ev .-target .-checked)))
                                                (generate-shown-events @events shown-events))}]
      "Kun stevner i midtnorge"]]]])

(defn event-item [ev]
  [:li
   [:div.timeline-badge.success [:i (month-to-monthname (time/month (timec/from-date (:date ev))))]]
   [:div.timeline-panel
    [:div.timeline-heading
     [:h4.timeline-title (str (date-to-string (:date ev)) " - " (:organizer-full ev))]]
    [:div.timeline-body
     [:div
      [:div (:competition ev)]
      [:div (:comments ev)]]
     [:span.pull-right.status
      (if (has-invitation? ev)
        [:a.btn.btn-success {:href (:statuslink ev)} "Invitasjon"]
        [:span])]]]])

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
