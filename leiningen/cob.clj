(ns leiningen.cob
  (use [clojure.java.shell]
       [leiningen.uberjar])) 


(defn cob [project & args]
  (println "starting fitnesse server on port 9090..")
  (sh "java" "-jar" "fitnesse.jar" "-p" "9090" :dir "cob_spec"))
