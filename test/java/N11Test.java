import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RunWith(BlockJUnit4ClassRunner.class)
public class N11Test {

    private static final String[] abc = {"A", "B", "C", "Ç", "D", "E", "F", "G", "H", "I", "İ", "J", "K", "L", "M", "N", "O", "Ö", "P", "Q", "R", "S", "Ş", "T", "U", "Ü", "V", "W", "X", "Y", "Z"};
    private WebDriver driver;

    {
        //TODO:replace with your chrome driver path
        System.setProperty("webdriver.chrome.driver", "/C:/chromedriver.exe");
    }

    @Before
    public void before() {
        driver = new ChromeDriver();
    }

    @Test
    public void n11Test() throws InterruptedException {

        login();

        loginControl();

        goToBooks();

        goToAuthors();

        authorsCheck();
    }

    private void login()  {

        //go to website
        driver.get("http://www.n11.com");

        //go to login page
        driver.findElement(By.className("btnSignIn")).click();

        //go to facebook login
        driver.findElement(By.className("facebookBtn")).click();

        //keep main window
        String mainWindow = driver.getWindowHandle();

        openPopUp();

        loginFacebook(mainWindow);
    }

    private void loginFacebook(String mainWindow) {

        //fill in credetials
        driver.findElement(By.id("email")).sendKeys("aykayac@hotmail.com");
        driver.findElement(By.id("pass")).sendKeys("181992");

        //click log in button
        driver.findElement(By.id("u_0_0")).click();

        //switch from pop up to main window
        driver.switchTo().window(mainWindow);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void authorsCheck()
    {
        for (String letter : abc) {
            WebElement last = letterControl(letter);
            duplicateAuthorCheck(letter, last);
        }
    }

    private void loginControl()
    {
        //are we on main page?
        assertTrue(driver.getCurrentUrl().equals("http://www.n11.com/"));

        //if this element is in the page, then we are still not logged in
        WebElement username = driver.findElement(By.className("username"));

        if(username != null) {

            assertNotNull(username);
        }
    }

    private void goToBooks()
    {
        // tried to find the Kitap link by xpath on main page menu, but could not because it is not visible by default
        //driver.findElements(By.xpath(".//*[@title='Kitap']")).get(0).click();
        driver.get("http://www.n11.com/kitap");
        assertTrue(driver.getCurrentUrl().equals("http://www.n11.com/kitap"));
    }

    private void goToAuthors()
    {
        driver.findElement(By.partialLinkText("Yazarlar")).click();

        assertTrue(driver.getCurrentUrl().equals("http://www.n11.com/yazarlar"));
    }


    private void openPopUp() {
        String subWindowHandler = null;
        Set<String> handles = driver.getWindowHandles();
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }
        driver.switchTo().window(subWindowHandler);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private WebElement letterControl(String letter)
    {
        String url = "http://www.n11.com/yazarlar/" + letter;
        driver.get(url);

        List<WebElement> authors = findAuthors();

        WebElement lastElement = null;

        for (WebElement we : authors) {
            if (!we.getText().toUpperCase().startsWith(letter)) {
                fail("The author " + we.getText() + "should not be in letter" + letter + " list");
            }
            lastElement = we;
        }

        return lastElement;
    }

    private void goToSecondPage(String letter) {
        String urlToGo = "http://www.n11.com/yazarlar/" + letter + "?pg=2";

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.get(urlToGo);
    }


    private void duplicateAuthorCheck(String letter, WebElement lastElement)
    {

        goToSecondPage(letter);

        List<WebElement> authors = findAuthors();

        if (lastElement.equals(authors.get(0))) {
            fail("Letter " + letter + " : The first author on page 2 is the same as the last one on page 1");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<WebElement> findAuthors()
    {
        WebElement authorsList = driver.findElement(By.id("authorsList"));
        List<WebElement> elements = authorsList.findElements(By.xpath(".//*"));

        return elements;
    }



}
