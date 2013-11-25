package bacci.giovanni.bio.sequencing.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class DigestedSequence {
	private byte md5[] = null;
	
	private DigestedSequence(byte[] md5){
		this.md5 = md5;
	}
	
	public static DigestedSequence newSeq(String seq){
		byte arr[] = null;
		try{
			arr = MessageDigest.getInstance("MD5").digest(seq.getBytes("UTF-8"));
		}catch(NoSuchAlgorithmException e){
			// TODO
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			// TODO
			e.printStackTrace();
		}
		return new DigestedSequence(arr);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DigestedSequence))return false;
		DigestedSequence that = (DigestedSequence)obj;		
		return Arrays.equals(this.md5, that.md5);
	}

	@Override
	public int hashCode() {
		int h = 1;
		for(int i = 0; i < md5.length; i++){
			h = 31 * h + md5[i];
		}
		return h;
	}
	
}
