(ns http_server.spec_helper)

(defn request-reader [string]
  (clojure.java.io/reader (char-array string)))

(defn status-of [response]
  (nth response 0))

(defn headers-of [response]
  (nth response 1))

(defn body-of [response]
  (nth response 2))

(defn to-bytes [string]
  (bytes (byte-array (map byte string))))

(defn to-byte-seq [string]
  (seq (to-bytes string)))

