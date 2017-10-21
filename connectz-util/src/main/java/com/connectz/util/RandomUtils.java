package com.connectz.util;

import java.util.Random;

public class RandomUtils {
	public static String generateRandom(int length) {
		Random random = new Random();
		byte[] byteArray = new byte[length];
		int i;
		random.nextBytes(byteArray);
		for (i = 0; i < byteArray.length; i++) {
			if (byteArray[i] < 0)
				byteArray[i] *= -1;

			while (!((byteArray[i] >= 65 && byteArray[i] <= 90)
					|| (byteArray[i] >= 97 && byteArray[i] <= 122) || (byteArray[i] <= 57 && byteArray[i] >= 48))) {

				if (byteArray[i] > 122)
					byteArray[i] -= random.nextInt(byteArray[i]);
				if (byteArray[i] < 48)
					byteArray[i] += random.nextInt(5);
				else
					byteArray[i] += random.nextInt(10);
			}
		}
		return new String(byteArray);
	}
	
	public static String generateRandom(String prefix, int length) {
		return String.format("%s%s", prefix , generateRandom(length));
	}
}
