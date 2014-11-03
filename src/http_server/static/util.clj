(ns http_server.static.util)

(defn hex [byte-ary]
  (apply str (map (partial format "%02x") byte-ary)))

(defn sha-sum [byte-ary]
  (let [crypt (java.security.MessageDigest/getInstance "SHA-1")]
    (.update crypt byte-ary)
    (hex (.digest crypt))))
