(ns advent-of-code.day18
  (:require
    [clojure.string :as str]))



;102         total 0
;103         operation +
;104         len 0]
;105      (let [item (first items)]
;106     (case item
;107
;108          nil
;109          [total len]
;110
;111      ")"
;112      [total (+ 2 len)]
;113
;114       "+"
;115       (recur (rest items) total + (inc len))
;116
;117       "*"
;118       (recur (rest items) total * (inc len))
;119
;120       "("
;121       (let [[sub-total sub-len] (new-math (drop (inc len) items))]
;122         (print ":" (vec (drop (inc len) items))"\n")
;123         (print "xx" total sub-len "\n\n")
;124         (recur (drop sub-len items) (+ total sub-total) nil (+ len sub-len)))
;125
;126
;127       (recur (rest items) (operation total (read-string item)) nil (inc len))))))
