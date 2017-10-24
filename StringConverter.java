
/**
 * Write a description of class StringConverter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.awt.*;
import java.io.File;
import squint.*;
import javax.swing.*;
import java.util.*;
public class StringConverter
{

    public StringConverter()
    {

    }

    public static int[] convert2Bits(String text)
    {
        int[] secretArray = new int[text.length()*8];

        int val = 0;

        int[] a = new int[8];
        for(int i=0; i < text.length(); ++i)
        {
            val = (int)text.charAt(i);
            a = convertFromInt(val);

            //System.out.println(val);
            for(int q=0; q <8; ++q)
            {
                secretArray[i*8 + q] = a[q];

            }

        }

        

        return secretArray;
    }

    public static String convert2String(int[] bits)
    {
        StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bits.length / 8; i++) 
		{
			int[] toConvert = new int[8];
			for (int j = 0; j < 8; j++) 
			{
				toConvert[j] = bits[i * 8 + j];
			}
			int val = convertToInt(toConvert);
			sb.append((char) val);
		}
		
		String str = sb.toString();
		
		int index = 0;
		for (int q = 0; q < str.length(); q++) {
			if (str.charAt(q) != 0) {
				index = q;
			}
		}
		return str.substring(0, index + 1);
		
		
    }

    private static int[] convertFromInt(int x)
    {
        
        int[] list= new int[8];
        int reminder = x;
        
        for(int u=7; u >=0; u--)
        {
            list[u] = x%2;
            x = x/2;
        }
        
        
        return list;
    }

    private static int convertToInt(int[] bits)
    {
        int result = 0;
		for (int i = 0; i < 8; i++) {
			result += bits[7 - i] * Math.pow(2, i);
		}
		return result;
    }
}