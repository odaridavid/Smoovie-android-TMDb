package smoovie.apps.com.kayatech.smoovie.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created By blackcoder
 * On 03/05/19
 **/
public class PaletteExtractorUtil {

    private static Palette createPalette(Bitmap bitmap) {
        return Palette
                .from(bitmap)
                .generate();
    }

    public static Palette.Swatch getDarkVibrantColor(Bitmap bitmap) {
        return createPalette(bitmap).getDarkVibrantSwatch();
    }

    public static Bitmap getBitmapFromUrl(String src) {
        try {
            URL vURL = new URL(src);
            HttpURLConnection vConnection = (HttpURLConnection) vURL.openConnection();
            vConnection.setDoInput(true);
            vConnection.connect();
            InputStream vInputStream = vConnection.getInputStream();
            return BitmapFactory.decodeStream(vInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
