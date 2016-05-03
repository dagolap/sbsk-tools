(ns sbsk-tools.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [sbsk-tools.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[sbsk-tools started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[sbsk-tools has shutdown successfully]=-"))
   :middleware wrap-dev})
