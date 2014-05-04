(ns AirportRoutes.core)

(use 'clojure.java.io)
(use ['clojure.string :only '(split)])

(def r 6371); R is the radius of the Earth 6371km

(defn remove-last [x]
  (subs x 0 (- (count x) 1)))

(def max-double (Double/MAX_VALUE))
(defn sin [n] (Math/sin n))
(defn cos [n] (Math/cos n))
(defn atan2 [x y] (Math/atan2 x y))
(defn sin2 [n] (* (sin n) (sin n)))
(defn sqrt [n] (Math/sqrt n))
(defn to-radians [n] (Math/toRadians n))
(defn abs [n] (Math/abs n))
(defn to-int [x]
 (java.lang.Integer/parseInt x)) 
(defn to-double [x]
 (java.lang.Double/parseDouble x))


;**********************       READING FROM FILE     ********************

(def file-with-airports (reader (file "airports.dat"))); takes a pointer to a file

(defn read-file [file-with-airports] ;read the content of file
  (line-seq file-with-airports))

(def read-raw (read-file file-with-airports))

;Airport ID Unique OpenFlights identifier for this airport.
;Name Name of airport. May or may not contain the City name.
;City Main city served by airport. May be spelled differently from Name.
;Country Country or territory where airport is located.
;IATA/FAA 3-letter FAA code, for airports located in Country "United States of America".
;3-letter IATA code, for all other airports.
;Blank if not assigned.
;ICAO 4-letter ICAO code.
;Blank if not assigned.
;Latitude Decimal degrees, usually to six significant digits. Negative is South, positive is North.
;Longitude Decimal degrees, usually to six significant digits. Negative is West, positive is East.
;Altitude In feet.
;Timezone Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5.
;DST

(defn read-data [contents-file]
  (reduce (fn [acc row]
            (let [[airport-id name city country f i-la-lo-a-t d] (split row #",\"")
                 [faa iata lantitude logitude altitude timezone dst] (split (str "\"" f ",\"" i-la-lo-a-t ",\"" d) #",")]
                 (assoc acc (to-int airport-id)
                  {:name (remove-last name)
                   :city (remove-last city)
                   :country (remove-last country)
                   :faa faa
                   :iata iata
                   :lantitude (to-double lantitude)
                   :logitude (to-double logitude)
                   :altitude (to-int altitude)
                   :timezone (to-double timezone)
                   :dst dst
                   })))
                 {} contents-file))

(def data (read-data read-raw))

;*******************************FUNCTION FOR CALCULATE DISTANCE ***********************
;dlon = lon2 - lon1
;dlat = lat2 - lat1
;a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2
;c = 2 * atan2( sqrt(a), sqrt(1-a) )
;d = R * c (where R is the radius of the Earth)

(defn calculate-distance [airport-1 airport-2]
  (let[f1 (to-radians (:lantitude  airport-1))
       f2 (to-radians (:lantitude  airport-2))
       l1 (to-radians (:logitude   airport-1))
       l2 (to-radians (:logitude   airport-2))
       dlon-2 (/ (- l1 l2) 2)
       dlat-2 (/ (- f1 f2) 2)
       a (+ (sin2 dlat-2) (* (cos f1) (cos f2) (sin2 dlon-2)))
       c (* 2 (atan2 (sqrt a) (sqrt (- 1 a))))]
    (* r c)))

;******************************* FUNCTION FOR SELECT AIRPORTS (BETWEEN START-END) ***********************

;because i can't process all airports, i must select airports which are between my start and the end
;        ---o--------o-------------o------- x (the end)  
;        |     o           o          o     |       
; (start) x-----------o-----------o----------

(defn airport-online [airport-1 airport-2]
  (let[f-max (max (:lantitude airport-1) (:lantitude airport-2))
       f-min (min (:lantitude airport-1) (:lantitude airport-2))
       l-max (max (:logitude  airport-1) (:logitude  airport-2))
       l-min (min (:logitude  airport-1) (:logitude  airport-2))]
    (apply dissoc data (for [[k e] data :when (not= (and 
                                                      (>= (:lantitude e) f-min) (<= (:lantitude e) f-max) 
                                                      (>= (:logitude  e) l-min) (<= (:logitude e ) l-max)) true)] k))))

;*************************************** calculate airports which are reachable ************************************************************
; if distance more than airplane range, that is unreachable

(defn all-connection [airport data-on-line airplane-range] 
  (reduce (fn [acc [k e]]
            (let [curent-distance (calculate-distance airport e)]
              (if (< curent-distance airplane-range)
                 (assoc acc k {:distance curent-distance}),
                 (assoc acc k {:distance max-double}))))
                 {} data-on-line))

;**************************************** FUNCTION WHICH ARE NEEDED FOR dijkstra algorithm ****************************************



;///////////////////////////////////////////////////////////////////////////////////////
;this function is using to create map with all airports with :
;    -distance from start airport 
;    - r --> is distance from start airport (+ next ditance, etc ), for start airport it is zero
;    -s  --> is sign, is this airport was visit ( 0-not visited )
;    -point --> is vector with airports id from we come

(defn create-r-s-point [id-airport data-on-line] ; 
  (reduce (fn [acc  [k e]]
            (if (= k id-airport)
              (assoc acc k  {:r 0 :s 1 :point []}),
              (assoc acc k {:r max-double :s 0 :point []})))
            {} data-on-line))
;///////////////////////////////////////////////////////////////////////////////////////
;this function is using for show which airports (ID of airports) we should visit
; it use element created by this function create-r-s-point
; starts from  target airport, and take the last airports from :point (last we come from)
; than airport and takes last from :point and etc. 
; make array of airports which are best solution

(defn view-solution [r-s-p id-airport-start id-airport-target data-on-line]
  (reset! array-airports (conj @array-airports id-airport-target)); put target airport to array
  (if (not= nil (last (:point (r-s-p id-airport-target))))
    (view-solution r-s-p id-airport-start (last (:point (r-s-p id-airport-target))) data-on-line))
  (reverse @array-airports))

;////////////////////////////////////////////  dijkstra algorithm   ///////////////////////////////////////////

(defn dajks-alg [id-airport-start id-airport-target  airplane-range] 
  (def data-on-line (airport-online (data id-airport-start) (data id-airport-target)))
  (def r-s-airports (atom (create-r-s-point id-airport-start data-on-line)))
  (def tr (atom id-airport-start))
  
  (doseq [[ki ei] data-on-line :while (= (:s (@r-s-airports id-airport-target)) 0)]
    (let [row (all-connection (data @tr) data-on-line airplane-range)] ;I take one airport, and its all connection
      (doseq [[kj ej] row]  
        (if (and 
              (= (:s (@r-s-airports kj)) 0)
              (< (+ 
                   (:r (@r-s-airports @tr))
                   (:distance ej)) 
                 (:r (@r-s-airports kj))))
          (reset! r-s-airports (assoc @r-s-airports kj 
                                   {:r (+ 
                                         (:r (@r-s-airports @tr)) 
                                         (:distance ej)) 
                                    :s (:s (@r-s-airports kj)) 
                                    :point (conj (:point (@r-s-airports kj)) @tr)}))))  
      
      (def minimum max-double)
      (doseq [[k v] @r-s-airports]
        (if (and (= (:s v) 0) (< (:r v) minimum))
          (do 
            (def minimum (:r v))
            (def mini k))))
      
      (reset! r-s-airports (assoc @r-s-airports mini {:r (:r (@r-s-airports mini))
                                                :s 1 
                                                :point (:point (@r-s-airports mini))}))
      (reset! tr mini)))
  
  (def array-airports (atom []))
  (view-solution @r-s-airports id-airport-start id-airport-target data-on-line))


(dajks-alg 1741 8157 220)





























































