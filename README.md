# Usage

Run
> ./gradlew build

> java -jar build/libs/meter-reader.jar src/test/resources/sample.txt
384 records created.

> java -jar build/libs/meter-reader.jar src/test/resources/error.txt
Invalid date format occurred at line 3
0 records created.

# Assumptions:
- If any error is found while processing the file, the entire file is discarded and the data within is rejected. No data would be committed to the database.
- An empty line can be ignored.
- One file processed at a time, eg. No concurrency concerns.
- Each file contains exactly one 200 record for each NMI.
- Each file contains exactly one 300 record for each NMI and Date.

# What is the rationale for the technologies you have decided to use?
There a number of way to solve this problem. 

Language Choice: Java
Java is a well known language and it was chosen over others mainly because of its Object Oriented nature. While my first choice would be Kotlin, also a OO language, I did’t choose it as I made an assumption that the reviewer of this code may not be familiar with it and more likely that Java is more understood.

Other choices I considered were JavaScript, but because it was dealing with files and databases, JavaScript can be a little complicated dealing with those and there are many different thoughts on how this is best achieved using JavaScript.

Within the Java landscape, there are a number of different choices when it comes to parsing and loading into a database. 

Because of the nature of the file format, being a CSV like format, with less complicated structure, a simple String.split(“,”) can achieve the desired result. Therefore no need for a complex CSV parser.

As stated, the files could be quite large, therefore IO Streaming is required. The simple BufferedInputStream could suffice, saving on memory in the process and having the ability to handle large files.

A simple service is used to process each line, delegating to specific line processors to validate the current context and the line itself for data. Once each line is considered valid within the context of the file, the line is processed.

In order to store in the database, Hibernate was used. While this has some overhead in performance, given the time and object oriented nature, it made sense, however, JDBC template from Spring are more performant, but also come with a larger overhead.

As for database, I chose a database with some transaction control. Because of the assumption made earlier, it should be possible to write data to the database as we are processing, but if an error is found later in the file, that data should not be committed.

# What would you have done differently if you had more time?
Firstly, I enjoyed working on this. It was fun and gave me a break from my current workload.

With more time, I would have handled the NEM13 version also. 

The Map used to hold the context is suitable for this, but over time I would exchange it for a dedicated class. Rationale is that a lot more can be done with the context model, including event handlers, monitoring, etc. This is a good source of advancement.

Also there are other validations that need to be considered. Eg. Quality metrics and manual and automatic reading. As the specification is 45 pages long, there are a myriad of other requirements.

This implementation only handles a single file. It would be part of a more elaborate batch processor, with functions to handle errors and retry mechanisms. Realtime monitoring and performance metrics.

Given more data, would look at common usage patterns and tolerances of usage. There is also room to dispute the values if the values fall out of range, and given that the readings re estimated, we could request a manual reading to rectify issues.

# What is the rationale for the design choices that you have made?
I chose this design as the context changes during the file processing. I first of all needed to know the bare minimum to process and save the data extracted from each 300 record. That meant reading the 200 record and storing the data for later use. Hence using a simple Map would suffice. 

The file was read each line at a time wrapped in a try catch, in order to catch any exceptions thrown during the processing. This made for a simple error handler.

An exception hierarchy was created in order to have easy to understand exception handling. Each exception took in the current context at the time of the exception. Any exceptions thrown during processing that are not expected are captured in a UnknownException, which can be used to catch any further exceptions that may arise.

The LineProcessor interface meant that each line could be parsed by its own LineProcessor implementation. Each having its own set of context validation, line validation and finally processing.

The 100 record would start a transaction. The 900 record would commit the transaction. If no 900 record was provided or an error was detected, the entire file was rejected and the transaction was rolled back.

H2 database was chosen because it is in memory, supports transaction and UUID as primary key natively.

Hibernate was chosen as dealing to UUID in JDBC can be difficult. Given that Hibernate is easy to use, and has UUID support for primary keys and supported H2, it was an obvious choice. However, it may result in some performance downgrades, but given the nature of the problem, it can be considered a lesser problem.

Alternatives Considered
- Write a parser using ANTLR to extract the desired values. It would have been faster, but the code would be more complicated and less maintainable in the future. This would have been a reviewers nightmare.
- Read the entire file into memory. While faster, as the file can be quite large, the memory usage would have blown up.
- Usage of Spring, Micronaut or other framework. The problem is quite small and confined. While some of the features in these frameworks can be helpful for testing and dependency management, they also come with some inherit overhead in both memory and performance.
- Database needed to be small and require no infrastructure. Oracle, SQL Server and PostgreSQL were considered too ‘enterprise’ for the task at hand.
- Concurrency concerns were considered, but given the simple nature of the problem, I decided to not handle these. If they were, a complex environment would prevail. Locking each NMI for update could lead to deadlocks, which would be difficult to resolve. However, there is a potential to have more than 1 file, create the same, overlapping data. Assumption was noted.
- There was consideration for multiple 200 records for the same NMI within the same file with potentially different interval duration. Assumption was noted.