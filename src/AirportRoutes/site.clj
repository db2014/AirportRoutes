(ns AirportRoutes.site
(:use (compojure handler 
                 [core :only (GET POST defroutes)]))

(:require compojure.route 
          [net.cgrand.enlive-html :as en]
          [ring.util.response :as response] 
          [ring.adapter.jetty :as jetty]
          [clj-json.core :as json]))


(defonce list-names (atom {}))
(defonce counter (atom 0))

(defn add-to-list [name]
  (swap! list-names assoc (swap! counter inc) name ))



(def server (jetty/run-jetty #'app {:port 8088 :join false}))

(en/deftemplate homepage
  (en/xml-resource "homepage.html")  
  [request]
  [:#list :li] (en/clone-for [[id name] @list-names]                               
                             (en/content (format "%s : %s" id name))))


(defroutes app*
  (GET "/" request (homepage request))
  (POST "/test" request 
        #_{:status 200
           :body (with-out-str (print request))
           :headers {"Content-Type" "text/plain"}}
        (add-to-list (-> request :params :name))
        (homepage request)))


(def app (compojure.handler/site app*))