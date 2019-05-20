
package tailForJ;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Tail {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("Not Input FileName");
			return;
		}
		Tail tail = new Tail();
		tail.execute(args);
	}

	public void execute(String... args) {
		final String path = args[0];
		File file = new File(path);

		// File存在チェック
		if (!file.exists()) {
			System.out.println("File Not Found");
			return;
		}

		System.out.println("Tail START");
		System.out.println("File is " + file.getName());

		long fp;
		long len;
		RandomAccessFile reader;
		String line;
		try {
			while (true) {
				fp = 0;
				while (true) {
					try {
						Thread.sleep(1000);
						len = file.length();
						if (len < fp) {
							System.out.println("The File was reset");
							fp = len;
						} else if (len > fp) {
							reader = new RandomAccessFile(file, "r");
							reader.seek(fp);
							long point = fp;
							long returnPoint;
							while ((line = reader.readLine()) != null) {
								returnPoint = reader.getFilePointer();
								reader.seek(point);
								byte[] bytes = new byte[(int) (returnPoint - point)];
								reader.read(bytes);
								point = reader.getFilePointer();
								line = new String(bytes, "UTF-8");
								// 末尾の改行コードを除去
								while (line.endsWith("¥r") || line.endsWith("¥n"))
									line = line.substring(0, line.length() - 1);
								System.out.println(line);
							}
							fp = reader.getFilePointer();
							reader.close();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Tail END");
	}

}
