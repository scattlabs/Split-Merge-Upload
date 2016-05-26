package learn3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUploadFileDemo {

	/*
	 * static String server = "172.18.2.91"; static int port = 22; static String
	 * user = "soki"; static String pass = "soki2014";
	 */

	static String server = "172.18.2.75";
	static int port = 21;
	static String user = "swahp";
	static String pass = "103040132";

	public static FTPClient connectFTP() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			int reply = ftpClient.getReplyCode();
			if (FTPReply.isPositiveCompletion(reply)) {
				if (ftpClient.login(user, pass)) {
					System.out.println("connect");
				} else {
					System.out.println("not connected");
					System.exit(0);
				}
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			} else {
				System.out.println("masuk else reply");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("e : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("e2 : " + e.getMessage());
			// TODO: handle exception
		}

		return ftpClient;
	}

	public static void splitFile(File inputFile) {
		int fileSize = (int) inputFile.length();
		int nChunks = 0, read = 0, readLength = 1000;

		byte[] byteChunkPart;
		try {
			FileInputStream fileInputStream = new FileInputStream(inputFile);
			System.out.println("fileSize : " + fileSize);
			while (fileSize > 0) {
				System.out.println("nChunks : " + nChunks);
				if (fileSize <= readLength) {
					readLength = fileSize;
				}
				byteChunkPart = new byte[readLength];
				read = fileInputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;
				assert (read == byteChunkPart.length);
				uploadFile(connectFTP(), byteChunkPart, inputFile.getName() + ".part" + Integer.toString(nChunks));
				// uploadFile(connectFTP(), byteChunkPart, inputFile.getName());
				nChunks++;
				byteChunkPart = null;
			}
			System.out.println(nChunks);
			fileInputStream.close();
			System.out.println("SELESAI");
			Scanner scanner = new Scanner(System.in);
			System.out.print("Merge File : y/n?");
			String comfirm = scanner.nextLine();
			if (comfirm.equals("y")) {
				mergeFile("D:/FTP/test.txt", nChunks);
			}
		} catch (IOException exception) {
			System.out.println("ex :" + exception.getMessage());
			exception.printStackTrace();
		}
	}

	public static void uploadFile(FTPClient ftpClient, byte[] byteChunkPart, String secondRemoteFile) {
		try {
			System.out.println(secondRemoteFile);
			System.out.println("Start uploading second file");
			// OutputStream outputStream =
			// ftpClient.storeFileStream(secondRemoteFile);
			// System.out.println("create directory : " +
			// ftpClient.makeDirectory("TestCreateDirectoryy"));

			if (ftpClient == null) {
				System.out.println("ftp client null");
			} else {
				System.out.println("ftp client not null");
			}
			OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
			// System.out.println("sini : " + ftpClient.getReplyString());
			if (outputStream == null) {
				System.out.println("output stream is null");
			} else {
				System.out.println("output stream is not null");
			}
			outputStream.write(byteChunkPart);
			outputStream.close();
			boolean completed = ftpClient.completePendingCommand();
			if (completed) {
				System.out.println("The second file is uploaded successfully.");
			}

		} catch (IOException ex) {
			// System.out.println("Error: " + ex.getMessage());
			// ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				// System.out.println("ex : " + ex.getMessage());
				// ex.printStackTrace();
			}
		}
	}

	public static void mergeFile(String fileName, int nChunks) {
		File ofile = new File(fileName);
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		List<File> list = new ArrayList<File>();
		for (int i = 0; i < nChunks; i++) {
			list.add(new File(fileName + ".part" + i + ""));
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
			System.out.println("ex : " + exception.getMessage());
			exception.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("MASUKAN FILE PATH YANG AKAN DI UPLOAD : ");
		String fileUploadName = scanner.nextLine();
		File inputFile = new File(fileUploadName);
		splitFile(inputFile);

	}

}