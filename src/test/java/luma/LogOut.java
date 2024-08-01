package luma;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogOut {
	WebDriver driver;
	public LogOut(WebDriver idriver) {
		driver=idriver;
		PageFactory.initElements(idriver, this);
	}
	
	//Repositories
	@FindBy(xpath="//div[@class='panel header']/ul/child::li[2]/span/button") WebElement welcomeDrpdwn;
	@FindBy(linkText="Sign Out") WebElement signOut;
	
	public void clickWelcomeLink() {
		welcomeDrpdwn.click();
	}
	public void clickSignOut() {
		signOut.click();
	}
}
