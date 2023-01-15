package ConsoleEngine.Utilities;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Class for Zip Utilities
 */
public class ZipUtilities {

	private static ZipUtilities instance;

	private File fileToGet;


	public static ZipUtilities getInstance() {
		if (instance == null) {
			instance = new ZipUtilities();
		}
		return instance;
	}

	/**
	 * Compress a file to a zip
	 * 
	 * @param filename
	 */
	public void compressFile(String filename) {
		File file = new File(filename);
		String zipFilename = filename  + ".zip";
		File zipFileName = new File(zipFilename);
		List<File> fileList = new ArrayList<File>();
		fileList.add(file);

		try {
			FileOutputStream fos = new FileOutputStream(zipFilename);
			ZipOutputStream zos = new ZipOutputStream(fos, Charset.forName("windows-1251"));

			for (File file1 : fileList) {
				if (!file1.isDirectory()) { // we only zip directory, not
											// directories
					addToZip(zipFileName, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String unzip(File inputFile) throws IOException {
		String newFileName = "";
		File destDir = new File(inputFile.getParent());
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(inputFile.getAbsolutePath()));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry);
			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					throw new IOException("Failed to create directory " + newFile);
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile();
				newFileName = newFile.getAbsolutePath();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
		return newFileName;
	}
	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());
		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}
	/**
	 * Compress a given directory recursively and store the zip in the provided
	 * directory name
	 * 
	 * @param fileDirectory
	 */
	public void compressDirectory(String fileDirectory,
			String savedZipFileDirectory) {
		File directoryToZip = new File(fileDirectory);

		List<File> fileList = new ArrayList<File>();
		try {
			System.out.println("---Getting references to all directory in: "
					+ directoryToZip.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getAllFiles(directoryToZip, fileList);
		System.out.println("---Creating zip file");
		String folder = savedZipFileDirectory + File.separator
				+ directoryToZip.getName();

		writeZipFile(folder, directoryToZip, fileList);
		System.out.println("---Done");
	}

	/**
	 * Uncompress a zip file
	 * @param zipFileName
	 */
	public void unCompressZipFile(String zipFileName) {
		try {
			ZipFile zipFile = new ZipFile(zipFileName);
			Enumeration<?> enu = zipFile.entries();

			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();

				String name = zipEntry.getName();
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				System.out.printf(
						"name: %-20s | size: %6d | compressed size: %6d\n",
						name, size, compressedSize);

				File file = new File(name);
				if (name.endsWith("/")) {
					file.mkdirs();
					continue;
				}

				File parent = file.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}

				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				is.close();
				fos.close();

			}
			zipFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read all the files recursively from the directory
	 * @param dir
	 * @param fileList
	 */
	private void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileList.add(file);
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					System.out.println("file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * See the contents of a zip file
	 */
	public void seeContentOfZipFile(String zipfile) {
		try {
			ZipFile zipFile = new ZipFile(zipfile);
			Enumeration<?> enu = zipFile.entries();
			while (enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				String name = zipEntry.getName();
				long size = zipEntry.getSize();
				long compressedSize = zipEntry.getCompressedSize();
				System.out.printf(
						"name: %-20s | size: %6d | compressed size: %6d\n",
						name, size, compressedSize);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the zip file
	 * @param directoryToZip
	 * @param fileList
	 */
	private void writeZipFile(String folder, File directoryToZip,
			List<File> fileList) {

		try {
			FileOutputStream fos = new FileOutputStream(folder + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip directory, not
											// directories
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a file to the zip
	 * @param zipfilename
	 * @param file
	 * @param zos
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void addToZip(File zipfilename, File file, ZipOutputStream zos)
			throws FileNotFoundException, IOException {

		FileInputStream fis = new FileInputStream(file);

		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath = file.getName();
		System.out.println("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	/**
	 * Compress a given list of Files to the given zipped file name
	 * @param fileList
	 * @param zipfileName
	 */

	public void compressFiles(List<File> fileList, String zipfileName) {

		File zip = new File(zipfileName);
		try {
			FileOutputStream fos = new FileOutputStream(zip);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file1 : fileList) {
				if (!file1.isDirectory()) { // we only zip directory, not
											// directories
					addToZip(zip, file1, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
