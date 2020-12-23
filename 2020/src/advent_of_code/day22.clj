(ns advent-of-code.day22
  (:require
    [clojure.string :as str]))


(defn play-round
  [deck1 deck2]
  (let [card1 (first deck1)
        card2 (first deck2)]
    (cond
      (or (nil? card1) (nil? card2))
      [deck1 deck2]

      ;; player 1 wins
      (> card1 card2)
      ;[card1 card2]
      #(play-round
         (conj (subvec deck1 1) card1 card2)
         (subvec deck2 1))
      ;; player 2 wins
      (< card1 card2)
      #(play-round
         (subvec deck1 1)
         (conj (subvec deck2 1) card2 card1)))))

;; Part 1
(->> "inputs/day22.txt"
     (slurp)
     (#(str/split % #"\n\n"))
     (map (fn [deck]
            (let [[_ cards] (re-find #"(?s)Player \d+:\n(.*)" deck)]
              (->> cards
                   (str/split-lines)
                   (mapv read-string)))))
     ;; Play till game is over
     (#(trampoline (play-round (first %) (second %))))
     ;; Calc score
     (filter seq)
     (first)
     (reverse)
     (vec)
     (reduce-kv
       (fn [acc k v]
         (+ acc (* (inc k) v)))
       0))

;; Part 2
(defn play-round-recur
  ([deck1 deck2]
   (play-round-recur deck1 deck2 #{} #{}))
  ([deck1 deck2 prev-rounds1 prev-rounds2]
   (let [card1 (first deck1)
         card2 (first deck2)]
     (cond
      ;; Prevent infinite recursion
       (or (contains? prev-rounds1 deck1) (contains? prev-rounds2 deck2))
       [deck1 []]

      ;; Someone is out of cards
       (or (nil? card1) (nil? card2))
       [deck1 deck2]

      ;; Recursive combat
       (and (< card1 (count deck1)) (< card2 (count deck2)))
       (let [[sub-game1 sub-game2] (trampoline
                                     (play-round-recur
                                       (subvec deck1 1 (inc card1))
                                       (subvec deck2 1 (inc card2))))]
        ;; kinda hax to figure out who won the subgame
         (if (> (count sub-game1) (count sub-game2))
          ;; Player 1 wins subgame
           #(play-round-recur
              (conj (subvec deck1 1) card1 card2)
              (subvec deck2 1)
              (conj prev-rounds1 deck1)
              (conj prev-rounds2 deck2))
          ;; Player 2 wins subgame
           #(play-round-recur
              (subvec deck1 1)
              (conj (subvec deck2 1) card2 card1)
              (conj prev-rounds1 deck1)
              (conj prev-rounds2 deck2))))

      ;; player 1 wins
       (> card1 card2)
       #(play-round-recur
          (conj (subvec deck1 1) card1 card2)
          (subvec deck2 1)
          (conj prev-rounds1 deck1)
          (conj prev-rounds2 deck2))

      ;; player 2 wins
       (< card1 card2)
       #(play-round-recur
          (subvec deck1 1)
          (conj (subvec deck2 1) card2 card1)
          (conj prev-rounds1 deck1)
          (conj prev-rounds2 deck2))))))


;; Part 2 
(->> "inputs/day22.txt"
     (slurp)
     (#(str/split % #"\n\n"))
     (map (fn [deck]
            (let [[_ cards] (re-find #"(?s)Player \d+:\n(.*)" deck)]
              (->> cards
                   (str/split-lines)
                   (mapv read-string)))))
     ;; Play till game is over
     (#(trampoline (play-round-recur (first %) (second %) #{} #{})))
     ;; Calc score - gross
     (filter seq)
     (first)
     (reverse)
     (vec)
     (reduce-kv
       (fn [acc k v]
         (+ acc (* (inc k) v)))
       0))
