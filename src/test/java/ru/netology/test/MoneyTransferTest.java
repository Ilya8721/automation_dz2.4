package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {
  DataHelper.CardNumber card1 = DataHelper.getFirstCardNumber();
  DataHelper.CardNumber card2 = DataHelper.getSecondCardNumber();

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

    DashboardPage dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card1);
    moneyTransfer.topUpBalance(amount, card2);

    assertEquals(firstBalance + amount, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance - amount, dashboardPage.getCardBalance(card2));
  }

  @Test
  void transferFromSecondCardToFirstCard() {
    int amount = 2000;

    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card2);
    moneyTransfer.topUpBalance(amount, card1);

    assertEquals(firstBalance - amount, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance + amount, dashboardPage.getCardBalance(card2));
  }

  @Test
  void shouldReturnToDashboardPage1() {
    var dashboardPage = new DashboardPage();
    var moneyTransfer = dashboardPage.topUpButtonClick(card1);
    moneyTransfer.cancelAndReturn();
  }

  @Test
  void shouldReturnToDashboardPage2() {
    var dashboardPage = new DashboardPage();
    var moneyTransfer = dashboardPage.topUpButtonClick(card2);
    moneyTransfer.cancelAndReturn();
  }

  @Test
  void transferZeroFromFirstCardToSecondCard() {
    int amount = 0;

    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card1);
    moneyTransfer.topUpBalance(amount, card2);

    assertEquals(firstBalance + amount, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance - amount, dashboardPage.getCardBalance(card2));
  }

  @Test
  void transferZeroFromSecondCardToFirstCard() {
    int amount = 0;

    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card2);
    moneyTransfer.topUpBalance(amount, card1);

    assertEquals(firstBalance - amount, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance + amount, dashboardPage.getCardBalance(card2));
  }

  @Test
  void shouldNotTransferFromFirstCardToSecondCard() {
    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card1);
    int amount = 30_000;
    moneyTransfer.topUpBalance(amount, card2);

    assertEquals(firstBalance, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance, dashboardPage.getCardBalance(card2));
  }
}