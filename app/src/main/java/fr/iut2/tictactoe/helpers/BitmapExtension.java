package fr.iut2.tictactoe.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class BitmapExtension {

    private static final Integer DEFAULT_BITMAP_QUALITY = 100;

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, DEFAULT_BITMAP_QUALITY, stream);

        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap fromBase64(String b64) {
        byte[] bytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap fromImageView(ImageView view) {
        return ((BitmapDrawable) view.getDrawable()).getBitmap();
    }
}
