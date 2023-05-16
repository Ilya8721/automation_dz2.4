package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class MoneyTransfer {
    private SelenideElement heading = $(withText("Пополнение карты"));
    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement fromField = $("[data-test-id='from'] input");
    private SelenideElement topUpButton = $("[data-test-id='action-transfer']");
    private SelenideElement canselButton = $("[data-test-id='action-cancel']");
    private SelenideElement errorMessage = $("[data-test-id='error-notification']");

    public MoneyTransfer() {
        heading.shouldBe(visible);
    }

    public DashboardPage topUpBalance(int amount, DataHelper.CardNumber cardNumber) {
        amountField.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        amountField.setValue(amount + "");
        fromField.setValue(String.valueOf(cardNumber));
        topUpButton.click();

        return new DashboardPage();
    }

    public DashboardPage cancelAndReturn() {
        canselButton.click();
        return new DashboardPage();
    }

    public void errorMessage() {
        errorMessage
                .shouldBe(visible)
                .shouldHave(Condition.text("Ошибка"));
    }
}
