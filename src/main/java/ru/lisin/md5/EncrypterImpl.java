package ru.lisin.md5;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class EncrypterImpl implements Encrypter {
	
	@Override
	public String getMessageHash(String message) {
		int[][] resultBinaryArray = DataUtility.getBinary(message);
		List<Byte> alignedBits = DataUtility.alignInputBits(message.getBytes());
		List<Byte> preparedBytes = DataUtility.add64ViewOfMessage(message.getBytes(), alignedBits);
		
		log.info("The input message bytes have been aligned to: {}", preparedBytes.size());

		List<List<Byte>> byteBlockList = splitMessageBytesToBlocks(preparedBytes);

		byte[] a = {(byte) 103, (byte) 69, (byte) 35, (byte) 1};
		byte[] b = {(byte) 127, (byte) 125, (byte) 115, (byte) 104};
		byte[] c = {(byte) 126, (byte) 119, (byte) 111, (byte) 87};
		byte[] d = {(byte) 95, (byte) 83, (byte) 78, (byte) 64};

		log.info("Getting a hash of the message");
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 16; ++j) {
				countHash(preparedBytes, byteBlockList, message);
			}
		}

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] digest = md5.digest(message.getBytes());

			BigInteger bigInt = new BigInteger(1, digest);
			String md5Hex = bigInt.toString(16);

			while( md5Hex.length() < 32 ){
				md5Hex = "0" + md5Hex;
			}
			return md5Hex;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private List<List<Byte>> splitMessageBytesToBlocks(List<Byte> messageBytes) {
		int blockSize = 64;
		List<List<Byte>> byteBlockList = new ArrayList<>();

		log.info("Splitting the message byte to byte blocks");

		if (messageBytes.size() != blockSize) {
			for (int i = 0; i < messageBytes.size() / blockSize; ++i) {
				List<Byte> byteBlock = new ArrayList<>();

				for (int j = 0; j < blockSize; ++j) {
					byteBlock.add(messageBytes.get(j));
					messageBytes.remove(j);
				}

				byteBlockList.add(byteBlock);
			}
		} else {
			byteBlockList.add(messageBytes);
		}

		return byteBlockList;
	}

	private void countHash(List<Byte> l1, List<List<Byte>> l2, String message) {
		byte[] a = {(byte) 103, (byte) 69, (byte) 35, (byte) 1};
		byte[] b = {(byte) 127, (byte) 125, (byte) 115, (byte) 104};
		byte[] c = {(byte) 126, (byte) 119, (byte) 111, (byte) 87};
		byte[] d = {(byte) 95, (byte) 83, (byte) 78, (byte) 64};

		byte[] inputBytes = message.getBytes();
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
	}
	
}
