package ar.manumaanu.renamer;

import java.io.File;
import java.text.DecimalFormat;

public class FileRenamer {

	private File directory;

	/**
	 * Post: Sets the directory with the files to be renamed
	 * 
	 * @param directory
	 *            directory of the files to be renamed
	 */

	public FileRenamer(File directory) {

		this.directory = directory;

	}

	/**
	 * Post: Renames all the files that meets the conditions of 'fileNames'
	 * 
	 * @param fileNames
	 *            the name of the files with the joker(*)
	 * 
	 * @param serieName
	 *            the desired name for the files
	 * 
	 * 
	 * @return Returns the number of files renamed
	 */

	public int renameFiles(String fileNames, String serieName) {

		int filesRenamed = 0;

		for (File archivo : this.directory.listFiles()) {

			if (fileNames.length() <= archivo.getName().length()) {

				if (archivo.getName().contains(getFileCompare(fileNames))) {

					filesRenamed++;

					String obtenerExtencion = archivo.getName().substring(archivo.getName().length() - 4);

					File newFile = new File(
							this.directory + "/" + serieName + getNumberByToken(fileNames, archivo) + obtenerExtencion);
					archivo.renameTo(newFile);
				}
			}

		}

		return filesRenamed;
	}

	/**
	 * Post: Returns the directory of the files selected on the constructor
	 */

	public File getDirectory() {
		return this.directory;
	}

	public int countFiles() {

		int fileCount = 0;

		for (File archivo : this.directory.listFiles()) {
			if (!archivo.isDirectory()) {
				fileCount++;
			}
		}

		return fileCount;
	}

	/*
	 * Post: busca el token asignado '#!' en fileNames
	 */

	public int getTokenfromFileName(String fileNames, char requestToken) {

		int token = -1;

		for (int i = 0; i < fileNames.length(); i++) {
			if (fileNames.charAt(i) == requestToken) {
				token = i;
			}
		}

		if (token == -1) {

			throw new Error("Token no encontrado");
		}

		return token;
	}

	public String getFileCompare(String fileNames) {

		int token = getTokenfromFileName(fileNames, '*');

		return fileNames.substring(0, token - 1);

	}

	/*
	 * Pre: se debe haber encontrado el token de numero de capitulo
	 * 
	 * Post: devuelve el numero de capitulo del archivo actual
	 */

	private String getNumberByToken(String fileNames, File archivo) {

		String chapterNumber = "0";
		int token = getTokenfromFileName(fileNames, '*');

		if ((archivo.getName().charAt(token) == 'S' || archivo.getName().charAt(token) == 's')
				&& isNumber((archivo.getName().charAt(token + 1)))) {

			DecimalFormat decimalFormat = new DecimalFormat("00");
			String seasonNumber = "-1";
			String episodeNumber = "-1";
			int episodeToken = -1;
			boolean seasonNumberFound = false;

			for (int i = token + 3; i > token && !seasonNumberFound; i--) {
				seasonNumber = archivo.getName().substring(token + 1, i);
				if (isNumber(seasonNumber)) {
					seasonNumberFound = true;
					episodeToken = i;
					int aux = Integer.parseInt(seasonNumber);
					seasonNumber = decimalFormat.format(aux);
				}

			}

			if (seasonNumberFound && archivo.getName().charAt(episodeToken) == 'E'
					|| archivo.getName().charAt(episodeToken) == 'e') {
				boolean episodeNumberFound = false;
				for (int i = episodeToken + 3; i > episodeToken && !episodeNumberFound; i--) {
					episodeNumber = archivo.getName().substring(episodeToken + 1, i);
					if (isNumber(seasonNumber)) {
						episodeNumberFound = true;
						int aux = Integer.parseInt(episodeNumber);
						episodeNumber = decimalFormat.format(aux);
					}
				}
			}
			chapterNumber = "S" + seasonNumber + "E" + episodeNumber;
		} else {
			boolean chapterTokenFound = false;
			for (int i = token + 3; i > token && !chapterTokenFound; i--) {
				chapterNumber = archivo.getName().substring(token, i);
				if (isNumber(chapterNumber)) {
					chapterTokenFound = true;
				}
			}

		}

		return chapterNumber;
	}

	private boolean isNumber(String string) {
		try {
			Long.parseLong(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private boolean isNumber(char character) {
		try {
			String string = "" + character;
			Long.parseLong(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
