package com.triplelands.utils;

import java.util.Vector;

import net.rim.device.api.crypto.MD5Digest;
import net.rim.device.api.system.DeviceInfo;

public class StringUtils {
	
	public static String getOSVersion(){
		String osversion;
		//#ifdef HANDHELD_VERSION_42
		osversion =  "4.2.0";
		//#else
		osversion =  DeviceInfo.getSoftwareVersion();
		//#endif
		return osversion;
	}
	
	private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
   
    public static String getMD5(String text) {
        byte[] bytes = text.getBytes();
        MD5Digest digest = new MD5Digest();
        digest.update(bytes, 0, bytes.length);
        int length = digest.getDigestLength();
        byte[] md5 = new byte[length];
        digest.getDigest(md5, 0, true);
        return convertToHex(md5);
   }
    
    public static String[] explode(char separator, String str) {
		Vector exploded = new Vector(0, 1);
		String tmpstr = null;
		int beginIndex = 0, endIndex = 0;
		while (endIndex < str.length()) {
			if (str.charAt(endIndex) == separator) {
				if (endIndex > beginIndex) {
					tmpstr = str.substring(beginIndex, endIndex);
					exploded.addElement(tmpstr);
					endIndex++;
					beginIndex = endIndex;
					tmpstr = null;
				} else {
					exploded.addElement("");
					endIndex++;
					beginIndex = endIndex;
				}
			} else {
				endIndex++;
			}
		}
		if (endIndex > beginIndex) {
			tmpstr = str.substring(beginIndex, endIndex);
			System.out.println("TMPSTR>: " + tmpstr);
			exploded.addElement(tmpstr);
		}
		String[] res = new String[exploded.size()];
		exploded.copyInto(res);
		return res;
	}
    
    public static String[] split(String strString, String strDelimiter)
	{
		int iOccurrences = 0;
		int iIndexOfInnerString = 0;
		int iIndexOfDelimiter = 0;
		int iCounter = 0;

		// Check for null input strings.
		if (strString == null)
		{
			throw new NullPointerException("Input string cannot be null.");
		}
		// Check for null or empty delimiter
		// strings.
		if (strDelimiter.length() <= 0 || strDelimiter == null)
		{
			throw new NullPointerException("Delimeter cannot be null or empty.");
		}

		// If strString begins with delimiter
		// then remove it in
		// order
		// to comply with the desired format.

		if (strString.startsWith(strDelimiter))
		{
			strString = strString.substring(strDelimiter.length());
		}

		// If strString does not end with the
		// delimiter then add it
		// to the string in order to comply with
		// the desired format.
		if (!strString.endsWith(strDelimiter))
		{
			strString += strDelimiter;
		}

		// Count occurrences of the delimiter in
		// the string.
		// Occurrences should be the same amount
		// of inner strings.
		while((iIndexOfDelimiter= strString.indexOf(strDelimiter,iIndexOfInnerString))!=-1)
		{
			iOccurrences += 1;
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
		}

		// Declare the array with the correct
		// size.
		String[] strArray = new String[iOccurrences];

		// Reset the indices.
		iIndexOfInnerString = 0;
		iIndexOfDelimiter = 0;

		// Walk across the string again and this
		// time add the
		// strings to the array.
		while((iIndexOfDelimiter= strString.indexOf(strDelimiter,iIndexOfInnerString))!=-1)
		{

			// Add string to
			// array.
			strArray[iCounter] = strString.substring(iIndexOfInnerString, iIndexOfDelimiter);

			// Increment the
			// index to the next
			// character after
			// the next
			// delimiter.
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();

			// Inc the counter.
			iCounter += 1;
		}
            return strArray;
	}
    
    public static String replace(String source, String pattern, String replacement)
	{	
	
		//If source is null then Stop
		//and return empty String.
		if (source == null)
		{
			return "";
		}

		StringBuffer sb = new StringBuffer();
		//Intialize Index to -1
		//to check against it later 
		int idx = -1;
		//Intialize pattern Index
		int patIdx = 0;
		//Search source from 0 to first occurrence of pattern
		//Set Idx equal to index at which pattern is found.
		idx = source.indexOf(pattern, patIdx);
		//If Pattern is found, idx will not be -1 anymore.
		if (idx != -1)
		{
			//append all the string in source till the pattern starts.
			sb.append(source.substring(patIdx, idx));
			//append replacement of the pattern.
			sb.append(replacement);
			//Increase the value of patIdx
			//till the end of the pattern
			patIdx = idx + pattern.length();
			//Append remaining string to the String Buffer.
			sb.append(source.substring(patIdx));
		}
		//Return StringBuffer as a String

                if ( sb.length() == 0)
                {
                    return source;
                }
                else
                {
                    return sb.toString();
                }
	}
    
    public static String replaceAll(String source, String pattern, String replacement)
    {    

        //If source is null then Stop
        //and retutn empty String.
        if (source == null)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        //Intialize Index to -1
        //to check agaist it later 
        int idx = -1;
        //Search source from 0 to first occurrence of pattern
        //Set Idx equal to index at which pattern is found.

        String workingSource = source;
        
        //Iterate for the Pattern till idx is not be -1.
        while ((idx = workingSource.indexOf(pattern)) != -1)
        {
            //append all the string in source till the pattern starts.
            sb.append(workingSource.substring(0, idx));
            //append replacement of the pattern.
            sb.append(replacement);
            //Append remaining string to the String Buffer.
            sb.append(workingSource.substring(idx + pattern.length()));
            
            //Store the updated String and check again.
            workingSource = sb.toString();
            
            //Reset the StringBuffer.
            sb.delete(0, sb.length());
        }

        return workingSource;
    }
}
