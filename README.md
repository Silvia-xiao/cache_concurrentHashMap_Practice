# cache_concurrentHashMap_Practice

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
### GET Data url
* Example GET url: localhost:8080/api/get?key={key}
  * Example response if data exists and not expired: 
    * Cache key: 1, Cache Value: student, expireTime: 30000
  * Example response if data exists and not expired:
    * No Valid Data!
### POST Data
* POST action needs to be tested through PostMan or other applications.
  * Example input:
    * 
