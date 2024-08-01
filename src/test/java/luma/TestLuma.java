package luma;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TestLuma {
	JavascriptExecutor js;
	WebDriver driver;
	String filePath = "E:\\Testing\\Selenium\\LumaProject\\LumaData.xlsx";
	public FileInputStream fis;
	public XSSFWorkbook workbook;
	public CreateAccount signUp;
	public LogOut signOut;
	public LogIn signIn;
	Actions act;
	WebElement th;
	Select sc;

	public TestLuma() throws IOException {
		fis = new FileInputStream(filePath);
		workbook = new XSSFWorkbook(fis);
	}

	@BeforeSuite
	public void setUp() throws IOException {
		this.driver = new EdgeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		driver.get("https://magento.softwaretestingboard.com/");
	}

	@Test(priority = 1)
	// To Create accounts of various User details
	public void createAcc() throws IOException {
		signUp = new CreateAccount(driver);
		signOut = new LogOut(driver);
		XSSFSheet sheet = workbook.getSheet("createAcc");

		int rows = sheet.getLastRowNum();
		for (int i = 1; i <= rows; i++) {
			XSSFRow row = sheet.getRow(i);
			XSSFCell fname = row.getCell(0);
			XSSFCell lname = row.getCell(1);
			XSSFCell email = row.getCell(2);
			XSSFCell pass = row.getCell(3);
			XSSFCell cpass = row.getCell(4);
			XSSFCell res = row.createCell(5);

			try {
				signUp.clickSignUpLink();
				signUp.EnterFirstName(fname.toString());
				signUp.EnterLastName(lname.toString());
				signUp.EnterEmail(email.toString());
				signUp.EnterPassword(pass.toString());
				signUp.EnterConfirmPassword(cpass.toString());
				System.out.println("-----------UserCredentials " + i + "-----------");
				System.out.println("FirstName::" + fname + "\t" + "LastName::" + lname + "\t" + "Email::" + email + "\n"
						+ "Password::" + pass + "\t" + "Confirm Password::" + cpass);
				signUp.clickCreateAccBtn();
				Thread.sleep(1500);
				signOut.clickWelcomeLink();
				Thread.sleep(1000);
				signOut.clickSignOut();
				System.out.println("Valid Credentials");
				res.setCellValue("Valid Credentials");
			} catch (Exception e) {
				System.out.println("Invalid Credentials");
				res.setCellValue("Invalid Credentials");
			}

		}
		fis.close();
		FileOutputStream fos = new FileOutputStream(filePath);
		workbook.write(fos);
	}

	@Test(priority = 2)
	// To Login various user credentials
	public void logIn() throws IOException {
		signOut = new LogOut(driver);
		signIn = new LogIn(driver);
		XSSFSheet sheet = workbook.getSheet("LogIn");
		int rows = sheet.getLastRowNum();
		for (int i = 1; i <= rows; i++) {
			XSSFRow row = sheet.getRow(i);
			XSSFCell email = row.getCell(0);
			XSSFCell pass = row.getCell(1);
			XSSFCell res = row.createCell(2);

			try {
				signIn.clickSignInLink();
				signIn.enterEmail(email.toString());
				signIn.enterPassword(pass.toString());
				signIn.clickSignInBtn();
				System.out.println("-----------UserCredentials " + i + "-----------");
				System.out.println("Email Id::" + email + "\t" + "Password::" + pass);
				signOut.clickWelcomeLink();
				signOut.clickSignOut();
				System.out.println("Valid Credentials");
				res.setCellValue("Valid Credentials");
			} catch (Exception e) {
				System.out.println("Invalid Credentials");
				res.setCellValue("Invalid Credentials");
			}
		}
		fis.close();
		FileOutputStream fos = new FileOutputStream(filePath);
		workbook.write(fos);

	}

	@Test(priority = 3)
	// To Test all the nabar links navigates to appropriate webpage or not
	public void testNavLinks() throws InterruptedException {
		String titles[] = { "What's New", "Women", "Men", "Gear", "Training", "Sale" };
		int elements = driver.findElements(By.xpath("//nav[@class='navigation']/ul/li")).size();
		for (int i = 0; i < elements; i++) {
			List<WebElement> navLinks = driver.findElements(By.xpath("//nav[@class='navigation']/ul/li"));
			WebElement link = navLinks.get(i);
			Thread.sleep(1000);
			link.click();
			Thread.sleep(1500);
			if (driver.getTitle().equals(titles[i])) {
				System.out.println("Apropriate Link. Test Case Pass");
			} else {
				System.out.println("Inapropriate Link. Test Case Fail");
			}
		}
	}

	@Test(priority = 4)
	// to Test forgot password
	public void forgotPass() throws InterruptedException {
		signIn = new LogIn(driver);
		signIn.clickSignInLink();
		// click on Forgot pass
		driver.findElement(By.xpath("//fieldset[@class='fieldset login']//descendant::a")).click();
		WebElement email = driver.findElement(By.name("email"));
		email.sendKeys("stilesstilinki@gmail.com");
		Thread.sleep(1000);
		// click on submit
		driver.findElement(By.className("submit")).click();
		Thread.sleep(1000);

		String passSetemailMsg = driver.findElement(By.xpath("//div[@class='page messages']//descendant::div[5]"))
				.getText();
		System.out.println(passSetemailMsg);

		signIn.enterEmail("stilesstilinki@gmail.com");
		signIn.enterPassword("Styles123");
		Thread.sleep(1000);
		signIn.clickSignInBtn();

	}

	@Test(priority = 5)
	// To Edit user details
	public void editUserDetails() throws InterruptedException {
		signOut = new LogOut(driver);
		signOut.clickWelcomeLink();
		// click on My Account
		driver.findElement(By.linkText("My Account")).click();
		// click on Edit
		driver.findElement(By.xpath("//main[@id='maincontent']/descendant::div[13]/child::a[1]")).click();

		WebElement firstName = driver.findElement(By.id("firstname"));
		firstName.clear();
		firstName.sendKeys("Styles");
		Thread.sleep(1000);

		WebElement lastName = driver.findElement(By.id("lastname"));
		firstName.clear();
		firstName.sendKeys("Stilinski");
		Thread.sleep(1000);

		WebElement changeEmail = driver.findElement(By.id("change-email"));
		if (changeEmail.isEnabled()) {
			changeEmail.click();
		} else {
			System.out.print("Change Email Checkbox is not Enabled");
		}

		WebElement email = driver.findElement(By.id("email"));
		email.clear();
		email.sendKeys("styles@gmail.com");
		Thread.sleep(1000);

		WebElement changePass = driver.findElement(By.id("change-password"));
		if (changePass.isEnabled()) {
			changePass.click();
		} else {
			System.out.print("Change Email Checkbox is not Enabled");
		}

		WebElement currentPass = driver.findElement(By.id("current-password"));
		currentPass.sendKeys("Styles123");
		Thread.sleep(1000);

		WebElement newPass = driver.findElement(By.id("password"));
		newPass.sendKeys("Lydia123");
		Thread.sleep(1000);

		WebElement confirmPass = driver.findElement(By.id("password-confirmation"));
		confirmPass.sendKeys("Lydia123");
		Thread.sleep(1000);

		WebElement submitBtn = driver.findElement(By.xpath("//form[@id='form-validate']//button"));
		submitBtn.click();
	}

	@Test(priority = 6)
	public void testLogo() throws InterruptedException {
		WebElement logoImg = driver.findElement(By.xpath("//a[@class='logo']/img"));
		if (logoImg.isDisplayed()) {
			System.out.println("Logo is displayed");
			// To test that clicking on logo navigates to home page
			logoImg.click();
			Thread.sleep(1000);
			String title = driver.getTitle();
			if (title.equals("Home Page")) {
				System.out.println("Clicking on logo navigates to Home page");
			}
		}
	}

	@Test(priority = 7)
	// To Test that the Item visible mode element is Enabled and Disabled
	public void itemVisibleMode() throws InterruptedException {
		js = (JavascriptExecutor) driver;

		// Check List Mode is Enabled
		WebElement listMode = driver.findElement(By.id("mode-list"));
		if (listMode.isEnabled()) {
			listMode.click();
			js.executeScript("window.scrollBy(0,450)", "");
			Thread.sleep(1000);
			js.executeScript("window.scrollBy(0,-450)", "");
			Thread.sleep(1000);
			System.out.println("List Mode is Enabled. Test Case Pass");
		} else {
			System.out.println("List Mode is not Enabled. Test Case Fail");
		}

		// Check Grid mode is Enabled
		WebElement gridMode = driver.findElement(By.id("mode-grid"));
		if (gridMode.isEnabled()) {
			gridMode.click();
			Thread.sleep(1000);
			System.out.println("Grid Mode is Enabled. Test Case Pass");
		} else {
			System.out.println("Grid Mode is not Enabled. Test Case Fail");
		}
	}

	@Test(priority = 8)
	// To Test that the Item visible mode element is Enabled and Disabled
	public void itemVisibleMode1() throws InterruptedException {
		js = (JavascriptExecutor) driver;

		// Check List Mode is Enabled
		WebElement listMode = driver.findElement(By.id("mode-list"));
		if (listMode.isEnabled()) {
			listMode.click();
			js.executeScript("window.scrollBy(0,450)", "");
			Thread.sleep(1000);
			js.executeScript("window.scrollBy(0,-450)", "");
			Thread.sleep(1000);
			System.out.println("List Mode is Enabled. Test Case Pass");
		} else {
			System.out.println("List Mode is not Enabled. Test Case Fail");
		}

		// Check Grid mode is Enabled
		WebElement gridMode = driver.findElement(By.id("mode-grid"));
		if (gridMode.isEnabled()) {
			gridMode.click();
			Thread.sleep(1000);
			System.out.println("Grid Mode is Enabled. Test Case Pass");
		} else {
			System.out.println("Grid Mode is not Enabled. Test Case Fail");
		}
	}

	@Test(priority = 9)
	// To Test that total items are equal to the item count displayed
	public void itemCount() {
		// displayed number
		WebElement ItemCountDisplayed = driver
				.findElement(By.xpath("//div[@class='column main']/descendant::div[2]/p"));
		String Count = ItemCountDisplayed.getText();
		String num = Count.substring(0, 2);
		int displayedCount = Integer.parseInt(num);

		// Count total items in page displayed
		List<WebElement> totalItems = driver
				.findElements(By.xpath("//ol[@class='products list items product-items']/li"));
		int actualCount = totalItems.size();
		System.out.println("Number of items displayed:" + num + " Actual number of items on the page" + actualCount);
		if (displayedCount == actualCount) {
			System.out.println("Total Number of Items is equal to Displayed Item Count. Test Case Pass");
		} else {
			System.out.println("Total Number of Items is not equal to Displayed Item Count. Test Case Fail");
		}

	}

	@Test(priority = 10)
	// To Test that sorting of the products is done by Position, price and Product
	public void sortByDropdwn() throws InterruptedException {
		WebElement sorter = driver.findElement(By.id("sorter"));
		Select sc = new Select(sorter);
		sorter.click();
		// ByIndex
		sc.selectByIndex(1);
		Thread.sleep(1000);

		// ByValue
		WebElement sorter2 = driver.findElement(By.id("sorter"));
		Select sc2 = new Select(sorter2);
		sorter2.click();
		sc2.selectByValue("price");
		Thread.sleep(1000);

		// ByVisibleText
		WebElement sorter3 = driver.findElement(By.id("sorter"));
		Select sc3 = new Select(sorter3);
		sorter3.click();
		sc3.selectByVisibleText("Position");
	}

	@Test(priority = 11)
	// To test that all the filter dropdown are working and can be selected
	public void shoppingOptionSelection() throws InterruptedException {
		driver.navigate().to("https://magento.softwaretestingboard.com/women/tops-women/tees-women.html");
		// Dropdown 1
		WebElement style = driver.findElement(By.xpath("//div[@class='filter-options']/div[1]"));
		style.click();
		Thread.sleep(1000);
		style.click();

		// Dropdown 2
		WebElement size = driver.findElement(By.xpath("//div[@class='filter-options']/div[2]"));
		size.click();
		// Thread.sleep(1000);
		// Click on size S
		driver.findElement(By.xpath("//div[@class='filter-options']/div[2]//a[2]/div")).click();

		// Dropdown 3
		WebElement climate = driver.findElement(By.xpath("//div[@class='filter-options']/div[2]"));
		climate.click();
		// Thread.sleep(1000);
		// Click on Warm option
		driver.findElement(By.xpath("//div[@class='filter-options']/div[2]//ol/li[2]/a")).click();

		// Dropdown 4
		WebElement colour = driver.findElement(By.xpath("//div[@class='filter-options']/div[2]"));
		colour.click();
		// Thread.sleep(1000);
		// Click on Purple option
		driver.findElement(By.xpath("//div[@class='filter-options']/div[2]//a[1]/div")).click();

		// Dropdown 5
		WebElement ecoCollection = driver.findElement(By.xpath("//div[@class='filter-options']/div[2]"));
		ecoCollection.click();
		Thread.sleep(1000);

		// Dropdown 6
		WebElement erinRecomends = driver.findElement(By.xpath("//div[@class='filter-options']/div[3]"));
		erinRecomends.click();
		Thread.sleep(1000);

		// Dropdown 7
		WebElement material = driver.findElement(By.xpath("//div[@class='filter-options']/div[4]"));
		material.click();
		// Thread.sleep(1000);
		// Click on Polyster option
		driver.findElement(By.xpath("//div[@class='filter-options']/div[4]//ol/li[4]/a")).click();

		// Dropdown 8
		WebElement newArrivals = driver.findElement(By.xpath("//div[@class='filter-options']/div[4]"));
		newArrivals.click();
		Thread.sleep(1000);

		// Dropdown 9
		WebElement pattern = driver.findElement(By.xpath("//div[@class='filter-options']/div[5]"));
		pattern.click();
		Thread.sleep(1000);

		// Dropdown 10
		WebElement fabric = driver.findElement(By.xpath("//div[@class='filter-options']/div[6]"));
		fabric.click();
		Thread.sleep(1000);

		// Dropdown 11
		WebElement price = driver.findElement(By.xpath("//div[@class='filter-options']/div[7]"));
		price.click();
		Thread.sleep(1000);
		// Click on $20.00 - $29.99 option
		driver.findElement(By.xpath("//div[@class='filter-options']/div[7]//ol/li[1]/a")).click();

		// Dropdown 12
		WebElement sale = driver.findElement(By.xpath("//div[@class='filter-options']/div[7]"));
		sale.click();
		Thread.sleep(1000);
	}

	@Test(priority = 12)
	// To test that user is able to view all the images properly
	public void viewImage() throws InterruptedException {
		// Click on the product
		WebElement productClick = driver
				.findElement(By.xpath("//div[@class='column main']//ol/li[1]//descendant::div[2]/strong/a"));
		productClick.click();
		Thread.sleep(1000);

		// click on product image and view all product images
		WebElement image = driver.findElement(By.xpath("//div[@class='gallery-placeholder']/descendant::img[1]"));
		image.click();

		WebElement forwardBtn = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div[1]/div[4]/div"));
		WebElement backwardBtn = driver.findElement(By.xpath("/html/body/div[5]/div[2]/div[1]/div[2]/div"));
		act = new Actions(driver);
		for (int i = 0; i < 2; i++) {
			act.moveToElement(forwardBtn).perform();
			forwardBtn.click();
			Thread.sleep(1000);
		}
		for (int i = 0; i < 2; i++) {
			act.moveToElement(backwardBtn).perform();
			backwardBtn.click();
			Thread.sleep(1000);
		}

		// zoom in and zoom out product
		WebElement zoomIn = driver.findElement(By.xpath("//body/div[5]/div[2]/div[1]/div[6]"));
		zoomIn.click();
		Thread.sleep(1000);
		WebElement zoomOut = driver.findElement(By.xpath("//body/div[5]/div[2]/div[1]/div[7]"));
		zoomOut.click();
		Thread.sleep(1000);

		WebElement cancel = driver.findElement(By.xpath("//body/div[5]/div[2]/div[1]/div[1]"));
		cancel.click();
	}

	@Test(priority = 13)
	// To test that item can be added to wishlist or Shared via email
	public void addWishlist() throws InterruptedException {
		// to Add item in wishlist
		WebElement heart = driver.findElement(By.xpath("//div[@class='column main']/div[1]//descendant::a[3]"));
		heart.click();
		if (driver.getTitle().equals("My Wish List")) {
			System.out.println("Added to wishlist. TestCase Pass");
		}
		// Share Wishlist
		WebElement shareBtn = driver.findElement(By.name("save_and_share"));
		shareBtn.click();

		// Enter emails in two different ways
		WebElement emailsTextArea = driver.findElement(By.name("emails"));
		try {
			String ems[] = { "niklausmikealson@gmail.com", "elijhamikealson@gmail.com", "rebekkhamikealson@gmail.com" };
			String ems2[] = { "niklausmikealson@gmail.com,", "elijhamikealson@gmail.com,",
					"rebekkhamikealson@gmail.com" };
			List<String[]> emails = new ArrayList<>();
			emails.add(ems);
			emails.add(ems2);
			int j = 0;
			while (j < 2) {
				emailsTextArea.clear();
				emailsTextArea.sendKeys(emails.get(j));
				Thread.sleep(1000);

				// Write a essage while sharing
				WebElement msg = driver.findElement(By.id("message"));
				msg.sendKeys("This the Tee i have added to wishlist, i hope you all like it!!");
				WebElement shareBtn1 = driver.findElement(By.xpath("//div[@class='column main']//button"));
				shareBtn1.click();
				Thread.sleep(1000);

				// Asks to enter valid email if not added as instructed or,
				// Displays a message if item is already shared
				try {
					WebElement emailErrorText = driver
							.findElement(By.xpath("//fieldset[@class='fieldset']/descendant::div[3]"));
					WebElement moreThan1 = driver.findElement(By.xpath("//main[@id='maincontent']/descendant::div[6]"));
					if (emailErrorText.isDisplayed()) {
						String errorTxt = emailErrorText.getText();
						System.out.println("Test Case Fail. " + errorTxt);
					} else if (moreThan1.isDisplayed()) {
						String errorTxt = moreThan1.getText();
						System.out.println("Test Case Fail. " + errorTxt);
					}
				} catch (NoSuchElementException e) {
					System.out.println(e);
				}
				j++;
			}

			// Displays a message if Shared successfully
			try {
				WebElement shredmsg = driver.findElement(By.xpath("//div[@class='page messages']//descendant::div[5]"));
				if (shredmsg.isDisplayed()) {
					String successmsg = shredmsg.getText();
					System.out.println("Test Case pass. " + successmsg);
					driver.navigate().back();
					driver.navigate().back();
				}
			} catch (NoSuchElementException e) {
				System.out.println(e);
			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e);
		}
	}

	@Test(priority = 13)
	// To test that item can be added to wishlist or Shared via email
	public void addWishlist1() throws InterruptedException {
		// to Add item in wishlist
		WebElement heart = driver.findElement(By.xpath("//div[@class='column main']/div[1]//descendant::a[3]"));
		heart.click();
		if (driver.getTitle().equals("My Wish List")) {
			System.out.println("Added to wishlist. TestCase Pass");
		}
		// Share Wishlist
		WebElement shareBtn = driver.findElement(By.name("save_and_share"));
		shareBtn.click();

		// Enter emails in two different ways
		WebElement emailsTextArea = driver.findElement(By.name("emails"));
		try {
			String ems[] = { "niklausmikealson@gmail.com", "elijhamikealson@gmail.com", "rebekkhamikealson@gmail.com" };
			String ems2[] = { "niklausmikealson@gmail.com,", "elijhamikealson@gmail.com,",
					"rebekkhamikealson@gmail.com" };
			List<String[]> emails = new ArrayList<>();
			emails.add(ems);
			emails.add(ems2);
			int j = 0;
			while (j < 2) {
				emailsTextArea.clear();
				emailsTextArea.sendKeys(emails.get(j));
				Thread.sleep(1000);

				// Write a essage while sharing
				WebElement msg = driver.findElement(By.id("message"));
				msg.sendKeys("This the Tee i have added to wishlist, i hope you all like it!!");
				WebElement shareBtn1 = driver.findElement(By.xpath("//div[@class='column main']//button"));
				shareBtn1.click();
				Thread.sleep(1000);

				// Asks to enter valid email if not added as instructed or,
				// Displays a message if item is already shared
				try {
					WebElement emailErrorText = driver
							.findElement(By.xpath("//fieldset[@class='fieldset']/descendant::div[3]"));
					WebElement moreThan1 = driver.findElement(By.xpath("//main[@id='maincontent']/descendant::div[6]"));
					if (emailErrorText.isDisplayed()) {
						String errorTxt = emailErrorText.getText();
						System.out.println("Test Case Fail. " + errorTxt);
					} else if (moreThan1.isDisplayed()) {
						String errorTxt = moreThan1.getText();
						System.out.println("Test Case Fail. " + errorTxt);
					}
				} catch (NoSuchElementException e) {
					System.out.println(e);
				}
				j++;
			}

			// Displays a message if Shared successfully
			try {
				WebElement shredmsg = driver.findElement(By.xpath("//div[@class='page messages']//descendant::div[5]"));
				if (shredmsg.isDisplayed()) {
					String successmsg = shredmsg.getText();
					System.out.println("Test Case pass. " + successmsg);
					driver.navigate().back();
					driver.navigate().back();
				}
			} catch (NoSuchElementException e) {
				System.out.println(e);
			}
		} catch (StaleElementReferenceException e) {
			System.out.println(e);
		}
	}
	
	@Test(priority = 14)
	// To test if user can edit and delete item from wishlist
	public void wishlistEditDelete() throws InterruptedException {
		// to test edit option is enabled and working fine
		js = (JavascriptExecutor) driver;
		WebElement onItem = driver.findElement(By.xpath("//form[@id='wishlist-view-form']//ol/li"));
		act = new Actions(driver);
		act.moveToElement(onItem);
		Thread.sleep(1000);
		WebElement edit = driver.findElement(By.xpath(
				"//form[@id='wishlist-view-form']//ol/li/descendant::div[2]/following-sibling::div[2]//descendant::a[1]"));
		js.executeScript("arguments[0].scrollIntoView();", edit);
		Thread.sleep(1000);
		js.executeScript("arguments[0].click();", edit);
		Thread.sleep(1000);

		// to Update the wishlist
		try {
			WebElement heart = driver.findElement(By.xpath("//div[@class='column main']/div[1]//descendant::a[3]"));
			heart.click();
			Thread.sleep(1000);
		} catch (StaleElementReferenceException e) {
			System.out.println(e);
		}

		// to Remove from wishlist
		WebElement delete = driver.findElement(By.xpath(
				"//form[@id='wishlist-view-form']//ol/li/descendant::div[2]/following-sibling::div[2]//descendant::a[2]"));
		act.moveToElement(onItem);
		Thread.sleep(1000);
		js.executeScript("arguments[0].scrollIntoView();", delete);
		Thread.sleep(1000);
		js.executeScript("arguments[0].click();", delete);
		Thread.sleep(1000);

		WebElement deletedMsg = driver.findElement(By.xpath("//div[@class='page messages']//descendant::div[5]"));
		if (deletedMsg.isDisplayed()) {
			String msg = deletedMsg.getText();
			System.out.println("Test Case Pass " + msg);
			driver.navigate().back();
			driver.navigate().back();
		}
	}
	
	@Test(priority = 15)
	// Add the product to compare
	public void addToCompare() throws InterruptedException {
		WebElement addCompare = driver.findElement(By.xpath("//div[@class='column main']/div[1]//descendant::a[4]"));
		addCompare.click();
		Thread.sleep(1000);
		WebElement addedMsg = driver.findElement(By.xpath("//main[@id='maincontent']/descendant::div[6]"));
		if (addedMsg.isDisplayed()) {
			System.out.println("Test Case Pass. " + addedMsg.getText());
		}

		// Add another item from recomended items to compare
		WebElement anotherItem = driver.findElement(By.xpath("//div[@class='column main']//ol/li[2]"));
		anotherItem.click();
		addCompare = driver.findElement(By.xpath("//div[@class='column main']/div[1]//descendant::a[4]"));
		addCompare.click();
		addedMsg = driver.findElement(By.xpath("//main[@id='maincontent']/descendant::div[6]"));
		if (addedMsg.isDisplayed()) {
			System.out.println("Test Case Pass. " + addedMsg.getText());
		}

		WebElement comparePageLink = driver.findElement(By.xpath("//ul[@class='compare wrapper']/li/a"));
		comparePageLink.click();
		// Veiew details of both products
		List<WebElement> tableHead = driver.findElements(By.xpath("//div[@class='column main']//table/tbody[2]/tr/th"));
		List<WebElement> tData1 = driver.findElements(By.xpath("//div[@class='column main']//table/tbody[2]/tr[1]/td"));
		List<WebElement> tData2 = driver.findElements(By.xpath("//div[@class='column main']//table/tbody[2]/tr[2]/td"));

		for (int i = 0; i < tableHead.size(); i++) {
			String th = tableHead.get(i).getText();
			if (i == 0) {
				System.out.println(th);
				for (int j = 0; j < tData1.size(); j++) {
					String td = tData1.get(i).getText();
					System.out.print(td + "\t");
				}
				System.out.println();
			} else {
				System.out.println(th);
				for (int j = 0; j < tData1.size(); j++) {
					String td = tData2.get(i).getText();
					System.out.println(td);
				}
			}

		}
		System.out.println();
		// Remove a product from compare list
		WebElement remove = driver.findElement(By.xpath("//div[@class='column main']//table/thead//td[1]/a"));
		remove.click();
		Thread.sleep(1000);
		WebElement alert = driver.findElement(By.xpath("//footer[@class='modal-footer']//button[2]"));
		alert.click();
		WebElement removedMsg = driver.findElement(By.xpath("//div[@class='page messages']//descendant::div[5]"));
		if (removedMsg.isDisplayed()) {
			System.out.println("Test Case Pass. " + removedMsg.getText());
		}
		driver.navigate().back();
		driver.navigate().back();
		driver.navigate().back();
		driver.navigate().back();
	}
	
//	@Test(priority = 16)
//	// To test that compared product details can be printed or not
//	public void printCompare() throws InterruptedException, TimeoutException {
//		WebElement printLink = driver.findElement(By.xpath("//div[@class='column main']/a"));
//		 String originalWindow = driver.getWindowHandle();
//		try {
//			printLink.click();
//			// Wait for the new window to open
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//			wait.until(ExpectedConditions.numberOfWindowsToBe(2));
//
//			// Switch to the new window
//			driver.switchTo().window(driver.getWindowHandles().stream().skip(0).findFirst().get());
//			driver.switchTo().frame(1);
//			driver.switchTo().activeElement();
//			List<WebElement> dropDowns = driver.findElements(By.xpath("//div[@id='container']/select"));
//					for (int i = 0; i < dropDowns.size(); i++) {
//						WebElement dp = dropDowns.get(i);
//						if (dp.isEnabled()) {
//							System.out.println("Dropdown is working");
//							dp.click();
//							sc = new Select(dp);
//							switch (i) {
//							case 0:
//								sc.selectByIndex(2);
//								Thread.sleep(1000);
//								break;
//							case 1:
//								sc.selectByVisibleText("Landscape");
//								Thread.sleep(1000);
//								break;
//							case 2:
//								sc.selectByVisibleText("Color");
//								Thread.sleep(1000);
//								break;
//							case 3:
//								sc.selectByIndex(6);
//								Thread.sleep(1000);
//								break;
//							case 4:
//								sc.selectByValue("2");
//								Thread.sleep(1000);
//								break;
//							case 5:
//								sc.selectByVisibleText("Minimum");
//								Thread.sleep(1000);
//								break;
//							case 6:
//								sc.selectByValue("1");
//								Thread.sleep(1000);
//								break;
//							}
//						} else {
//							System.out.println("Dropdown is not working");
//						}
//						WebElement bg = driver.findElement(By.id("cssBackground"));
//						bg.click();
//						WebElement cancelBtn = driver.findElement(By.xpath("//div[@class='controls']/cr-button[2]"));
//						cancelBtn.click();
//						driver.switchTo().window(originalWindow);
//					}
//		} catch (UnreachableBrowserException e) {
//			System.out.println(e.getMessage());
//		}
//	}
	
	@Test(priority = 17)
	// To apply required filter to the selected filter
	public void applyFilter() throws InterruptedException {
		// Selecting Size
		List<WebElement> sizes = driver
				.findElements(By.xpath("//div[@class='product-info-main']//form/descendant::div[5]//div"));
		for (int i = 0; i < sizes.size(); i++) {
			sizes.get(i).click();
			Thread.sleep(1000);
		}

		// Selecting Colour
		List<WebElement> colours = driver
				.findElements(By.xpath("//div[@class='product-info-main']//form/descendant::div[3]/div[2]/div[1]/div"));
		for (int i = 0; i < colours.size(); i++) {
			colours.get(i).click();
			Thread.sleep(1000);
		}
	}
	
	@Test(priority = 18)
	// To test that user is able to view the product details and all the Reviews
	public void readReviews() throws InterruptedException {
		// View Details
		WebElement detailsLink = driver.findElement(By.linkText("Details"));
		detailsLink.click();
		Thread.sleep(1000);

		System.out.println("----------Details----------");
		List<WebElement> details = driver
				.findElements(By.xpath("//div[@class='product data items']/descendant::div[4]/p"));
		System.out.println(details.size());
		for (int i = 0; i < details.size(); i++) {
			String det = details.get(i).getText();
			System.out.println(det);
		}

		// View More details
		WebElement moreInfo = driver.findElement(By.xpath("//div[@class='product data items']/descendant::div[5]"));
		moreInfo.click();
		Thread.sleep(1000);
		List<WebElement> tableHeader = driver.findElements(By.xpath("//div[@id='additional']//table/tbody/tr/th"));
		List<WebElement> tableData = driver.findElements(By.xpath("//div[@id='additional']//table/tbody/tr/td"));
		System.out.println("----------More Information----------");
		for (int i = 0; i < tableHeader.size(); i++) {
			String th = tableHeader.get(i).getText();
			String td = tableData.get(i).getText();
			System.out.println(th + " | " + td);
		}

		// View Reviews given by customers
		WebElement reviews = driver.findElement(By.xpath("//div[@class='product data items']/descendant::div[8]"));
		reviews.click();
		Thread.sleep(3000);
		List<WebElement> totalReviews = driver
				.findElements(By.xpath("//div[@class='column main']/descendant::ol[1]/li"));
		System.out.println("Total Reviews given are::" + totalReviews.size());

		List<WebElement> reviews1 = driver
				.findElements(By.xpath("//div[@class='column main']/descendant::ol[1]/li/div"));
		for (int i = 0; i < reviews1.size(); i++) {
			System.out.println("----------Review" + i + "----------");
			String rev = reviews1.get(i).getText();
			System.out.println(rev);
		}
		js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0)", "");
	}
	
	@Test(priority = 19)
	// To Add item to Cart
	public void addItemtoCart() throws NoSuchElementException {
		// To change quantity of product
		WebElement size = driver
				.findElement(By.xpath("//div[@class='product-info-main']//form/descendant::div[5]//div[2]"));
		size.click();
		WebElement colour = driver.findElement(
				By.xpath("//div[@class='product-info-main']//form/descendant::div[3]/div[2]/div[1]/div[2]"));
		colour.click();
		WebElement qty = driver.findElement(By.name("qty"));
		WebElement addToCart = driver.findElement(By.xpath("//div[@class='fieldset']//descendant::button[1]"));
		String num[] = { "0", "100001", "3" };
		try {
			for (int i = 0; i < 3; i++) {
				qty.clear();
				qty.sendKeys(num[i]);
				Thread.sleep(1000);
				addToCart.click();
				Thread.sleep(2000);
				Thread.sleep(2000);
				WebElement qtyErrorMsg = driver.findElement(By.xpath("//input[@name='qty']/following-sibling::div"));
				if (qtyErrorMsg.isDisplayed()) {
					System.out.println("Test Case Fail. " + qtyErrorMsg.getText());
				}
			}
			//sometimes add to cart button is clicked but not added can throw exception			
		} catch (Exception e) {
			System.out.println(e);
		}
		WebElement itemAddedMsg = driver.findElement(By.xpath("//div[@class='page messages']//descendant::div[5]"));
		if (itemAddedMsg.isDisplayed()) {
			System.out.println("Test Case Pass. " + itemAddedMsg.getText());
		}
	}
	
	@Test(priority = 20)
	public void searchItem() throws InterruptedException {
		// Search for an item from search Text Box
		WebElement searchTab = driver.findElement(By.id("search"));
		act = new Actions(driver);
		act.sendKeys(searchTab, "shorts");
		Thread.sleep(4000);
		act.keyDown(Keys.ARROW_DOWN).perform();
		Thread.sleep(1500);
		act.perform();
		WebElement optionsFromSearch = driver.findElement(By.xpath("//div[@class='control']//ul/li[3]"));
		optionsFromSearch.click();

		// View Related Searches
		WebElement relatedSearchLink = driver.findElement(By.xpath("//dl[@class='block']/dd[1]/a"));
		relatedSearchLink.click();
		driver.navigate().back();

		// Select The items
		WebElement selectItem1 = driver.findElement(By.xpath("//div[@class='search results']//ol/li[3]"));
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", selectItem1);
		selectItem1.click();
		WebElement size = driver
				.findElement(By.xpath("//div[@class='product-info-main']//form/descendant::div[5]//div[2]"));
		size.click();
		WebElement colour = driver.findElement(
				By.xpath("//div[@class='product-info-main']//form/descendant::div[3]/div[2]/div[1]/div[2]"));
		colour.click();

		// Add an item to cart
		WebElement addToCart = driver.findElement(By.xpath("//div[@class='fieldset']//descendant::button[1]"));
		addToCart.click();
		Thread.sleep(3000);
		driver.navigate().back();

		// Add another item tomcart
		WebElement selectItem2 = driver.findElement(By.xpath("//div[@class='search results']//ol/li[4]"));
		selectItem2.click();
		size = driver.findElement(By.xpath("//div[@class='product-info-main']//form/descendant::div[5]//div[2]"));
		size.click();
		colour = driver.findElement(
				By.xpath("//div[@class='product-info-main']//form/descendant::div[3]/div[2]/div[1]/div[2]"));
		colour.click();
		addToCart = driver.findElement(By.xpath("//div[@class='fieldset']//descendant::button[1]"));
		addToCart.click();
		Thread.sleep(3000);
		driver.navigate().back();

		// Add another item tomcart
		WebElement selectItem3 = driver.findElement(By.xpath("//div[@class='search results']//ol/li[2]"));
		selectItem3.click();
		size = driver.findElement(By.xpath("//div[@class='product-info-main']//form/descendant::div[5]//div[3]"));
		size.click();
		colour = driver.findElement(
				By.xpath("//div[@class='product-info-main']//form/descendant::div[3]/div[2]/div[1]/div[3]"));
		colour.click();
		addToCart = driver.findElement(By.xpath("//div[@class='fieldset']//descendant::button[1]"));
		addToCart.click();
		Thread.sleep(3000);
	}
	
	@Test(priority = 21)
	// To make changes in Cart
	public void changesInCart() throws InterruptedException {
		// Click on Cart to view items in cart
		WebElement cart = driver.findElement(By.xpath("//div[@class='header content']/div[1]/a"));
		cart.click();
		Thread.sleep(3000);

		// Check that proceed to buy navigates to appropriate webpage
		WebElement proceedToBuy = driver.findElement(By.xpath("//body/descendant::div[10]/descendant::div[7]//button"));
		proceedToBuy.click();
		driver.navigate().back();

		cart = driver.findElement(By.xpath("//div[@class='header content']/div[1]/a"));
		cart.click();

		// Delete an item from cart
		System.out.println("Before deleting an item"
				+ driver.findElement(By.xpath("//span[@class='counter qty']/span[1]")).getText());
		WebElement deleteFromCart = driver
				.findElement(By.xpath("//body//descendant::div[20]/ol/li[1]/descendant::a[4]"));
		deleteFromCart.click();
		Thread.sleep(2000);

		// accept alert on deleting item from cart
		WebElement acceptDeleteAlert = driver.findElement(By.xpath("//body//descendant::footer[2]/button[2]"));
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", acceptDeleteAlert);
		Thread.sleep(3000);
		System.out.println("After deleting an item"
				+ driver.findElement(By.xpath("//span[@class='counter qty']/span[1]")).getText());

		// Update quantity of item
		System.out.println("Total Amount before updating quantity::"
				+ driver.findElement(By.xpath("//div[@class='subtotal']//descendant::span[4]")).getText());
		WebElement editQuantity = driver.findElement(By.xpath("//body//descendant::div[20]/ol/li[1]//input"));
		js = ((JavascriptExecutor) driver);
		js.executeScript("arguments[0].value = '';", editQuantity);
		editQuantity.sendKeys("2");
		WebElement UpdateBtn = driver.findElement(By.xpath("//body//descendant::div[20]/ol/li[1]//button"));
		UpdateBtn.click();
		Thread.sleep(2000);
		System.out.println("Total Amount After updating quantity::"
				+ driver.findElement(By.xpath("//div[@class='subtotal']//descendant::span[4]")).getText());

		// Test Edit button
		WebElement editBtn = driver.findElement(By.xpath("//body//descendant::div[20]/ol/li[1]//descendant::div[9]/a"));
		editBtn.click();

		// Change colour and size and click on Update Cart
		WebElement size = driver
				.findElement(By.xpath("//div[@class='product-info-main']//form/descendant::div[5]//div[2]"));
		size.click();
		WebElement colour = driver.findElement(
				By.xpath("//div[@class='product-info-main']//form/descendant::div[3]/div[2]/div[1]/div[2]"));
		colour.click();
		WebElement updateCartBtn = driver.findElement(By.xpath("//fieldset[@class='fieldset']//button"));
		updateCartBtn.click();

		// Check view and edit link navigates to Shopping Cart
		WebElement viewvAndEditCart = driver.findElement(By.xpath("//body/descendant::div[11]/descendant::div[41]/a"));
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", viewvAndEditCart);
		Thread.sleep(1500);

	}
	
	@Test(priority = 22)
	// Test view and Update Cart page
	public void shoppingCart() throws InterruptedException {
		// Move an item from Shopping cart to wishlist
		WebElement moveToWishlist = driver
				.findElement(By.xpath("//div[@class='column main']//descendant::table[2]/tbody[1]//descendant::a[3]"));
		moveToWishlist.click();
		Thread.sleep(1000);
		WebElement movedMsg = driver.findElement(By.xpath("//div[@class='page messages']//descendant::div[5]"));
		if (movedMsg.isDisplayed()) {
			System.out.println(movedMsg.getText());
		}

		// check if Edit updates the cart
		WebElement edit = driver
				.findElement(By.xpath("//div[@class='column main']//descendant::table[2]/tbody[1]//descendant::a[4]"));
		edit.click();
		WebElement size = driver
				.findElement(By.xpath("//div[@class='product-info-main']//form/descendant::div[5]//div[1]"));
		size.click();
		Thread.sleep(1000);
		Thread.sleep(1000);
		WebElement updateCartBtn = driver.findElement(By.xpath("//fieldset[@class='fieldset']//button"));
		updateCartBtn.click();
		Thread.sleep(1000);

		// Apply Discount code
		WebElement Discount = driver.findElement(By.xpath("//div[@class='cart-discount']/div/div[1]"));
		Discount.click();
		Thread.sleep(1000);
		WebElement code = driver.findElement(By.xpath("//div[@class='cart-discount']/div//form//descendant::input[2]"));
		code.sendKeys("LUMA123");
		Thread.sleep(1000);
		WebElement applyBtn = driver
				.findElement(By.xpath("//div[@class='cart-discount']/div//form//descendant::button"));
		applyBtn.click();
		Thread.sleep(1000);
		WebElement codeMsg = driver.findElement(By.xpath("//div[@class='page messages']//descendant::div[5]"));
		if (codeMsg.isDisplayed()) {
			System.out.println(codeMsg.getText());
		}

		// Estimate Shipping Tax dropdown
		WebElement estimateDrpdwn = driver.findElement(By.xpath("//div[@id='block-shipping']/div[1]"));
		estimateDrpdwn.click();
		Thread.sleep(1000);

		WebElement countrydrpDwn = driver.findElement(By.xpath("//body//fieldset[1]//select[1]"));
		sc = new Select(countrydrpDwn);
		sc.selectByVisibleText("India");
		Thread.sleep(1000);

		WebElement statedrpDwn = driver.findElement(By.xpath("//body//fieldset[1]//descendant::select[2]"));
		sc = new Select(statedrpDwn);
		sc.selectByVisibleText("Karnataka");
		Thread.sleep(1000);

		WebElement postalCode = driver.findElement(By.xpath("//body//fieldset[1]//descendant::input[2]"));
		postalCode.clear();
		postalCode.sendKeys("582103");
		Thread.sleep(1000);

		WebElement flatRate = driver.findElement(By.xpath("//body//descendant::fieldset[2]//input"));
		if (flatRate.isEnabled()) {
			flatRate.click();
		} else {
			System.out.println("Shipping Charges is Fixed");
		}

		// Sub Total
		List<WebElement> tableHeader = driver.findElements(By.xpath("//div[@id='cart-totals']//tbody/tr/th"));
		List<WebElement> tableData = driver.findElements(By.xpath("//div[@id='cart-totals']//tbody/tr/td"));
		System.out.println("----------Payment Information----------");
		for (int i = 0; i < tableHeader.size(); i++) {
			WebElement th = tableHeader.get(i);
			String header = th.getText();
			WebElement tr = tableData.get(i);
			String body = tr.getText();
			System.out.println(header + " | " + body);
		}

		estimateDrpdwn = driver.findElement(By.xpath("//div[@id='block-shipping']/div[1]"));
		estimateDrpdwn.click();
		WebElement multipleAddLink = driver
				.findElement(By.xpath("//div[@id='cart-totals']//following-sibling::ul/li[2]/a"));
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", multipleAddLink);
		Thread.sleep(1000);
	}
	
	@Test(priority = 23)
	// Test to add multiple address before proceeding to buy
	public void addAddress() throws InterruptedException {
		WebElement firstName = driver.findElement(By.id("firstname"));
		if (firstName.getText().isEmpty()) {
			firstName.clear();
			firstName.sendKeys("Stiles");
			Thread.sleep(1000);
		} else {
			System.out.println("First name is auto filled");
		}

		WebElement lastName = driver.findElement(By.id("lastname"));
		if (lastName.getText().isEmpty()) {
			lastName.clear();
			lastName.sendKeys("Stilinski");
			Thread.sleep(1000);
		} else {
			System.out.println("Last name is auto filled");
		}
		WebElement company = driver.findElement(By.id("company"));
		company.sendKeys("ExcelR");
		Thread.sleep(1000);

		WebElement phoneNo = driver.findElement(By.id("telephone"));
		phoneNo.sendKeys("9876543210");
		Thread.sleep(1000);

		WebElement streetAdd_1 = driver.findElement(By.id("street_1"));
		streetAdd_1.sendKeys("Near Trikuteshwar Temple Road");
		Thread.sleep(1000);

		WebElement streetAdd_2 = driver.findElement(By.id("street_2"));
		streetAdd_2.sendKeys("Killa");
		Thread.sleep(1000);

		WebElement city = driver.findElement(By.name("city"));
		city.sendKeys("Gadag");
		Thread.sleep(1000);

		WebElement country = driver.findElement(By.id("country"));
		sc = new Select(country);
		sc.selectByVisibleText("India");
		Thread.sleep(1000);

		WebElement state = driver.findElement(By.id("region_id"));
		sc = new Select(state);
		sc.selectByVisibleText("Karnataka");
		Thread.sleep(1000);

		WebElement postalCode = driver.findElement(By.name("postcode"));
		js.executeScript("arguments[0].value='582103';", postalCode);
		Thread.sleep(1000);

		WebElement saveAdd = driver.findElement(By.xpath("//div[@class='column main']//button"));
		saveAdd.click();
		Thread.sleep(1000);
	}
	
	@Test(priority = 24)
	// ToTest Ship to multiple address page
	public void updateAddress() throws InterruptedException {
		WebElement enterNewAddBtn = driver.findElement(By.xpath("//body//descendant::button[3]"));
		js.executeScript("arguments[0].click();", enterNewAddBtn);
		// add another address
		WebElement fn = driver.findElement(By.id("firstname"));
		fn.clear();
		fn.sendKeys("Allison");

		WebElement ln = driver.findElement(By.id("lastname"));
		ln.clear();
		ln.sendKeys("Argent");

		driver.findElement(By.id("company")).sendKeys("Teen wolf");
		driver.findElement(By.id("telephone")).sendKeys("2345678910");
		driver.findElement(By.id("street_1")).sendKeys("Beacon Hills Highschool");
		driver.findElement(By.id("street_2")).sendKeys("In the woods");
		driver.findElement(By.name("city")).sendKeys("Beacon Hills");
		driver.findElement(By.id("country")).sendKeys("United States");
		WebElement state = driver.findElement(By.id("region_id"));
		sc = new Select(state);
		sc.selectByVisibleText("California");
		WebElement postalCode = driver.findElement(By.name("postcode"));
		js.executeScript("arguments[0].value='02108';", postalCode);
		driver.findElement(By.xpath("//div[@class='column main']//button")).click();

		WebElement removeItem = driver.findElement(By.xpath("//table//tbody/tr[1]/td[4]/a"));
		removeItem.click();
		Thread.sleep(1000);

		// changeQuantity
		WebElement qty = driver.findElement(By.xpath("//table//tbody/tr[1]/td[2]//input"));
		qty.clear();
		qty.sendKeys("3");
		Thread.sleep(1000);

		// Change Address
		WebElement otherAdd = driver.findElement(By.xpath("//table//tbody/tr[1]/td[3]//select"));
		otherAdd.click();
		sc = new Select(otherAdd);
		sc.selectByIndex(1);
		Thread.sleep(1000);

		// Click on Update Qty and Address
		WebElement updateQtyAddBtn = driver.findElement(By.xpath("//body//descendant::button[2]"));
		updateQtyAddBtn.click();
		Thread.sleep(1000);

		// goTo Shipping Info
		WebElement shippingInfoBtn = driver
				.findElement(By.xpath("//div[@class='column main']//form/descendant::div[16]/child::button"));
		shippingInfoBtn.click();
		Thread.sleep(1000);

		// goTo Billing Info
		WebElement billingInfoBtn = driver.findElement(By.xpath("//div[@class='column main']/descendant::button[1]"));
		billingInfoBtn.click();
		Thread.sleep(1000);

		driver.navigate().back();
		driver.navigate().back();
		Thread.sleep(1000);
		WebElement backToShoppingCart = driver
				.findElement(By.xpath("//body//descendant::button[2]//following-sibling::a"));
		backToShoppingCart.click();
		Thread.sleep(1000);

		WebElement proceedToCheckOut = driver
				.findElement(By.xpath("//div[@id='cart-totals']//following-sibling::ul/li[2]/a"));
		proceedToCheckOut.click();
	}
	
	@Test(priority = 25)
	// To Test shipping page
	public void shipping() throws InterruptedException {
		// select shipping address
		driver.navigate().to("https://magento.softwaretestingboard.com/checkout/#shipping");
		Thread.sleep(3000);
		WebElement shipHere = driver.findElement(By.xpath("//div[@class='field addresses']//descendant::button[3]"));
		js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", shipHere);
		Thread.sleep(2000);

		// view order summary
		WebElement orderSummaryDDrpdwn = driver.findElement(By.xpath("//div[@class='opc-block-summary']/div/div[1]"));
		js.executeScript("arguments[0].click();", orderSummaryDDrpdwn);
		Thread.sleep(2000);

		// view Details
		WebElement viewdetails = driver
				.findElement(By.xpath("//div[@class='opc-block-summary']/div/div[2]//ol/li/descendant::div[7]/span"));
		js.executeScript("arguments[0].click();", viewdetails);
		Thread.sleep(2000);

		// next
		WebElement next = driver.findElement(By.xpath("//ol//descendant::li[2]//button"));
		js.executeScript("arguments[0].click();", next);
		Thread.sleep(1000);
	}
	
	@Test(priority = 26)
	// To test review and pay page and place order
	public void reviewPay() throws InterruptedException {
		WebElement billingCheckbox = driver
				.findElement(By.xpath("//div[@class='payment-group']/descendant::div[4]/descendant::div[4]/input"));
		js.executeScript("arguments[0].click();", billingCheckbox);
		Thread.sleep(1000);

		WebElement placeOrderBtn = driver
				.findElement(By.xpath("//div[@class='payment-group']/descendant::div[4]/descendant::div[44]/button"));
		placeOrderBtn.click();
		Thread.sleep(2000);

		WebElement thankYouMsg = driver.findElement(By.xpath("//main[@id='maincontent']/div[1]/h1/span"));
		System.out.println(thankYouMsg.getText());

		WebElement orderNum = driver.findElement(By.xpath("//div[@class='checkout-success']/p[1]/a/strong"));
		System.out.println("Your Order number is: " + orderNum.getText());

		WebElement continueShopBtn = driver.findElement(By.xpath("//div[@class='checkout-success']/div//a"));
		continueShopBtn.click();

//		String originalWindow = driver.getWindowHandle();
//		//Printing the link
//		WebElement printLink = driver.findElement(By.xpath("//main[@id='maincontent']/descendant::div[1]/a"));
//		printLink.click();
//		try {
//		    // Wait for the new window to open
//		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//		    wait.until(ExpectedConditions.numberOfWindowsToBe(2));
//
//		    // Switch to the new window
//		    Set<String> allWindows = driver.getWindowHandles();
//		    String newWindow = null;
//		    for (String windowHandle : allWindows) {
//		        if (!windowHandle.equals(originalWindow)) {
//		            newWindow = windowHandle;
//		            break;
//		        }
//		    }
//		    driver.switchTo().window(newWindow);
//
//		    // Wait for the print dialog to load
//		    wait.until(ExpectedConditions.urlToBe("chrome://print/"));
//
//			//WebElement printSideBar=driver.findElement(By.id("container"));
//			driver.get("chrome://print/");
//			driver.switchTo().activeElement();
//			List<WebElement> dropDowns = driver.findElements(By.xpath("//div[@id='container']/select"));
//					for (int i = 0; i < dropDowns.size(); i++) {
//						WebElement dp = dropDowns.get(i);
//						if (dp.isEnabled()) {
//							System.out.println("Dropdown is working");
//							dp.click();
//							sc = new Select(dp);
//							switch (i) {
//							case 0:
//								sc.selectByIndex(2);
//								Thread.sleep(1000);
//								break;
//							case 1:
//								sc.selectByVisibleText("Landscape");
//								Thread.sleep(1000);
//								break;
//							case 2:
//								sc.selectByVisibleText("Color");
//								Thread.sleep(1000);
//								break;
//							case 3:
//								sc.selectByIndex(6);
//								Thread.sleep(1000);
//								break;
//							case 4:
//								sc.selectByValue("2");
//								Thread.sleep(1000);
//								break;
//							case 5:
//								sc.selectByVisibleText("Minimum");
//								Thread.sleep(1000);
//								break;
//							case 6:
//								sc.selectByValue("1");
//								Thread.sleep(1000);
//								break;
//							}
//						} else {
//							System.out.println("Dropdown is not working");
//						}
//						WebElement bg = driver.findElement(By.id("cssBackground"));
//						bg.click();
//						WebElement cancelBtn = driver.findElement(By.xpath("//div[@class='controls']/cr-button[2]"));
//						cancelBtn.click();
//					}
//		} catch (UnreachableBrowserException e) {
//			System.out.println(e.getMessage());
//		}
//		driver.switchTo().window(originalWindow);
	}
	
	@Test(priority = 27)
	// Test To view My orders
	public void viewOrders() {
		// go to my account to view the orders
		signOut = new LogOut(driver);
		signOut.clickWelcomeLink();
		// click on my account
		driver.findElement(By.linkText("My Account")).click();

		// click on my Orders
		driver.findElement(By.xpath("//ul[@class='nav items']/li[2]/a")).click();

		// print all the order details
		List<WebElement> tableHead = driver.findElements(By.xpath("//table/thead/tr/th"));
		for (int i = 0; i < tableHead.size(); i++) {
			String th = tableHead.get(i).getText();
			if (i == 2) {
				System.out.print(th + "\t" + "\t" + "\t" + "\t");
			} else if (i == 3) {
				System.out.print(th + "\t");
			} else {
				System.out.print(th + "\t" + "\t");
			}
		}
		System.out.println();
		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));
		for (WebElement row : rows) {
			List<WebElement> cols = driver.findElements(By.xpath("//table/tbody/tr/td"));
			String orderNumber = cols.get(0).getText();
			System.out.print(orderNumber + "\t");
			String date = cols.get(1).getText();
			System.out.print(date + "\t" + "\t");
			String shipTo = cols.get(2).getText();
			System.out.print(shipTo + "\t");
			WebElement ot = cols.get(3).findElement(By.className("price"));
			String orderTotal = ot.getText();
			System.out.print(orderTotal + "\t" + "\t");
			String status = cols.get(4).getText();
			System.out.print(status + "\t" + "\t");
			List<WebElement> actionLinks = cols.get(5).findElements(By.tagName("a"));
			for (WebElement actions : actionLinks) {
				WebElement at = actions.findElement(By.tagName("span"));
				String action = at.getText();
				System.out.print(action + " | ");
			}
			System.out.println();
		}
		WebElement logoImg = driver.findElement(By.xpath("//a[@class='logo']/img"));
		logoImg.click();
	}
	
	@Test(priority = 28)
	// To test that user is able to and add reviews properly
	public void writeReviews() throws InterruptedException {
		driver.navigate().to("https://magento.softwaretestingboard.com/desiree-fitness-tee.html");
		// Add Reviews
		WebElement addReviews = driver.findElement(By.xpath("//div[@class='reviews-actions']/a[2]"));
		addReviews.click();

		// provide Ratings
		js = (JavascriptExecutor) driver;
		List<WebElement> ratings = driver.findElements(By.xpath("//input[@type='radio']"));
		for (int i = 0; i < 3; i++) {
			WebElement star = ratings.get(i);
			js.executeScript("arguments[0].click();", star);
		}

		// provide details
		WebElement nickname = driver.findElement(By.name("nickname"));
		nickname.clear();
		nickname.sendKeys("Bon Bon");
		Thread.sleep(1000);

		WebElement summary = driver.findElement(By.xpath("//input[@id='summary_field']"));
		summary.sendKeys("Black Tee");
		Thread.sleep(1000);

		WebElement detail = driver.findElement(By.xpath("//textarea[@id='review_field']"));
		detail.sendKeys("I bought S size it fits perfectly. But, the colour is litle bit fade.");
		Thread.sleep(1000);

		WebElement submitReview = driver
				.findElement(By.xpath("//main[@id='maincontent']//descendant::form[2]//button"));
		submitReview.click();
		Thread.sleep(1000);

		WebElement logoImg = driver.findElement(By.xpath("//a[@class='logo']/img"));
		logoImg.click();
		driver.quit();
	}
}
