
/**
 * Write a description of class ImageViewer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import squint.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class ImageViewer extends GUIManager
{
    private JLabel imageLabel;
    private JButton loadButton;
    private JButton saveButton;
    private JPanel buttonPanel;
    private JFileChooser chooser;
    private SImage theImage;
    //private JPanel imagePanel;

    public ImageViewer()
    {
        //this.createWindow(500,650);
        contentPane.setLayout(new BorderLayout());

        imageLabel = new JLabel("", SwingConstants.CENTER);
        //imageLabel.setBackground(Color.WHITE);
        //imagePanel = new JPanel();
        //imagePanel.setBackground(Color.WHITE);
        //imagePanel.add(new JScrollPane(imageLabel));

        loadButton = new JButton("Load Image");
        loadButton.setActionCommand("Load Image");
        saveButton = new JButton("Save Image");
        saveButton.setActionCommand("Save Image");

        buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);

        contentPane.add(new JScrollPane(imageLabel), BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        chooser = new JFileChooser(new File(System.getProperty("user.dir"))+"/../AllImages");
        /*
        Color backgroundColor = this.getBackground();
        System.out.print(backgroundColor.getRed() + "\t" + backgroundColor.getGreen() + "\t" + 
        backgroundColor.getBlue() + "\n");
         */

    }

    public void buttonClicked(JButton whichButton)
    {

        String actionCommand = whichButton.getActionCommand();

        //if press load button
        if(actionCommand.equals(loadButton.getActionCommand()))
        {

            //chooser.showOpenDialog( this );

            //theImage = new SImage(chooser.getSelectedFile().getAbsolutePath());

            if ( chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ) 
            {
                theImage = new SImage(chooser.getSelectedFile().getAbsolutePath());
                imageLabel.setIcon(theImage);
            }
        }else if (actionCommand.equals(saveButton.getActionCommand())) 
        {
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) 
            {
                boolean overWriteFile = false;
                if (chooser.getSelectedFile().exists()) 
                {

                    int returnValue = JOptionPane.showOptionDialog(this, "File already exists. Replace?",
                            "Warning: File Exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                            null, null);
                    if (returnValue == JOptionPane.YES_OPTION) 
                    {
                        overWriteFile = true;
                    }
                } else {
                    overWriteFile = true;
                }

                if (overWriteFile) 
                {
                    String fileName = chooser.getSelectedFile().getName();
                    String pathname = chooser.getSelectedFile().getAbsolutePath();

                    if (fileName.toLowerCase().endsWith(".pgm")) 
                    {
                        writePGMImage(pathname);
                    } else {
                        theImage.saveAs(pathname);
                    }
                }
            }
        }
    }

    public void clear()
    {
        imageLabel.setIcon(new SImage(100,100,238));
        theImage = null;
    }

    public SImage getImage()
    {
        return theImage;
    }

    public void setImage( SImage image)
    {
        imageLabel.setIcon(image);
        theImage = image;
    }

    private void writePGMImage(String filename) {
        try {
            BufferedWriter outStream = new BufferedWriter(new FileWriter(filename));
            // At this point, outStream is connected to the given file, for
            // writing
            // As I have it set up, it will overwrite any existing file with the
            // same
            // name in the same directory.

            // Let's write our file theImage to a file in pgm format.

            // First line of a pgm file is what? It's just a line with P2
            outStream.write("P2\n");
            // Now write the width and length of image
            int numCols = theImage.getWidth();
            int numRows = theImage.getHeight();

            outStream.write(numCols + " " + numRows + "\n");
            // Now write the 255 (max intensity value)
            outStream.write("255\n");
            // At this point, we need to start writing the pixels

            // Get the pixels that we need to write
            int[][] pixel = theImage.getPixelArray();

            // Loop through pixels writing as we go
            int pixelsWritten = 0;

            for (int row = 0; row < numRows; ++row) {
                for (int col = 0; col < numCols; ++col) {
                    outStream.write(pixel[col][row] + " ");
                    ++pixelsWritten;
                    if (pixelsWritten % 15 == 0) {
                        outStream.write("\n");
                    }
                }
            }

            // We should be done! Except for closing the stream!
            outStream.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}