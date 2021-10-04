Feature: User can see validation results

  Scenario: Student is validated
    Given an already imported student
    When user calls search by name and cnp
    Then the student is valid

  Scenario: User can see validation errors
    Given an invalid student is imported
    When user calls validation
    Then the error is returned

