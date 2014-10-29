(ns http_server.clack.core
  (use [http_server.clack.request]
       [http_server.clack.response]))

(def middleware (atom []))

(defn use-middleware [app]
   (swap! middleware conj app))

(defn enter-middleware [env]
   ((first @middleware) nil env))

(defn clack [raw-request]
  (->> raw-request
       parse
       enter-middleware
       raw-response))
