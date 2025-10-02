import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class SimpleFormTest {

    @DisplayName("Использование специальных команд Selenide")
    @Test
    void specificCommands() {
        open("https://slqa.ru/cases/SimpleForm/");
        $(By.id("unique_id")).setValue("Уникальный id");
        $(By.name("unique_name")).setValue("Уникальное имя");
        $(By.tagName("blockquote")).shouldHave(text("спрашивает"));

        $(By.className("unique_class")).shouldBe(visible);
    }

    @DisplayName("CSS-селекторы")
    @Test
    void cssSelectors() {
        open("https://slqa.ru/cases/SimpleForm/");
        $("#unique_id").setValue("Уникальный id");
        $("[name=unique_name]").setValue("Уникальное имя");
        $("blockquote").shouldHave(text("спрашивает"));

        $(".unique_class").shouldBe(visible);
    }

    @DisplayName("xPath")
    @Test
    void xPathSelectors() {
        open("https://slqa.ru/cases/SimpleForm/");
        $x("//*[@id='unique_id']").setValue("Уникальный id");
        $x("//*[@name='unique_name']").setValue("Уникальное имя");
        $x("//blockquote").shouldHave(text("спрашивает"));

        $x("//*[@class='unique_class']").shouldBe(visible);
    }

    @DisplayName("Поиск текста")
    @Test
    void textSearch() {
        open("https://slqa.ru/cases/xPathSimpleForm/");

        // Поиск элемента по точному совпадению текста (библиотека Selenide)
        $(byText("Текстовое поле 1:")).shouldBe(visible);

        // Поиск элемента по точному содержаию текста (xPath)
        $x("//*[text()='Текстовое поле 2:']").shouldBe(visible); // Не находит !!!

        // Поиск элемента по содержанию текста (библиотека Selenide)
        $(withText("Москва")).shouldHave(text("250 единиц"));

        // Поиск элемента по содержаию текста (xPath)
        $x("//*[contains(text(), 'Питер')]").shouldHave(text("180 единиц"));

        // Поиск элемента по началу текста (только xPath)
        $x("//*[starts-with(text(),'Россия')]").shouldHave(text("площадь 17 234 031"));
    }
}
