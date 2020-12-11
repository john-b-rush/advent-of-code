(ns advent-of-code.day9
  (:require
    [clojure.string :as str]))


(->> "inputs/day9.txt"
     (slurp)
     (str/split-lines)
     (map read-string)
     (partition 26 1)
     (some (fn [window]
             (let [target (last window)
                   items (disj (set window) target)
                   pair (for [item items
                              :when (contains? (disj items item)
                                               (- target item))]
                          item)]
               (when-not (seq pair)
                 target)))))


(->> "inputs/day9.txt"
     (slurp)
     (str/split-lines)
     (map read-string)
     (reduce
       (fn [acc item]
         (let [total (reduce + acc)]
           (cond
             (> 31161678 total)
             (conj acc item)

             (= 31161678 total)
             (reduced acc)

             (< 31161678 total)
             (loop [new-acc (conj acc item)] ;; gross
               (if (>= 31161678 (reduce + new-acc))
                 new-acc
                 (recur (drop-last new-acc)))))))
       ())
     (sort)
     (#(+ (first %) (last %))));; ugly as sin
