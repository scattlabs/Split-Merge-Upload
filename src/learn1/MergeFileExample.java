package learn1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MergeFileExample {
	private static String FILE_NAME_SPLIT = "C:\\USPLIT\\";
	private static String NAME = "split.txt";
	private static String FILE_NAME = "";

	public static void main(String[] args) {
		FILE_NAME = FILE_NAME_SPLIT + NAME;
		System.out.println(FILE_NAME);
		File ofile = new File(FILE_NAME);
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		int partSize = 4; // jumlah part yang di split
		List<File> list = new ArrayList<File>();
		for (int i = 0; i < partSize; i++) {
			list.add(new File(FILE_NAME + ".part" + i + ""));
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
}
