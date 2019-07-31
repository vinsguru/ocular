package com.testautomationguru.ocular.comparator;

import com.testautomationguru.ocular.exception.OcularException;
import org.arquillian.rusheye.oneoff.ImageUtils;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.*;

public class ImageUtil {

    private final static AlphaComposite COMPOSITE = AlphaComposite.getInstance(AlphaComposite.CLEAR);
    private final static Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static BufferedImage getPageSnapshot(WebDriver driver) {
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage page = null;
        try {
            page = ImageIO.read(screen);
        } catch (Exception e) {
            throw new OcularException("Unable to get page snapshot", e);
        }
        return page;
    }

    public static BufferedImage getElementSnapshot(WebDriver driver, WebElement element) {
        Point p;
        try {
            Map bounding = (Map) ((JavascriptExecutor) driver).executeScript("return arguments[0].getBoundingClientRect()", element);
            int x = ((Number) bounding.get("x")).intValue();
            int y = ((Number) bounding.get("y")).intValue();
            p = new Point(x, y);
        } catch (ClassCastException e) {
            //falling back to WebElement::getLocation
            p = element.getLocation();
        }

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
