(ns adventofcode.day11
  (:require
    [adventofcode.intcode :refer [intcode]]
    [adventofcode.utils :as utils]
    [clojure.core.async :refer [go >! chan >!! <!! close!]]
    [clojure.string :as str]))

;; Facing map
;;  0
;; 3 1
;;  2

(defn paint
  [loc color white-pannels black-pannels]
  (case color
    0 ;; paint black
    [(disj white-pannels loc) (conj black-pannels loc)]

    1 ;; paint white
    [(conj white-pannels loc) (disj black-pannels loc)]))


(defn move
  [current-pos facing turn]
  (case facing
    0 ;; up
    (if (= 0 turn)
      ;; left
      [[(dec (first current-pos)) (second current-pos)]
       3]
      ;; right
      [[(inc (first current-pos)) (second current-pos)]
       1])

    1 ;; right
    (if (= 0 turn)
      ;; left
      [[(first current-pos) (dec (second current-pos))]
       0]
      ;; right
      [[(first current-pos) (inc (second current-pos))]
       2])

    2 ;; down
    (if (= 0 turn)
      ;; left
      [[(inc (first current-pos)) (second current-pos)]
       1]
      ;; right
      [[(dec (first current-pos)) (second current-pos)]
       3])

    3 ;; left
    (if (= 0 turn)
      ;; left
      [[(first current-pos) (inc (second current-pos))]
       2]
      ;; right
      [[(first current-pos) (dec (second current-pos))]
       0])))


(defn painting
  [starting-pannels]
  (let [robot-in (chan)
        robot-out (intcode robot-in (utils/day11-input))]
    (loop [white-pannels starting-pannels
           black-pannels #{}
           current-pos [0 0]
           facing 0]
      (go (>! robot-in (if (contains? white-pannels current-pos) 1 0)))
      (let [color (<!! robot-out)]
        (if (nil? color)
          [white-pannels black-pannels]
          (let [[white black] (paint current-pos color white-pannels black-pannels)
                [pos face] (move current-pos facing (<!! robot-out))]
            (recur white black pos face)))))))



;; rotated 90
(defn print-paints
  [points]
  (do
    (print "\n")
    (apply print (repeat (second (first points)) " "))
    (print "#"))
  (reduce
    (fn [acc x]
      (if (< (first acc) (first x))
        (do
          (print "\n")
          (apply print (repeat (second x) " "))
          (print "#"))
        (do
          (apply print (repeat (- (second x) (second acc)) " "))
          (print "#")))
      x)
    points))


(comment 
  (def part1-paints (painting #{}))
  (->> part1-paints
    (mapv count)
    (reduce +))




  (def part2-paints (painting #{[0 0]}))
  (->> part2-paints
       (first)
       (sort)
       (print-paints))
)
