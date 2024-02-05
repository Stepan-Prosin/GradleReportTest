import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class AuthTest {
    @BeforeAll
    static void setUpAllure() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAllure() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void successOfRegisteredActiveUserTest() {
        var validUser = DataGenerator.Registration.getRegistredUser("active");
        $("[data-test-id='login'] input").sendKeys(validUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(validUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный Кабинет"))
                .shouldBe(visible);

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void NotRegisteredActiveUserTest() {
        var notValidUser = DataGenerator.Registration.getUser("active");
        $("[data-test-id='login'] input").sendKeys(notValidUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(notValidUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"),
                        Duration.ofSeconds(10)).shouldBe(visible);
    }


    @Test
    @DisplayName("Should get error message if login with blocked user")
    void BlockedRegisteredActiveUserTest() {
        var blockedValidUser = DataGenerator.Registration.getRegistredUser("blocked");
        $("[data-test-id='login'] input").sendKeys(blockedValidUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(blockedValidUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10)).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void wrongLogRegisteredActiveUserTest() {
        var validUser = DataGenerator.Registration.getRegistredUser("active");
        var wrongLogUser = DataGenerator.generateName();
        $("[data-test-id='login'] input").sendKeys(wrongLogUser);
        $("[data-test-id='password'] input").sendKeys(validUser.getPassword());
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10)).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void wrongPasswordRegisteredActiveUserTest() {
        var validUser = DataGenerator.Registration.getRegistredUser("active");
        var wrongPasUser = DataGenerator.generatePassword();
        $("[data-test-id='login'] input").sendKeys(validUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(wrongPasUser);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10)).shouldBe(visible);
    }
}