package learn2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleFileServer {

	static File inputFile;
	static FileInputStream inputStream;
	static String newFileName;
	static FileOutputStream filePart;
	private static byte PART_SIZE = 2;

	public final static int SOCKET_PORT = 13267; // you may change this
	// public final static String FILE_TO_SEND = "F:/sokibot.zip";

	public static void main(String[] args) throws IOException {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		ServerSocket servsock = null;
		Socket sock = null;
		try {
			servsock = new ServerSocket(SOCKET_PORT);
			while (true) {
				System.out.println("Waiting...");
				try {
					sock = servsock.accept();
					System.out.println("Accepted connection : " + sock);
					// send file
					Scanner scanner = new Scanner(System.in);
					System.out.print("Masukan path file (F:/sokibot.zip) : ");
					String FILE_TO_SEND = scanner.nextLine();
					File myFile = new File(FILE_TO_SEND);
					byte[] mybytearray = new byte[(int) myFile.length()];
					fis = new FileInputStream(myFile);
					bis = new BufferedInputStream(fis);
					bis.read(mybytearray, 0, mybytearray.length);
					os = sock.getOutputStream();
					System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
					os.write(mybytearray, 0, mybytearray.length);
					os.flush();
					System.out.println("Done.");
				} finally {
					if (bis != null)
						bis.close();
					if (os != null)
						os.close();
					if (sock != null)
						sock.close();
				}
			}
		} finally {
			if (servsock != null)
				servsock.close();
		}
	}

	/*public static void splitFile() {
		int fileSize = (int) inputFile.length();
		System.out.println("filesize : " + fileSize);
		System.out.println(inputFile.getName());
		int nChunks = 0, read = 0, readLength = PART_SIZE;
		byte[] byteChunkPart;
		try {
			inputStream = new FileInputStream(inputFile);
			while (fileSize > 0) {
				System.out.println(nChunks);
				if (fileSize <= 10) {
					readLength = fileSize;
				}
				byteChunkPart = new byte[readLength];
				read = inputStream.read(byteChunkPart, 0, readLength);
				fileSize -= read;
				assert (read == byteChunkPart.length);
				nChunks++;
				newFileName = resultSplit + fileName + ".part" + Integer.toString(nChunks - 1);
				filePart = new FileOutputStream(new File(newFileName));
				filePart.write(byteChunkPart);
				filePart.flush();
				filePart.close();
				byteChunkPart = null;
				filePart = null;
			}
			System.out.println(nChunks);
			inputStream.close();
			System.out.println("SELESAI");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}*/
}