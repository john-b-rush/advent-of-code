(ns advent-of-code.day7
  (:require
    [clojure.string :as str]
    [com.stuartsierra.dependency :as dep]))


(def clean-inputs
  (->>
    "inputs/day7.txt"
    (slurp)
    (str/split-lines)
    (map
      (comp #(str/split % #"\scontain\s")
            #(str/replace % #" bags?\.?" ""))))) ;; Fuck your bag(s)


(def bag-graph
  (->> clean-inputs
       (map (fn [x]
              (let [outer-bag (first x)
                    other-bags (str/split (second x) #", ")]
                (map (fn [bag]
                       [outer-bag (second (str/split bag #" " 2))])
                     other-bags))))
       (reduce concat)
       (reduce (fn [g input]
                 (dep/depend g (first input) (second input))) (dep/graph))))

;; Part 1
(->> "shiny gold"
     (dep/transitive-dependents bag-graph)
     (count))


(def bags-hold
  (->> clean-inputs
       (into {})))


(defn- lookup-bag
  [bag]
  (let [holds (get bags-hold bag)]
    (->> (str/split holds #", ")
         (map #(str/split % #" " 2))
         (map (fn [[amount bag-name]]
                (if (= "no" amount)
                  0
                  (* (read-string amount) (lookup-bag bag-name)))))
         (reduce +)
         (inc))))

;; Part 2 
(dec (lookup-bag "shiny gold")) ;; dec because we also count the outerbag in lookup bag
