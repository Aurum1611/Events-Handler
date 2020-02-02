package com.internship.project.eventshandler.util;

import java.util.regex.Pattern;

public final class EmailValidator {

    // We use below regular expression provided in OWASP Validation Regex repository.
    // https://www.owasp.org/index.php/OWASP_Validation_Regex_Repository
    private static final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(emailRegex);

    public static boolean isEmailValid(String email) {
        if (email == null)
            return false;
        return pattern.matcher(email).matches();
    }

}
