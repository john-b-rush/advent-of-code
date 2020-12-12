(ns advent-of-code.day6
  (:require
    [clojure.string :as str]))


(def lights
  (reduce merge
          (for [x (range 1000)
                y (range 1000)]
            {[x y] false})))

;; Part 1
#_(->> "inputs/day6.txt"
     (slurp)
  (str/split-lines)
  (reduce
    (fn [acc line]
      (reduce 
      merge
        acc
        (let [[_ command start-x start-y end-x end-y] (re-find #"(.*?) (\d+),(\d+) .*? (\d+),(\d+)" line)]
          (for [x (range (read-string start-x) (inc (read-string end-x)))
                y (range (read-string start-y) (inc (read-string end-y)))]
            (case command
              "turn off"
              {[x y] false}

              "turn on"
              {[x y] true}

              "toggle"
              {[x y] (not (get acc [x y] false))}))))
     )
    {})
  (vals)
  (filter true?)
  (count))


#_(->> "inputs/day6.txt"
     (slurp)
     (str/split-lines)
     (reduce
       (fn [acc line]
         (reduce
           merge
           acc
           (let [[_ command start-x start-y end-x end-y] (re-find #"(.*?) (\d+),(\d+) .*? (\d+),(\d+)" line)]
             (for [x (range (read-string start-x) (inc (read-string end-x)))
                   y (range (read-string start-y) (inc (read-string end-y)))]
               (case command
                 "turn off"
                 {[x y] (max 0 (dec (get acc [x y] 0)))}

                 "turn on"
                 {[x y] (inc (get acc [x y] 0))}

                 "toggle"
                 {[x y] (+ 2 (get acc [x y] 0))})))))
       {})
     (vals)
     (reduce +))
