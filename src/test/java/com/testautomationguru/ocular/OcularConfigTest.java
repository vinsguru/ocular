package com.testautomationguru.ocular;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.testautomationguru.ocular.comparator.OcularResult;
import com.testautomationguru.ocular.exception.OcularException;
import com.testautomationguru.ocular.pages.RichFaceTheme;
import com.testautomationguru.ocular.pages.RichFaceWithSimilarity;
import com.testautomationguru.ocular.pages.SnapPage;
import com.testautomationguru.ocular.pages.SnapPageNoAnnotation;
import com.testautomationguru.ocular.pages.SnapPageWithSimilarity;
import com.testautomationguru.ocular.snapshot.SnapshotBuilder;
import com.testautomationguru.ocular.snapshot.SnapshotBuilderImpl;

public class OcularConfigTest {
    
    @BeforeTest
    public void ocularConfigSetup() {
        Ocular.config().reset();
    }
    
    @Test
    public void defaultConfigTest() {
        Assert.assertEquals(100, Ocular.config().getGlobalSimilarity());
        Assert.assertEquals(null, Ocular.config().getResultPath());
        Assert.assertEquals(null, Ocular.config().getSnapshotPath());
        Assert.assertTrue(Ocular.config().canSaveSnapshot());
    }

    @Test(dependsOnMethods = { "defaultConfigTest" })
    public void configOverrideTest() throws IOException {
        Ocular.config()
               .resultPath(Paths.get("."))
               .snapshotPath(Paths.get("."))
               .globalSimilarity(10)
               .saveSnapshot(false);

        Assert.assertEquals(10, Ocular.config().getGlobalSimilarity());
        Assert.assertEquals(0,
            Paths.get(System.getProperty("user.dir")).compareTo(Ocular.config().getResultPath().toRealPath()));
        Assert.assertEquals(0,
            Paths.get(System.getProperty("user.dir")).compareTo(Ocular.config().getSnapshotPath().toRealPath()));
        Assert.assertFalse(Ocular.config().canSaveSnapshot());
        
        Ocular.config().globalSimilarity(100)
                      .resultPath(Paths.get(".", "target"))
                      .saveSnapshot(true);

    }

    @Test(dependsOnMethods = { "configOverrideTest" }, expectedExceptions = OcularException.class)
    public void snapNoAnnationTest() {
        SnapshotBuilder snapshot = new SnapshotBuilderImpl();
        snapshot.from(SnapPageNoAnnotation.class);

        SnapPageNoAnnotation sno = new SnapPageNoAnnotation();
        snapshot.from(sno);
    }

    @Test(dependsOnMethods = { "snapNoAnnationTest" })
    public void snapAnnationTest() {
        SnapshotBuilder snapshot = new SnapshotBuilderImpl();
        snapshot.from(SnapPage.class);
        snapshot.from(SnapPageWithSimilarity.class);
    }

    @Test(dependsOnMethods = { "snapAnnationTest" })
    public void ocularCompareUsingPath() {
        
        Path snapshotPath = Paths.get(System.getProperty("user.dir"), "src/test/resources/image-compare/RichFacesTheme-blueSky.png");
        Path samplePath = Paths.get(System.getProperty("user.dir"), "src/test/resources/image-compare/RichFacesTheme-classic.png");
               
        OcularResult result = Ocular.snapshot()
            .from(snapshotPath)
            .sample()
            .using(samplePath)
            .compare();
        
        Assert.assertFalse(result.isEqualsImages());
        Assert.assertEquals(99, result.getSimilarity());
        
        result = Ocular.snapshot()
            .from(snapshotPath)
            .sample()
            .using(samplePath)
            .similarity(98)
            .compare();
        
        Assert.assertTrue(result.isEqualsImages());
        Assert.assertEquals(99, result.getSimilarity());        
    }
    
    @Test(dependsOnMethods = { "ocularCompareUsingPath" })
    public void ocularCompareUsingSnap() {
        
        Path samplePath = Paths.get(System.getProperty("user.dir"), "src/test/resources/image-compare/RichFacesTheme-blueSky.png");
        
        OcularResult result = Ocular.snapshot()
            .from(RichFaceTheme.class)
            .replaceAttribute("THEME", "blueSky")
            .sample()
            .using(samplePath)
            .compare();
        
        Assert.assertTrue(result.isEqualsImages());
        Assert.assertEquals(100, result.getSimilarity());        
        
        Ocular.config().globalSimilarity(100);
        result = Ocular.snapshot()
            .from(RichFaceTheme.class)
            .replaceAttribute("THEME", "classic")
            .sample()
            .using(samplePath)
            .compare();
        
        Assert.assertFalse(result.isEqualsImages());
        Assert.assertEquals(99, result.getSimilarity());  
        
    }  
    
    @Test(dependsOnMethods = { "ocularCompareUsingSnap" })
    public void ocularCompareUsingSnapSimilarity() {
        
        Path samplePath = Paths.get(System.getProperty("user.dir"), "src/test/resources/image-compare/RichFacesTheme-classic.png");
        
        OcularResult result = Ocular.snapshot()
            .from(RichFaceWithSimilarity.class)
            .sample()
            .using(samplePath)
            .compare();
        
        Assert.assertTrue(result.isEqualsImages());
        Assert.assertEquals(99, result.getSimilarity());
    }      
}
