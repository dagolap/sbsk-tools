(ns sbsk-tools.routes.home
  (:require [sbsk-tools.layout :as layout]
            [compojure.core :refer [defroutes GET]]))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
           (GET "/" [] (home-page)))

