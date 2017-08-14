Feature: Error Handling

Scenario: File not found

Given a file that doesn't exist "no-such-file.txt"
When I try to import that file
Then I should see an error 'The file "no-such-file.txt" could not be found'