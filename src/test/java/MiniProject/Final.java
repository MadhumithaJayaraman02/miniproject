package MiniProject;


	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.IOException;
	import java.io.PrintStream;
	import java.time.Duration;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
	import org.apache.poi.xssf.usermodel.XSSFCell;
	import org.apache.poi.xssf.usermodel.XSSFRow;
	import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	import org.openqa.selenium.By;
	import org.openqa.selenium.JavascriptExecutor;
	import org.openqa.selenium.OutputType;
	import org.openqa.selenium.TakesScreenshot;
	import org.openqa.selenium.WebDriver;
	import org.openqa.selenium.WebElement;
	import org.openqa.selenium.chrome.ChromeDriver;
	import org.openqa.selenium.edge.EdgeDriver;
	import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
	import org.openqa.selenium.support.ui.WebDriverWait;
	import io.github.bonigarcia.wdm.WebDriverManager;

	public class Final {
		static WebDriver driver;
		
		public static WebDriver setdriver() {
			driver = DriverSetup.getDriver();
			return driver;
			}
		
		// A method to read data from an Excel file and return the first row
		public static XSSFRow readExcelData(String filePath) throws IOException {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheet("Sheet1");
			XSSFRow row = sheet.getRow(1);
			wb.close();
			fis.close();
			return row;
		}
		
		// A method to perform Google search and navigate back and forth
		public static String googleSearch(String query) throws InterruptedException {
			String search;
			String navigate;
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.get("https://google.com");
			driver.manage().window().maximize();
			driver.findElement(By.id("APjFqb")).sendKeys(query);
			driver.findElement(By.id("APjFqb")).sendKeys(query);
			driver.findElement(By.id("jZ2SBf")).click();
		
			driver.navigate().back();
			if (driver.getCurrentUrl().equals("https://www.google.com/")) {
				search = "page navigated to google";
			} else {
				search = "fail";
			}
			Thread.sleep(2000);
			
			driver.navigate().forward();
			if (driver.getCurrentUrl().contains("https://www.google.com/search?q=orangehrm+demo")) {
				navigate = "page navigated to orangehrm";
			}
			else {
				navigate = "fail";
			}
			Thread.sleep(2000);
			return search+"\n"+navigate;
		}

		// A method to navigate to OrangeHRM website and fill contact form
		public static String orangeHRMContact(XSSFRow row) throws InterruptedException, IOException {
			String contact;
			driver.navigate().to("https://www.orangehrm.com/");
			//details
			driver.findElement(By.linkText("Contact Sales")).click();
			driver.findElement(By.name("FullName")).sendKeys(row.getCell(1).toString());
			
			driver.findElement(By.name("Contact")).sendKeys(row.getCell(2).toString());
			driver.findElement(By.name("Email")).sendKeys(row.getCell(3).toString());
			driver.findElement(By.name("JobTitle")).sendKeys(row.getCell(4).toString());
			Select country = new Select(driver.findElement(By.name("Country")));
			country.selectByVisibleText("India");
			Select employees = new Select(driver.findElement(By.name("NoOfEmployees")));
			employees.selectByIndex(2);
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollBy(0,450)", "");
			driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='reCAPTCHA']")));
			Thread.sleep(5000);
			WebElement  captcha= driver.findElement(By.xpath("//div[@class='recaptcha-checkbox-border']"));
			captcha.click();
			driver.switchTo().defaultContent();
			Thread.sleep(2000);
			
			WebElement  click= driver.findElement(By.xpath("//*[@id=\"Form_getForm_action_submitForm\"]"));
			js.executeScript("arguments[0].click();", click);
			
			Thread.sleep(5000);
			takeScreenshot("C:\\Users\\2318412\\eclipse-workspace\\MiniProject\\Screenshot\\ss.png");
			
			driver.findElement(By.id("Form_getForm_Comment")).sendKeys(row.getCell(5).toString());
			
			click= driver.findElement(By.xpath("//*[@id=\"Form_getForm_action_submitForm\"]"));
			js.executeScript("arguments[0].click();", click);
		    Thread.sleep(2000);
			driver.navigate().back();
			if(driver.getCurrentUrl().equals("https://www.orangehrm.com/en/contact-sales/")) {
				contact = "page navigated to login page ";
			}
			else {
				contact = "fail";
			}
			Thread.sleep(5000);
			return contact;
		}
		
		// A method to take screenshot and save it
		public static void takeScreenshot(String filePath) throws IOException {
			TakesScreenshot ts = (TakesScreenshot) driver;
			File src = ts.getScreenshotAs(OutputType.FILE);
			File trg = new File(filePath);
			FileUtils.copyFile(src, trg);
		}
		
		public static void main(String[] args) throws FileNotFoundException {
			setdriver();
			String search;
			String contact;
			PrintStream ps = new PrintStream(new File("C:\\Users\\2318412\\eclipse-workspace\\MiniProject\\src\\test\\java\\result.txt"));
			System.setOut(ps);
			try {
				// Read data from Excel file
				XSSFRow row = readExcelData("C:\\Users\\2318412\\eclipse-workspace\\MiniProject\\excel.xlsx\\miniproject.xlsx");
				
				// Perform Google search
				search = googleSearch(row.getCell(0).toString());
				
				// Navigate to OrangeHRM website and fill contact form
				contact = orangeHRMContact(row);
				Thread.sleep(10000);
				
				// Take screenshot and save it
				takeScreenshot("C:\\Users\\2318412\\eclipse-workspace\\selenium1\\screenshot\\ss.png");
				
				// Quit the driver
				driver.quit();
				ps.print(search);
				System.out.println();
				ps.print(contact);
			} 
			catch (Exception e) {
				
			}		
		}
	}


