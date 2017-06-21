package com.testautomationguru.ocular.sample;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.testautomationguru.ocular.comparator.ImageUtil;
import com.testautomationguru.ocular.comparator.OcularComparator;
import com.testautomationguru.ocular.comparator.OcularResult;
import com.testautomationguru.ocular.exception.OcularException;
import com.testautomationguru.ocular.snapshot.SnapshotAttributes;

public class SampleBuilderImpl implements SampleBuilder {

    private WebDriver driver;
    private BufferedImage sample;
    private List<WebElement> exclusionList = new LinkedList<WebElement>();
    private SnapshotAttributes sanpshotAttribute;

    public SampleBuilderImpl(SnapshotAttributes snapshotAttributes) {
        this.sanpshotAttribute = snapshotAttributes;
    }

    public SampleBuilder using(WebDriver driver) {
        this.driver = driver;
        return this;
    }

    public SampleBuilder using(Path path) {
        try {
            this.sample = ImageIO.read(path.toFile());
        } catch (IOException e) {
            throw new OcularException(e.getMessage() + " " + path.toString());
        }
        return this;
    }

    public SampleBuilder element(WebElement element) {
        this.sample = ImageUtil.getElementSnapshot(this.driver, element);
        return this;
    }

    public SampleBuilder excluding(WebElement element) {
        exclusionList.add(element);
        return this;
    }

    public SampleBuilder excluding(List<WebElement> list) {
        exclusionList.addAll(list);
        return this;
    }

    public SampleBuilder similarity(int cutoff) {
        this.sanpshotAttribute.setSimilarity(cutoff);
        return this;
    }

    public OcularResult compare() {
        if(null==this.sample){
            this.sample =ImageUtil.getPageSnapshot(this.driver);
        }
        return OcularComparator.compare(this.sanpshotAttribute, this.sample, this.exclusionList);
    }

}
