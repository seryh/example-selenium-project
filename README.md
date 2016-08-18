# example-selenium-project

Пример простого clojure проекта для тестирования с помощью selenium.

Для примера тестов используется сайт - gosuslugi.ru

В примере реализовано 2 теста:

- проверка формы регистрации 

- проверка поиска

Профайл uberjar использует phantom webdriver.

Выбор webdriver осуществляется через профайлы :phantom и :selenium

## Installation
Необходимо скачать и положить в корень проекта, phantom и selenium webdrivers

[phantom](http://phantomjs.org/download.html)
[selenium](https://sites.google.com/a/chromium.org/chromedriver/downloads)

Для сборки или запуска repl проекта под windows, необходимо использовтаь профайл - windows

lein with-profile +windows uberjar

Для сборки проекта под linux использовать профайл не нужно

lein uberjar

## Usage

    $ example-selenium-project>lein with-profile +windows,+phantom run
      Open driver with profile:phantom and url: https://esia.gosuslugi.ru/registration/
      2016-08-18 16:32:37,892 [main] INFO  example-selenium-project.tests.gosuslugi-reg - gosuslugi-reg-form -> ok
      Open driver with profile:phantom and url: https://www.gosuslugi.ru/
      2016-08-18 16:32:48,315 [main] INFO  example-selenium-project.tests.gosuslugi-main - gosuslugi-main-search-form -> ok
      {:tests-fail 0, :tests-success 2}

    
При запуске собранного через uberjar исполняемого файла, 
phantom webdriver должен находится в каталоге с запускаемым jar файлом.

