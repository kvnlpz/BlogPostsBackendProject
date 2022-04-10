#REQUIREMENTS:

- MUST HAVE GRADLE VERSION Gradle >= 7.4.1
- USED JAVA >= 16.0.1
- OS: Windows 10 10.0 amd64 or Linux system


#My Testing Environment Details:

###FOR LINUX UBUNTU:

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


###FOR WINDOWS 10:

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


#How to run:

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



