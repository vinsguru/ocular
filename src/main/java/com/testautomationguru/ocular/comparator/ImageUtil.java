package com.testautomationguru.ocular.comparator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.arquillian.rusheye.oneoff.ImageUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.testautomationguru.ocular.exception.OcularException;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ImageUtil {

    private final static AlphaComposite COMPOSITE = AlphaComposite.getInstance(AlphaComposite.CLEAR);
    private final static Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static BufferedImage getPageSnapshot(WebDriver driver) {
        Augmenter augmenter = new Augmenter();
        TakesScreenshot ts = (TakesScreenshot) augmenter.augment(driver);
        File screen = ts.getScreenshotAs(OutputType.FILE);
        BufferedImage page = null;
        try {
            page = ImageIO.read(screen);
        } catch (Exception e) {
            throw new OcularException("Unable to get page snapshot", e);
        }
        return page;
    }

    public static BufferedImage getElementSnapshot(WebDriver driver, WebElement element) {
        Point p = element.getLocation();
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        return getPageSnapshot(driver).getSubimage(p.getX(), p.getY(), width, height);
    }

    public static BufferedImage maskElement(BufferedImage img, WebElement element) {
        return maskArea(img, element);
    }

    public static BufferedImage maskElements(BufferedImage img, List<WebElement> elements) {
        for (WebElement element : elements) {
            img = maskArea(img, element);
        }
        return img;
    }

    public static void saveImage(BufferedImage result, File file) {
        try {
            ImageUtils.writeImage(result, file.getParentFile(), file.getName());
        } catch (IOException e) {
            throw new OcularException("Unable to write the difference", e);
        }
    }

    private static BufferedImage maskArea(BufferedImage img, WebElement element) {
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setComposite(COMPOSITE);
        g2d.setColor(TRANSPARENT);

        Point p = element.getLocation();
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        g2d.fillRect(p.getX(), p.getY(), width, height);

        return img;
    }

}
