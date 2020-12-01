(ns adventofcode.day8
  (:require
    [clojure.string :as str]
    [adventofcode.utils :as utils]))

; TODO: refactor this
(defn part1
  []
  (reduce * (vals (select-keys (first (sort-by #(get % 0) (map frequencies (partition (* 25 6) (utils/day8-input))))) [1 2]))))


(defn resolve-colors
  [top-color bottom-color]
  (if (= top-color 2)
    bottom-color
    top-color))

(defn resolve-layers
  [top-layer bottom-layer]
  (mapv resolve-colors top-layer bottom-layer))


(defn combine-layers
  [layers]
  (loop [top-layer (first layers)
         remaining-layers (rest layers)]
    (if (empty? remaining-layers)
      top-layer
      (recur (resolve-layers top-layer (first remaining-layers)) (rest remaining-layers)))))


(defn more-readable
  " changes 1 and zeros to more redable # and ."
  [img-str]
  (-> img-str
      (str/replace "1" "#")
      (str/replace "0" ".")))


(defn part2
  []
  (run! println (map (partial apply str) (partition 25 (more-readable (str/join "" (combine-layers (partition (* 25 6) (utils/day8-input)))))))))
