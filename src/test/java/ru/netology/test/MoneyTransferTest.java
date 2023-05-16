package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

  @BeforeEach
  void shouldTransferMoneyBetweenOwnCardsV2() {
    open("http://localhost:9999");
    var loginPage = new LoginPageV2();
    var authInfo = DataHelper.getAuthInfo();
    var verificationPage = loginPage.validLogin(authInfo);
    var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    verificationPage.validVerify(verificationCode);
  }

  @Test
  void transferFromFirstCardToSecondCard() {
    int amount = 1000;

    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getFirstCardBalance();
    int secondBalance = dashboardPage.getSecondCardBalance();
    var moneyTransfer = dashboardPage.topUpFirstBalanceButtonClick();
    var cardNumber = DataHelper.getSecondCardNumber();
    moneyTransfer.topUpBalance(amount, cardNumber);

    assertEquals(firstBalance + amount, dashboardPage.getFirstCardBalance());
    assertEquals(secondBalance - amount, dashboardPage.getSecondCardBalance());
  }

  @Test
  void transferFromSecondCardToFirstCard() {
    int amount = 1000;

    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getFirstCardBalance();
    int secondBalance = dashboardPage.getSecondCardBalance();
    var moneyTransfer = dashboardPage.topUpSecondBalanceButtonClick();
    var cardNumber = DataHelper.getFirstCardNumber();
    moneyTransfer.topUpBalance(amount, cardNumber);

    assertEquals(firstBalance - amount, dashboardPage.getFirstCardBalance());
    assertEquals(secondBalance + amount, dashboardPage.getSecondCardBalance());
  }

  @Test
  void shouldReturnToDashboardPage1() {
    var dashboardPage = new DashboardPage();
    var moneyTransfer = dashboardPage.topUpFirstBalanceButtonClick();
    moneyTransfer.cancelAndReturn();
  }

  @Test
  void shouldReturnToDashboardPage2() {
    var dashboardPage = new DashboardPage();
    var moneyTransfer = dashboardPage.topUpSecondBalanceButtonClick();
    moneyTransfer.cancelAndReturn();
  }

  @Test
  void transferZeroFromFirstCardToSecondCard() {
    int amount = 0;

    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getFirstCardBalance();
    int secondBalance = dashboardPage.getSecondCardBalance();
    var moneyTransfer = dashboardPage.topUpFirstBalanceButtonClick();
    var cardNumber = DataHelper.getSecondCardNumber();
    moneyTransfer.topUpBalance(amount, cardNumber);

    assertEquals(firstBalance + amount, dashboardPage.getFirstCardBalance());
    assertEquals(secondBalance - amount, dashboardPage.getSecondCardBalance());
  }

  @Test
  void transferZeroFromSecondCardToFirstCard() {
    int amount = 0;

    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getFirstCardBalance();
    int secondBalance = dashboardPage.getSecondCardBalance();
    var moneyTransfer = dashboardPage.topUpSecondBalanceButtonClick();
    var cardNumber = DataHelper.getFirstCardNumber();
    moneyTransfer.topUpBalance(amount, cardNumber);

    assertEquals(firstBalance - amount, dashboardPage.getFirstCardBalance());
    assertEquals(secondBalance + amount, dashboardPage.getSecondCardBalance());
  }

  @Test
  void shouldNotTransferFromFirstCardToSecondCard() {
    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getFirstCardBalance();
    int secondBalance = dashboardPage.getSecondCardBalance();
    var moneyTransfer = dashboardPage.topUpFirstBalanceButtonClick();
    var cardNumber = DataHelper.getSecondCardNumber();
    int amount = secondBalance * 2;
    moneyTransfer.topUpBalance(amount, cardNumber);

    assertEquals(firstBalance, dashboardPage.getFirstCardBalance());
    assertEquals(secondBalance, dashboardPage.getSecondCardBalance());
  }
}