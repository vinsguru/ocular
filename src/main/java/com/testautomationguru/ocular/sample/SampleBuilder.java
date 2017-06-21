package com.testautomationguru.ocular.sample;

import java.nio.file.Path;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.testautomationguru.ocular.comparator.OcularResult;

public interface SampleBuilder {
    public SampleBuilder using(WebDriver driver);
    public SampleBuilder using(Path path);
    public SampleBuilder element(WebElement element);
    public SampleBuilder excluding(WebElement element);
    public SampleBuilder excluding(List<WebElement> list);
    public SampleBuilder similarity(int cutoff);
    public OcularResult compare();
}
