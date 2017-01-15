1. Путь до файла с днями рождения и именами можно задать
в файле application.properties.
В противном случае будет использоватся файл persons.txt из папки resources.
2. Для запуска задачи на выборку по месяцу, необходимо обратиться по ссылке:
http://<host>:<port>/BirthdayService/rest/birthday/findMatchBirthdays?month=<номер месяца>
3. Для проверки выполнения задачи, неообходимо перейти по ссылке
http://<host>:<port>/BirthdayService/rest/birthday/checkBirthdaysTaskDone?taskId=<id задачи>