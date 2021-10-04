Feature: User can import and export students

  Scenario: User imports a student
    Given a valid student payload is prepared
    When user calls import
    Then the student is created

  Scenario: User exports the imported student
    Given the imported student
    When user calls export endpoint
    Then the student is exported

    #todo with file
  Scenario: Student from file gets imported
    When the user pastes a student xml file
    Then the student is imported
