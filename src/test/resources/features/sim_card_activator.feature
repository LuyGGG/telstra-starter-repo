Feature: SIM Card Activation

  Scenario: Successfully activating a SIM card
    Given the SIM card with ICCID "1255789453849037777" and email "user@example.com"
    When I send an activation request
    Then the activation should be successful

  Scenario: Failing to activate a SIM card
    Given the SIM card with ICCID "8944500102198304826" and email "user@example.com"
    When I send an activation request
    Then the activation should fail
