package utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.john.nguyen.minnanonihongovocabulary.BuildConfig;

/**
 * Created by PC on 7/1/2016.
 */
public class Util {
    private static boolean mShowLog  = BuildConfig.DEBUG;
    private static String TAG = "JohnNguyen";

    public static void LOG(String str){
        if(mShowLog) {
            Log.d(TAG, str);
        }
    }

    public static void showToast(Context context, int stringID){
        showToast(context, context.getString(stringID));
    }

    public static void showToast(Context context, String string){
        Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, heightPixels/5);
        toast.show();
    }

    //read json from assets folder
    public static String readAssetJSONFile (Context context, String jsonFile) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(jsonFile);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        return new String(formArray);
    }

    //get Drawable from file in assets folder
    public static Drawable getDrawableFromAssets(Context context, String imagePath) {
        try {
            // get input stream
            InputStream ims = context.getAssets().open(imagePath);
            // load image as Drawable
            Drawable drawable = Drawable.createFromStream(ims, null);
            return drawable;
        } catch (IOException ex) {
            return null;
        }
    }

    public static void extractZipFiles(String zipFile, String destFolder) throws IOException {
        Util.LOG("Extract from " + zipFile);
        Util.LOG("to " + destFolder);
        BufferedOutputStream dest = null;
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(
                        new FileInputStream(zipFile)));
        ZipEntry entry;
        while (( entry = zis.getNextEntry() ) != null) {
            LOG("Extracting: " + entry.getName());
            int count;
            byte data[] = new byte[4096];

            if (entry.isDirectory()) {
                new File( destFolder + "/" + entry.getName() ).mkdirs();
                continue;
            } else {
                int di = entry.getName().lastIndexOf( '/' );
                if (di != -1) {
                    new File( destFolder + "/" + entry.getName()
                            .substring( 0, di ) ).mkdirs();
                }
            }
            FileOutputStream fos = new FileOutputStream( destFolder + "/"
                    + entry.getName() );
            dest = new BufferedOutputStream( fos );
            while (( count = zis.read( data ) ) != -1)
                dest.write( data, 0, count );
            dest.flush();
            dest.close();
        }
    }

    public static String readFile(String path){
        String content = "";
        FileReader fileReader = null;
        try{
            fileReader = new FileReader(path);
            int ch;
            while((ch = fileReader.read()) != -1){
                content += (char)ch;
            }

        }
        catch (Exception e){
            if(fileReader != null){
                try {
                    fileReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            Util.LOG("Can not read file: " + path);
            e.printStackTrace();
        }

        return content;
    }

    public static void deleteFile(String path){
        LOG("Delete file: " + path);
        File file = new File(path);
        if(!file.delete()){
            LOG("Delete ERROR: " + path);
        }
    }

    public static void exitApp(){
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
