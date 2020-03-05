# PROGRAMMING TEST

## Getting Started

### Run

- download docker
- [MacOS] install docker-machine before
- [MacOS] eval $(docker-machine env default)
- bin/run.sh

### Requirements

Write a command-line Java program that fetches data from the following public API:
https://www.coindesk.com/api

Once executed, the program should request the user to input a currency code (USD, EUR, GBP, etc.)

Once the user provides the currency code, the application should display the following information:
- The current Bitcoin rate, in the requested currency
- The lowest Bitcoin rate in the last 30 days, in the requested currency
- The highest Bitcoin rate in the last 30 days, in the requested currency

If the currency code provided is not supported by the API, a message should inform the user. Additional requirements:
- Application should have test coverage.
- The code should follow good practices.
- Maven or Gradle should be used for code building
- Docker should be used to run application
- The final result of this exercise should be a link to project Git repository and build instructions.
