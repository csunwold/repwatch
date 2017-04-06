# repwatch
An application for staying up to date with your Member's of Congress (MOC). 

## Interfaces
### Alexa Skill
#### Setup
* Create an API key for Google's Civic API - https://console.cloud.google.com/apis/credentials
* Create an API key for Propublica's Congress API
* Create a configuration file called alexa/src/main/resource/repwatch.conf and put both API keys in the configuration file.
Example:

```
google.api-key=<API KEY>
propublica.api-key=<API KEY>
```