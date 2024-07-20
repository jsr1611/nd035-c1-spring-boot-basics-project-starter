package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(CloudStorageApplicationTests.class);
    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();
		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        log.info("success-msg: {}", driver.findElement(By.id("success-msg")).getText());
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));

        webDriverWait.until(ExpectedConditions.titleContains("Login"));

    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }


    /**
     * Helper method for logout
     */
    private void doLogout() {
        WebElement uploadButton = driver.findElement(By.id("logoutButton"));
        uploadButton.click();
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {

        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        log.info("url: {}", driver.getCurrentUrl());
        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");
        doGoToFilesTab();
        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }


    private void doCheckHomePageIsNotAccessible() {
        driver.get("http://localhost:" + this.port + "/home");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        Assertions.assertNotEquals("Home", driver.getTitle());
    }

    /**
     * homepage should not be accessible without login
     */
    @Test
    public void testAccessHomePageWithoutLogin() {
        doCheckHomePageIsNotAccessible();
    }

    @Test
    public void testAccessHomePageAfterLogout() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Verify home page is accessed
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        Assertions.assertEquals("Home", driver.getTitle());

        //Log out
        doLogout();

        webDriverWait.until(ExpectedConditions.titleContains("Login"));

        //Verify home page is no longer accessible
        doCheckHomePageIsNotAccessible();
    }


    private List<WebElement> getNotes() {
        return driver.findElement(By.id("note-table-body")).findElements(By.className("notes-tr"));
    }

    private List<WebElement> getCredentials() {
        return driver.findElement(By.id("credential-table-body")).findElements(By.className("credentials-tr"));
    }

    private void doMockNoteCreation(String noteTitle, String noteDescription) {

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        //enter note title
        WebElement noteTitleEl = driver.findElement(By.id("note-title"));
        noteTitleEl.click();
        noteTitleEl.clear();
        noteTitleEl.sendKeys(noteTitle);

        //enter note description
        WebElement noteDescriptionEl = driver.findElement(By.id("note-description"));
        noteDescriptionEl.click();
        noteDescriptionEl.clear();
        noteDescriptionEl.sendKeys(noteDescription);

        WebElement noteSubmitButton = driver.findElement(By.id("noteSaveButton"));
        noteSubmitButton.click();
        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("noteModal")));
    }

    private void doGoToNotesTab() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
        // Go to notes tab
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();
    }

    private void doGoToFilesTab(){
        WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
        // Go to notes tab
        WebElement filesTab = driver.findElement(By.id("nav-files-tab"));
        filesTab.click();
    }

    private void doOpenNotesModal() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
        // Go to notes tab
        doGoToNotesTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNotesButton")));

        //Open note modal for creating a note
        WebElement noteModalAddButton = driver.findElement(By.id("addNotesButton"));
        noteModalAddButton.click();

        //wait until notemodal opens
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));
    }

    @Test
    public void testAddEditDeleteNotes() {


        //Log in to existing user
        doLogIn("test", "test");

        //Go to Notes tab
        doGoToNotesTab();
        // check initial number of notes
        int numberOfNotesBefore = getNotes().size();
        // Open add new note modal
        doOpenNotesModal();
        //add note title
        String noteTitle = "To Do List";
        String noteDescription = "1. Write failing tests. \n2. Then implement API that passes those tests.";
        doMockNoteCreation(noteTitle, noteDescription);
        Integer numberOfNotesAfter = getNotes().size();
        Assertions.assertEquals(numberOfNotesBefore + 1, numberOfNotesAfter);
    }

    @Test
    public void testNoteEditWorks() {

        //Log in to existing user
        doLogIn("test", "test");

        //Go to Notes tab and click on add new note
        doOpenNotesModal();

        // add mock note
        String noteTitle = "To Do List";
        String noteDescription = "1. Write failing tests. 2. Then implement API that passes those tests.";
        doMockNoteCreation(noteTitle, noteDescription);
        // edit existing note 1
        List<WebElement> notes = getNotes();

        if (!notes.isEmpty()) {
            String noteTitleBefore = notes.get(0).findElement(By.className("note-titles")).getText();

            String noteDescBefore = notes.get(0).findElement(By.className("note-descriptions")).getText();


            //click on edit button
            WebElement noteEditButton = notes.get(0).findElement(By.className("note-edit-buttons"));
            noteEditButton.click();

            //wait until notemodal opens
            WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

            //edit note
            String noteTitleUpd = "To Do List Updated";
            String noteDescriptionUpd = "1. Write failing tests. 2. Then implement API that passes those tests. 3. Submit work on time!";
            doMockNoteCreation(noteTitleUpd, noteDescriptionUpd);


            String noteTitleAfter = getNotes().get(notes.size() - 1).findElement(By.className("note-titles")).getText();
            String noteDescAfter = getNotes().get(notes.size() - 1).findElement(By.className("note-descriptions")).getText();

            log.info("Note title before: {}", noteTitleBefore);
            log.info("Note description before: {}", noteDescBefore);

            log.info("Note title after: {}", noteTitleAfter);
            log.info("Note description after: {}", noteDescAfter);

            Assertions.assertEquals(noteTitleUpd.length(), noteTitleAfter.length());
            Assertions.assertEquals(noteDescriptionUpd.length(), noteDescAfter.length());
            Assertions.assertEquals(noteTitleUpd, noteTitleAfter);
            Assertions.assertEquals(noteDescriptionUpd, noteDescAfter);

            // Test NoteDeletion
            doLogout();

            //Log in to existing user
            doLogIn("test", "test");

            //Go to Notes tab and click on add new note
            doOpenNotesModal();

            // add mock note
            noteTitle = "Test if deletion works";
            noteDescription = "1. Click on notes tab. \n2. Select and click on delete on any note. 3. Finished!";
            doMockNoteCreation(noteTitle, noteDescription);


            // test deletion

            //check number of notes
            int numNotesBefore = getNotes().size();
            // get the title and description for the first note in the list
            WebElement firstElement = getNotes().get(0);

            String firstNoteTitle = firstElement.findElement(By.className("note-titles")).getText();
            String firstNoteDesc = firstElement.findElement(By.className("note-descriptions")).getText();

            WebElement deleteNoteButton = firstElement.findElement(By.className("note-delete-buttons"));
            deleteNoteButton.click();

            doGoToNotesTab();
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-table-body")));

            int numNotesAfter = getNotes().size();

            Assertions.assertEquals(numNotesBefore-1, numNotesAfter);

            WebElement firstElementAfterDeletion = getNotes().get(0);

            String firstNoteTitleAftDlt = firstElementAfterDeletion.findElement(By.className("note-titles")).getText();
            String firstNoteDescAftDlt = firstElementAfterDeletion.findElement(By.className("note-descriptions")).getText();

            Assertions.assertNotEquals(firstNoteTitle, firstNoteTitleAftDlt);
            Assertions.assertNotEquals(firstNoteDesc, firstNoteDescAftDlt);

        }else {
            Assertions.fail("No notes exist");
        }
    }


    private void doGoToCredentialsTab() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
        // Go to notes tab
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
    }

    private void doOpenCredentialModal() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 1);
        // Go to notes tab
        doGoToCredentialsTab();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredentialsButton")));

        //Open note modal for creating a note
        WebElement credentialsModalAddButton = driver.findElement(By.id("addCredentialsButton"));
        credentialsModalAddButton.click();

        //wait until notemodal opens
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
    }

    private void doMockCredentialCreation(String url, String username, String password) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        //enter url
        WebElement credentialUrlEl = driver.findElement(By.id("credential-url"));
        credentialUrlEl.click();
        credentialUrlEl.clear();
        credentialUrlEl.sendKeys(url);

        //enter username
        WebElement credentialUsernameEl = driver.findElement(By.id("credential-username"));
        credentialUsernameEl.click();
        credentialUsernameEl.clear();
        credentialUsernameEl.sendKeys(username);

        //enter password
        WebElement passwordEl = driver.findElement(By.id("credential-password"));
        passwordEl.click();
        passwordEl.clear();
        passwordEl.sendKeys(password);

        WebElement credentialSaveButton = driver.findElement(By.id("credentialSaveButton"));
        credentialSaveButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-table-body")));
    }

    @Test
    public void testCreateCredentialAndVerify(){

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        String loginUser = "test";
        String loginPassword = "test";

        //Log in to existing user
        doLogIn(loginUser, loginPassword);

        doGoToCredentialsTab();
        int numCredentialsBefore = getCredentials().size();
        log.info("Number of credentials: {}", numCredentialsBefore);
        //Go to Credentials tab and click on add new credential
        doOpenCredentialModal();

        // add mock credential
        String url = "gmail.com";
        String username = "testUser";
        String password = "testPassword123&%";

        String url2 = "fb.com";
        String username2 = "testUser2";
        String password2 = "testPassword333%";
        doMockCredentialCreation(url, username, password);
        // edit existing note 1
        List<WebElement> credentials = getCredentials();
        Assertions.assertFalse(credentials.isEmpty());

        String urlAdded = credentials.get(0).findElement(By.className("credential-url")).getText();
        log.info("url added: {}", urlAdded);
        String usernameAdded = credentials.get(0).findElement(By.className("credential-username")).getText();
        log.info("username added: {}", usernameAdded);

        Assertions.assertEquals(url, urlAdded);
        Assertions.assertEquals(username, usernameAdded);

        doOpenCredentialModal();

        doMockCredentialCreation(url2, username2, password2);

        doLogout();

        webDriverWait.until(ExpectedConditions.titleContains("Login"));

        //Log in to existing user
        doLogIn(loginUser, loginPassword);

        //Go to credentials
        doGoToCredentialsTab();

        credentials = getCredentials();
        //click on edit button
        WebElement credentialEditButton = credentials.get(0).findElement(By.className("credential-edit-buttons"));
        credentialEditButton.click();

        //wait until credentialmodal opens
        WebDriverWait webDriverWait2 = new WebDriverWait(driver, 1);
        webDriverWait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));


        //edit note
        url = "gmail.com";
        username = "updTestUsername";
        password = "updTestPwd12&*";
        doMockCredentialCreation(url, username, password);

        WebElement lastCredentialEl = getCredentials().get(credentials.size() - 1);

        String urlAfter = lastCredentialEl.findElement(By.className("credential-url")).getText();
        log.info("url after: {}", urlAfter);
        String usernameAfter = lastCredentialEl.findElement(By.className("credential-username")).getText();
        log.info("username after: {}", usernameAfter);

        Assertions.assertNotEquals(url, urlAfter);
        Assertions.assertNotEquals(username, usernameAfter);


        // test credential deletion

        //check number of notes
        numCredentialsBefore = getCredentials().size();
        // get the title and description for the first note in the list
        WebElement firstElement = getCredentials().get(0);

        String firstCredentialUrl = firstElement.findElement(By.className("credential-url")).getText();
        String firstCredentialUsername = firstElement.findElement(By.className("credential-username")).getText();

        WebElement deleteCredentialButton = firstElement.findElement(By.className("credential-delete-buttons"));
        deleteCredentialButton.click();

        webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-table-body")));

        int numCredentialsAfter = getCredentials().size();

        Assertions.assertEquals(numCredentialsBefore-1, numCredentialsAfter);

        WebElement firstElementAfterDeletion = getCredentials().get(0);

        String firstCredentialUrlAftDlt = firstElementAfterDeletion.findElement(By.className("credential-url")).getText();
        String firstCredentialUsernameAftDlt = firstElementAfterDeletion.findElement(By.className("credential-username")).getText();

        Assertions.assertNotEquals(firstCredentialUrl, firstCredentialUrlAftDlt);
        Assertions.assertNotEquals(firstCredentialUsername, firstCredentialUsernameAftDlt);
    }
}
