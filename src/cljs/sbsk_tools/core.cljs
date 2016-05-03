(ns sbsk-tools.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [sbsk-tools.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(defn nav-link [uri title page collapsed?]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href     uri
     :on-click #(reset! collapsed? true)} title]])

(defn navbar []
  (let [collapsed? (r/atom true)]
    (fn []
      [:nav.navbar.navbar-light.bg-faded
       [:button.navbar-toggler.hidden-sm-up
        {:on-click #(swap! collapsed? not)} "☰"]
       [:div.collapse.navbar-toggleable-xs
        (when-not @collapsed? {:class "in"})
        [:a.navbar-brand {:href "#/"} "Sverresborg Bueskyttere - Verktøy"]
        [:ul.nav.navbar-nav
         [nav-link "#/" "Hovedside" :home collapsed?]
         [nav-link "#/events" "Stevner" :about collapsed?]]]])))

(def filtered-clubs (r/atom ["Geita" "Sverr" "Tustn" "Yrjar" "Stoks" "VIGRA"]))
(defn event-in-midtnorge [ev]
  (some #(= (:organizer-short ev) %) @filtered-clubs))

(def events (r/atom ()))
(defn events-page []
  (GET "/api/events" {
                      :handler #(reset! events %)})
  [:div.container
   [:div.row
    (if (empty? @events) [:div.col-md-12 "Laster stevner..."]
                         (for [ev (filter event-in-midtnorge @events)]
                           ^{:key (first ev)} [:div.col-md-12
                                               [:div.row
                                                [:div.col-md-12
                                                 [:h3 (str (:date ev) " - " (:competition ev) " - " (:organizer-full ev))]]]
                                               [:div.row.last-in-item
                                                [:div.col-md-3
                                                 [:span (:date ev)]]
                                                [:div.col-md-3
                                                 [:span (:competition ev)]]
                                                [:div.col-md-6
                                                 [:span (:comments ev)]]]]))]])

(defn home-page []
  [:div.container
   [:div.jumbotron
    [:h1 "Sverresborg Bueskytterverktøy"]
    [:p "Her finnes diverse hjelpsomme verktøy ifm. bueskyting i Norge."]
    ]
   [:div.row
    [:div.col-md-12 [:a {:href "#/events"} "Stevner"]]]])

(def pages
  {:home   #'home-page
   :events #'events-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
                    (session/put! :page :home))

(secretary/defroute "/events" []
                    (session/put! :page :events))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
