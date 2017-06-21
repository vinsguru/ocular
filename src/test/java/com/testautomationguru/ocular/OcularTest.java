package com.testautomationguru.ocular;

import java.nio.file.Paths;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.testautomationguru.ocular.comparator.OcularResult;
import com.testautomationguru.ocular.end2end.RichFaces;
import com.testautomationguru.ocular.end2end.RichFacesPoll;
import com.testautomationguru.ocular.end2end.RichFacesWithFragment;

public class OcularTest {

    private WebDriver driver;

    @BeforeTest
    public void ocularConfigSetup() {
        Ocular.config().reset();
        Ocular.config().snapshotPath(Paths.get(System.getProperty("user.dir"), "src/test/resources/end2end/snapshot"))
            .resultPath(Paths.get(".", "target"));

        driver = new PhantomJSDriver();
    }

    @Test
    public void ocularPageCompareUsingWebDriver() {
        
        RichFaces richFacePage = new RichFaces(driver);

        richFacePage.goTo("repeat", "ruby");

        OcularResult result = richFacePage.comparePage();

        Assert.assertTrue(result.isEqualsImages());
        Assert.assertEquals(100, result.getSimilarity());
    }

    @Test(dependsOnMethods = { "ocularPageCompareUsingWebDriver" })
    public void ocularFragmentCompareUsingWebDriver() {
        RichFacesWithFragment richFacePage = new RichFacesWithFragment(driver);

        richFacePage.goTo("repeat", "ruby");

        OcularResult result = richFacePage.compareElement();
        Assert.assertTrue(result.isEqualsImages());
        Assert.assertEquals(100, result.getSimilarity());
    }

    @Test(dependsOnMethods = { "ocularFragmentCompareUsingWebDriver" })
    public void ocularCompareExcludingElement() throws InterruptedException {

        RichFacesPoll richFacePage = new RichFacesPoll(driver);

        richFacePage.goTo("poll", "wine");

        OcularResult result = richFacePage.compare();

        Assert.assertTrue(result.isEqualsImages());
        Assert.assertEquals(100, result.getSimilarity());

        // Wait for sometime to get the server date time changed
        Thread.sleep(4000);

        result = richFacePage.compare();

        Assert.assertTrue(result.isEqualsImages());
        Assert.assertEquals(100, result.getSimilarity());

    }

    @AfterTest
    public void quitDriver() {
        driver.quit();
    }
}
