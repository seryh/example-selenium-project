;; Небольшой набор хелперов

(ns utils.webdriver
  (:require [clj-webdriver.taxi :refer :all]))

(defn ->>click
  "Обертка для click taxi,
   для использования в макросе ->>"
  [& args]
  (apply click args)
  (last args))

(defn ->>keys
  "Обертка для send-keys taxi,
   для использования в макросе ->>"
  [keycode el]
  (send-keys el keycode) el)

(defn wait
  ([t el] (Thread/sleep t) el)
  ([t] (Thread/sleep t))
  ([] (Thread/sleep 100)))

(defn wait-for-jq-ajax
  "Ожидание завершения всех ajax запросов,
   вызванных jQuery"
  []
  (loop [ajax-active nil]
    (when (not= ajax-active 0)
      (wait)
      (recur @(future (execute-script "return jQuery.active"))))))

(defn $ [^String selector]
  (find-element {:css selector}))

(defn x-path [^String selector]
  (find-element {:xpath selector}))

(defn type-text
  "Печатает текст s в елементе el, с задержкой,
   эмуляция ручного ввода"
  ([s el] (type-text s 100 el))
  ([s timeout el]
   (clear el)
   (let [size (count s)]
     (loop [i 0]
       (let [char (str (get s i))]
         (when (< i size)
           (send-keys el char)
           (wait timeout)
           (recur (inc i)))))) el))

(defn multi-type-text
  "Печатает текст асинхронно сразу для нескольких полей ввода
   пример:
       (multi-type-text
          \"RichHickey\" ($ \"#user_login\")
          \"rich@gmail.com\" ($ \"#user_email\")
          \"qwerty\" ($ \"#user_password\"))"
  [& body]
  (let [el-list (apply hash-map body)
        doit (fn [coll s el]
               (let [promise (future (type-text s el) :ok)]
                 (conj coll promise)))
        promises (reduce-kv doit [] el-list)]
    (mapv deref promises)))


