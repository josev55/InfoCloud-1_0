package cl.colabra.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: josevildosola
 * Date: 05-08-13
 * Time: 9:38
 * To change this template use File | Settings | File Templates.
 */
public class AndroidUtils {
    public static void copy(String in, File out, Context context) throws IOException {
        InputStream inputStream = context.getAssets().open(in);
        FileOutputStream f = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = inputStream.read(buffer)) > 0) {
            f.write(buffer, 0, len1);
        }
        f.close();
    }

    public static void copy(InputStream in, File out, Context context) throws IOException {
        InputStream inputStream = in;
        FileOutputStream f = new FileOutputStream(out);
        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = inputStream.read(buffer)) > 0) {
            f.write(buffer, 0, len1);
        }
        f.close();
    }
}
