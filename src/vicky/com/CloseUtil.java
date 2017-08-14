package vicky.com;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {
	public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
