# data-services-directory

[![Build Status](https://travis-ci.com/TOOP4EU/data-services-directory.svg?branch=master)](https://travis-ci.com/TOOP4EU/data-services-directory)

## Running the service

1. Clone the repository via `git-clone`
2. Run `mvn verify`
3. Deploy `dsd-service/target/dsd-service-[VERSION].war` to an application container.

## Configuration

DSD service pulls its data from `TOOP-Directory`. By default, the directory address
is set to `http://directory.acc.exchange.toop.eu`. If you need to change this address and
make it point to another directory address, please use `TOOP_DIR_URL` as an environment 
variable or System property.
