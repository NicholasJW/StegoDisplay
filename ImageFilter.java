import java.awt.*;
import javax.swing.*;
import squint.*;

/**
 * Write a description of class ImageFilter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ImageFilter
{
    public ImageFilter()
    {
    }

    public static SImage clearLowerBit(SImage image)
    {
        

        return scaleImage(image, 2, 2);

        
    }

    public static SImage scaleImage(SImage image, int factor, int divisor)
    {
        SImage newImage;

        if(isColor(image))
        {
            int[][] redArray;
            redArray = image.getPixelArray( SImage.RED );
            int[][] greenArray;
            greenArray = image.getPixelArray( SImage.GREEN );
            int[][] blueArray;
            blueArray = image.getPixelArray( SImage.BLUE );

            for (int x = 0; x < image.getWidth(); ++x)
            {
                for(int y = 0; y < image.getHeight(); ++y)
                {
                    redArray[x][y] = (int) ((redArray[x][y]/divisor)*factor );
                }
            }

            newImage = new SImage(redArray, greenArray, blueArray);

        }else 
        {
            int[][] pixel;
            pixel = image.getPixelArray();

            for (int x = 0; x < image.getWidth(); ++x)
            {
                for(int y = 0; y < image.getHeight(); ++y)
                {
                    pixel[x][y] = (int) ((pixel[x][y]/divisor)*factor );
                }
            }

            newImage = new SImage(pixel);
        }

        return newImage;
    }
    
    

    public static SImage addImages(SImage coverImage, SImage secretImage)
    {
        int[][] coverPixel;
        
        if (isColor(coverImage))
        {
            coverPixel = coverImage.getRedPixelArray();
        } else {
            coverPixel = coverImage.getPixelArray();
        }
        
        int[][] secretPixel = secretImage.getPixelArray();
        
        
        for (int x = 0; x < coverImage.getWidth(); ++x) 
        {
            for (int y = 0; y < coverImage.getHeight(); ++y) 
            {
                coverPixel[x][y] = coverPixel[x][y] + secretPixel[x][y];
            }
        }
        
        if (isColor(coverImage)) 
        {
            return new SImage(coverPixel, coverImage.getGreenPixelArray(), coverImage.getBluePixelArray());
        } else {
            return new SImage(coverPixel);
        }
    }

    
    public static SImage subtractImage(SImage oldImage, SImage newImage)
    {
        int[][] pixel1;
        int[][] pixel2;
        
        if (isColor(oldImage)) {
            pixel1 = oldImage.getRedPixelArray();
            pixel2 = newImage.getRedPixelArray();
        } else {
            pixel1 = oldImage.getPixelArray();
            pixel2 = newImage.getPixelArray();
        }
        
        for (int x = 0; x < oldImage.getWidth(); ++x) {
            for (int y = 0; y < oldImage.getHeight(); ++y) {
                
                pixel2[x][y] = pixel1[x][y] - pixel2[x][y];
                
            }
        }
        return new SImage(pixel2);
        
        
        /*
        
        int[][] oldPixel = oldImage.getPixelArray();
        int[][] newPixel = newImage.getPixelArray();
        
        int[][] pixel = new int[oldImage.getWidth()][oldImage.getHeight()];
        for (int x = 0; x < oldImage.getWidth(); ++x)
        {
            for(int y = 0; y < oldImage.getWidth(); ++y)
            {
                pixel[x][y] = oldPixel[x][y] - newPixel[x][y]; 
            }
        }
        
        SImage image = new SImage(pixel);
        //image = ImageFilter.scaleImage(new SImage(pixel),128,1);
        return image;
        */
    }

    public static boolean isColor(SImage image)
    {

        int[][] bluePixel = image.getBluePixelArray();
        int[][] greenPixel = image.getGreenPixelArray();        
        int[][] redPixel = image.getRedPixelArray();    

        // Are all of the arrays equal? If so, it's grayscale.
        // If not, it's color (well, likely color).

        if ( areEqual(bluePixel, greenPixel) && areEqual(redPixel, greenPixel)) 
        {
            // All arrays equal, so grayscale
            return false;
        }
        // Arrays not all equal, so color
        return true;

    }

    private static boolean areEqual(int[][] a, int[][] b) {

        //When are two arrays equal?
        // a.length is first dimension
        // a[0].length is second dimension

        if ( (a.length != b.length) || (a[0].length != b[0].length)) {
            // Not same size, so return false
            return false;
        }

        // Now check all of the entries
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {

                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }

        // If we get to here, they have the same size and
        // the same entries, so they are the same!
        return true;

    }

}
