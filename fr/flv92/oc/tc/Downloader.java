package fr.flv92.oc.tc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;

public class Downloader {

    public static void download(String httpURLofImage, String fileName) {
        BufferedImage img;
        try
        {

            //URI uri = new URI("http", httpURLofImage, "/" + fileName, null);
            URL url = new URL(httpURLofImage);
            System.out.println(url.toString());
            img = ImageIO.read(url);
            if(img == null)
            {
                System.out.println("sdr");
            }
            File file = new File(fileName);
            System.out.println(file == null);
            ImageIO.write(img, "png", file);
        } catch (IOException ex)
        {
            System.out.println("Some problem with the file " + fileName + " -IOEXCEPTION");
        }
    }
}