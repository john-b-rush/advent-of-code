(ns adventofcode.utils
  (:require
    [clojure.string :as str]))


(defn- split-string
  [string]
  (str/split string #","))


(defn- split-chars
  [string]
  (str/split string #""))


(defn- csv->seq
  [string]
  (map read-string (split-string string)))


(defn- read-input
  [filename]
  (slurp filename))


(defn day1-input
  []
  (->> "input/d1input.txt"
       (read-input)
       (str/split-lines)
       (map read-string)))


(defn day2-input
  []
  (->> "input/d2input.txt"
       (read-input)
       (csv->seq)
       (vec)))


(defn day3-input
  []
  (->> "input/d3input.txt"
       (read-input)
       (str/split-lines)
       (map split-string)))


(defn- split-orbit
  [string]
  (str/split string (re-pattern "\\)")))


(defn day5-input
  []
  (->> "input/day5.txt"
       (read-input)
       (csv->seq)
       (vec)))


(defn day6-input
  []
  (->> "input/day6_input.txt"
       (read-input)
       (str/split-lines)
       (map split-orbit)))


(defn day7-input
  []
  (->> "input/day7.txt"
       (read-input)
       (csv->seq)
       (vec)))


(defn day8-input
  []
  (->> "input/day8.txt"
       (slurp)
       (str/trim-newline)
       (split-chars)
       (map read-string)))


(defn day9-input
  []
  (->> "input/day9.txt"
       (read-input)
       (csv->seq)
       (vec)))


(defn day14-input
  []
  (->> "input/day14.txt"
       (slurp)
       (str/split-lines)))
