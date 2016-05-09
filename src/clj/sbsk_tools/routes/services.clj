(ns sbsk-tools.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.swagger.schema :as rs]
            [sbsk-tools.routes.events :as eventsapi])
  (:import (java.util Date)))

(s/defschema Event {
                    :event-id        (rs/describe (s/maybe s/Str) "National unique ID for event.")
                    :organizer-short (rs/describe (s/maybe s/Str) "Organizer short form.")
                    :organizer-full  (rs/describe (s/maybe s/Str) "Organizer full club name.")
                    :competition     (rs/describe (s/maybe s/Str) "Competition type (e.g. 18m or 720-runde).")
                    :date            (rs/describe (s/maybe Date) "Start date of event.")
                    :comments        (rs/describe (s/maybe s/Str) "Comments describing location, special comments and names etc.")
                    :statuslink      (rs/describe (s/maybe s/Str) "Url to invitation or status results if they exists. Otherwise nil.")})


(defapi service-routes
        {:swagger {:ui   "/swagger-ui"
                   :spec "/swagger.json"
                   :data {:info {:version     "1.0.0"
                                 :title       "SBSK Tools API"
                                 :description "Various tools used by Sverresborg Bueskyttere Archery club"}}}}

        (context "/api/events" []
                 :tags ["Events"]
                 (GET "/" []
                      :summary "Returns all events in Norway for the current year"
                      :responses {200 {:schema [Event] :description "Downloaded and parsed correct"}}
                      (ok (eventsapi/map-all-events)))
                 (GET "/cached-time" []
                      :summary "Returns latest cache time of competition data"
                      :responses {200 {:schema {:cache-time (rs/describe (s/maybe Date) "Latest cache time for IANSEO events.")} :model "test"}}
                      (ok {:cache-time (eventsapi/latest-competition-cache)}))))