### AirportRoutes

This application is written in Clojure programming language, uses Ring, Compojure, Noir and Json format for transfering data from the application to the web site. To show the route the aircraft used by Google Maps.The main function of this application is to enable the user to find the shortest path between two airport, limited the maximum range of the aircraft. To calculate the shortest path is used Dajkstrin algorithm (http://en.wikipedia.org/wiki/Dijkstra's_algorithm).

###### Usage

To start this project, it is necessary in your computer Counterclockwise (visit the link for the download http://doc.ccw-ide.org/documentation.html#_install_counterclockwise). 
src->AirportRoutes package contains two .clj files, core .clj contains Dajkstrin's implementation of the algorithm, until the launch of the application and user interface is in file site.clj.
To start you need site.clj file is loaded in the REPL. 
Right-click on site.clj -> Run As -> Clojure Application (preload file), it starts the REPL and at the same time the file is loaded. Evaluating the file launches the test jetty server at the adress localhost:8088. Typing this addres in web browser the user interface will be showen.
## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
