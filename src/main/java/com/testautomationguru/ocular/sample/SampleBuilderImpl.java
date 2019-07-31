package com.testautomationguru.ocular.sample;

import com.testautomationguru.ocular.comparator.*;
import com.testautomationguru.ocular.exception.OcularException;
import com.testautomationguru.ocular.snapshot.SnapshotAttributes;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class SampleBuilderImpl implements SampleBuilder {

    private WebDriver driver;
    private BufferedImage sample;
    private List<WebElement> exclusionList = new LinkedList<WebElement>();
    private SnapshotAttributes snapshotAttributes;

    public SampleBuilderImpl(SnapshotAttributes snapshotAttributes) {
        this.snapshotAttributes = snapshotAttributes;
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
        this.snapshotAttributes.setSimilarity(cutoff);
        return this;
    }

    public OcularResult compare() {
        if(null==this.sample){
            this.sample =ImageUtil.getPageSnapshot(this.driver);
        }
        return OcularComparator.compare(this.snapshotAttributes, this.sample, this.exclusionList);
    }

}
