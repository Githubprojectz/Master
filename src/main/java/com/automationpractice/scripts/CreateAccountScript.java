package com.automationpractice.scripts;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.automationpractice.modules.CreateAccountPageModule;
import com.automationpractice.modules.HomePageModule;
import com.automationpractice.modules.LoginPageModule;
import com.automationpractice.modules.MyAccountPageModule;
import com.automationpractice.utility.ConfigProperties;
import com.automationpractice.utility.ExcelUtility;
import com.automationpractice.utility.GlobalVariable;
import com.automationpractice.utility.ReportManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class CreateAccountScript {
	private Logger log = Logger.getLogger(CreateAccountScript.class.getName());
	private WebDriver driver;
	private HomePageModule homePageModule;
	private LoginPageModule loginPageModule;
	private CreateAccountPageModule createAccountPageModule;
	private MyAccountPageModule myAccountPageModule;
	private String basePath = GlobalVariable.basePath;

	public CreateAccountScript(WebDriver driver) {
		this.driver = driver;
		homePageModule = new HomePageModule(driver);
		loginPageModule = new LoginPageModule(driver);
		createAccountPageModule = new CreateAccountPageModule(driver);
		myAccountPageModule	= new MyAccountPageModule(driver);
	}

	public void createAnAccount(String testCaseName) {
		ExtentTest test = ReportManager.getTest();
		try {
			String excelSheetPath = basePath+ConfigProperties.getProperty("testDataPath")
			+ConfigProperties.getProperty("excelSheetName");
			Map<String, String> testDataMap = ExcelUtility.getData(testCaseName, excelSheetPath, 
					ConfigProperties.getProperty("testDataSheetName"));

			homePageModule.navigateToLoginPage();
			test.log(Status.INFO, "Navigated to login page.");
			String email = loginPageModule.enterEmailAddressToCreateAccount();
			log.info("Account will be created with email ID: "+email);
			test.log(Status.INFO, "Account will be created with email ID: "+email);
			createAccountPageModule.createAccount(testDataMap);	
			String pageHeading = myAccountPageModule.userNavigatedToMyAccount();
			log.info("User is in "+pageHeading+" page");
			Assert.assertEquals(pageHeading, "MY ACCOUNT", "Page Heading did not match ");
			test.log(Status.INFO, "Account was created.");
			myAccountPageModule.logOutOfApplication();
			test.log(Status.INFO, "Logged out of Application");
		} catch(Exception exp) {
			log.error(exp.getMessage(), exp);
			Assert.fail(testCaseName);
		}
	}
}
