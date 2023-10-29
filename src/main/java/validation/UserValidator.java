package validation;

import dto.UserDto;

public class UserValidator {
    public static boolean isValid(UserDto userDto) {
        String login = userDto.getLogin();
        String password = userDto.getPassword();

        if(login == null || login.length() > 9 || login.length() < 6
                || password == null || password.length() > 9 || password.length() < 6) {
            return false;
        }
        return true;
    }
}
