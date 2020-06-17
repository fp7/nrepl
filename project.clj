(defproject nrepl "0.8.0-alpha2"
  :description "nREPL is a Clojure *n*etwork REPL."
  :url "https://nrepl.org"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:name "git" :url "https://github.com/nrepl/nrepl"}
  :min-lein-version "2.9.1"
  :source-paths ["src/clojure"]
  :java-source-paths ["src/java"]
  :test-paths ["test/clojure"]
  :javac-options ["-target" "8" "-source" "8"]

  :aliases {"bump-version" ["change" "version" "leiningen.release/bump-version"]
            "test-all" ["with-profile" "+1.7:+1.8:+1.9:+1.10:+fastlane" "test"]
            "docs" ["with-profile" "+maint" "run" "-m" "nrepl.impl.docs" "--file"
                    ~(clojure.java.io/as-relative-path
                      (clojure.java.io/file "doc" "modules" "ROOT" "pages" "ops.adoc"))]
            "kaocha" ["with-profile" "+test" "run" "-m" "kaocha.runner"]}

  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]]

  :deploy-repositories [["clojars" {:url "https://clojars.org/repo"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    :sign-releases false}]]

  :profiles {:fastlane {:dependencies [[nrepl/fastlane "0.1.0"]]}
             :test {:dependencies [[com.hypirion/io "0.3.1"]
                                   [commons-net/commons-net "3.6"]
                                   [lambdaisland/kaocha "1.0-612"]
                                   [lambdaisland/kaocha-junit-xml "0.0-70"]]
                    :plugins      [[test2junit "1.4.2"]]
                    :test2junit-output-dir "test-results"
                    :aliases {"test" "test2junit"}}
             ;; Clojure versions matrix
             :provided {:dependencies [[org.clojure/clojure "1.10.1"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :1.10 {:dependencies [[org.clojure/clojure "1.10.1"]]
                    :source-paths ["src/spec"]}
             :master {:repositories [["snapshots"
                                      "https://oss.sonatype.org/content/repositories/snapshots"]]
                      :dependencies [[org.clojure/clojure "1.11.0-master-SNAPSHOT"]]}

             ;;; Maintenance profile
             ;;
             ;; It contains the small CLI utility (aliased to "lein docs") that
             ;; generates the nREPL ops documentation from their descriptor
             ;; metadata.
             :maint {:source-paths ["src/maint"]
                     :dependencies [[org.clojure/tools.cli "1.0.194"]]}

             ;; CI tools
             :cloverage [:test
                         {:plugins [[lein-cloverage "1.1.2"]]
                          :dependencies [[cloverage "1.1.2"]]
                          :cloverage {:codecov? true
                                      ;; Cloverage can't handle some of the code
                                      ;; in this project
                                      :test-ns-regex [#"^((?!nrepl.sanity-test).)*$"]}}]

             :cljfmt {:plugins [[lein-cljfmt "0.6.1"]]
                      :cljfmt {:indents {as-> [[:inner 0]]
                                         with-debug-bindings [[:inner 0]]
                                         merge-meta [[:inner 0]]
                                         testing-dynamic [[:inner 0]]
                                         testing-print [[:inner 0]]}}}

             :eastwood [:test
                        {:plugins [[jonase/eastwood "0.3.4"]]
                         :eastwood {:config-files ["eastwood.clj"]}}]})
