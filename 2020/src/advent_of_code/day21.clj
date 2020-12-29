(ns advent-of-code.day21
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str]))


(defn get-items
  [inputs]
  (for [line inputs
        :let [[_ items alergins] (re-find #"(.*) \(contains (.*)\)" line)]]
    (str/split items #" ")))


(defn alergin-to-ingredients
  [inputs]
  (for [line inputs
        :let [[_ items alergins] (re-find #"(.*) \(contains (.*)\)" line)]
        alergin (map str/trim (str/split alergins #","))]
    [alergin (set (str/split items #" "))]))

;; Part 1
(let [inputs (->> "inputs/day21.txt"
                  (slurp)
                  (str/split-lines))
      alergins (->> inputs
                    (alergin-to-ingredients)
                    (reduce
                      (fn [acc [k v]]
                        (assoc
                          acc
                          k
                          (if-let [existing (get acc k)]
                            (clojure.set/intersection existing v)
                            v)))
                      {})
                    (vals)
                    (reduce concat)
                    (set))]
  (->> inputs
       (get-items)
       (map (comp
              count
              #(remove alergins %)))
       (reduce +)))


(defn alergin-ingrents
  "Takes the alergin map and possible ingredients and finds the unique ingredeints per alergen"
  [alergin-ingredent-map]
  (loop [mapping alergin-ingredent-map]
    (let [[singles more] (split-with #(>= 1 (count (second %))) (sort-by #(count (second %)) (seq mapping)))
          single-values (reduce (fn [acc [alergen ingre]] (conj ingre)) #{} singles)]
      (if (empty? more)
        mapping
        (recur (merge
                 (into {} singles)
                 (into {} (->> more
                               (map (fn [[k v]] [k (clojure.set/difference v single-values)]))))))))))

;; Part 2
(->> "inputs/day21.txt"
     (slurp)
     (str/split-lines)
     (alergin-to-ingredients)
     (reduce
       (fn [acc [k v]]
         (assoc
           acc
           k
           (if-let [existing (get acc k)]
             (clojure.set/intersection existing v)
             v)))
       {})
     (alergin-ingrents)
     (sort-by key)
     (map (comp
            first
            second))
     (str/join ","))
