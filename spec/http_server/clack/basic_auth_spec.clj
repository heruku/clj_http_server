(ns http_server.clack.basic_auth_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.clack.methods]
       [http_server.clack.basic_auth]))

(defn two-hundred-app [env]
  [200 {} ""])

(defn request-protected-path-with-credentials [setup-credentials user-credentials]
  (basic-auth "/protected" setup-credentials two-hundred-app {:method GET :path "/protected" :headers {"Authorization" user-credentials}}))

(defn request-protected-path []
  (basic-auth "/protected" "password" two-hundred-app {:method GET :path "/protected"}))

(defn request-unprotected-path []
  (basic-auth "/protected" "password" two-hundred-app {:method GET :path "/"}))

(describe "basic auth"
  (it "sends 401 Not Authorized for a simple get"
    (should= 401 (status-of (request-protected-path))))

  (it "requires authentication in the body for simple-get"
    (should= "Authentication required" (body-of (request-protected-path))))

  (it "calls next app if path and credentials match"
    (should= 200 (status-of (request-protected-path-with-credentials "password" "Basic password"))))

  (it "calls next app if path is not protected"
    (should= 200  (status-of (request-unprotected-path))))

  (context "initializing"
    (it "returns a partial function that complies the clack interface"
      (let [my-basic-auth (init-basic-auth "/logs" "username" "password")]
        (should= 401 (status-of (my-basic-auth two-hundred-app {:method GET :path "/logs"})))))

    (def base64-admin-hunter2-credentials "Basic YWRtaW46aHVudGVyMg==")

    (it "encodes username and password in base64"
      (let [my-basic-auth (init-basic-auth "/logs" "admin" "hunter2")]
        (should= 200 (status-of (my-basic-auth two-hundred-app {:method GET :path "/logs" :headers {"Authorization" base64-admin-hunter2-credentials}}))))))) 
