import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


public class FirstLoginTest {
    private static final String USERNAME = "#username";
    private static final String PASSWORD = "#password";
    private static final String LOGIN_BUTTON = "#loginButton";
    private static final String LOGOUT_BUTTON = "#logoutButton";
    private static final String GREETING_MSG = "#greeting";
    private static final String LOGIN_ERR_MSG = "#message";

    @BeforeEach
    void prepareForTest() {
        open("https://slqa.ru/cases/ChatGPTLogin/");
    }

    @DisplayName("1. Проверка успешный вход по нажатию кнопки Login")
    @Test
    void checkSuccessLoginByLoginButton() {
        String username = "standard_user";
        $(USERNAME).setValue(username);
        $(PASSWORD).setValue("secret_sauce");
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldBe(visible);
        checkMsg(GREETING_MSG, "Welcome, " + username + "!");
    }

    @DisplayName("2. Проверка ошибки входа при некорректном пароле")
    @Test
    void checkWrongLoginWithWrongPass() {
        $(USERNAME).setValue("standard_user");
        $(PASSWORD).setValue("wrong_pass");
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldNotBe(visible);
        checkMsg(LOGIN_ERR_MSG, "Invalid username or password.");
    }

    @DisplayName("3. Проверка успешного входа по нажатию Enter")
    @Test
    void checkSuccessLoginByEnter() {
        String username = "standard_user";
        $(USERNAME).setValue(username);
        $(PASSWORD).setValue("secret_sauce");
        $(LOGIN_BUTTON).pressEnter();

        $(LOGOUT_BUTTON).shouldBe(visible);
        checkMsg(GREETING_MSG, "Welcome, " + username + "!");
    }

    @DisplayName("4. Проверка выхода")
    @Test
    void checkSuccessLogout() {
        $(USERNAME).setValue("standard_user");
        $(PASSWORD).setValue("secret_sauce");
        $(LOGIN_BUTTON).click();
        sleep(5_000);
        $(LOGOUT_BUTTON).click();

        $(LOGIN_BUTTON).shouldBe(visible);
    }

    @DisplayName("5. Проверка ошибки входа при некорректном имени пользователя")
    @Test
    void checkWrongLoginWithWrongUsername() {
        $(USERNAME).setValue("wrong_user");
        $(PASSWORD).setValue("secret_sauce");
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldNotBe(visible);
        checkMsg(LOGIN_ERR_MSG, "Invalid username or password.");
    }

    @DisplayName("6. Проверка ошибки входа при корректных, но несовпадающих credentials")
    @Test
    void checkWrongLoginWithMismatchUsernameAndPass() {
        $(USERNAME).setValue("standard_user");
        $(PASSWORD).setValue("user06_password");
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldNotBe(visible);
        checkMsg(LOGIN_ERR_MSG, "Invalid username or password.");
    }

    @DisplayName("7. Проверка ошибки входа заблокированного пользователя")
    @Test
    void checkWrongLoginWithLockedUser() {
        $(USERNAME).setValue("locked_out_user");
        $(PASSWORD).setValue("secret_sauce");
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldNotBe(visible);
        checkMsg(LOGIN_ERR_MSG, "Пользователь заблокирован.");
    }

    @DisplayName("8. Проверка ошибки входа при незаполненом имени пользователя")
    @Test
    void checkWrongLoginWithEmptyUsername() {
        $(USERNAME).clear();
        $(PASSWORD).setValue("secret_sauce");
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldNotBe(visible);
        checkMsg(LOGIN_ERR_MSG, "Username is required.");
    }

    @DisplayName("9. Проверка ошибки входа при незаполненом пароле")
    @Test
    void checkWrongLoginWithEmptyPass() {
        $(USERNAME).setValue("standard_user");
        $(PASSWORD).clear();
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldNotBe(visible);
        checkMsg(LOGIN_ERR_MSG, "Password is required.");
    }

    @DisplayName("10. Проверка ошибки входа при пустых credentials")
    @Test
    void checkWrongLoginWithEmptyUsernameAndPass() {
        $(USERNAME).clear();
        $(PASSWORD).clear();
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldNotBe(visible);
        checkMsg(LOGIN_ERR_MSG, "Username and Password are required.");
    }

    @DisplayName("11. Проверка скрытия пароля при вводе")
    @Test
    void checkHidePass() {
        $("[type=password]").shouldBe(visible);
    }

    @DisplayName("12. Проверка успешного входа под несколькими пользователями")
    @ParameterizedTest
    @CsvSource({"standard_user", "problem_user", "performance_glitch_user", "visual_user"})
    void checkSuccessLoginSomeUsers(String username) {
        $(USERNAME).setValue(username);
        $(PASSWORD).setValue("secret_sauce");
        $(LOGIN_BUTTON).click();

        $(LOGOUT_BUTTON).shouldBe(visible);
        checkMsg(GREETING_MSG, "Welcome, " + username + "!");
    }

    private void checkMsg(String msgLocator, String errMsg) {
        $(msgLocator).shouldBe(visible);
        $(msgLocator).shouldHave(text(errMsg));
    }
}
