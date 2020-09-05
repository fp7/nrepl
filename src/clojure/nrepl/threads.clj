(ns nrepl.threads
  (:import [java.util.concurrent ThreadFactory]
           [java.util.concurrent Executors]))



(def ^{:dynamic true} *platform-thread-factory* nil)
