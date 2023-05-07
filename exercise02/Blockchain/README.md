# Testing the blockchain
In the [requests.http](./request/requests.http) file you can find a small collection of requests that can be used to test some scenarios.

# Additional Features
The difficulty can be configured to be adjustable.  
This feature can be activated/deactivated and configured using the [application.properties](./src/main/resources/application.properties) file with the properties:
```properties
blockchain.adjustableDifficulty=true
blockchain.maxDifficulty=4
blockchain.targetBlockDurationSeconds=10
```
If `blockchain.adjustableDifficulty` is turned off, the difficulty will always be the same (2).  
With `blockchain.maxDifficulty` the maximal difficulty can be configured this makes, sense because e.g. on my machine there is a huge difference between difficulty 4 and 5.
With `blockchain.targetBlockDurationSeconds` the average amount of seconds that should be needed to mine a block can be configured.   

***Warning:*** The use of `maxDifficulty` can falsify the `targetBlockDurationSeconds` configuration.