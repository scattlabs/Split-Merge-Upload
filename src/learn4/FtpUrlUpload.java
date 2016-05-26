package learn4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class FtpUrlUpload {

	private static final int BUFFER_SIZE = 4096;

	public static void main(String[] args) {
		String ftpUrl = "ftp://172.18.2.75";
		String host = "";
		String user = "swahp";
		String pass = "103040132";
		String filePath = "F:/test.txt";
		String uploadPath = "test.txt";

		ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
		System.out.println("Upload URL: " + ftpUrl);

		try {
			URL url = new URL(ftpUrl);
			URLConnection conn = url.openConnection();
			OutputStream outputStream = conn.getOutputStream();
			FileInputStream inputStream = new FileInputStream(filePath);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outputStream.close();

			System.out.println("File uploaded");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}