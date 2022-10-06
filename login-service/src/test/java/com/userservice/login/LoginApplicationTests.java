package com.springboot.login;

import com.springboot.controllers.SignUpController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
class LoginApplicationTests {

    @Autowired
    private SignUpController signUpController;

    void test(String[][] test_data) {
//        Solution solution = new Solution();
        for (String[] test_case : test_data) {
            String input = test_case[0];
            int expected = Integer.parseInt(test_case[1]);
            int actual = signUpController.validatePasswordStrength(input);
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    void testSignUpForm(){
        String[][] test_data = {
                {"shortpw", "3"},
                {"toolonglonglonglonglonglonglonglong", "4"},
                {"noSpecialCharacter123", "9"},
                {"NOlOWERCASE", "6"},
                {"nouppercase", "7"},
                {"PWlacksnumber", "8"},
                {"1234567890", "9"},
                {"P@ssw0rd", "3"},
                {"P@ssw0rdligit1", "0"},
                {"noSpecialCharacter", "9"},
                {"validPassword123.~", "0"},
        };
        test(test_data);
    }


}
