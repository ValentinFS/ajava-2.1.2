package ru.netology;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class WebFormTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
    }

    @BeforeEach
    void setUp() {
//        driver = new ChromeDriver();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
        // Все поля пустые
    void shouldTestAllFieldsEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        String expectedMessage = "Поле обязательно для заполнения";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    @Test
        // Пустое поле ФИО
    void shouldTestNameEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        String expectedMessage = "Поле обязательно для заполнения";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    @Test
        // Пустое поле Тел
    void shouldTestTelEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Петров Петр");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        String expectedMessage = "Поле обязательно для заполнения";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    @Test
        // Не нажат чек-бокс
    void shouldTestCheckBoxEmpty() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Петров Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector(".checkbox__text")).getText();
        String expectedMessage = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }


    @Test
        // ФИО латинскими буквами
    void shouldTestEngName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Petrov Petr");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }


    @Test
        // ФИО содержит спецсимволы
    void shouldTestSpecialSymbol() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Петров Петр%");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        String expectedMessage = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    @Test
        // ФИО начинается с -            БАГ БАГ БАГ
    void shouldTestDashBegin() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("-Петров Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        String expectedMessage = "Имя и Фамилия указаны неверно. Недопустимы дефисы не разделяющие Фамилию или Имя";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    @Test
        // ФИО имеет несколько “-” подряд            БАГ БАГ БАГ
    void shouldTestTwoDash() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Петров--Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        String expectedMessage = "Имя и Фамилия указаны неверно. Недопустимы повторяющиеся дефисы";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }


    @Test
        // Короткое ФИО - 1 буква                 БАГ БАГ БАГ
    void shouldTestShortName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("П");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        String expectedMessage = "Имя и Фамилия указаны неверно. Фамилия и Имя должны разделяться пробелом";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    @Test
        // Длинное ФИО                  БАГ БАГ БАГ
    void shouldTestLongName() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Подловжаыдлвоашыжмвомлодлодлофлыоуцоыдлваофва жфвлаофждлвоафвлафываущшцудцлаываылфожлваофыщвшафлудфылофудлцащцаофдываодфлвадлфо");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234567");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        String expectedMessage = "Имя и Фамилия указаны неверно. Слишком много символов.";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

    @Test
        // Невалидный Тел
    void shouldTestNotValidTel() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Петров Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("+79161234");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }


    @Test
        // Невалидный Тел
    void shouldTestNotValidTelPlus() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type='text']")).sendKeys("Петров Петр");
        driver.findElement(By.cssSelector("[type='tel']")).sendKeys("79161234567+");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("button")).click();
        String actualMessage = driver.findElement(By.cssSelector(".input_invalid .input__sub")).getText();
        String expectedMessage = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        Assertions.assertEquals(expectedMessage, actualMessage.trim());
    }

}