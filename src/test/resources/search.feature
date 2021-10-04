Feature: User can search imported students

  Scenario: User searches imported student by name and cnp
    Given an already imported student
    When user calls search by name and cnp
    Then the student is found

  Scenario: User searches imported student by search term
    Given an already imported student
    When user calls search by search term
    Then the student list is found

  Scenario: User searches only valid students
    Given an already imported student
    When user calls search by isValid
    Then the student list is found