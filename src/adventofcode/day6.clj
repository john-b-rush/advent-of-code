(ns adventofcode.day6
  (:require [adventofcode.utils :as utils]))

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

(defn count-orbits
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
              :children #{}}))

  )
  (apply + (map :depth (vals @a)))
  ))


