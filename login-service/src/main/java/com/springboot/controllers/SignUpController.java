package com.springboot.controllers;

import com.springboot.dto.request.AppUserSignUpRequest;
import com.springboot.entity.AppUser;
import com.springboot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController {

    private enum SignUpErrorCode {

        USERNAME_ALREADY_EXISTS(1),
        PASSWORDS_DO_NOT_MATCH(2),
        PASSWORD_TOO_SHORT(3),
        PASSWORD_TOO_LONG(4),
        PASSWORD_CONTAINS_INVALID_CHARACTERS(5),
        PASSWORD_LACKS_LOWERCASE_CHARACTERS(6),
        PASSWORD_LACKS_UPPERCASE_CHARACTERS(7),
        PASSWORD_LACKS_DIGITS(8),
        PASSWORD_LACKS_SPECIAL_CHARACTERS(9);
        private final int code;
        SignUpErrorCode(int code) {
            this.code = code;
        }
    }


    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AppUserSignUpRequest appUser) {

        String password = appUser.getPassword();

        if (!(validatePasswordStrength(password) == 0 )) {
            return ResponseEntity.badRequest().body("Please enter a stronger password, your " +
                    "password must contain at least an uppercase letter, an lowercase letter, a " +
                    "number and a special character");
        }

        AppUser user = new AppUser();
        user.setUsername(appUser.getUsername());
        user.setPassword(password);

        return ResponseEntity.ok(userService.saveUser(user));

    }

    public int getNumberOfDigits(String password) {
        int numberOfDigits = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                numberOfDigits++;
            }
        }
        return numberOfDigits;
    }

    public int getNumberOfUppercaseChars(String password) {
        int numberOfUppercaseChars = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                numberOfUppercaseChars++;
            }
        }
        return numberOfUppercaseChars;
    }

    public int getNumberOfLowercaseChars(String password) {
        int numberOfLowercaseChars = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                numberOfLowercaseChars++;
            }
        }
        return numberOfLowercaseChars;
    }

    public int getNumberOfSpecialChars(String password) {
        int numberOfSpecialChars = 0;
        for (int i = 0; i < password.length(); i++) {
            if (!Character.isLetterOrDigit(password.charAt(i))) {
                numberOfSpecialChars++;
            }
        }
        return numberOfSpecialChars;
    }

    // validate password and return corresponding enum
    public int validatePasswordStrength(String password) {
        if (password.length() < 8) { // 1
            return SignUpErrorCode.PASSWORD_TOO_SHORT.code; //2
        } 
        if (password.length() > 32) { // 3
            return SignUpErrorCode.PASSWORD_TOO_LONG.code; // 4
        } 
        if (getNumberOfLowercaseChars(password) < 1) { // 5
            return SignUpErrorCode.PASSWORD_LACKS_LOWERCASE_CHARACTERS.code; // 6
        }
        if (getNumberOfUppercaseChars(password) < 1) { // 7
            return SignUpErrorCode.PASSWORD_LACKS_UPPERCASE_CHARACTERS.code; // 8
        }
        if (getNumberOfDigits(password) < 1) { // 9
            return SignUpErrorCode.PASSWORD_LACKS_DIGITS.code; // 10
        } 
        if (getNumberOfSpecialChars(password) < 1) { // 11
            return SignUpErrorCode.PASSWORD_LACKS_SPECIAL_CHARACTERS.code; // 12
        }
        return 0; // 13
    }


}
