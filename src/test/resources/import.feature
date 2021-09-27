Feature: User can import students

  Scenario: User imports a student
    Given a valid student JSON
    When user calls import endpoint
    Then the student is created
