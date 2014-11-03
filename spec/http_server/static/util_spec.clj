(ns http_server.static.util_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.static.util]))

(describe "sha-sum"
  (it "returns hex of sha"
    (should= "79b7dfe6262691ad86e1e85add9791bc8a3af302" (sha-sum (to-bytes "text content\n")))))
