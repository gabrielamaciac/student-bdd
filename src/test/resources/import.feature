Feature: User can import and export students

  Scenario: User imports a student
    Given a valid student
    When user calls import endpoint
    Then the student is created

  Scenario: User exports the imported student
    Given the imported student
    When user calls export endpoint
    Then the student is exported
