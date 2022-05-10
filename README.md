This is a simple backend JSON API. Written in Java and Spring Boot, this backend project is used to return results from the Hatchways blogs API. Since the Hatchways API only supports a single tag a time, it isn't suited for multiple requests and is inefficient for users. 

In this project, The API response will be a list of all the blog posts that have at least one tag specified in the tags parameter. The sortBy parameter specifies which field should be used to sort the returned results. This is an optional parameter, with a default value of `id`. The direction parameter specifies if the results should be returned in ascending order (if the value is "asc") or descending order (if the value is "desc"). The default value of the direction parameter is `asc`.

For every tag specified in the tags parameter, the backend project fetches the posts with that tag using the Hatchways API (making a separate API request for every tag specified). It then combines all the results from the API requests above and removes all the repeated posts. I made concurrent requests to the API to improve the response times and decrease error rates by requesting in parallel the same information.



```
Request:
Route: /api/ping
Method: GET
Response:
Response body (JSON):
{
  "success": true
}
Response status code: 200
```


Request:
Route: /api/posts
Method: GET
Query Parameters:
![project](https://user-images.githubusercontent.com/7539561/167565872-e7f8cd89-5ee4-45bd-b3a9-0aee905461bc.png)

Here is how the response look:
```

{
  "posts": [{
  "id": 1,
  "author": "Rylee Paul",
  "authorId": 9,
  "likes": 960,
  "popularity": 0.13,
  "reads": 50361,
  "tags": [ "tech", "health" ]
  },
  ...
  ]
}
Response status code: 20

```


If `tags` parameter is not present:
```
{
  "error": "Tags parameter is required"
}
Response status code: 400
```


If a `sortBy` or `direction` are invalid values, specify an error like below:
```
{
  "error": "sortBy parameter is invalid"
}
Response status code: 400
```







# REQUIREMENTS:

- MUST HAVE GRADLE VERSION Gradle >= 7.4.1
- USED JAVA >= 16.0.1
- OS: Windows 10 10.0 amd64 or Linux system


# My Testing Environment Details:

### FOR LINUX UBUNTU:

java version "17.0.1" 2021-10-19 LTS
Java (TM) SE Runtime Environment (build 17.0.1+12-LTS-39)
Java HotSpot (TM) 64-Bit Server VM (build 17.0.1+12-LTS-39, mixed mode, sharing)

Gradle 7.4.1:
Build time:   2022-03-09 15:04:47 UTC
Revision:     36dc52588409b4b72f2010bc07599e0ee0434e2e
Kotlin:       1.5.31
Groovy:       3.0.9
Ant:          Apache Ant (TM) version 1.10.11 compiled on July 10 2021
JVM:          1.8.0_312 (Private Build 25.312-b07)
OS:           Linux 5.4.0-97-generic amd64


### FOR WINDOWS 10:

java version "17" 2021-09-14 LTS
Java(TM) SE Runtime Environment (build 17+35-LTS-2724)
Java HotSpot(TM) 64-Bit Server VM (build 17+35-LTS-2724, mixed mode, sharing)

Gradle 7.4.1:
Build time:   2022-03-09 15:04:47 UTC
Revision:     36dc52588e09b4b72f2010bc07599e0ee0434e2e
Kotlin:       1.5.31
Groovy:       3.0.9
Ant:          Apache Ant(TM) version 1.10.11 compiled on July 10 2021
JVM:          16.0.1 (Oracle Corporation 16.0.1+9-24)
OS:           Windows 10 10.0 amd64


# How to run:

Open a terminal/command prompt window in backend folder and run this command (without quotations):


Linux:
"gradle build"

Windows:
"gradlew build"


This command will make sure all the dependencies are installed

Afterwards, run:

Linux:
"gradle bootRun"

Windows:
"gradlew bootRun"

This command will run the program. It will be hosted on localhost:8080.

To re-run my own unit tests type in the command:

Linux:
gradle :test --tests "com.walnut.backend.BackendApplicationTests"

Windows: 	
gradlew :test --tests "com.walnut.backend.BackendApplicationTests"

However, when running the previous "build" command, those unit tests are already ran. 

Here are some sample pictures:

![image](https://user-images.githubusercontent.com/7539561/167566469-c06f0428-8d35-4669-96aa-774fd3e82a54.png)
![image](https://user-images.githubusercontent.com/7539561/167566513-527ccbca-f782-45ae-b55b-c3572cfa2e10.png)
![image](https://user-images.githubusercontent.com/7539561/167566633-8fcc9ef7-6cb7-4973-ab88-623aa9223fad.png)


