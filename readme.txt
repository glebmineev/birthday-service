Путь до файла с днями рождения и именами можно задать
в файле application.properties.
В противном случае будет использоватся файл persons.txt

для запуска задачи на выборку необходимо обратиться по ссылке
http://<host>:<port>/BirthdayService/rest/birthday/findMatchBirthdays?month=<номер месяца>


для проверки выполнения задачи, неообходимо перейти по ссылке
http://<host>:<port>/BirthdayService/rest/birthday/checkBirthdaysTaskDone?taskId=<id задачи>