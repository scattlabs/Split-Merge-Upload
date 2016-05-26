package learn1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SplitFileExample {
	// private static String FILE_NAME = "F:\\repo.txt";
	private static String FILE_NAME = "F:\\test.txt";

	private static String resultSplit = "C:\\TEMP\\";
	private static int PART_SIZE = 1; // 1kb = 1000byte

	static File inputFile;
	static String fileName;
	static FileInputStream fileInputStream;
	static String newFileName;
	static FileOutputStream fileOutputPart;

	private static FileOutputStream fos;

	public static void checkAndCreateFolder() {
		File file = new File(resultSplit);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}

	public static void main(String[] args) throws IOException {
		inputFile = new File(FILE_NAME);
		fileName = inputFile.getName();
		// splitFile();
		// mergeFile();

		// String content = "wahyudin scatt ganteung pisan sumpah demi";
		// byte[] fileBytes = content.getBytes();
		// System.out.println(fileBytes.toString());
		// System.out.println(new String(fileBytes));
		// File ofile = new File(resultSplit + fileName);
		// fos = new FileOutputStream(ofile, true);
		// fos.write(content.getBytes());
		// fos.flush();

		LiveSplitFile();

	}

	public static void mergeFile() {
		File ofile = new File(resultSplit + fileName);
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		int partSize = 11; // jumlah part yang di split
		List<File> list = new ArrayList<File>();
		for (int i = 0; i < partSize; i++) {
			list.add(new File(resultSplit + fileName + ".part" + i + ""));
		}
		try {
			fos = new FileOutputStream(ofile, true);
			// merge file
			for (File file : list) {
				fis = new FileInputStream(file);
				fileBytes = new byte[(int) file.length()];
				bytesRead = fis.read(fileBytes, 0, (int) file.length());
				assert (bytesRead == fileBytes.length);
				assert (bytesRead == (int) file.length());
				fos.write(fileBytes);
				fos.flush();
				fileBytes = null;
				fis.close();
				fis = null;
			}
			// delete file part
			for (File file : list) {
				file.delete();
			}
			fos.close();
			fos = null;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void splitFile() {
		int fileSize = (int) inputFile.length();
		int nChunks = 0, read = 0, readLength = PART_SIZE;

		byte[] byteChunkPart;
		try {
			fileInputStream = new FileInputStream(inputFile);
			while (fileSize > 0) {
				if (fileSize <= 10) {
					readLength = fileSize;
				}
				byteChunkPart = new byte[readLength];
				read = fileInputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;
				assert (read == byteChunkPart.length);
				nChunks++;
				newFileName = resultSplit + fileName + ".part" + Integer.toString(nChunks - 1);
				fileOutputPart = new FileOutputStream(new File(newFileName));
				fileOutputPart.write(byteChunkPart);
				fileOutputPart.flush();
				fileOutputPart.close();
				byteChunkPart = null;
				fileOutputPart = null;
			}
			System.out.println(nChunks);
			fileInputStream.close();
			System.out.println("SELESAI");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void LiveSplitFile() {
		int fileSize = (int) inputFile.length();
		int nChunks = 0, read = 0, readLength = PART_SIZE;
		int breakPart = -1;
		int nextPart = 3;
		byte[] byteChunkPart;
		try {
			fileInputStream = new FileInputStream(inputFile);
			while (fileSize > 0) {
				if (breakPart != nChunks) {
					if (fileSize <= 1) {
						readLength = fileSize;
					}
					byteChunkPart = new byte[readLength];
					read = fileInputStream.read(byteChunkPart, 0, readLength);
					fileSize -= read;
					assert (read == byteChunkPart.length);
					if (nChunks >= nextPart) {
						LiveMergeFile(byteChunkPart);
					}
					byteChunkPart = null;
				} else {
					break;
				}
				nChunks++;
			}
			System.out.println(nChunks);
			fileInputStream.close();
			System.out.println("SELESAI");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void LiveMergeFile(byte[] fileBytes) {
		System.out.println(new String(fileBytes));
		File ofile = new File(resultSplit + fileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(ofile, true);
			// merge file
			fos.write(fileBytes);
			fos.flush();
			fileBytes = null;
			fos.close();
			fos = null;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
