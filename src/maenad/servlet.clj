(ns maenad.servlet
  (:gen-class :extends com.google.wave.api.AbstractRobotServlet)
  (:import
    [com.google.appengine.api.users UserServiceFactory]
    [com.google.wave.api Blip Event EventType RobotMessageBundle TextView Wavelet])
  (:require [clojure.contrib.str-utils  :as str-utils]
            [clojure.contrib.str-utils2 :as str-utils2]))

(defn append-blip
  [wavelet message]
  (let [view (.getDocument (.appendBlip wavelet))]
    (.append view message)))

(defn welcome-if-new
  [bundle wavelet]
  (if (.wasSelfAdded bundle) 
    (append-blip wavelet "user=>")))

(defn blip-submitted-events
  [events]
  (filter (fn [e] (= (EventType/BLIP_SUBMITTED) (.getType e))) events))

(defn process-blip
  [blip]
  (let [document (.getDocument blip)
        text (.getText document)]
    (.append document 
      (str "\n" (eval (read-string text))))))

(defn -processEvents
  [this bundle]
  (let [wavelet (.getWavelet bundle)]
    (welcome-if-new bundle wavelet)
    (doseq [event (blip-submitted-events (.getEvents bundle))]
      (process-blip (.getBlip event)))))
