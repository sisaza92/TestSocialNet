package com.accenture.automatization;

import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.accenture.automatization.dto.Credential;
import com.accenture.automatization.util.DataDrivenUser;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class TestPinterest {

	private static final String SUCCESS_MESSAGE = "Success";
	private static final String FAIL_MESSAGE = "Fail";
	
	public final String APPIUM_URL_SERVICE = "http://127.0.0.1:4723/wd/hub";

	public final String NAME_PACKAGE = "com.pinterest";
	public final String NAME_ACTIVITY = "activity.PinterestActivity";

	public final String DEVICE_UUID = "TA06808SD5";
	public final String DEVICE_NAME = "Name";
	public final String PLATFORM_VERSION = "6.0";
	public final String PLATFORM_NAME = "Android";
	public final String APP_ACTIVITY = "appActivity";

	public final String CAPABILITY_DEVICE_UUID = "udid";
	public final String CAPABILITY_DEVICE_NAME = "deviceName";
	public final String CAPABILITY_PLATFORM_VERSION = "platformVersion";
	public final String CAPABILITY_PLATFORM_NAME = "platformName";
	public final String CAPABILITY_APP_PACKAGE = "appPackage";
	public final String CAPABILITY_APP_ACTIVITY = "appActivity";

	public final String FILE_PATH = "C://Users//Administrator//workspace//TestSocialNet//credentialsFile.xlsx";
	
	public static AppiumDriver<WebElement> driver;
	DesiredCapabilities capabilities;
	DataDrivenUser dataDrivenUser = null;

	@BeforeMethod
	public void setUpAppium() throws MalformedURLException, InterruptedException {

		capabilities = new DesiredCapabilities();
		try {
			dataDrivenUser = new DataDrivenUser(FILE_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String activityname = NAME_PACKAGE + "." + NAME_ACTIVITY;

		capabilities.setCapability(CAPABILITY_DEVICE_NAME, DEVICE_NAME);
		capabilities.setCapability(CAPABILITY_DEVICE_UUID, DEVICE_UUID);
		capabilities.setCapability(CAPABILITY_PLATFORM_VERSION, PLATFORM_VERSION);
		capabilities.setCapability(CAPABILITY_PLATFORM_NAME, PLATFORM_NAME);
		capabilities.setCapability(CAPABILITY_APP_PACKAGE, NAME_PACKAGE);
		capabilities.setCapability(CAPABILITY_APP_ACTIVITY, activityname);

		driver = new AndroidDriver<WebElement>(new URL(APPIUM_URL_SERVICE), capabilities);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

	}

	@AfterTest
	public void CleanUpAppium() {

		driver.quit();
	}

	@Test
	public void testLogin() throws InterruptedException {

		int rowNum = 1;
		Credential credential;		
		credential = dataDrivenUser.getCredential(rowNum);
		System.out.println("Usando credenciales de "+credential.getUsername());
		
		int startSwipeX = 380;
		int startSwipeY = 1050;
		int endSwipeX = 250;
		int endSwipeY = 450;
		int swipeDuration = 3000;

		
		assertNotNull(credential, "There are not credentials on the file");
		while (!credential.getUsername().isEmpty() && !credential.getPassword().isEmpty()) {

			try {

				Assert.assertNotNull(driver.getContext());

				WebElement email = driver.findElement(By.id("com.pinterest:id/email_address"));
				email.click();

				WebElement dimmissSuggestion = driver.findElement(By.id("com.google.android.gms:id/cancel"));
				dimmissSuggestion.click();

				email.clear();
				email.sendKeys(credential.getUsername());

				WebElement btnContinue = driver.findElement(By.id("com.pinterest:id/continue_email_bt"));
				btnContinue.click();

				WebElement chkShowPassword = driver.findElement(By.id("com.pinterest:id/password_toggle_cb"));
				chkShowPassword.click();

				WebElement password = driver.findElement(By.id("com.pinterest:id/password"));
				password.click();
				password.sendKeys(credential.getPassword());

				WebElement btnLogin = driver.findElement(By.id("com.pinterest:id/next_bt"));
				btnLogin.click();

				WebElement txtSearchBar = driver.findElement(By.id("com.pinterest:id/search_tv"));
				txtSearchBar.click();

				WebElement queyInput = driver.findElement(By.id("com.pinterest:id/query_input"));
				queyInput.sendKeys("pastor australiano");
				driver.tap(1, 660, 1125, 100);

				Thread.sleep(2000);
				driver.swipe(startSwipeX, startSwipeY, endSwipeX, endSwipeY, swipeDuration);

				WebElement photos = driver.findElementById("com.pinterest:id/adapter_vw");
				System.out.println(photos.toString());

				WebElement photo = photos.findElement(By.xpath("//android.view.View[@index='6']"));
				photo.click();

				WebElement options = driver.findElement(By.id("com.pinterest:id/menu_pin_overflow"));
				options.click();

				WebElement btnDownload = driver.findElement(By.id("com.pinterest:id/value_tv"));
				btnDownload.click();

				WebElement userProfile = driver.findElement(By.id("com.pinterest:id/profile_icon"));
				userProfile.click();

				WebElement menu_create = driver.findElement(By.id("com.pinterest:id/menu_create"));
				menu_create.click();

				WebElement create_board = driver.findElement(By.id("com.pinterest:id/create_board"));
				create_board.click();

				WebElement board_name = driver.findElement(By.id("com.pinterest:id/board_name_edittext"));
				board_name.click();
				board_name.sendKeys("New Board");
				driver.hideKeyboard();

				WebElement btnCreate = driver.findElement(By.id("com.pinterest:id/create_btn"));
				btnCreate.click();

				WebElement btnSettings = driver.findElement(By.id("com.pinterest:id/menu_settings"));
				btnSettings.click();

				WebElement btnLogOut = driver.findElementByXPath(
						"//android.widget.TextView[@bounds='[0,657][655,740]']");
				btnLogOut.click();
				//driver.hideKeyboard();
				
				dataDrivenUser.setStatus(rowNum, SUCCESS_MESSAGE);
				System.err.println(SUCCESS_MESSAGE);
				rowNum++;
				credential = dataDrivenUser.getCredential(rowNum);
				if (null == credential) {
					break;
				}
				
			} catch (NoSuchElementException e) {
				System.err.println(FAIL_MESSAGE);
				WebElement messageFail = driver.findElementByXPath("//android.widget.TextView[@bounds='[90,575][630,669]']");
				System.err.println(messageFail.getText());
				dataDrivenUser.setStatus(rowNum, FAIL_MESSAGE);
				AndroidDeviceActionShortcuts shortcuts =(AndroidDeviceActionShortcuts) driver;
				shortcuts.pressKeyCode(AndroidKeyCode.BACK);
				shortcuts.pressKeyCode(AndroidKeyCode.BACK);
				
			}	
		}

		Thread.sleep(10000);

	}
}
