package api.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            // Capture screenshot logic using external tool (replace with your preferred method)
            String testName = result.getName();
            String filePath = "./resources/screenshots"; // Adjust the path as needed

            // Check if Greenshot is installed (adjust command based on your tool)
            if (isGreenshotInstalled()) {
                captureScreenshotUsingGreenshot(filePath, testName);
            } else {
                System.err.println("Greenshot not found. Install Greenshot or use an alternative screenshot capture method.");
            }
        } catch (Exception e) {
            System.err.println("Error capturing screenshot: " + e.getMessage());
        }
    }

    private boolean isGreenshotInstalled() throws IOException, InterruptedException {
        // Check if Greenshot executable exists (adjust path based on your installation)
        Process process = Runtime.getRuntime().exec("\"C:\\Program Files\\Greenshot\\Greenshot.exe.config\"");
        process.waitFor();
        String output = new String(process.getInputStream().readAllBytes()).trim();
        return !output.isEmpty();
    }

    private void captureScreenshotUsingGreenshot(String filePath, String testName) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        String command = "greenshot -c"; // Replace with Greenshot capture command (refer to Greenshot documentation)
        Process process = runtime.exec(command);
        process.waitFor();

        // Delay to allow Greenshot to save the screenshot (adjust if needed)
        Thread.sleep(2000);

        // Assuming Greenshot saves to default location (modify based on Greenshot settings)
        File screenshotFile = new File("C:\\Users\\dimpl\\Greenshot.png"); // Replace with Greenshot's default save location
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File destFile = new File(filePath + "/screenshot_" + testName + "_" + timestamp + ".png");
        Files.copy(screenshotFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Optional: Implement logic to be executed before each test starts (e.g., logging)
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Optional: Implement logic to be executed after a successful test (e.g., logging)
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Optional: Implement logic to be executed for skipped tests (e.g., logging)
    }

    @Override
    public void onStart(ITestContext context) {
        // Optional: Implement logic to be executed before the test context starts (e.g., logging)
    }

    @Override
    public void onFinish(ITestContext context) {
        // Optional: Implement logic to be executed after all tests finish (e.g., logging)
    }
}