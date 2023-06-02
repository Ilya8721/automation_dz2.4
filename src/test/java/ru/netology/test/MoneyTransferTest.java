package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
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

  @AfterEach
  void defaultSetup() {
    DashboardPage dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    int difference = firstBalance - secondBalance;
    if (difference > 0) {
      var moneyTransfer = dashboardPage.topUpButtonClick(card2);
      moneyTransfer.topUpBalance(difference / 2, card1);
    } else if (difference < 0) {
      var moneyTransfer = dashboardPage.topUpButtonClick(card1);
      moneyTransfer.topUpBalance(Math.abs(difference)/ 2, card2);
    }

  }

  @Test
  void transferFromFirstCardToSecondCard() {
    int amount = 1000;

    DashboardPage dashboardPage = new DashboardPage();
    int firstDefaultBalance = dashboardPage.getCardBalance(card1);
    int secondDefaultBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card1);
    moneyTransfer.topUpBalance(amount, card2);

    assertEquals(firstDefaultBalance + amount, dashboardPage.getCardBalance(card1));
    assertEquals(secondDefaultBalance - amount, dashboardPage.getCardBalance(card2));
  }

  @Test
  void transferFromSecondCardToFirstCard() {
    int amount = 5000;

    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card2);
    moneyTransfer.topUpBalance(amount, card1);

    assertEquals(firstBalance - amount, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance + amount, dashboardPage.getCardBalance(card2));
  }

  @Test
  void transferFromFirstCardToSecondCardTo0() {

    DashboardPage dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card1);
    moneyTransfer.topUpBalance(secondBalance, card2);

    assertEquals(firstBalance + secondBalance, dashboardPage.getCardBalance(card1));
    assertEquals(0, dashboardPage.getCardBalance(card2));
  }

  @Test
  void transferFromSecondCardToFirstCardTo0() {
    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card2);
    moneyTransfer.topUpBalance(firstBalance, card1);

    assertEquals(0, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance + firstBalance, dashboardPage.getCardBalance(card2));
  }

  @Test
  void shouldReturnToDashboardPage1() {
    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card1);
    moneyTransfer.cancelAndReturn();

    assertEquals(firstBalance, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance, dashboardPage.getCardBalance(card2));
  }

  @Test
  void shouldReturnToDashboardPage2() {
    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card2);
    moneyTransfer.cancelAndReturn();

    assertEquals(firstBalance, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance, dashboardPage.getCardBalance(card2));
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

  @Test
  void shouldNotTransferFromSecondCardToFirstCard() {
    var dashboardPage = new DashboardPage();
    int firstBalance = dashboardPage.getCardBalance(card1);
    int secondBalance = dashboardPage.getCardBalance(card2);
    var moneyTransfer = dashboardPage.topUpButtonClick(card2);
    int amount = 35_000;
    moneyTransfer.topUpBalance(amount, card1);

    assertEquals(firstBalance, dashboardPage.getCardBalance(card1));
    assertEquals(secondBalance, dashboardPage.getCardBalance(card2));
  }
}