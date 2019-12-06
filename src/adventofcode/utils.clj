(ns adventofcode.utils
  (:require [clojure.string :as str]))


(defn- csv->seq
  [string]
  (map read-string (str/split string #",")))

(defn- read-input
  [filename]
  (slurp filename))

(def d3-input
  (->> "input/d3-input.txt"
       (read-input)
       (str/split-lines)
       (map csv->seq)))
