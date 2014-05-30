### AirportRoutes

This application is written in Clojure programming language, uses Ring, Compojure, Noir and Json format for transfering data from the application to the web site. To show the route the aircraft used by Google Maps.The main function of this application is to enable the user to find the shortest path between two airport, limited the maximum range of the aircraft. To calculate the shortest path is used Dajkstrin algorithm (http://en.wikipedia.org/wiki/Dijkstra's_algorithm).

###### Usage

To start this project, it is necessary in your computer Counterclockwise (visit the link for the download http://doc.ccw-ide.org/documentation.html#_install_counterclockwise). 
src->AirportRoutes package contains two .clj files, core .clj contains Dajkstrin's implementation of the algorithm, until the launch of the application and user interface is in file site.clj.
To start you need site.clj file is loaded in the REPL. 
Right-click on site.clj -> Run As -> Clojure Application (preload file), it starts the REPL and at the same time the file is loaded. Evaluating the file launches the test jetty server at the adress localhost:8088. Typing this addres in web browser the user interface will be showen.
###### Goal

The main objective of this application was insight with functional abilities (Clojure) language, and the ability of language to quickly process large amounts of data. Good knowledge of HTML and JavaScript helped us pomoglo da stranicu za prikaz podataka lako napravimo but it certainly represented a challenge to the integration Clojure programming language. Also, some other concepts are so interesting for us, like passing functions as arguments and returning them from function calls; atoms and the way to change their states; multimethods; and of course map and reduce function that are present everywhere. Solely, we had a lot of problems with passing Json object to JavaScript and finding appropriate documentations. Developing this application we had opportunity to experience one new and efficient approach in considering the problems, thinking and code writing.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version the same as Clojure.
