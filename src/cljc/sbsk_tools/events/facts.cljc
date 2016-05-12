(ns sbsk-tools.events.facts
  (:require [clojure.string :as str]))

(def ^:const club-facts {
                         :midtnorge {:clubs ["bøfjo" "geita" "hegil" "harøy" "hitra" "hunde"
                                             "goma" "yrjar" "levah" "lyngs" "malvi" "molde"
                                             "roan" "salns" "stoks" "surna" "sverr" "tustn"
                                             "ulvun" "verda" "vestn" "vigra" "volla" "ålesu"]}
                         })

(defn month-to-name-lower [month]
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

(defn month-to-name [month]
  (str/capitalize (month-to-name-lower month)))