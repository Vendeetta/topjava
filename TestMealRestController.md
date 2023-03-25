curl -I http://localhost:8080/topjava/rest/meals
curl http://localhost:8080/topjava/rest/meals
curl http://localhost:8080/topjava/rest/meals/100003
curl -d '{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}' -H 'Content-Type:
application/json' http://localhost:8080/topjava/rest/meals
curl -d '{"dateTime":"2020-02-01T18:15:00","description":"Обновленный завтра","calories":200}' -H 'Content-Type:
application/json' -X PUT http://localhost:8080/topjava/rest/meals/100003
curl http://localhost:8080/topjava/rest/meals/100003
curl -X DELETE http://localhost:8080/topjava/rest/meals/100003
#entity not found
curl http://localhost:8080/topjava/rest/meals/100003
curl http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=00:00$endDate=2020-01-30&endTime=23:59