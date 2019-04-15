# NASA Asteroids API requests

## How it works?

* Enter the START_DATE and END_DATE in Main function (date formatting: "YYYY-MM-DD") and run the project. 

## Problems and Limitations

* Currently, if you make a request to obtain 10 years worth of asteroids data, NASA server will throw 'HTTP 429 ERROR'
* NASA database goes back to '1900-01-01', all the way to '2201-01-01'. You cannot go below earliest date, and above second one.

## Necessary JARS

* jackson-core
* jackson-databind
* jackson-annotation