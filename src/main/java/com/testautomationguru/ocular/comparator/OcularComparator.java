package com.testautomationguru.ocular.comparator;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.arquillian.rusheye.comparison.ImageComparator;
import org.arquillian.rusheye.core.DefaultImageComparator;
import org.arquillian.rusheye.suite.ComparisonResult;
import org.openqa.selenium.WebElement;

import com.testautomationguru.ocular.Ocular;
import com.testautomationguru.ocular.exception.OcularException;
import com.testautomationguru.ocular.snapshot.SnapshotAttributes;

public class OcularComparator {

    private static final ImageComparator imageComparator = new DefaultImageComparator();
 
    public static OcularResult compare(SnapshotAttributes snapshotAttribute, BufferedImage sample, List<WebElement> exclusionList) {

        final File patternFile = snapshotAttribute.getSnapshotPath().toFile();
               
        if(!patternFile.exists()){
            ImageUtil.saveImage(sample, patternFile);
        }
        
        //pattern
        BufferedImage pattern = getImage(patternFile); 
        pattern = ImageUtil.maskElements(pattern, exclusionList);
        
        //sample
        sample = ImageUtil.maskElements(sample, exclusionList);
        
        ComparisonResult result = imageComparator.compare(pattern,
                                                     sample,
                                                     snapshotAttribute.getPerception(),
                                                     snapshotAttribute.getMasks());
        
        File outputFile = Ocular.config().getResultPath().resolve(snapshotAttribute.getSnapshotPath().getFileName()).toFile();
        ImageUtil.saveImage(result.getDiffImage(), outputFile);
        return new OcularResult(result, snapshotAttribute.getSimilarity());
    }
    
    private static BufferedImage getImage(final File file){
        BufferedImage pattern = null;
        try {
            pattern = ImageIO.read(file);
        } catch (IOException e) {
            
            throw new OcularException(e.getMessage() + " - " + file.getAbsolutePath());
        }
        return pattern;
    }
    
}
