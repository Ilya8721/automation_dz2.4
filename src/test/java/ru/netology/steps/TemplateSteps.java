package ru.netology.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPageV2;
import ru.netology.page.MoneyTransfer;
import ru.netology.page.VerificationPage;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TemplateSteps {
    private static LoginPageV2 loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private static MoneyTransfer moneyTransfer;

    @Пусть("открыта страница с формой авторизации {string}")
    public void openAuthPage(String url) {
        loginPage = Selenide.open(url, LoginPageV2.class);
    }

    @Когда("пользователь пытается авторизоваться с именем {string} и паролем {string}")
    public void loginWithNameAndPassword(String login, String password) {
        verificationPage = loginPage.validLogin(new DataHelper.AuthInfo(login, password));
    }

    @И("пользователь вводит проверочный код 'из смс' {string}")
    public void setValidCode(String verificationCode) {
        dashboardPage = verificationPage.validVerify(new DataHelper.VerificationCode(verificationCode));
    }

    @Тогда("происходит успешная авторизация и пользователь попадает на страницу 'Личный кабинет'")
    public void verifyDashboardPage() {
        dashboardPage.DashboardPage();
    }

    @Тогда("появляется ошибка о неверном коде проверки")
    public void verifyCodeIsInvalid() {
        verificationPage.VerificationPage();
    }

    @Пусть("пусть пользователь залогинен с именем {string} и паролем {string}")
    public void dashboardPageOpened(String login, String password) {
        loginPage = Selenide.open("http://localhost:9999", LoginPageV2.class);
        verificationPage = loginPage.validLogin(new DataHelper.AuthInfo(login, password));
        dashboardPage = verificationPage.validVerify(new DataHelper.VerificationCode("12345"));
        dashboardPage.DashboardPage();
    }

    @Когда("пользователь переводит {int} рублей с карты с номером 5559 0000 0000 0001 на свою 1 карту с главной страницы")
    public void transferFromSecondCardToFirstCard(int amount) {
        moneyTransfer = dashboardPage.topUpButtonClick(DataHelper.getFirstCardNumber());
        moneyTransfer.topUpBalance(amount, DataHelper.getSecondCardNumber());
    }

    @Тогда("баланс его первой карты из списка на главной странице должен стать {int} рублей")
    public void firstCardBalance(int newBalance) {
        assertEquals(newBalance, dashboardPage.getCardBalance(DataHelper.getFirstCardNumber()));
    }
}
