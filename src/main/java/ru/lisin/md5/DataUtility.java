package ru.lisin.md5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataUtility {
	private static final int REMAINDER = 448;

	/*
	 * Converts an input message to a binary array.
	 */
	public static int[][] getBinary(String inputMessage) {
		byte[] inputBytes = inputMessage.getBytes();
		int[] binaryArray = new int[inputBytes.length * 8];
		int[][] arrayBlock = new int[inputBytes.length][8];
		
		for (int i = 0; i < inputBytes.length; ++i) {
			int counter = 7;
			int[] binaryBlock = new int[8];	
			int byteValue = (int) inputBytes[i];
			int binaryBlockCounter = 0;
			
			while (byteValue > 0) {
				int remainder = byteValue % 2;
				byteValue = byteValue / 2;
				
				if (remainder > 0) {
					binaryBlock[counter] = 1;
				} else {
					binaryArray[counter] = 0;
				}
				--counter;
				++binaryBlockCounter;
			}
			
			if (binaryBlockCounter != 8) {
				binaryArray[counter] = 0;
				--counter;
				++binaryBlockCounter;
			}
			
			arrayBlock[i] = binaryBlock;
		}
		
		return arrayBlock;
	}
	
	/*
	 * Converts an input array of bits to a list of bits. Aligns the length of the input bits.
	 */
	public static List<Byte> alignInputBits(byte[] inputBytes) {
		List<Byte> inputByteList = new ArrayList<>();
		
		for (int i = 0; i < inputBytes.length; ++i) {
			inputByteList.add(inputBytes[i]);
		}
		
		// add 1 bit to the list
		inputByteList.add((byte) 1);
		
		while (!((inputByteList.size() % 64) == 56)) {
			inputByteList.add((byte) 0);
		}
		
		return inputByteList;
	}
	
	public static List<Byte> add64ViewOfMessage(byte[] messageBytes, List<Byte> alignedMessageBytes) {
		List<Byte> additional64Bytes = new ArrayList<>();
		additional64Bytes.add((byte) messageBytes.length);
		
		int necessaryNullBits = 8 - additional64Bytes.size();
		
		for (int i = 0; i < necessaryNullBits; ++i) {
			additional64Bytes.add((byte) 0);
		}
			
		Collections.reverse(additional64Bytes);
		alignedMessageBytes.addAll(additional64Bytes);
		return alignedMessageBytes;
	}

}
