(ns adventofcode.core
  (:require
    [adventofcode.day1 :as day1]
    [adventofcode.day12 :as day12]
    [adventofcode.day14 :as day14]
    [adventofcode.day2 :as day2]
    [adventofcode.day5 :as day5]
    [adventofcode.day6 :as day6]
    [adventofcode.day7 :as day7]
    [adventofcode.day8 :as day8]
    [adventofcode.day9 :as day9]
    [adventofcode.intcode :as intcode]
    [adventofcode.utils :as utils]
    [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

