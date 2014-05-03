(ns AirportRoutes.core)


(use 'clojure.java.io)
(use ['clojure.string :only '(split)])

(def max-value 99999);max value for the distance

(defn to-int [x]
 (java.lang.Integer/parseInt x)) 

;********************** reading from a file and create function  ********************

(def file-with-cities (reader (file "cities.txt"))); takes a pointer to a file

(defn read-file [file-with-cities] ;read the content of file
  (line-seq file-with-cities))

(def contents-file (read-file file-with-cities))

(defn get-number-cities [contents-file]
   (to-int (first contents-file)))

(defn create-vector [contents-file] ; create vector-map {:index_city [:name name-of-city :distance [connection with cities]]}
  (reduce (fn [acc row]
            (let [array (split row #";")
                  city-name (first array)
                  distance-between (into [] (conj (map to-int (rest array)) 0))]
              (assoc acc (count acc)  {:index (count acc) :name city-name :distance distance-between })))
            {} (rest contents-file)))

(def vector-map-data (create-vector contents-file))

(defn row-matrix [vector-map-data index]
(let [array []]
  (for [i (range 6)] 
    (if (> i index)
      (conj array((:distance (vector-map-data index)) (- i index))),
      (conj array ((:distance (vector-map-data i)) (- index i)))))))


(defn create-r-s-point [vector-map-data start] ; index start city
  (reduce (fn [acc  [index name distance]]
            (if (= index start)
              (assoc acc index  {:r 0 :s 1 :point []}),
              (assoc acc index  {:r max-value :s 0 :point []})))
            {} vector-map-data))


(defn view-solution [r-s-p idex-start index-target]
  (reset! array (conj @array index-target))
  (if (not= nil (last (:point (r-s-p index-target))))
    (view-solution r-s-p idex-start (last (:point (r-s-p index-target))))),
  (reverse @array))

;******************************* dijkstra algorithm   *********************************

(defn dijks-alg [vector-map-data index-start index-target] 
  (def r-s-point(atom (create-r-s-point vector-map-data  index-start)))
  (def tr (atom index-start))
  (def number-of-cities (get-number-cities contents-file))
  
  (doseq [i (range number-of-cities) :while (= (:s (@r-s-point index-target)) 0) ]
    (let [row (into [](row-matrix  vector-map-data @tr))]
      (doseq [j (range number-of-cities)]  
        (if (and 
              (= (:s (@r-s-point j)) 0)
              (< (+ 
                   (:r (@r-s-point @tr))
                   ((row j) 0)) 
                 (:r (@r-s-point j))))
          (reset! r-s-point (assoc @r-s-point j 
                                   {:r (+ 
                                         (:r (@r-s-point @tr)) 
                                         ((row j) 0)) 
                                    :s (:s (@r-s-point j)) 
                                    :point (conj (:point (@r-s-point j)) @tr)}))))  
      
      (def minimum max-value)
      (doseq [[k v] @r-s-point]
        (if (and (= (:s v) 0) (< (:r v) minimum))
          (do 
            (def minimum (:r v))
            (def mini k))))
      
      (reset! r-s-point (assoc @r-s-point mini {:r (:r (@r-s-point mini))
                                                :s 1 
                                                :point (:point (@r-s-point mini))}))
      (reset! tr mini)))
  (def array (atom []))
  (view-solution @r-s-point index-start index-target))


(def sulution (dijks-alg vector-map-data 1 5)) 






