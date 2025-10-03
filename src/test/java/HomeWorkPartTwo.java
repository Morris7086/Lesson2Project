import com.codeborne.selenide.ElementsCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class HomeWorkPartTwo {

    @BeforeEach
    void prepareBeforeTest() {
        open("https://slqamsk.github.io/cases/loan-calc/v01/");
    }

    @DisplayName("Проверка выполнения расчета")
    @Test
    void checkWorkCalculation() {
        $("#amount").setValue("10000");
        $("#term").setValue("12");
        $("#rate").setValue("15.5");
        $("#calculate-btn").click();

        $x("//*[contains(text(), 'Результаты расчёта')]").shouldBe(visible, Duration.ofSeconds(10));
    }

    @DisplayName("Проверка параметров кредита")
    @ParameterizedTest
    @CsvSource({"1000, 1, 0.01, Аннуитетный","10000000, 360, 100, Аннуитетный"})
    void checkCreditParameters(String amount, String term, String rate, String paymentType) {
        $("#amount").setValue(amount);
        $("#term").setValue(term);
        $("#rate").setValue(rate);
        $x("//label[contains(.,'" + paymentType + "')]").click();
        $("#calculate-btn").click();

        $x("//*[contains(text(), 'Результаты расчёта')]").shouldBe(visible, Duration.ofSeconds(15));
        $("#result-amount").shouldHave(text(amount));
        $("#result-term").shouldHave(text(term));
        $("#result-rate").shouldHave(text(rate));
        $("#result-payment-type").shouldHave(text(paymentType));
        $("#monthly-payment").shouldHave(text(getMonthlyPayment(amount, term, rate)));
    }

    private String getMonthlyPayment(String amount, String term, String rate) {
        int creditSum = Integer.parseInt(amount);
        int months = Integer.parseInt(term);

        double monthlyRate = Double.parseDouble(rate)/12/100;
        double coefficient = monthlyRate * Math.pow(1 + monthlyRate, months)/(Math.pow(1 + monthlyRate, months) - 1);

        return String.format("%.2f", (creditSum * coefficient)).replace(",", ".");
    }

    @DisplayName("Проверка параметров кредита на графике платежей")
    @Test
    void checkCreditParametersOnPaymentSchedule() {
        String amount = "10000";
        String term = "12";
        String rate = "16.75";
        String paymentType = "Аннуитетный";

        $("#amount").setValue(amount);
        $("#term").setValue(term);
        $("#rate").setValue(rate);
        $x("//label[contains(.,'" + paymentType + "')]").click();
        $("#calculate-btn").click();
        $x("//*[contains(text(), 'Результаты расчёта')]").shouldBe(visible, Duration.ofSeconds(15));
        $("#show-schedule-btn").click();

        switchTo().window(1);

        $x("//h2[text()='График платежей']").shouldBe(visible);
        $x("//*[contains(text(), 'Сумма кредита')]/child::*").shouldHave(text(amount));
        $x("//*[contains(text(), 'Срок кредита')]/child::*").shouldHave(text(term));
        $x("//*[contains(text(), 'Процентная ставка')]/child::*").shouldHave(text(rate));
        $x("//*[contains(text(), 'Тип платежа')]/child::*").shouldHave(text(paymentType));

        ElementsCollection paymentScheduleRows = $$("tbody tr:not(:first-child)");
        paymentScheduleRows.shouldHave(size(Integer.parseInt(term)));
    }
}
