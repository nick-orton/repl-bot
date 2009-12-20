(ns replbot.servlet
  (:gen-class :extends com.google.wave.api.AbstractRobotServlet)
  (:use clojure.stacktrace)
  (:import
    [com.google.appengine.api.users UserServiceFactory]
    [com.google.wave.api Blip Event EventType 
                         RobotMessageBundle TextView Wavelet]))

(defn blip-submitted-events
  [events]
  (filter (fn [e] (= (EventType/BLIP_SUBMITTED) (.getType e))) events))

(defn evaluate [text] 
  (str "\n evaluates to: " 
           (try 
             (eval (read-string text)) 
             (catch Exception e (root-cause e)))))

(defn process-blip
  [blip]
  (let [document (.getDocument blip)
        text (.getText document)]
    (.append document (evaluate text))))

(defn -processEvents
  [this bundle]
  (let [wavelet (.getWavelet bundle)]
    (doseq [event (blip-submitted-events (.getEvents bundle))]
      (process-blip (.getBlip event)))))
