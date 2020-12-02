(ns advent-of-code.day5
  (:require
    [clojure.math.combinatorics :as combo]
    [clojure.string :as str]))


(defn- vowel-count
  [word]
  (->> word
       (frequencies)
       (reduce
         (fn [acc [k v]]
           (+ acc
              (if (#{\a \e \i \o \u} k)
                v
                0)))
         0)
       (< 2)))


(defn- double-letters
  [word]
  (->> word
       (partition 2 1)
       (map distinct)
       (some #(= 1 (count %)))))


(defn- no-bad-dbls
  [word]
  (->> word
       (partition 2 1)
       (map (partial str/join ""))
       (not-any? #{"ab" "cd" "pq" "xy"})))

;; Part 1
#_(->> "inputs/day5.txt"
     (slurp)
     (str/split-lines)
     (filter #(and (vowel-count %)
                   (double-letters %)
                   (no-bad-dbls %)))
     (count))


(defn- twice-no-overlap
  [word]
  (->> word
       (partition 2 1)
       (map (partial str/join ""))
       (partition-by identity)
       (map distinct)
       (frequencies)
       (vals)
       (some #(< 1 %))))


(defn- letter-between
  [word]
  (->> word
       (partition 3 1)
       (map (partial str/join ""))
       (some #(= (first %) (last %)))))

;; Part 2
#_(->> "inputs/day5.txt"
     (slurp)
     (str/split-lines)
     (filter #(and (twice-no-overlap %)
                   (letter-between %)))
     (count))
