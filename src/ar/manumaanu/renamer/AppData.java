package ar.manumaanu.renamer;

import java.io.File;

public class AppData {

	private String OS = System.getProperty("os.name").toLowerCase();
	private String ourAppDirectory;

	public AppData(String directory) {
		this.ourAppDirectory = getAppDataFolder() + "/" + directory;
		createFolder();
	}

	private void createFolder() {
		File ourAppDataFolder = new File(ourAppDirectory);

		if (!ourAppDataFolder.exists()) {
			ourAppDataFolder.mkdir();
		}

	}

	private String validateOS() {

		String currentOS = "DOS";

		if (OS.contains("win")) {
			currentOS = "Windows";
		} else if (OS.contains("mac")) {
			currentOS = "Mac";
		} else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
			currentOS = "Linux";
		}

		System.out.println("Current Operative System: " + currentOS);

		return currentOS;
	}

	private String getAppDataFolder() {
		String appDataFolder = "";
		String currentOS = validateOS();

		if (currentOS == "Windows") {
			appDataFolder = System.getenv("AppData");
		} else if (currentOS == "Linux") {
			appDataFolder = System.getProperty("user.home");
		} else {
			appDataFolder = System.getProperty("user.home") + "/Library/Application Support";

		}

		return appDataFolder;

	}

	public String getOurAppDataFolder() {
		return this.ourAppDirectory;
	}

}
