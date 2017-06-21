package com.testautomationguru.ocular.end2end;

import org.openqa.selenium.WebDriver;
import com.testautomationguru.ocular.Ocular;
import com.testautomationguru.ocular.comparator.OcularResult;
import com.testautomationguru.ocular.snapshot.Snap;

@Snap("RichFaces.png")
public class RichFaces {

    private final WebDriver driver;
    
    public RichFaces(WebDriver driver) {
        this.driver = driver;
    }

    public void goTo(String demo, String skin){
        driver.get("http://showcase.richfaces.org/richfaces/component-sample.jsf?demo=" + demo + "&skin=" + skin);
    }
    
    public OcularResult comparePage() {
        return Ocular.snapshot()
                     .from(this)
                     .sample()
                     .using(driver)
                     .compare();
    }  
}
