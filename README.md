The app is using in order to check weather. Source had updated every 15 minutes for the better result.

---

This app has configuration for docker, you can create image and run it by command :

```bash
$ docker-compose up
```

---

It has two REST endpoints :

1) GET http://localhost:8080/current

This one using to check the most current weather at the time of request.

---

2) GET http://localhost:8080/forecast

This endpoint gets response body in the JSON format which should includes 2 dates, between which will be calculated average temperature by celsius.


### Data entry example :
```
{
"from":"2023-12-17",
"to":"2023-12-18"
}
```

### NOTICE!
#### The source has weather information for 2 days ahead !!!

---

#### If you want to change location for weather, it can be changed in the ConfigProperties :
```java
public class ConfigProperties {

    public static final String location = "Minsk";

}
```