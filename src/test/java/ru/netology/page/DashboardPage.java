package ru.netology.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
  private SelenideElement heading = $("[data-test-id=dashboard]");
  private ElementsCollection cards = $$("li div[data-test-id]");
  private final String balanceStart = "баланс: ";
  private final String balanceFinish = " р.";
  private String cssLocatorForTopUpButton = "[data-test-id='action-deposit']";
  private SelenideElement pageRefresh = $("[data-test-id='action-reload']");


  public DashboardPage() {
    heading.shouldBe(visible);
  }

  public MoneyTransfer topUpButtonClick(DataHelper.CardNumber card) {
    findCard(card).$(cssLocatorForTopUpButton).click();
    return new MoneyTransfer();
  }

  public SelenideElement findCard(DataHelper.CardNumber card) {
    String cardNumber = card.getCardNumber().substring(15);
    for (SelenideElement element : cards) {
      if (element.text().contains(cardNumber)) {
        return element;
      }
    }
    return null;
  }

  public int getCardBalance(DataHelper.CardNumber card) {
    val text = findCard(card).text();
    return extractBalance(text);
  }

  private int extractBalance(String text) {
    int start = text.indexOf(balanceStart);
    int finish = text.indexOf(balanceFinish);
    String value = text.substring(start + balanceStart.length(), finish);
    return Integer.parseInt(value);
  }
}
