(ns adventofcode.intcode)


(defn mode
  [instruction offset]
  (loop [offset (dec offset)
         mode (quot instruction 100)]
    (if (= offset 0)
      (rem mode 10)
      (recur (dec offset) (quot mode 10)))))

(defn read-param
  "Reads the value of a parameter"
  [input index offset]
  (if (= (mode (nth input index) offset) 0)
    (nth input (nth input (+ index offset)))
    (nth input (+ index offset))))

(defn dispatch-opcodes
  "Process opcodes"
  ([input]
   (dispatch-opcodes input 0))
  ([input index]
   (loop [input input
          index index]
     (let [opcode (nth input index)]
       (if (= opcode 99)
         input
         (recur
           (assoc input
                  (nth input (+ index 3))
                  ((cond (= opcode 1) +
                         (= opcode 2) *)
                   (read-param input index 1)
                   (read-param input index 2)))
           (+ index 4)))))))


(defn add
  "adds"
  [instructions inst-idx]
  (assoc instructions
         (nth instructions (+ inst-idx 3))
         (+ (read-param instructions inst-idx 1)
            (read-param instructions inst-idx 2))))


(defn multiply
  "multiplies"
  [instructions inst-idx]
  (assoc instructions
         (nth instructions (+ inst-idx 3))
         (* (read-param instructions inst-idx 1)
            (read-param instructions inst-idx 2))))


(defn save-input
  "takes input"
  [instructions inst-idx inputs]
  (assoc instructions
         (nth instructions (+ inst-idx 1))
         inputs))


(defn read-output
  "returns output"
  [instructions inst-idx]
  (read-param instructions inst-idx 1))


(defn run-program
  "Run the intcode program"
  ([program]
   (run-program program nil 0 false))
  ([program input]
   (run-program program input 0 false))
  ([program input inst-idx return-program?]
   (loop [program program
          inst-idx inst-idx
          output []]
     (let [inst (nth program inst-idx)
           opcode (rem inst 100)]
       (if (= opcode 99)
         (if return-program?
           program
           output)
         (cond
           (= opcode 1) (recur (add program inst-idx) (+ inst-idx 4) output)
           (= opcode 2) (recur (multiply program inst-idx) (+ inst-idx 4) output)
           (= opcode 3) (recur (save-input program inst-idx input) (+ inst-idx 2) output)
           (= opcode 4) (recur program (+ inst-idx 2) (conj output (read-output program inst-idx)))))))))
