# CSCI 497U Assignment 2
### Group members:
- Peter Schinske
- Shadrach Roundy

### API
https://uh60pjhvfc.execute-api.us-west-2.amazonaws.com/prod/

### Our progress

Peter worked on the TUI and Shadrach worked on the AWS scripts. We were able to get the server to start without errors, but it responds to all of our POST requests with "internal server error". The biggest issue we ran into was that it was hard to see what was failing, due to the lack of descriptive errors.

The script files are, as far as we can tell, mostly correct. We updated the code to authenticate with email instead, and changed the DynamoDB table and lambda function to work with tic tac toe.

The TUI is implemented in Java. It has all the calls to the server implemented, but we weren't able to test them. The program should compile with a simple `./gradlew build`.
