(ns http_server.clack.basic_auth_spec
  (use [speclj.core]
       [http_server.spec_helper]
       [http_server.clack.methods]
       [http_server.clack.basic_auth]))

(defn two-hundred-app [env]
  [200 {} ""])

(describe "basic auth"
  (it "sends 401 Not Authorized for a simple get"
    (should= 401 (status-of (basic-auth "/logs" "irrelevant" nil {:method GET :path "/logs"}))))

  (it "requires authentication in the body for simple-get"
    (should= "Authentication required" (body-of (basic-auth "/logs" "irrelevant" nil {:method GET :path "/logs"}))))

  (it "calls next app if path and credentials match"
    (should= 200 (status-of (basic-auth "/logs" "password" two-hundred-app {:method GET :path "/logs" :headers {"Authorization" "Basic password"}}))))

  (it "calls next app if path is not protected"
    (should= 200  (status-of (basic-auth "/logs" "irrelevant" two-hundred-app {:method GET :path "/"}))))

  (context "initializing"
    (it "returns a partial function that complies the clack interface"
      (let [my-basic-auth (init-basic-auth "/logs" "username" "password")]
        (should= 401 (status-of (my-basic-auth nil {:method GET :path "/logs"})))))

    (it "encodes username and password in base64"
      (let [my-basic-auth (init-basic-auth "/logs" "admin" "hunter2")]
        (should= 200 (status-of (my-basic-auth two-hundred-app {:method GET :path "/logs" :headers {"Authorization" "Basic YWRtaW46aHVudGVyMg=="}}))))))) 
