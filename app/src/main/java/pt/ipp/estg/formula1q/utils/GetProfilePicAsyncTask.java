package pt.ipp.estg.formula1q.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.net.URL;

public class GetProfilePicAsyncTask extends AsyncTask<Void, Void, Bitmap> {
    private FirebaseUser user;
    private ProfilePicListener listener;

    public interface ProfilePicListener {
        void onUserPhotoFetched(Bitmap bitmap);
    }

    public GetProfilePicAsyncTask(ProfilePicListener listener){
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        try {
            InputStream is = (InputStream) new URL(user.getPhotoUrl().toString()).getContent();
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            System.out.println("ERR " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        listener.onUserPhotoFetched(bitmapToCircle(bitmap));
    }

    private Bitmap bitmapToCircle(Bitmap bitmap){
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

        return circleBitmap;
    }
}