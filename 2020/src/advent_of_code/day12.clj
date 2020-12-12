(ns advent-of-code.day12
  (:require
    [clojure.string :as str]))


(->> "inputs/day12.txt"
     (slurp)
     (str/split-lines)
     (reduce
       (fn [acc line]
         (let [[[x y] facing] acc
               [_ operation value] (re-find #"(\w)(\d+)" line)
               value (read-string value)]
           (case operation
             "N"
             [[x (+ y value)] facing]

             "S"
             [[x (- y value)] facing]

             "E"
             [[(+ x value) y] facing]

             "W"
             [[(- x value) y] facing]

             "L"
             [[x y] (mod (+ facing value) 360)]

             "R"
             [[x y] (mod (- facing value) 360)]

             "F"
             (case facing
               0
               [[(+ x value) y] facing]

               90
               [[x (+ y value)] facing]

               180
               [[(- x value) y] facing]

               270
               [[x (- y value)] facing]))))
       [[0 0] 0])
     (first)
     (map #(Math/abs %))
     (reduce +))


(->> "inputs/day12.txt"
     (slurp)

     (str/split-lines)
     (reduce
       (fn [acc line]
         (let [[[x y] [wx wy]] acc
               [_ operation value] (re-find #"(\w)(\d+)" line)
               value (read-string value)]
           (case operation
             "N"
             [[x y] [wx (+ wy value)]]

             "S"
             [[x y] [wx (- wy value)]]

             "E"
             [[x y] [(+ wx value) wy]]

             "W"
             [[x y] [(- wx value) wy]]

             "L"
             (case value
               90
               [[x y]  [(* -1 wy) wx]]

               180
               [[x y]  [(* -1 wx) (* -1 wy)]]

               270
               [[x y]  [wy (* -1 wx)]])

             "R"
             (case value
               90
               [[x y]  [wy (* -1 wx)]]

               180
               [[x y]  [(* -1 wx) (* -1 wy)]]

               270
               [[x y]  [(* -1 wy) wx]])

             "F"
             [[(+ x (* wx value)) (+ y (* wy value))] [wx wy]] )))
    
       [[0 0] [10 1]])
     (first)
     (map #(Math/abs %))
     (reduce +))

;; Note: Being able to test with [1 1] and only synthized inputs L270 R90 L180 acclerated this quite a bit
