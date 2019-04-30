package com.jaeheonshim.supremebot;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;

import java.util.Scanner;
import java.util.List;

public class Main {
    //========Checkout Variables=========
    private static String checkout_name = "Michael L. Kellogg";
    private static String checkout_email = "MichaelLKellogg@jourrapide.com";
    private static String checkout_tel = "831-652-3192";
    private static String checkout_address = "1037 Emily Renzelli Boulevard";
    private static String checkout_zip = "93901";
    private static String checkout_city = "Salinas";
    private static String checkout_state = "CA"; //National standard abbreviation (Indiana: IN)
    private static String checkout_cardnum = "5558174972425621"; //No spaces or dashes
    private static String checkout_expmonth = "04"; //Card Expiration MONTH - If month is one digit, add 0 at beginning
    private static String checkout_expyear = "2022"; //Card Expiration YEAR
    private static String checkout_cvv = "217"; //Card security code

    //===========End Variables===========
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int selection;
        String category = "";
        do {
            System.out.println("Choose a category: ");
            System.out.println("[1] jackets");
            System.out.println("[2] shirts");
            System.out.println("[3] tops/sweaters");
            System.out.println("[4] sweatshirts");
            System.out.println("[5] pants");
            System.out.println("[6] shorts");
            System.out.println("[7] t-shirts");
            System.out.println("[8] hats");
            System.out.println("[9] bags");
            System.out.println("[10] accessories");
            System.out.print(">>> ");
            selection = scanner.nextInt();

            switch (selection) {
                case 1:
                    System.out.println("Selected jackets");
                    category = "jackets";
                    break;
                case 2:
                    System.out.println("Selected shirts");
                    category = "shirts";
                    break;
                case 3:
                    System.out.println("Selected tops/sweaters");
                    category = "tops_sweaters";
                    break;
                case 4:
                    System.out.println("Selected sweatshirts");
                    category = "sweatshirts";
                    break;
                case 5:
                    System.out.println("Selected pants");
                    category = "pants";
                    break;
                case 6:
                    System.out.println("Selected shorts");
                    category = "shorts";
                    break;
                case 7:
                    System.out.println("Selected t-shirts");
                    category = "t-shirts";
                    break;
                case 8:
                    System.out.println("Selected hats");
                    category = "hats";
                    break;
                case 9:
                    System.out.println("Selected bags");
                    category = "bags";
                    break;
                case 10:
                    System.out.println("Selected accessories");
                    category = "accessories";
                    break;
                default:
                    System.out.println("Could not identify selection. Try again. \n");
            }
        } while (selection < 1 || selection > 10);

        System.out.println();
        System.out.print("Input item color (Not case sensitive, algorithm will treat input as keyword.) >>> ");
        String color = scanner.next();
        System.out.println("Received Color: " + color);

        System.out.println();
        System.out.println("Enter item keywords (Words that are in the title of the item). \nYou don't need the full title, just a portion of it. \nFor example, you could truncate Supreme Box Logo Tee to just Box Logo Tee.");
        System.out.print("Item keywords >>> ");
        scanner.nextLine();
        String title = scanner.nextLine();

        System.out.println("\n==============================================");
        System.out.println("Summary: ");
        System.out.println("Category: " + category);
        System.out.println("Color: " + color);
        System.out.println("Item Keywords: " + title);
        System.out.println("==============================================");
        for (; ; ) {
            System.out.print("\nIs this correct? (y/n) >>> ");
            String yesorno = scanner.next();
            if (yesorno.equals("y")) {
                System.out.println("Bot initializing");
                bot(category, color, title);
                break;
            } else if (yesorno.equals("n")) {
                System.out.println("Starting over...");
                main(args);
            } else {
                System.out.println("Please enter y or n");
            }
        }
    }

    private static void bot(String category, String color, String keywords) {
        color = color.toLowerCase();
        String[] keywordsarr = keywords.split(" ");
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.supremenewyork.com/shop/all/" + category);
        List rows = driver.findElements(By.xpath("//*[@id=\"container\"]/article"));
        for (int i = 1; i <= rows.size(); i++) {
            WebElement productcolor = driver.findElement(By.xpath("//*[@id=\"container\"]/article[" + i + "]/div/p/a"));
            WebElement productname = driver.findElement(By.xpath("//*[@id=\"container\"]/article[" + i + "]/div/h1/a"));
            if (productcolor.getAttribute("innerHTML").toLowerCase().equals(color)) {
                boolean found = true;
                for (int j = 0; j < keywordsarr.length; j++) {
                    if (!productname.getAttribute("innerHTML").toLowerCase().contains(keywordsarr[j].toLowerCase())) {
                        System.out.println("Keywords don't match, moving on to next item");
                        found = false;
                    }
                }
                if (found == true) {
                    System.out.println("Item FOUND!!!");
                    driver.findElement(By.xpath("//*[@id=\"container\"]/article[" + i + "]/div/a")).click();
                    WebDriverWait wait = new WebDriverWait(driver, 20);
                    wait.until(ExpectedConditions.titleContains("Supreme:")); //if you want to wait for a particular title to show up
                    if (0 == driver.findElements(By.name("commit")).size()) {
                        System.out.println("Unfortunately, item is sold out.");
                        return;
                    } else {
                        driver.findElement(By.name("commit")).click();
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"cart\"]/a[2]")));
                        driver.findElement(By.xpath("//*[@id=\"cart\"]/a[2]")).click();
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"pay\"]/input")));
                        //START CHECKOUT
                        driver.findElement(By.xpath("//*[@id=\"order_billing_name\"]")).sendKeys(checkout_name);
                        driver.findElement(By.xpath("//*[@id=\"order_email\"]")).sendKeys(checkout_email);
                        driver.findElement(By.xpath("//*[@id=\"order_tel\"]")).sendKeys(checkout_tel);
                        driver.findElement(By.xpath("//*[@id=\"bo\"]")).sendKeys(checkout_address);
                        driver.findElement(By.xpath("//*[@id=\"order_billing_zip\"]")).sendKeys(checkout_zip);
                        driver.findElement(By.xpath("//*[@id=\"order_billing_city\"]")).sendKeys(checkout_city);
                        driver.findElement(By.xpath("//*[@id=\"order_billing_city\"]")).sendKeys(checkout_city);
                        Select state = new Select(driver.findElement(By.id("order_billing_state")));
                        state.selectByVisibleText(checkout_state);
                        driver.findElement(By.id("nnaerb")).sendKeys(checkout_cardnum);
                        Select expmonth = new Select(driver.findElement(By.xpath("//*[@id=\"credit_card_month\"]")));
                        expmonth.selectByVisibleText(checkout_expmonth);
                        Select expyear = new Select(driver.findElement(By.xpath("//*[@id=\"credit_card_year\"]")));
                        expyear.selectByVisibleText(checkout_expyear);
                        driver.findElement(By.xpath("//*[@id=\"orcer\"]")).sendKeys(checkout_cvv);
                        driver.findElement(By.xpath("//*[@id=\"cart-cc\"]/fieldset/p[2]/label/div")).click();
                        System.out.println("PLACING ORDER");
                        driver.findElement(By.xpath("//*[@id=\"pay\"]/input")).click();
                        return;
                    }
                }

            }
        }
    }
}