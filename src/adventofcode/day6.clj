(ns adventofcode.day6
  (:require
    [adventofcode.utils :as utils]
    [clojure.set :refer :all]))


(defn update-children!
  [orbit-atom parent-id]
  (let [parent (get @orbit-atom parent-id)]
    (doseq [child-id (:children (get @orbit-atom parent-id))
            :let [child (get @orbit-atom child-id)]]
      (swap! orbit-atom
             assoc
             child-id
             {:depth (+ 1 (:depth parent))
              :parent (:parent child)
              :children (:children child)})
      (update-children! orbit-atom child-id))))


(defn build-graph
  [orbit-pairs]
  (let [a (atom {})]
    (doseq [orb orbit-pairs
            :let [parent (get @a (first orb))
                  current (get @a (second orb))]]
      (if parent
        (swap! a
               assoc
               (first orb)
               {:depth (:depth parent)
                :parent (:parent parent)
                :children (conj (:children parent) (second orb))})
        (swap! a
               assoc
               (first orb)
               {:depth 0
                :parent nil
                :children #{(second orb)}}))
      (if current
        (doall
          (swap! a
                 assoc
                 (second orb)
                 {:depth (if parent (+ 1 (:depth parent)) 1)
                  :parent (first orb)
                  :children (:children current)})
          (update-children! a (second orb)))
        (swap! a
               assoc
               (second orb)
               {:depth (if parent (+ 1 (:depth parent)) 1)
                :parent (first orb)
                :children #{}})))
    @a))


(defn orbits
  [orbit-graph pos-id]
  (let [parent-id (:parent (get orbit-graph pos-id))]
    (if parent-id
      (conj (orbits orbit-graph parent-id) pos-id)
      [pos-id])))


(defn extrasection
  [& ss]
  (difference
    (union ss)
    (intersection ss)))


(defn common-orbits
  [orbit-graph first-pos second-pos]
  (let [first-orbits (set (orbits orbit-graph first-pos))
        second-orbits (set (orbits orbit-graph second-pos))]
    (difference
      (union first-orbits second-orbits)
      (intersection first-orbits second-orbits))))


(defn orbital-transfers
  [orbit-graph first-pos second-pos]
  (- (count (common-orbits orbit-graph first-pos second-pos)) 2)) ;; -2 because we don't want the YOU/SAN to count as an orbit



(defn count-orbits
  [orbit-pairs]
  (apply + (map :depth (vals (build-graph orbit-pairs)))))
