# Cache using ConcurrentHashMap Practice

This project uses Springboot and ConcurrentHashMap to build a thread safe concurrent local cache function.
User can store key-value pair data as cache and get the data by key. The stored data will be expired according to users' customised configuration 
and automatically deleted in fixed time period.

## Project introduction
### Structure
* Spring MVC
  * Cache RESTful Controller
  * Cache Service
  * Cache Entity
  * Test Class
  
## Usage
### GET Data URL
* Example GET url: localhost:8080/api/get?key={key}
* Example response if data exists and not expired: 
  * Cache key: 1, Cache Value: student, expireTime: 30000
* Example response if data exists and not expired:
  * No Valid Data!
### POST Data
* POST action needs to be tested through PostMan or other applications.
  * Example input:
    * {key:"1",value:"student", expireTime:"30"}

## Functions and concurrent performance tests
* Test if the user do not define an expire time. The data will expire after 10s by default.
* Test if the user defines an expire time.
* Test the concurrent performance by creating a thread pool with 10 threads and adding 10 million operations to the thread pool in 10 times.
### Example output:
<br>No predefined Expire Time
<br>Cache key: 1, Cache Value: test, expireTime: 10000
<br>No Valid Data!

<br>With Predefined expire time
<br>Cache key: 1, Cache Value: test, expireTime: 20000
<br>clean thread run
<br>No Valid Data!

<br>Concurrent test
<br>Added time spent：2587ms
<br>Select time spent：535ms
