package com.testautomationguru.ocular.end2end;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.testautomationguru.ocular.Ocular;
import com.testautomationguru.ocular.comparator.OcularResult;
import com.testautomationguru.ocular.snapshot.Snap;

@Snap("RichFaces-Poll.png")
public class RichFacesPoll {

    private final WebDriver driver;
    
    public RichFacesPoll(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    @FindBy(id="form:serverDate")
    private WebElement datetime;

    public void goTo(String demo, String skin){
        driver.get("http://showcase.richfaces.org/richfaces/component-sample.jsf?demo=" + demo + "&skin=" + skin);
    }
    
    
    public OcularResult compare() {

        return Ocular.snapshot()
                     .from(this)
                     .sample()
                     .using(driver)
                     .excluding(datetime)
                     .compare();
    }    
}
