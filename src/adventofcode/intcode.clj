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


(defn jump-if-true
  "if the first parameter is non-zero, it sets the instruction pointer to the value from the second parameter. "
  [instructions inst-idx]
  (if (zero? (read-param instructions inst-idx 1))
    (+ inst-idx 3)
    (read-param instructions inst-idx 2)))


(defn jump-if-false
  "if the first parameter is zero, it sets the instruction pointer to the value from the second parameter. "
  [instructions inst-idx]
  (if (zero? (read-param instructions inst-idx 1))
    (read-param instructions inst-idx 2)
    (+ inst-idx 3)))


(defn less-than
  [instructions inst-idx]
  (assoc instructions
         (nth instructions (+ inst-idx 3))
         (if (< (read-param instructions inst-idx 1) (read-param instructions inst-idx 2))
           1
           0)))


(defn equal
  [instructions inst-idx]
  (assoc instructions
         (nth instructions (+ inst-idx 3))
         (if (= (read-param instructions inst-idx 1) (read-param instructions inst-idx 2))
           1
           0)))


(defn run-program
  "Run the intcode program"
  ([program]
   (run-program program nil 0 false))
  ([program inputs]
   (run-program program inputs 0 false))
  ([program inputs inst-idx return-program?]
   (loop [program program
          inst-idx inst-idx
          output []
          inputs inputs]
     (let [inst (nth program inst-idx)
           opcode (rem inst 100)]
       (if (= opcode 99)
         (if return-program?
           program
           output)
         (cond
           (= opcode 1) (recur (add program inst-idx) (+ inst-idx 4) output inputs)
           (= opcode 2) (recur (multiply program inst-idx) (+ inst-idx 4) output inputs)
           (= opcode 3) (recur (save-input program inst-idx (first inputs)) (+ inst-idx 2) output (rest inputs))
           (= opcode 4) (recur program (+ inst-idx 2) (conj output (read-output program inst-idx)) inputs)
           (= opcode 5) (recur program (jump-if-true program inst-idx) output inputs)
           (= opcode 6) (recur program (jump-if-false program inst-idx) output inputs)
           (= opcode 7) (recur (less-than program inst-idx) (+ inst-idx 4) output inputs)
           (= opcode 8) (recur (equal program inst-idx) (+ inst-idx 4) output inputs)))))))

