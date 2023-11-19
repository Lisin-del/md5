package ru.lisin.md5;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class Application {

	public static void main(String[] args) {
		String inputMessage = "Some input message to get the hash of this message + another 12345 tex";
		Encrypter encrypter = new EncrypterImpl();
		String messageHash = encrypter.getMessageHash(inputMessage);
		log.info("Message hash is {}", messageHash);
	}

}
