(ns sbsk-tools.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [sbsk-tools.layout :refer [error-page]]
            [sbsk-tools.routes.home :refer [home-routes]]
            [sbsk-tools.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [sbsk-tools.env :refer [defaults]]
            [mount.core :as mount]
            [sbsk-tools.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    #'service-routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
