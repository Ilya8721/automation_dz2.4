package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");
  private SelenideElement firstCard = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
  private SelenideElement secondCard = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";
  private SelenideElement pageRefresh = $("[data-test-id='action-reload']");
  private SelenideElement topUpFirstBalanceButton = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] [data-test-id='action-deposit']");
  private SelenideElement topUpSecondBalanceButton = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] [data-test-id='action-deposit']");

  public DashboardPage() {
    heading.shouldBe(visible);
  }

  public int getFirstCardBalance() {
    val text = firstCard.text();
    return extractBalanceFirstCard(text);
  }

  private int extractBalanceFirstCard(String text) {
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(balanceFinish);
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public int getSecondCardBalance() {
    val text = secondCard.text();
    return extractBalanceSecondCard(text);
  }

  private int extractBalanceSecondCard(String text) {
    val start = text.indexOf(balanceStart);
    val finish = text.indexOf(balanceFinish);
    val value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }

  public MoneyTransfer topUpFirstBalanceButtonClick() {
    topUpFirstBalanceButton.click();
    return new MoneyTransfer();
  }

  public MoneyTransfer topUpSecondBalanceButtonClick() {
    topUpSecondBalanceButton.click();
    return new MoneyTransfer();
  }
}
