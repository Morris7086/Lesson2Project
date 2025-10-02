import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SimpleFormTest {

    @DisplayName("Использование специальных команд Selenide")
    @Test
    void test01SpecificCommands() {
        open("https://slqa.ru/cases/SimpleForm/");
        $(By.id("unique_id")).setValue("Уникальный id");
        $(By.name("unique_name")).setValue("Уникальное имя");
        $(By.tagName("blockquote")).shouldHave(text("спрашивает"));
        $(By.className("unique_class")).shouldBe(visible);
    }

    @Test
    void test02CSSSelectors() {
        open("https://slqa.ru/cases/SimpleForm/");
        $("#unique_id").setValue("Уникальный id");
        $("[name=unique_name]").setValue("Уникальное имя");
        $("blockquote").shouldHave(text("спрашивает"));
        $(".unique_class").shouldBe(visible);
    }
}
