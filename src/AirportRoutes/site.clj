(ns AirportRoutes.site
(:use (compojure handler 
                 [core :only (GET POST defroutes)]))

(:require compojure.route 
          [net.cgrand.enlive-html :as en]
          [ring.util.response :as response] 
          [ring.adapter.jetty :as jetty]
          [clj-json.core :as json]))

(en/deftemplate homepage
  (en/xml-resource "homepage.html")
  [request])

(def server (jetty/run-jetty #'app {:port 8088 :join false}))

(en/deftemplate homepage
  (en/xml-resource "homepage.html")
  [request])


(defroutes app*
  (GET "/" request (homepage request))
  (POST "/test" request 
          #_{:status 200
          :body (with-out-str (print request))
          :headers {"Content-Type" "text/plain"}}
        (let [n (-> request :params :number)]
          (response/redirect (str n)))))


(def app (compojure.handler/site app*))