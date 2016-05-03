(ns sbsk-tools.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [sbsk-tools.core-test]))

(doo-tests 'sbsk-tools.core-test)

