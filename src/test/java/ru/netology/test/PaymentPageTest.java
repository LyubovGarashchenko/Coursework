package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.PaymentPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.SQLHelper.cleanDatabase;
import static ru.netology.data.SQLHelper.getPaymentStatus;

public class PaymentPageTest {
    PaymentPage paymentPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        paymentPage = open("http://localhost:8080", PaymentPage.class);

        paymentPage.choosePayment();
    }

    @AfterEach
    void cleanDb() {
        cleanDatabase();
    }


    @Test
    @DisplayName("1. Отправка формы оплаты по APPROVED карте с заполнением на кириллице всех полей валидными данными")
    public void shouldPaymentWhenAllDataValid() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @DisplayName("2. Отправка формы оплаты по APPROVED карте с заполнением поля Владелец на латинице")
    public void shouldPaymentWhenFildHolderLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), getHolderNameWithLat(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @Test
    @DisplayName("3. Отправка формы оплаты по APPROVED карте с заполнением поля Владелец с двойным именем и " +
            "фамилией через тире")
    public void shouldPaymentWhenFirstNameAndLastNameWithDash() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(1), getHolderNameWithDash(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @Test
    @DisplayName("4. Отправка формы оплаты по APPROVED карте с заполнением поля Владелец с буквой ё в имени и й в фамилии")
    public void shouldPaymentWhenNameWithIotatedLetters() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolderNameWithIotatedLetters(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @Test
    @DisplayName("5. Отправка формы оплаты по APPROVED карте с заполнением поля Владелец с апострофом в фамилии")
    public void shouldPaymentWhenFielHolderWithAnApostropheInLastName() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolderWithAnApostropheInLastName(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("APPROVED", getPaymentStatus());
    }

    @Test
    @DisplayName("6. Отправка формы оплаты по активной карте с указанием следующего от текущего месяца и текущего года")
    public void shouldPaymentWhenCardDateIsNextCurrent() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getSpecifiedMonth(1), getYear(1), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
    }

    @Test
    @DisplayName("7. Отправка формы оплаты по Credit карте с заполнением всех полей валидными данными")
    public void shouldPaymentDeclinedCardWhenAllFieldsValid() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getDeclinedCardNumber(),
                getMonth(), getYear(1), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationSuccessPayment();
        assertEquals("DECLINED", getPaymentStatus());
    }

    //Перечень автоматизируемых негативных сценариев по активной (APPROVED) карте
    @Test
    @DisplayName("8. Отправка формы оплаты по карте без заполнения полей")
    public void sendEmptyForm() {
        paymentPage.verifyFormName();
        paymentPage.sendEmptyForm();
        paymentPage.verifyErrorNotification(0);
        paymentPage.verifyErrorNotification(1);
        paymentPage.verifyErrorNotification(2);
        paymentPage.verifyErrorNotification(3);
        paymentPage.verifyErrorNotification(4);
    }

    @Test
    @DisplayName("9. Отправка формы оплаты по активной карте с пустым полем номер карты")
    public void shouldNotPaymentWhenCardNumberIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo("",
                getMonth(), getYear(2), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("10. Отправка формы пустым полем Месяц")
    public void shouldNotPaymentWhenMonthIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                "", getYear(3), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("11. Отправка формы оплаты по активной карте с пустым полем год")
    public void shouldNotPaymentWhenYearIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), "", getNameHolder(), getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("12. Отправка формы оплаты по активной карте с пустым полем владелец")
    public void shouldNotPaymentWhenHolderIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), "", getCvc()));
        paymentPage.verifyNotificationRequiredField(0);
    }

    @Test
    @DisplayName("13. Отправка формы оплаты по активной карте с пустым полем cvc/cvv")
    public void shouldNotPaymentWhenCvcIsEmpty() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getNameHolder(), null));
        paymentPage.verifyNotificationRequiredField(1);
    }

    @Test
    @DisplayName("14. Появление сообщения под полем номер карты при вводе невалидных данных (13 цифр)")
    public void errorMsgWhenEnterInTheCardNumberField15Digits() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getCardNumberWith13Numbers(),
                getMonth(), getYear(1), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("15. Появление сообщения под полем номер карты при вводе невалидных данных (кириллица)")
    public void errorMsgWhenEnterInTheCardNumberFieldCyrillic() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getCardNumberWithCyrillic(),
                getMonth(), getYear(1), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("16. Появление сообщения под полем номер карты при вводе невалидных данных (латиница)")
    public void errorMsgWhenEnterInTheCardNumberFieldLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getCardNumberWithLatin(),
                getMonth(), getYear(3), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("17. Появление сообщения под полем номер карты при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheCardNumberFieldSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getCardNumberWithSymbols(),
                getMonth(), getYear(1), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("18. Отправка формы оплаты по рандомной карте с заполнением всех полей")
    public void shouldNotPaymentWithDefunctCard() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getRandomCardNumber(),
                getMonth(), getYear(2), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationErrorPayment();
    }

    @Test
    @DisplayName("19. Появление сообщения под полем месяц при вводе невалидных данных (несуществующее число 13)")
    public void errorMsgWhenEnterInTheMonthField13Number() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthOverTwelve(), getYear(4), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationCardDateIsIncorrect(0);
    }

    @Test
    @DisplayName("20. Появление сообщения под полем месяц при вводе невалидных данных (два нуля)")
    public void errorMsgWhenEnterInTheMonthField2Zero() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithZero(), getYear(3), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationCardDateIsIncorrect(0);
    }

    @Test
    @DisplayName("21. Появление сообщения под полем месяц при вводе любых буквенных значений")
    public void errorMsgWhenEnterInTheMonthFieldLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithLatinAndCyrillic(), getYear(2), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("22. Появление сообщения под полем месяц при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheMonthFieldSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithSymbols(), getYear(2), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("23. Появление сообщения под полем месяц при вводе невалидных данных (одна цифра)")
    public void errorMsgWhenEnterInTheMonthFieldOneDigit() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonthWithOneNumber(), getYear(4), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("24. Появление сообщения под полем Месяц при вводе невалидных данных (предыдущий месяц)")
    public void errorMsgWhenEnterInTheYearFieldLastYear() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getLastMonth(), getCurrentYear(), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationCardDateIsIncorrect(0);
    }

    @Test
    @DisplayName("25. Появление сообщения под полем Год при вводе прошедшего года")
    public void errorMsgWhenYearFieldWithLastYear() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getLastYear(), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationCardExpired(0);
    }

    @Test
    @DisplayName("26. Появление сообщения под полем год при вводе невалидных данных (два нуля)")
    public void errorMsgWhenYearFieldWith2Zero() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWithTwoZero(), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationCardExpired(0);
    }

    @Test
    @DisplayName("27. Появление сообщения под полем Год при вводе срока действия карты превышающего допустимый")
    public void errorMsgWhenYearNotExist() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearNotExist(), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationCardDateIsIncorrect(0);
    }

    @Test
    @DisplayName("28. Появление сообщения под полем год при вводе невалидных данных (одна цифра)")
    public void errorMsgWhenEnterInTheYearFieldOneDigit() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWith1Number(), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("29. Появление сообщения под полем год при вводе при вводе любых буквенных значений")
    public void errorMsgWhenYearFieldWithCyrillicAndLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWithCyrillicAndLatin(), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("30. Появление сообщения под полем год при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheYearFieldSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYearWithSymbols(), getNameHolder(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("31. Появление сообщения под полем владелец при вводе невалидных данных (только имя)")
    public void errorMsgWhenEnterInTheHolderFieldOnlyFirstName() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), getHolderOnlyFirstName(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("32. Появление сообщения под полем Владелец при вводе ФИ на кириллицы без пробела с прописной буквы")
    public void errorMsgWhenEnterInTheHolderFieldCyrillic() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(4), getHolderFirstNameWithoutSpace(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("33. Появление сообщения под полем владелец при вводе невалидных данных (кириллица+латиница)")
    public void errorMsgWhenEnterInTheHolderFieldCyrillicAndLatin() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolderNameWithLatinAndCyrillic(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("34. Появление сообщения под полем владелец при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheHolderFieldSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(1), getHolderNameWithSymbols(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("35. Появление сообщения под полем владелец при вводе невалидных данных (цифры)")
    public void errorMsgWhenHolderFieldIsNumbers() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getHolderNameWithNumbers(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("36. Появление сообщения под полем владелец при вводе невалидных данных (двойное имя с пробелом)")
    public void errorMsgWhenEnterInTheHolderFieldExists2Space() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(3), getHolderFirstNameWithSpace(), getCvc()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("37. Появление сообщения под полем CCV/CVV при вводе невалидных данных (1 цифра)")
    public void errorMsgWhenEnterInTheCvcFieldOneDigits() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getNameHolder(), getCvcWithOneNumber()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("38. Появление сообщения под полем CCV/CVV при вводе невалидных данных (2 цифры)")
    public void errorMsgWhenEnterInTheCvcFieldTwoDigits() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getNameHolder(), getCvcWithTwoNumbers()));
        paymentPage.verifyNotificationWrongFormat(0);
    }

    @Test
    @DisplayName("39. Появление сообщения под полем CCV/CVV при вводе невалидных данных (спецсимволы)")
    public void errorMsgWhenEnterInTheCvcFieldOneSymbols() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(2), getNameHolder(), getCvcWithSymbols()));
        paymentPage.verifyNotificationWrongFormat(1);
    }

    @Test
    @DisplayName("39. Появление сообщения под полем CCV/CVV при вводе любых буквенных значений")
    public void errorMsgWhenEnterInTheCvcFieldCyrillic() {
        paymentPage.verifyFormName();
        paymentPage.fillForm(new DataHelper.CardInfo(getApprovedCardNumber(),
                getMonth(), getYear(4), getNameHolder(), getCvcWithCyrillicAndLatin()));
        paymentPage.verifyNotificationWrongFormat(1);
    }
}
