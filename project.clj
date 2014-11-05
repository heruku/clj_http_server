(defproject http_server "0.1.0-SNAPSHOT"
  :description "An http server"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.taoensso/timbre "3.3.1"]]
  :profiles {:dev {:dependencies [[speclj "3.1.0"] ]}}
  :plugins [[speclj "3.1.0"]]
  :test-paths ["spec"]
;  :bootclasspath true
  :main http-server.core
  :aot [http-server.core])
