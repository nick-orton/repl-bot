(ns replbot.servlet
  (:gen-class :extends com.google.wave.api.AbstractRobotServlet)
  (:import
    [com.google.appengine.api.users UserServiceFactory]
    [com.google.wave.api Blip Event EventType 
                         RobotMessageBundle TextView Wavelet]))

(defn blip-submitted-events
  [events]
  (filter (fn [e] (= (EventType/BLIP_SUBMITTED) (.getType e))) events))

(defn process-blip
  [blip]
  (let [document (.getDocument blip)
        text (.getText document)]
    (.append document 
      (try (str "\n evaluates to: " (eval (read-string text))) 
        (catch Exception e (.getStackTrace e))))))

(defn -processEvents
  [this bundle]
  (let [wavelet (.getWavelet bundle)]
    (doseq [event (blip-submitted-events (.getEvents bundle))]
      (process-blip (.getBlip event)))))
