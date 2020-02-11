# Notes after the exercise ( 2020-02-11 )

- This repository contains Java code written as an exercise for applying to a Senior Backend Developer position.
- The [instructions for exercise](instructions_for_exercise.md) mandate usage of pom-file with deprecated dependencies.

# Top Grossing data

With Top Grossing data ('tg' from here onwards) you can make HTTP requests
to the tg server and get data about top ranking games in return. All data sent
in requests and received in responses is in JSON.

# Deployment

1. Using an openshift cluster create a new Wildfly 13 Service and use the URL
of the github repository of this project. Expose the service to the internet
by creating a route.
2. You should have the database password of this service. Use that to create a
secret, a key-value-pair: 
    ```
    key = DB_PASSWORD
    value = the password that you have
    ```
3. Add that secret to the environment of the Wildfly deployment in the 
deployment configurations
4. The server is running, and the address is SERVER_HOSTNAME and the port is 80.
We will use that string in the example queries below.

# Health check

After deployment you should be able to check that the server is up and running
with the following unix shell command:

```
curl -i http://SERVER_HOSTNAME/rest/v1.0/health
```

The health check end point also checks that the database connection works ok.
Health check does not need any authentication.

# Authentication

You should have received user credentials already if you are using this
software. Tg does not curretly support user management.

Tg uses 
[the 'Basic' HTTP authentication scheme](https://tools.ietf.org/html/rfc7617).
Suppose you have the following credentials:

```
username: Aladdin
password: open sesame
```

You can form an Authorization-header content string by using the following 
command:

```
echo -n 'Basic' `echo -n 'Aladdin:open sesame' | base64`
```

The result is:

```
Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
```

You can then use this string as a header value in the requests shown below. 
Just replace the 'XXX' with the created header value.

This kind of authentication is not a recommended way of implementing authentication
anymore and it should be replaced with a more secure modern way.

# Filters

Tg supports filtering data. You have to provide the filters as part of the
JSON sent to the server as seen in the examples below. If you specify 1 as a
filter value, then the field with the name of the filter value will be
included in the response. 0 will be excluded. All the values in the filter
listing have to be the same, so all are 1 or all are 0. If for example you
would like to exclude all the long rank listings from the response you can do
that with the following kind of filter:

```
{
    "appGenreHistories": 0,
    "appGenreDhistories": 0,
    "history": 0,
    "dhistory": 0,
    "historyWeek": 0,
    "dhistoryWeek": 0,
    "historyMonth": 0,
    "dhistoryMonth": 0,
    "srankHistory": 0,
    "sdrankHistory": 0
}
```

You can see the fields that can be filtered by first doing a query without any
filters set or with an empty filters object.


# Pagination

Tg supports pagination. If you want to query the top 200 games with lots of
fields included in the filters, you don't have to do that in one request as
the corresponding response would be pretty big. Instead you choose page_size
and page. Page numbers are indexed from zero. You have to specify both
page_size and page or none at all. See the examples below. Not all end points
support pagination as some them don't really need it. When you increase the
page and eventually you don't get any records back, then you know you are in
the end of the records of the query.

# Error messages

Tg tries to give helpful error messages if you send wrong kind of query.

# End points

All the end points have an example query that works after the Authorization
-header and SERVER_HOSTNAME are updated.

## Current Top
Read top 10, 20, 50, 100, 200 games from given market

```
curl \
-X POST \
-d '{
      "market": "fi",
      "count": 50,
      "filters": {
        "name": 1,
        "market": 1,
        "rank": 1
      },
      "page_size": 5,
      "page": 0
    }' \
-H "Authorization: XXX" \
-H "Content-type: application/json" \
http://SERVER_HOSTNAME/rest/v1.0/games/current_top
```

## Recent Top
List all games that have entered to top 10, 20, 50 between now and given
amount of days. Listing is done per market.

The possible alternatives for top_amount are:
```
"TOP10", "TOP20", "TOP50"
```

The pagination works in such a way that the matching are ordered by market and
the top_amount. Then that list is divided in pages and each page is grouped
into groups by market.

The markets field limits the games to those markets only. Not specifying the
parameter gives all the markets.


```
curl \
-X POST \
-d '{
      "markets": [
        "fi",
        "it"
      ],
      "days_ago": 400,
      "top_amount": "TOP20",
      "filters": {
        "name": 1,
        "appId": 1,
        "top20Entry": 1
      },
      "page_size": 20,
      "page": 0
    }' \
-H "Authorization: XXX" \
-H "Content-type: application/json" \
http://SERVER_HOSTNAME/rest/v1.0/games/recent_top
```

## Create Favourite

Here the path parts fi and 1333256716 represent market and appId respectively.

```
curl \
-X PUT \
-H "Authorization: XXX" \
http://SERVER_HOSTNAME/rest/v1.0/games/favourite/fi/1333256716
```

## List All Favourites
```
curl \
-X POST \
-d '{
      "filters": {
        "name": 1,
        "appId": 1,
        "market": 1
      },
      "page_size": 20,
      "page": 0
    }' \
-H "Authorization: XXX" \
-H "Content-type: application/json" \
http://SERVER_HOSTNAME/rest/v1.0/games/favourite/find_all
```

## Delete Favourite

Here the path parts fi and 1333256716 represent market and appId respectively.

```
curl \
-X DELETE \
-H "Authorization: XXX" \
http://SERVER_HOSTNAME/rest/v1.0/games/favourite/fi/1333256716
```

## Rank History

Fetch rank history of given game from given market in three different ranges;
daily, weekly and monthly.

The alternatives for the field frequency are:
```
"DAILY", "WEEKLY", "MONTHLY"
```

```
curl \
-X POST \
-d '{
      "frequency": "DAILY",
      "filters": {
        "ts": 1,
        "rank": 1
      }
    }' \
-H "Authorization: XXX" \
-H "Content-type: application/json" \
http://SERVER_HOSTNAME/rest/v1.0/games/fi/1333256716/rank_history
```

# Notes on architecture

- The games collection in the database is never written to, only read. There
isn't any need to do so as the favourites are stored in the user collection.
But if there was, then it could be good to avoid that anyway since there is
software component that regularly updates that collection (as there the history
data is time series data that has to be regularly updated) . Having two
components reading and writing the same collection is a case of tight coupling.
Whenever either one of those components is changed then the other has to
be inspected on how it uses the collection so that the changes won't break
anything. In practice this is very hard to do and that's why it's
good to design software to be loosely coupled, that is, so that different
components interact with each other minimum amount and through carefully
specified and managed interfaces.

# Further development

- Update the authentication system to for example keycloak.
- Speed improvements: There is only one index on id field in the users collection.
With this small data size, it doesn't really matter but with more data indices should be
added.
- Decrease database usage: Currently the user is fetched twice if the user is
needed after the initial authentication.
- Create automated tests for the project.
- logging
- Support different environments: data in EnvironmentalConstants should be in
files and it should be fetched from those files based on what environment.
This data could be stored in environment variable like the database password.

