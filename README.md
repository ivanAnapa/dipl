# Инструкция по настройке и запуску на ОС Windows
## 1. Клонировать проект
https://github.com/ivanAnapa/dipl</br>
## 2. Установить Docker desktop
https://docs.docker.com/desktop/install/windows-install/</br>
## 3. Ручные настройки для переключения БД
В зависимости от того, под какой БД требуется запустить тесты, необходимо вручную изменить настройки в файле build.gradle:</br>
- Запустить mysql:</br>

systemProperty 'db.url', System.getProperty('db.url', 'jdbc:mysql://localhost:3306/app')</br>
//systemProperty 'db.url', System.getProperty('db.url', 'jdbc:postgresql://localhost:5432/app')</br>

- Запустить postgresql:</br>

//systemProperty 'db.url', System.getProperty('db.url', 'jdbc:mysql://localhost:3306/app')</br>
systemProperty 'db.url', System.getProperty('db.url', 'jdbc:postgresql://localhost:5432/app')</br>

## Команды (запускать в разных вкладках терминала)
1. Запуск контейнеров: docker-compose up</br>
2. Запуск приложения sut</br>
- mysql: java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar</br>
- postgresql: java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar</br>
3. Запуск тестов с отчетом allre:</br>
- 1й запуск: ./gradlew clean test allureReport</br>
- 2й и последующие запуски: ./gradlew test allureServe</br>

## Путь, в которм хранится сгенерированный allure отчет
build/reports/allure-report/allureReport/index.html</br>
