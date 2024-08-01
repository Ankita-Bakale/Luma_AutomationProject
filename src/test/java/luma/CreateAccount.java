package luma;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CreateAccount {
	WebDriver driver;
	public CreateAccount(WebDriver idriver) {
		driver=idriver;
		PageFactory.initElements(idriver, this);
	}
	
	//Repositories
	@FindBy(linkText="Create an Account") WebElement signUpLink;
	@FindBy(id="firstname") WebElement first_name;
	@FindBy(id="lastname") WebElement last_name;
	@FindBy(id="email_address") WebElement email;
	@FindBy(id="password") WebElement password;
	@FindBy(id="password-confirmation") WebElement confirm_password;
	@FindBy(xpath="//form[@id='form-validate']/child::div/descendant::button") WebElement createAccBtn;
	
	public void clickSignUpLink() {
		signUpLink.click();
	}
	public void EnterFirstName(String fn) {
		first_name.sendKeys(fn);
	}
	public void EnterLastName(String ln) {
		last_name.sendKeys(ln);
	}
	public void EnterEmail(String mail) {
		email.sendKeys(mail);
	}
	public void EnterPassword(String pass) {
		password.sendKeys(pass);
	}
	public void EnterConfirmPassword(String cPass) {
		confirm_password.sendKeys(cPass);
	}
	public void clickCreateAccBtn() {
		createAccBtn.click();
	}
}
