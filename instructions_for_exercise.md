# GameRefinery - Backend Coding Assignment

## Abstract

Your task is to implement REST interfaces for displaying data from MongoDB database and also for
updating data in the database. Once implementation is completed you will push your code to Github
repository and from there deploy interfaces and single web application to Openshift Online Started
as a container.

## Github

Create github account if you don't have one and create a public repository which will contain your
code. You will be deploying your code to Openshift directly from Github repo

## Coding and Java

This assignment must be implemented with Java 8 and it will be running on Wildfly v13. Project
must be build with Maven using dependencies from attached pom.xml

## Openshift Online

If you don't have Openshift online account, create one in here https://manage.openshift.com/
register/confirm/5 Follow instructions and select "Sign up" link under LOG IN RED HAT button.
Openshift Online uses Red Hat Developer account for authentication. Once application is deployed it
should have public url and it can be tested thru that
Tip: Once logged in to Openshift search Wildfly from catalog that is displayed in the front page.

## Documentation

Write simple documentation about your REST interfaces in form of README.md file in your Git repo

## Use Cases

You must implement following REST interfaces. All output and input must be in JSON format. Data
for use cases is in the database and data contains Top 200 App Store games from fi, it, es markets.* Read top 10, 20, 50, 100, 200 games from given market

- List all games that have entered to top 10, 20, 50 between now and given amount of days. Listing
is done per market
- Create and interface which can be used to mark given game as an favourite.
- Fetch rank history of give game from given market in three different ranges; daily, weekly and
monthly

All data reading interfaces should contain option to fetch data only from given fields.

## Evaluation

We will be evaluating following

- quality of your code
- how much help you needed in the assignment.
- how quickly you complete the assignment. We are not talking about hours here. Concentrate on
quality instead of speed.