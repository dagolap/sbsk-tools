(ns user
  (:require [mount.core :as mount]
            [sbsk-tools.figwheel :refer [start-fw stop-fw cljs]]
            sbsk-tools.core))

(defn start []
  (mount/start-without #'sbsk-tools.core/repl-server))

(defn stop []
  (mount/stop-except #'sbsk-tools.core/repl-server))

(defn restart []
  (stop)
  (start))


