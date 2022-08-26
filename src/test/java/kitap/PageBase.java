package kitap;

import java.awt.Toolkit  ; 
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;

//import org.hamcrest.generator.qdox.tools.QDoxTester.Reporter;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.jayway.jsonpath.JsonPath;

/*
 * @KT1456
 * @date: 01/07/2022
 * @Description: This file contains common framework level methods for common interactions like click/wait and other actions on the page
 */

public class PageBase  { 
	public PageBase(WebDriver driver) {
		this.driver = driver;
		actions = new Actions(driver);
	}

	protected static WebDriver driver;
	String originalValue = null;
	Actions actions = null;
	protected String default_locale;
	protected Preferences prefs = Preferences.userRoot().node(this.getClass().getName());;

	public WebDriver getWebDriver() {
		return driver;
	}

	public static String OSDetector() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return "Windows";
		} else if (os.contains("nux") || os.contains("nix")) {
			return "Linux";
		} else if (os.contains("mac")) {
			return "Mac";
		} else if (os.contains("sunos")) {
			return "Solaris";
		} else {
			return "Other";
		}
	}

	public void loginSaleforceApp(String homepageURL) {
		driver.get(homepageURL);

		System.out.println("Opened URL : " + homepageURL);

		
	}
	public void openHomepage(String homepageURL) {
		try {
			
		getWebDriver().get(homepageURL);

		System.out.println("Opened URL : " + getWebDriver());
	}
		catch(Exception e)
		{
			System.out.println("Not able to navigate to url");
		}
		}


	public void openHomepageWithElement(String homepageURL, WebElement homePageElement) throws Exception {
		openHomepage(homepageURL);
		if (homePageElement != null) {
			if (!homePageElement.isDisplayed()) {
				throw new Exception();
			}
		}
	}
	
	
	
	public String getCurrentWindowHandle() {
		return driver.getWindowHandle();
	}

	public String getCurrentURL() {
		return driver.getCurrentUrl();
	}

	public void forceClickElement(WebElement element) {
		try {

			actions.moveToElement(element);
			actions.click();
			actions.build().perform();
		} catch (Exception e) {
			System.out.print("Failed to force click element" + e.toString());
		} finally {
			System.out.print("clicked on  " + element.toString());
		}
	}

	public boolean verifyWebElement(WebElement element) {
		
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.elementToBeClickable(element));

		
		if (element.isDisplayed()) {
			System.out.println("Element Displayed " + element.getText());
		} else {
			System.out.println("Element Not Displayed " + element.getText());
		}
		return element.isDisplayed();
	}

	public void waitAndClick(WebElement element) {
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.elementToBeClickable(element));

				if (element.isDisplayed()) {
			System.out.println("Element found and clicked");
			element.click();
		} else {
			System.out.println("Element Not Displayed " + element.getText());
		}

	}

	public boolean verifyElementEnabled(WebElement element) {
		if (element.isEnabled()) {
			System.out.println("Element Enabled " + element.getText());
		} else {
			System.out.println("Element Not Enabled " + element.getText());
		}
		return element.isEnabled();
	}

	public void refreshPage() {
		driver.navigate().refresh();
		System.out.println("Page Refreshed");
	}

	public void quitBrowser() {
		
		driver.quit();
	}
	public void closeCurrentBrowser() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String script = "window.onbeforeunload = null;" + "window.close();";
		js.executeScript(script);
	}

	public void handleIEError() {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM WerFault.exe");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sleep(long millis) {

		try {
			TimeUnit.MILLISECONDS.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void explicitWait(WebElement elmt, int timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(elmt));
	}

	public void explicitWaitInMinutes(WebElement elmt, int timeOutInMinutes) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInMinutes);
		wait.until(ExpectedConditions.visibilityOf(elmt));
	}

	public void explicitWaitClickable(WebElement elmt, int timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.elementToBeClickable(elmt));
	}

	public void explicitWait(List<WebElement> elmt, int timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(elmt.get(1)));
	}

	public boolean isAlertOpen() {
		WebDriverWait wait = new WebDriverWait(driver, 1);
		try {
			return wait.until(ExpectedConditions.alertIsPresent()) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public String replaceValue(String key, String... values) {
		originalValue = key;
		String eleValue = originalValue;
		for (int i = 0; i < values.length; i++) {
			String value = values[i];
			eleValue = eleValue.replace("{" + (i + 1) + "}", value);
		}
		return eleValue;
	}

	public String getTextOfElement(WebElement element) {
		return element.getText().trim();
	}

	public void mouseOverElement(WebElement element) {

		actions.moveToElement(element).build().perform();
	}

	public String getAttrValueForElement(WebElement element, String attrName) {
		if (null == element) {
			return null;
		}
		if (attrName.isEmpty()) {
			return null;
		}
		return element.getAttribute(attrName.trim().toLowerCase());
	}

	public void scrollToElement(WebElement element) {
		int elementPosition = element.getLocation().getY();
		String js = String.format("window.scroll(0, %s)", elementPosition);
		((JavascriptExecutor) driver).executeScript(js);
	}
	
	public void scrollToElementview() throws InterruptedException {
		WebElement element = driver.findElement(By.xpath("//input[@name='Site']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Thread.sleep(2000); 
		
	}
	
	
	public static void highlightelement(WebElement element) {
	for (int i = 0; i < 4; i++) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
js.executeScript("arguments[0].setAttribute('style', 'background: green; border: 2px solid red;');", element);
	}
	}

	
	public void scrollToElementviewweb() throws InterruptedException {
		
		WebElement element = driver.findElement(By.xpath("//input[@name='Website']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Thread.sleep(2000);
	
	}

	public void javascriptExecutor(String jscode) {
		((JavascriptExecutor) driver).executeScript(jscode);
	}

	public void scrollByCoordinate(int horizontal, int vertical) {
		String str1 = Integer.toString(horizontal);
		String str2 = Integer.toString(vertical);
		String arguments = "window.scrollBy(" + str1 + "," + str2 + ")";
		((JavascriptExecutor) driver).executeScript(arguments);
	}

	public void scrollToElementAsync(WebElement element) {
		int elementPosition = element.getLocation().getY();
		String js = String.format("window.scroll(0, %s)", elementPosition);
		((JavascriptExecutor) driver).executeAsyncScript(js);
	}

	public void scrollToElementInView(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.perform();
	}

	public void scrollToElementHorizontally(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft = arguments[0].offsetWidth", element);
	}

	public void scrollVerticallyInsideDiv(WebElement element) {

	}

	public boolean scroll_Page(WebElement webelement, int scrollPoints) {
		try {

			int numberOfPixelsToDragTheScrollbarDown = 10;
			for (int i = 10; i < scrollPoints; i = i + numberOfPixelsToDragTheScrollbarDown) {
				actions.moveToElement(webelement).clickAndHold().moveByOffset(0, numberOfPixelsToDragTheScrollbarDown)
						.release(webelement).build().perform();
			}
			Thread.sleep(500);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * @KT1456
	 * @date: 01/07/2022
	 * @Description: This method helps with exceptions on element not being into viewport and resulting in element x intercepts click on element y
	 */
	

	public void javascriptScrollToElement(WebElement webelement) {

		
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView()", webelement);
	}

	public void scrollToHeader() {
		String js = String.format("window.scroll(0, 0)");
		((JavascriptExecutor) driver).executeScript(js);
	}

	public void scrollToBottom() {
		String js = String.format("window.scrollTo(0, document.body.scrollHeight)");
		((JavascriptExecutor) driver).executeScript(js);
	}

	public void switchToNewWindow() {

		String firstWindow = driver.getWindowHandle();
		String newWindow = "";
		Set<String> allWindows = driver.getWindowHandles();

		for (String window : allWindows) {
			if (!window.equals(firstWindow)) {
				newWindow = window;
			}
		}

		driver.switchTo().window(firstWindow);
		driver.switchTo().window(newWindow);
		
	}

	public String getCurrentDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String currentdatetime = simpleDateFormat.format(new Date());
		return currentdatetime;
	}

	public String getCurrentDateInMMDDYYYY() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
		String currentdatetime = simpleDateFormat.format(new Date());
		return currentdatetime;
	}

	
	public String getCurrentDateInMDDYYYY() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy");
		String currentdatetime = simpleDateFormat.format(new Date());
		return currentdatetime;
	}

	public String getCurrentDateInYYYYMMDD() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
		String currentdatetime = simpleDateFormat.format(new Date());
		return currentdatetime;

	}

	public String getTomorrowateInYYYYMMDD() {
		Date dt = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");

		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();

		return simpleDateFormat.format(dt);
	}

	public String getTomorrowDateInMDYYYY() {
		Date dt = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy");

		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();

		return simpleDateFormat.format(dt);
	}

	public String getTomorrowDateInMDYYYYEST() {
		// This method returns the current date's next date in EST format
		Instant nowUtc = Instant.now();
		ZoneId usnewyork = ZoneId.of("America/New_York");
		TimeZone usnewyorktimezone = TimeZone.getTimeZone(usnewyork);

		Date dt = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/d/yyyy");

		Calendar c = Calendar.getInstance(usnewyorktimezone);
		c.setTime(dt);
		c.add(Calendar.DATE, 1);
		dt = c.getTime();

		return simpleDateFormat.format(dt);
	}

	public String getCurrentDateInMMDDYY() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String currentdatetime = simpleDateFormat.format(new Date());
		return currentdatetime;
	}

	public String getCurrentDateTimeStamp() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:h:mm:ss");
		String currentdatetime = simpleDateFormat.format(new Date());
		return currentdatetime;
	}

	public String getCurrentTimeStamp() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm");
		String currentdatetime = simpleDateFormat.format(new Date());
		return currentdatetime;
	}

	public String getCurrentDateWithCustomFormat(String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		String currentdatetime = simpleDateFormat.format(new Date());
		return currentdatetime;
	}

	public boolean isElementDisplayed(WebElement elmt) {
		boolean display = false;
		int defaultTimeout = 120;

		WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
		try {
			wait.until(ExpectedConditions.visibilityOf(elmt));
			display = true;
		} catch (Exception e) {
			display = false;
			System.out.println("Element not found");
		}

		return display;
	}

	public boolean isElementNotDisplayed(WebElement elmt) {

		int defaultTimeout = 10;

		WebDriverWait wait = new WebDriverWait(driver, defaultTimeout);
		try {
			wait.until(ExpectedConditions.visibilityOf(elmt));
			return true;
		} catch (NoSuchElementException | TimeoutException e) {
			return false;
		}

	}

	public boolean isElementDisplayedWithTimeOut(WebElement elmt, int timeout) {
		boolean display = false;

		WebDriverWait wait = new WebDriverWait(driver, timeout);
		try {
			wait.until(ExpectedConditions.visibilityOf(elmt));
			display = true;
		} catch (Exception e) {
			display = false;
		}

		return display;
	}

	public String getInnerText(WebElement elmt) {
		return elmt.getAttribute("textContent");
	}

	public int getWindowsCount() {
		Set<String> listOfWindows = driver.getWindowHandles();
		return listOfWindows.size();
	}

	public void acceptAlert() throws InterruptedException {
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			System.out.println("Alert not found on the page");
		}

	}

	public void clickControlHome() {
		
		actions.keyDown(Keys.CONTROL).sendKeys(Keys.HOME).keyUp(Keys.CONTROL).perform();
	}

	public void clickEscapeKey() {
		
		actions.sendKeys(Keys.ESCAPE).build().perform();
	}

	public void selectValueByVisibleText(WebElement wb, String visibletext) {
		Select select = new Select(wb);
		select.selectByVisibleText(visibletext);
		System.out.println("Selected value " + visibletext);
	}

	public void getAllOptions(WebElement wb) {
		Select select = new Select(wb);
		
		List<WebElement> options = select.getOptions();
		for (WebElement we : options) {
			System.out.println(we.getText());
		}
		System.out.println("Option values " + select.getOptions());
	}

	public void selectValueByIndex(WebElement ele, int index) {
		Select select = new Select(ele);
		select.selectByIndex(index);
		System.out.println("Selected value " + select.getAllSelectedOptions().get(index));
	}

	public void selectValue(WebElement ele, String value) {
		Select select = new Select(ele);
		select.selectByValue(value);
		System.out.println("Selected value : " + value);
	}

	public void enterValue(WebElement element, String value) {
		element.click();
		if (element.getTagName().equals("input")) {
			element.clear();
		}
		element.sendKeys(value);
		System.out.println("Entered value " + value);
	}

	public void enterValueTextArea(WebElement element, String value) {
		highlightelement(element);
		element.click();
		if (element.getTagName().equals("textarea")) {
			element.clear();
		}
		element.sendKeys(value + Keys.ENTER);
		System.out.println("Entered value " + value);
	}
	
	public void clearElement(WebElement element) {
		
		element.clear();
	}
	public void enterValueText(WebElement element, String value) {
		highlightelement(element);
		element.sendKeys(value + Keys.ENTER);
		System.out.println("Entered value " + value);
	}


	
	public void enterValueUsingScript(WebElement element, String value) {

		element.click();
		if (element.getTagName().equals("input")) {
			element.clear();
		}

		actions.moveToElement(element);
		actions.click();
		actions.sendKeys(value);
		actions.build().perform();
		System.out.println("Using Actions,Entered value:" + value);
	}

	public void selectRadio(WebElement element) {
		element.click();
	}

	public String captureText(WebElement element) {
		return element.getText();
	}

	public void safeClick(WebElement element) {
		if ((element != null) && (element.isDisplayed())) {
			element.click();
		} else {
			

			System.out.println("Element: " + element.toString() + ", is not available on a page - ");
		}
	}

	public File takeScreenshot() {
		WebDriver augment = new Augmenter().augment(driver);
		return ((TakesScreenshot) augment).getScreenshotAs(OutputType.FILE);
	}

	public void maximize() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenResolution = new Dimension((int) toolkit.getScreenSize().getWidth(),
				(int) toolkit.getScreenSize().getHeight());
		driver.manage().window().setSize(screenResolution);
	}

	public boolean imageCompare(String actImagePath, String expImagePath) {
		boolean ret = true;
		try {
			URL url = new URL(actImagePath);
			BufferedImage actImage = ImageIO.read(url);
			BufferedImage expImage = ImageIO.read(new File(expImagePath));
			Raster rasActImage = actImage.getData();
			Raster rasExpImage = expImage.getData();
			// Comparing the the two images for number of bands,width & height.
			if (rasActImage.getNumBands() != rasExpImage.getNumBands()
					|| rasActImage.getWidth() != rasExpImage.getWidth()
					|| rasActImage.getHeight() != rasExpImage.getHeight()) {
				ret = false;
			} else {
				// Once the band ,width & height matches, comparing the images.
				search: for (int i = 0; i < rasActImage.getNumBands(); ++i) {
					for (int x = 0; x < rasActImage.getWidth(); ++x) {
						for (int y = 0; y < rasActImage.getHeight(); ++y) {
							if (rasActImage.getSample(x, y, i) != rasExpImage.getSample(x, y, i)) {
								// If one of the result is false setting the result as false and breaking the
								// loop.
								ret = false;
								break search;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Cannot read the image file " + e.getMessage());
			ret = false;
		}
		return ret;
	}

   
	/*
	    * @KT1456
	    * @date: 11/07/2022
	    * @Description: Sets the amount of time to wait for a page load to complete, before throwing an error. If the timeout is negative, page loads can be indefinite.
	    
	 */
	
	public void pageLoadWait(int time) {


		driver.manage().timeouts().pageLoadTimeout(time, TimeUnit.SECONDS);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void waitAndforceClickElement(WebElement element) {
		try {
			Wait wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			actions.moveToElement(element);
			actions.click();
			actions.build().perform();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.print("Failed to force click element" + e.getMessage());
		}
	}

	public LogEntries getConsoleLogEntriesChromeBrowser() {
		
		LogEntries logs = driver.manage().logs().get("browser");

		return logs;
	}

	public boolean waitForJSandJQueryToLoad() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					Long r = (Long) ((JavascriptExecutor) driver).executeScript("return $.active");
					return r == 0;
				} catch (Exception e) {
					System.out.println("no jquery present");
					return true;
				}
			}
		};
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}

	public void implicitWait(int time) {
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);

	}

	public String getCurrentDateESTmdyyyy() {
		DateTimeFormatter etFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
		
		LocalDate localDate = LocalDate.now(ZoneId.of("America/New_York"));
		return (etFormat.format(localDate));
	}
	

	/*
	    * @KT1456
	    * @date: 11/07/2022
	    * @Description: This method returns the "By" value for a webelement as an input.
	    
	 */
	

	public static By webElementToByValue(WebElement we) {
		
		String[] data = we.toString().split(" -> ")[1].replace("]", "").split(": ");
		String locator = data[0];
		String term = data[1];

		switch (locator) {
		case "xpath":
			return By.xpath(term);
		case "css selector":
			return By.cssSelector(term);
		case "id":
			return By.id(term);
		case "tag name":
			return By.tagName(term);
		case "name":
			return By.name(term);
		case "link text":
			return By.linkText(term);
		case "class name":
			return By.className(term);
		}
		return (By) we;
	}

	public WebElement chainedElementLocator(By parentelement, By childelement) {
		WebElement element = driver.findElement(new ByChained(parentelement, childelement));

		return element;

	}

	public void browserback() throws InterruptedException {
		driver.navigate().back();
		implicitWait(5);
	}

	public void SFClick(WebElement we) {

		highlightelement(we);

		try { 
			WebDriverWait wait = new WebDriverWait(driver, 30);

			wait.withTimeout(Duration.ofSeconds(30)).pollingEvery(Duration.ofMillis(500))
					.ignoreAll(new ArrayList<Class<? extends Throwable>>() {

						private static final long serialVersionUID = 1L;

						{
							add(StaleElementReferenceException.class);
							add(NoSuchElementException.class);
							add(TimeoutException.class);
							add(InvalidElementStateException.class);
							add(NoSuchFrameException.class);
							add(WebDriverException.class);
						}
					}).withMessage("Fluent wait in process");
			String elementText = we.getText();

			System.out.println("Clicking on webelement: " + elementText);
			Function<WebDriver, Boolean> function = new Function<WebDriver, Boolean>() {

				@Override
				public Boolean apply(WebDriver arg0) {

					if (we.isDisplayed() && we.isEnabled()) {
						System.out.println("Element displayed is " + elementText);
						return true;
					} else {
						return false;
					}

				}
			};
			wait.until(function);

			JavascriptExecutor executor = (JavascriptExecutor) driver;

			executor.executeScript("arguments[0].focus(); arguments[0].click();", we);
			

		} catch (StaleElementReferenceException s) {
			System.out.println("StaleElement exception for web element" + we.getText());
			refreshPage();
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].focus(); arguments[0].click();", we);

		} catch (Exception e) {
			System.out.println("SFClick exception for web element" + we.getText());

		}
	}

	public void uploadFileToWebElement(String filepath, WebElement element) {
		sleep(4000);

		element.sendKeys(filepath);
		sleep(4000);
		

	}

	public char getNumericValueFromString(String text) {

		char ch;
		char intValue = 0;
		for (int i = 0; i < text.length(); i++) {
			ch = text.charAt(i);
			if (ch >= '0' && ch <= '9') {
				intValue = ch;
			}
		}
		return intValue;
	}

	public void clickOnElementUsingJavaScript(WebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}

	public void disablegpu() {
		System.setProperty("webdriver.chrome.driver", "C:\\Softwares\\drivers\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("useAutomationExtension", false);
	}

	public void uploadFile(String filepath, WebElement element) {
		sleep(4000);

		int size = driver.findElements(By.tagName("iframe")).size();
		System.out.println("Number of iFrames detected " + size);
		driver.switchTo().frame(0);

		element.sendKeys(filepath);
		sleep(4000);
		driver.switchTo().defaultContent();

	}

	public String getCurrentTimeEST() {
		DateTimeFormatter etFormat = DateTimeFormatter.ofPattern("h:mm");
		
		LocalTime localTime = LocalTime.now(ZoneId.of("America/New_York"));
		
		return (etFormat.format(localTime));

	}

	public String getCurrentDateESTmddyyyy() {
		DateTimeFormatter etFormat = DateTimeFormatter.ofPattern("M/dd/yyyy");
		
		LocalDate localDate = LocalDate.now(ZoneId.of("America/New_York"));
		return (etFormat.format(localDate));
	}

	public LocalTime gethmmTimeFromString(String str) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
		LocalTime time = LocalTime.parse(str, formatter);
		return time;

	}
	
	/*
	 * @KT1456
	 * @date: 13/07/2022
	 * @Description: This method takes two datetime values and returns the difference in minutes
	 */

	public String getTimeDiffMinutes(String startdatetime, String enddatetime) throws ParseException {
		

		DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy hh:mm a");

		Date startdate = dateFormat.parse(startdatetime);
		Date enddate = dateFormat.parse(enddatetime);
		DecimalFormat decFormatter = new DecimalFormat("#####");

		long diff = enddate.getTime() - startdate.getTime();

		int diffmin = (int) (diff / (60 * 1000));
		return decFormatter.format(diffmin);
	}

	public boolean scrollPage(WebElement webelement, int scrollPoints) {
		try {
			Actions dragger = new Actions(driver);
			
			int numberOfPixelsToDragTheScrollbarDown = 10;
			for (int i = 10; i < scrollPoints; i = i + numberOfPixelsToDragTheScrollbarDown) {
				dragger.moveToElement(webelement).clickAndHold().moveByOffset(0, numberOfPixelsToDragTheScrollbarDown)
						.release(webelement).build().perform();
			}
			Thread.sleep(500);
			
			System.out.println("Success");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception caught");
			return false;

		}
	}

	public void compareDateValue(String bdate, String adate) throws ParseException {
		Date beforedate = new SimpleDateFormat("dd/MM/yyyy").parse(bdate);
		Date afterdate = new SimpleDateFormat("dd/MM/yyyy").parse(adate);

		Calendar calen1 = Calendar.getInstance();
		Calendar calen2 = Calendar.getInstance();
		calen1.setTime(beforedate);
		calen2.setTime(afterdate);

		
		if (calen1.after(calen2)) {

			
			System.out.println(bdate + " is after " + adate);
		}

		else if (calen1.before(calen2)) {

			
			System.out.println(bdate + " is before " + adate);
		}
	}

	public void hardwait(int timeinsec) throws InterruptedException {
		Thread.sleep(timeinsec * 1000);
	}

	public static String readJsonFile(String jsonfilename, String path_key) {
		{ 
		

			try {

				String sPath = new java.io.File(".").getCanonicalPath();
				File jsonFile = new File(sPath + File.separator + "src" + File.separator + "main" + File.separator
						+ "resources" + File.separator + jsonfilename + ".json");
				return JsonPath.read(jsonFile, path_key).toString().replace("[\"", "").replace("\"]", "");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
