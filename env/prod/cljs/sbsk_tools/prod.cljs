(ns sbsk-tools.app
  (:require [sbsk-tools.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
