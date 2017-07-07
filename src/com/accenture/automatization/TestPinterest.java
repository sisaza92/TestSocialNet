package com.accenture.automatization;

import static org.testng.Assert.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
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

	private static final String SUCCESS_MESSAGE = "\tLogin Successful";
	private static final String FAIL_MESSAGE = "\tLogin Fail";

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

	private WebElement findElement(By by) {
		WebElement element = null;
		try {
			element = driver.findElement(by);
		} catch (NoSuchElementException e) {
			// TODO: handle exception
			// System.err.println("element not found");
		}

		return element;
	}

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
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	}

	@AfterTest
	public void CleanUpAppium() {

		driver.quit();
	}

	@Test
	public void testLogin() throws InterruptedException {

		int rowNum = 0;
		Credential credential;
		credential = dataDrivenUser.getCredential(rowNum);

		int startSwipeX = 380;
		int startSwipeY = 1050;
		int endSwipeX = 250;
		int endSwipeY = 450;
		int swipeDuration = 3000;

		assertNotNull(credential, "There are not credentials on the file");
		while (null != credential) {

			WebElement txtSearchBar = null;
			WebElement email = null;

			try {

				Assert.assertNotNull(driver.getContext());

				System.out.println("\nUsing " + credential.getUsername() + " credentials");

				email = driver.findElementById("com.pinterest:id/email_address");
				System.out.println("\tClicking email address field");
				email.click();

				WebElement dimmissSuggestion = findElement(By.id("com.google.android.gms:id/cancel"));
				if (null != dimmissSuggestion) {
					dimmissSuggestion.click();
					System.out.println("\tDimmising the suggestion popup");
				}
				System.out.println("\tClicking email address field");
				email.click();
				System.out.println("\tCleaning email address field");
				email.clear();
				System.out.println("\tSending string to email address field");
				email.sendKeys(credential.getUsername());

				WebElement btnContinue = driver.findElement(By.id("com.pinterest:id/continue_email_bt"));
				System.out.println("\tClicking continue button address field");
				btnContinue.click();

				WebElement chkShowPassword = findElement(By.id("com.pinterest:id/password_toggle_cb"));
				if (null == chkShowPassword) {
					WebElement singUpMessage = findElement(By.id("com.pinterest:id/signup_name"));
					if (singUpMessage == null) {
						singUpMessage = driver.findElementByXPath("//android.widget.TextView[@bounds='[95,584][622,667]']");
						String message = FAIL_MESSAGE + ": " + singUpMessage.getText();
						System.err.println(message);
						dataDrivenUser.setStatus(rowNum, message);
					} else {
						String message = FAIL_MESSAGE + ": " + singUpMessage.getText();
						System.err.println(message);
						dataDrivenUser.setStatus(rowNum, message);
						AndroidDeviceActionShortcuts shortcuts = (AndroidDeviceActionShortcuts) driver;
						System.out.println("\tPressing back button");
						shortcuts.pressKeyCode(AndroidKeyCode.BACK);
						shortcuts.pressKeyCode(AndroidKeyCode.BACK);
					}

				} else {

					WebElement password = driver.findElement(By.id("com.pinterest:id/password"));
					System.out.println("\tClicking password field");
					password.click();
					System.out.println("\tSending string to password field");
					password.sendKeys(credential.getPassword());

					WebElement btnLogin = driver.findElement(By.id("com.pinterest:id/next_bt"));
					System.out.println("\tClicking Login button field");
					btnLogin.click();

					txtSearchBar = findElement(By.id("com.pinterest:id/search_tv"));
					WebElement messageFail = findElement(
							By.xpath("//android.widget.TextView[@bounds='[90,575][630,669]']"));

					if (messageFail != null) {

						String message = FAIL_MESSAGE + ": " + messageFail.getText();
						System.err.println(message);
						dataDrivenUser.setStatus(rowNum, message);
						AndroidDeviceActionShortcuts shortcuts = (AndroidDeviceActionShortcuts) driver;
						System.out.println("\tPressing back button");
						shortcuts.pressKeyCode(AndroidKeyCode.BACK);
						shortcuts.pressKeyCode(AndroidKeyCode.BACK);

					} else {

						System.out.println("\tClicking Search bar");
						txtSearchBar.click();

						WebElement queyInput = driver.findElement(By.id("com.pinterest:id/query_input"));
						System.out.println("\tSending query to Search bar");
						queyInput.sendKeys("Pastor Australiano");
						driver.tap(1, 660, 1125, 100);

						Thread.sleep(2000);
						System.out.println("\tCSwiping down the screen");
						driver.swipe(startSwipeX, startSwipeY, endSwipeX, endSwipeY, swipeDuration);

						WebElement photos = driver.findElementById("com.pinterest:id/adapter_vw");

						WebElement photo = photos.findElement(By.xpath("//android.view.View[@index='6']"));
						System.out.println("\tClicking photo");
						photo.click();

						WebElement options = driver.findElement(By.id("com.pinterest:id/menu_pin_overflow"));
						System.out.println("\tClicking options menu");
						options.click();

						WebElement btnDownload = driver.findElement(By.id("com.pinterest:id/value_tv"));
						System.out.println("\tClicking Download button");
						btnDownload.click();

						WebElement userProfile = driver.findElement(By.id("com.pinterest:id/profile_icon"));
						System.out.println("\tClicking profile icon");
						userProfile.click();

						WebElement menu_create = driver.findElement(By.id("com.pinterest:id/menu_create"));
						System.out.println("\tClicking Menu Create");
						menu_create.click();

						WebElement create_board = driver.findElement(By.id("com.pinterest:id/create_board"));
						System.out.println("\tClicking create board icon");
						create_board.click();

						WebElement board_name = driver.findElement(By.id("com.pinterest:id/board_name_edittext"));
						System.out.println("\tClicking board name field");
						board_name.click();
						System.out.println("\tSending String to board name");
						board_name.sendKeys(String.valueOf(new Date().getTime()));
						driver.hideKeyboard();

						WebElement btnCreate = driver.findElement(By.id("com.pinterest:id/create_btn"));
						System.out.println("\tClicking create button");
						btnCreate.click();

						WebElement btnSettings = driver.findElement(By.id("com.pinterest:id/menu_settings"));
						System.out.println("\tClicking settings icon");
						btnSettings.click();

						WebElement btnLogOut = driver
								.findElementByXPath("//android.widget.TextView[@bounds='[0,657][655,740]']");
						System.out.println("\tClicking Logout button");
						System.out.println("\tLoggin Out the Account");
						btnLogOut.click();

						System.err.println(SUCCESS_MESSAGE);
						dataDrivenUser.setStatus(rowNum, SUCCESS_MESSAGE);
					}
				}

			} catch (NoSuchElementException e) {
				e.printStackTrace();
			}

			rowNum++;
			credential = dataDrivenUser.getCredential(rowNum);

			if (null == credential) {
				break;
			}
		}

		Thread.sleep(10000);

	}
}
