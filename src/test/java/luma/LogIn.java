package luma;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogIn {
	WebDriver driver;

	public LogIn(WebDriver idriver) {
		driver = idriver;
		PageFactory.initElements(idriver, this);
	}

	// Repositories
	@FindBy(linkText="Sign In") WebElement signInLink; 
	@FindBy(id = "email")
	WebElement email;
	@FindBy(id = "pass")
	WebElement password;
	@FindBy(id = "send2")
	WebElement signInBtn;

	
	public void clickSignInLink() {
		signInLink.click();
	}
	public void enterEmail(String mail) {
		email.sendKeys(mail);
	}

	public void enterPassword(String pass) {
		password.sendKeys(pass);
	}

	public void clickSignInBtn() {
		signInBtn.click();
	}

}
