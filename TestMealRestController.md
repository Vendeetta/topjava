# MealRestController API test.

## Test commands:

1. __GET HTTP HEAD.__

curl -I http://localhost:8080/topjava/rest/meals

2. __GET ALL MEALS.__

curl http://localhost:8080/topjava/rest/meals

3. __GET MEAL BY ID.__

curl http://localhost:8080/topjava/rest/meals/100003

4. __CREATE NEW MEAL.__

curl -d '{"dateTime":"2020-02-01T18:00:00","description":"Созданный ужин","calories":300}' -H 'Content-Type:
application/json' http://localhost:8080/topjava/rest/meals

5. __UPDATE MEAL.__

curl -d '{"dateTime":"2020-02-01T18:15:00","description":"Обновленный завтра","calories":200}' -H 'Content-Type:
application/json' -X PUT http://localhost:8080/topjava/rest/meals/100003

6. __CHECK FOR UPDATES.__

curl http://localhost:8080/topjava/rest/meals/100003

7. __DELETE MEAL.__

curl -X DELETE http://localhost:8080/topjava/rest/meals/100003

8. __TRYING TO GET DELETED MEAL AND GETTING AN EXCEPTION.__

curl http://localhost:8080/topjava/rest/meals/100003

9. __FILTER MEAL BY DATE AND TIME. AS RESULT WE GOT TWO MEALS WITH ID 100004 and 100003__

curl http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=00:00$endDate=2020-01-30&endTime=20:00