package com.springboot.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.dto.request.AppUserRequestDto;
import com.springboot.dto.request.AppUserSignUpRequest;
import com.springboot.dto.response.BaseResponse;
import com.springboot.services.UserService;

import lombok.RequiredArgsConstructor;

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

    private final AppUserController appUserController;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody AppUserSignUpRequest appUser) {

        // if passwords not match, return error
        if (!appUser.getPassword().equals(appUser.getConfirmedPassword())) {
            return ResponseEntity.badRequest().body(new BaseResponse("0", "Passwords do not " +
                    "match!", ""));
        }

        // make a request to /api/users/create
        AppUserRequestDto appUserRequestDto = new AppUserRequestDto(appUser);
        int numberOfDigits = 0;
        int numberOfUppercaseChars = 0;
        int numberOfLowercaseChars = 0;
        int numberOfSpecialChars = 0;

        String password = appUser.getPassword();

        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                numberOfDigits++;
            }
            if (Character.isUpperCase(password.charAt(i))) {
                numberOfUppercaseChars++;
            }
            if (Character.isLowerCase(password.charAt(i))) {
                numberOfLowercaseChars++;
            }
            if (!Character.isLetterOrDigit(password.charAt(i))) {
                numberOfSpecialChars++;
            }
        }

        if (validatePassword(password.length(), numberOfDigits, numberOfUppercaseChars,
                numberOfLowercaseChars,
                numberOfSpecialChars) == 0) {

            return appUserController.saveUser(appUserRequestDto);
        }

        return ResponseEntity.badRequest().body(new BaseResponse("0", "Password is not valid!",
                ""));

    }

    public int validatePassword(String password) {
        int numberOfDigits = 0;
        int numberOfUppercaseChars = 0;
        int numberOfLowercaseChars = 0;
        int numberOfSpecialChars = 0;

        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                numberOfDigits++;
            }
            if (Character.isUpperCase(password.charAt(i))) {
                numberOfUppercaseChars++;
            }
            if (Character.isLowerCase(password.charAt(i))) {
                numberOfLowercaseChars++;
            }
            if (!Character.isLetterOrDigit(password.charAt(i))) {
                numberOfSpecialChars++;
            }
        }

        return validatePassword(password.length(), numberOfDigits, numberOfUppercaseChars,
                numberOfLowercaseChars,
                numberOfSpecialChars);
    }

    public int validatePassword(int passLength, int numberOfDigits, int numberOfUppercaseChars,
            int numberOfLowercaseChars, int numberOfSpecialChars) {

        if (passLength < 8) {
            return SignUpErrorCode.PASSWORD_TOO_SHORT.code;
        }

        if (passLength > 32) {
            return SignUpErrorCode.PASSWORD_TOO_LONG.code;
        }

        if (numberOfDigits == 0) {
            return SignUpErrorCode.PASSWORD_LACKS_DIGITS.code;
        }

        if (numberOfLowercaseChars == 0) {
            return SignUpErrorCode.PASSWORD_LACKS_LOWERCASE_CHARACTERS.code;
        }

        if (numberOfUppercaseChars == 0) {
            return SignUpErrorCode.PASSWORD_LACKS_UPPERCASE_CHARACTERS.code;
        }

        if (numberOfSpecialChars == 0) {
            return SignUpErrorCode.PASSWORD_LACKS_SPECIAL_CHARACTERS.code;
        }

        return 0;
    }

}
