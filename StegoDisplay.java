
/**
 * Write a description of class StegoDisplay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.lang.Object;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import java.util.*;
import squint.*;

public class StegoDisplay extends GUIManager
{
    private ImageViewer cover;
    private ImageViewer secret;
    private ImageViewer encrypted;
    private JSplitPane split;
    private JPanel bigPanel;
    private JTextArea theTextArea;
    private JButton encodeButton;
    private JButton decodeButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton clearTextButton;
    private JButton clearImageButton;
    private JPanel buttonPanel;
    private JButton whichButton;

    public StegoDisplay()
    {
        this.createWindow(1000,600);
        contentPane.setLayout(new BorderLayout());

        cover = new ImageViewer();
        secret = new ImageViewer();
        encrypted = new ImageViewer();

        theTextArea = new JTextArea();
        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setTopComponent(theTextArea);
        split.setBottomComponent(secret);

        bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(1,3));
        bigPanel.add(cover);
        bigPanel.add(split);
        bigPanel.add(encrypted);

        contentPane.add(bigPanel, BorderLayout.CENTER);

        encodeButton = new JButton("Encode");
        encodeButton.setActionCommand("Encode");
        decodeButton = new JButton("Decode");
        decodeButton.setActionCommand("Decode");
        encryptButton = new JButton("Encrypt");
        encryptButton.setActionCommand("Encrypt");
        decryptButton = new JButton("Decrypt");
        decryptButton.setActionCommand("Decrypt");
        clearTextButton = new JButton("Clear Text");
        clearTextButton.setActionCommand("Clear Text");
        clearImageButton = new JButton("Clear Image");
        clearImageButton.setActionCommand("Clear Image");

        buttonPanel = new JPanel();
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearTextButton);
        buttonPanel.add(clearImageButton);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        split.setDividerLocation(0.5);
    }

    public void buttonClicked( JButton whichButton )
    {
        String actionCommand = whichButton.getActionCommand();

        if(actionCommand.equals(clearImageButton.getActionCommand()))
        {
            secret.clear();
        }else if(actionCommand.equals(clearTextButton.getActionCommand()))
        {
            theTextArea.setText("");
        }else if(actionCommand.equals(encryptButton.getActionCommand()))
        {
            SImage coverImage = cover.getImage();
            SImage secretImage = secret.getImage() ;

            //to test
            //System.out.print(secretImage);
            //secret.setImage(secretImage);
            SImage encryptImage;
            if(coverImage.getWidth() == secretImage.getWidth() && coverImage.getHeight() == secretImage.getHeight())
            {

                encryptImage = ImageFilter.addImages(ImageFilter.clearLowerBit(coverImage),ImageFilter.scaleImage(secretImage,1,128));

            }else
            {
                int[][] pixel = new int[coverImage.getWidth()][coverImage.getHeight()];

                for (int x = 0; x < coverImage.getWidth(); ++x)
                {
                    for(int y = 0; y < coverImage.getHeight(); ++y)
                    {
                        pixel[x][y] = 0;
                    }
                }

                int[][] secretPixel = secretImage.getPixelArray();
                int minWidth = Math.min(coverImage.getWidth(),secretImage.getWidth());
                int minHeight = Math.min(coverImage.getHeight(),secretImage.getHeight());

                for (int x = 0; x < minWidth; ++x)
                {
                    for(int y = 0; y < minHeight; ++y)
                    {

                        pixel[x][y] = secretPixel[x][y];
                    }
                }
                secretImage = new SImage(pixel);

                encryptImage = ImageFilter.addImages(ImageFilter.clearLowerBit(coverImage), ImageFilter.scaleImage(secretImage,1,128));

            }

            encrypted.setImage(encryptImage);
        }else if(actionCommand.equals(decryptButton.getActionCommand()))
        {
            SImage oldImage = encrypted.getImage();
            SImage newImage = ImageFilter.clearLowerBit(oldImage);

            SImage newSecretImage = ImageFilter.subtractImage(oldImage,newImage);
            newSecretImage = ImageFilter.scaleImage(newSecretImage,128,1);

            secret.setImage(newSecretImage);

        }else if(actionCommand.equals(encodeButton.getActionCommand()))
        {

            SImage coverImage = cover.getImage();
            SImage secretImage = convert2Image(StringConverter.convert2Bits(theTextArea.getText()),
                    coverImage.getWidth(), coverImage.getHeight());
            secret.setImage(secretImage);

        }else if(actionCommand.equals(decodeButton.getActionCommand()))
        {
            String text = StringConverter.convert2String(convert2Bits(secret.getImage()));
            theTextArea.setText(text);
        }

    }

    public SImage convert2Image(int[] bits, int width, int height)
    {
        SImage image;

        int[][] pixel = new int[width][height];
        
        for (int x = 0; x < width; ++x) 
        {
            for (int y = 0; y < height; ++y) 
            {
                if (height * x + y >= bits.length)
                {

                    pixel[x][y] = 0;
                } else {
                    pixel[x][y] = bits[height * x + y] * 128;
                }
            }
        }
        return new SImage(pixel);

    }

    private int[] convert2Bits(SImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] pixel = image.getPixelArray();
        int[] bits = new int[width * height];
        
        for (int x = 0; x < width; ++x) 
        {
            for (int y = 0; y < height; ++y) 
            {
                bits[height * x + y] = pixel[x][y] / 128;
            }
        }
        return bits;
    }

}
