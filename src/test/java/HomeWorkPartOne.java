import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class HomeWorkPartOne {

    @DisplayName("Проверка расчета комисси")
    @ParameterizedTest
    @CsvSource({"100", "2000"})
    void checkCommissionCalculation(int sum) {
        open("http://92.51.36.108:7777/sl.qa/fc/v01/index.php");
        $("[name=sum]").setValue(String.valueOf(sum));
        $("[name=submit]").click();

        $("[name=com]").shouldHave(text(String.valueOf(getCom(sum))));
        $("[name=total]").shouldHave(text(String.valueOf(getTotalSum(sum))));
    }

    private int getCom(int sum) {
        double commission = sum * 0.01;
        commission = Math.max(10, Math.min(commission, 100));

        return (int) Math.round(commission);
    }

    private int getTotalSum(int sum) {
        return sum + getCom(sum);
    }

    @DisplayName("Проверка формы входа")
    @Test
    void checkLoginForm() {
        open("https://slqa.ru/cases/ChatGPTLogin/");

        $x("//*[text()='Имя пользователя']/following-sibling::input").shouldBe(visible);
        $x("//*[text()='Пароль']/following-sibling::input").shouldBe(visible);
        $x("//button[text()='Войти']").shouldBe(visible);
    }

    @DisplayName("Проверка сообщения ошибки входа заблокированным пользователем")
    @Test
    void checkLoginLockUserMsg() {
        open("https://slqa.ru/cases/ChatGPTLogin/");
        $("#username").setValue("locked_out_user");
        $("#password").setValue("secret_sauce");
        $("#loginButton").click();

        $("#message").shouldBe(visible);
        $("#message").shouldHave(text("Пользователь заблокирован."));
    }

    @DisplayName("Проверка одной части текста с помощую другой")
    @Test
    void checkTextByText() {
        open("https://slqa.ru/cases/xPathSimpleForm/");

        $x("//*[contains(text(), 'Воронеж')]").shouldHave(text("нет поступлений"));
    }
}
