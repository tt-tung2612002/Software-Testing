package com.springboot.login;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.controllers.SignUpController;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
class LoginApplicationTests {

    @Autowired
    private SignUpController signUpController;

    void test(String[][] test_data) {
        // Solution solution = new Solution();
        for (String[] test_case : test_data) {
            String input = test_case[0];
            int expected = Integer.parseInt(test_case[1]);
            int actual = signUpController.validatePasswordStrength(input);
            // assert with input
            Assertions.assertEquals(expected, actual, "Input: " + input);
        }
    }

    @Test
    void testSignUpForm() {
        String[][] test_data = {
                // testcase where the password is too short
                { "shortpw", "3" },
                { "lackone", "3" },

                // testcase where the password is too long
                { "toolonglonglonglonglonglonglonglong", "4" },
                { "toolonglonglonglonglonglonglonglonglong", "4" },

                // testcase where the password doesn't contain any lowercase character
                { "NOLOWERCASE", "6" },
                { "N0LOWERCASEEEE~>", "6" },

                // testcase where the password doesn't contain any special character
                { "noSpecialCharacter123", "9" },
                { "noSpecialCharacter1234567890", "9" },

                // testcase where the password doesn't contain any uppercase character
                { "nouppercase", "7" },
                { "n0upp3rcase", "7" },

                // testcase where the password doesn't contain any digit
                { "PWlacksnumber", "8" },
                { "PWlacksnumber~>", "8" },

                // testcase where the password only contains digits.
                { "1234567890", "6" },

                // testcase where the password meets all the requirements
                { "P@ssw0rd", "0" },
                { "P@ssw0rdligit1", "0" },
                { "validPassword123.~", "0" },
        };

        test(test_data);
    }

}
