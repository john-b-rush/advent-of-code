(ns advent-of-code.day24
  (:require
    [clojure.string :as str]))


(defn remove-nops
  [tile-map]
  (assoc
    tile-map

    "se"
    (drop (count (get tile-map "nw")) (get tile-map "se"))
    "ne"
    (drop (count (get tile-map "sw")) (get tile-map "ne"))

    "sw"
    (drop (count (get tile-map "ne")) (get tile-map "sw"))
    "nw"
    (drop (count (get tile-map "se")) (get tile-map "nw"))

    "e"
    (drop (count (get tile-map "w")) (get tile-map "e"))
    "w"
    (drop (count (get tile-map "e")) (get tile-map "w"))))


(defn convert-to-e-w
  [tile-map]
  (let [new-e (concat (get tile-map "e") (repeat (min (count (get tile-map "ne")) (count (get tile-map "se"))) "e"))
        new-w (concat (get tile-map "w") (repeat (min (count (get tile-map "nw")) (count (get tile-map "sw"))) "w"))]
    (assoc
      tile-map

      "se"
      (drop (count (get tile-map "ne")) (get tile-map "se"))
      "ne"
      (drop (count (get tile-map "se")) (get tile-map "ne"))

      "sw"
      (drop (count (get tile-map "nw")) (get tile-map "sw"))
      "nw"
      (drop (count (get tile-map "sw")) (get tile-map "nw"))

      "e"
      new-e
      "w"
      new-w)))


(defn convert-se-w-to-sw
  [tile-map]
  (let [sw-count (min (count (get tile-map "se")) (count (get tile-map "w")))
        new-sw (concat (get tile-map "sw") (repeat sw-count "sw"))]
    (assoc
      tile-map

      "sw"
      new-sw
      "w"
      (drop sw-count (get tile-map "w"))
      "se"
      (drop sw-count (get tile-map "se")))))


(defn convert-sw-e-to-se
  [tile-map]
  (let [se-count (min (count (get tile-map "sw")) (count (get tile-map "e")))
        new-se (concat (get tile-map "se") (repeat se-count "se"))]
    (assoc
      tile-map
      "se"
      new-se
      "e"
      (drop se-count (get tile-map "e"))
      "sw"
      (drop se-count (get tile-map "sw")))))


(defn convert-ne-w-to-nw
  [tile-map]
  (let [nw-count (min (count (get tile-map "ne")) (count (get tile-map "w")))
        new-nw (concat (get tile-map "nw") (repeat nw-count "nw"))]
    (assoc
      tile-map
      "nw"
      new-nw
      "w"
      (drop nw-count (get tile-map "w"))
      "ne"
      (drop nw-count (get tile-map "ne")))))


(defn convert-nw-e-to-ne
  [tile-map]
  (let [ne-count (min (count (get tile-map "nw")) (count (get tile-map "e")))
        new-ne (concat (get tile-map "ne") (repeat ne-count "ne"))]
    (assoc
      tile-map
      "ne"
      new-ne
      "e"
      (drop ne-count (get tile-map "e"))
      "nw"
      (drop ne-count (get tile-map "nw")))))


(defn unique-tile-name
  [tile-map]
  (->> tile-map
       (remove-nops)
       (convert-to-e-w)
       (convert-se-w-to-sw)
       (convert-sw-e-to-se)
       (convert-ne-w-to-nw)
       (convert-nw-e-to-ne)))


(def process-tile-inst
  "Takes a tile instruction and produces the unique path to the tile"
  (memoize
    (fn [tile-inst]
      (->> tile-inst
           (re-seq #"se|sw|nw|ne|w|e")
           (group-by identity)
           (iterate unique-tile-name)
           (partition 2 1)
           (drop-while (fn [[tm1 tm2]] (not= tm1 tm2)))
           (first)
           (first)
           (seq)
           (mapcat second)
           (sort)
           (str/join)))))

;; Part 1
(->> "inputs/day24.txt"
     (slurp)
     (str/split-lines)
     (map process-tile-inst)
     (group-by identity)
     (filter #(odd? (count (val %))))
     (count))


(defn new-black-tiles
  [tiles]
  (let [black-tiles (set tiles)]
    (->
      (for [x black-tiles
            y ["se" "sw" "nw" "ne" "w" "e"]
            :let [z (process-tile-inst (str/join [x y]))]
            :when (not (contains? black-tiles z))
            :let [s-tile-count (->> (surrounding-tiles z)
                                    (filter #(contains? black-tiles %))
                                    (count))]

            :when (= 2 s-tile-count)]
        z)
      (distinct))))


(def surrounding-tiles
  (memoize
    (fn [location]
      (for [y ["se" "sw" "nw" "ne" "w" "e"]
            :let [d (process-tile-inst (str/join [location y]))]]
        d))))


(defn old-black-tiles
  [tiles]
  (let [black-tiles (set tiles)]
    (for [x black-tiles
          :let [s-tile-count (->> (surrounding-tiles x)
                                  (filter #(contains? black-tiles %))
                                  (count))]
          :when (<= 1 s-tile-count 2)]
      x)))


(defn process-tiles
  [tiles]
  (distinct (concat
              (new-black-tiles tiles)
              (old-black-tiles tiles))))


;; Part 2
(->> "inputs/day24.txt"
     (slurp)
     (str/split-lines)
     ;; Get ini black tiles
     (map process-tile-inst)
     (group-by identity)
     (filter #(odd? (count (val %))))
     (keys)
     ;; Process existing tiles
     (iterate process-tiles)
     (drop 100)
     (first)
     (count))
