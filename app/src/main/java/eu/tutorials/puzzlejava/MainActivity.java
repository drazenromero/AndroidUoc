package eu.tutorials.puzzlejava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import org.opencv.android.OpenCVLoader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

public class MainActivity extends AppCompatActivity {
     static {
         if(!OpenCVLoader.initDebug()){
             Log.d("ERROR","Unable to load OpenCV");
         }else{
             Log.d("SUCCES","OpenCV loaded");
         }
         //Log.d("INICIO_OPENCV","Inicio el Opencv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         File dir_to_clear = new File(getApplicationContext().getCacheDir().toString());
         cleanDir(dir_to_clear);
        try {
            checkCroppedSaved();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
    }
    private void cleanDir(File dir){
         float bytesDeleted = 0;
         File[] files = dir.listFiles();
         for(File file: files){
             file.delete();
             Log.d("FILES DELETE",file.getName());
         }
     }
    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
    private void checkCroppedSaved() throws IOException {

        File file_x = new File(String.format("%s/Images", getApplicationContext().getFilesDir().toString()));
        if (file_x.exists()) {
            deleteRecursive(file_x);
        } else {
            file_x.mkdirs();
            Log.d("FOLDER", "Folder Created");
        }
        String[] ListFolderImages;
        ListFolderImages = getApplicationContext().getAssets().list("ImagesPuzzle");
        for(String folderImage: ListFolderImages ){
            Log.d("FOLDER", "Folder Created");
            try{
                File folder_image_im = new File(String.format("%s/Images/%s",getApplicationContext().getFilesDir().toString(),folderImage.replace('.','-')));
                File folder_image_im_cropped;
                if(!folder_image_im.exists()){
                    folder_image_im.mkdirs();
                    folder_image_im_cropped = new File(String.format("%s/Images/%s/Cropped",getApplicationContext().getFilesDir().toString(),folderImage.replace('.','-')));
                    folder_image_im_cropped.mkdirs();

                }
                else{
                    folder_image_im_cropped = new File(String.format("%s/Images/%s/Cropped",getApplicationContext().getFilesDir().toString(),folderImage.replace('.','-')));
                    if(!folder_image_im_cropped.exists()){
                        folder_image_im_cropped.mkdirs();
                    }
                }
                File file_image_a = new File(String.format("%s/tmpImage",getApplicationContext().getCacheDir().toString()));
                file_image_a.createNewFile();
                InputStream is = getApplicationContext().getAssets().open(String.format("ImagesPuzzle/%s",folderImage));
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                FileOutputStream fos = new FileOutputStream(file_image_a);
                fos.write(buffer);
                fos.close();
                cropImagesPuzzle(folder_image_im_cropped,file_image_a,4);

            }catch(Exception e){
                throw e;
            }


        }

    }
    private void cropImagesPuzzle(File FolderCropped,File FileImage,int CantidadDiv){
        Mat image = Imgcodecs.imread(FileImage.getAbsolutePath());
        int height = image.height();
        int width = image.width();
        if(Math.sqrt(Double.valueOf(CantidadDiv)) - (int) Math.sqrt(Double.valueOf(CantidadDiv)) == 0 ){
            float resto_x = width %  (int) Math.sqrt(Double.valueOf(CantidadDiv));
            float width_r = width - resto_x;
            float width_div = width_r /  (int) Math.sqrt(Double.valueOf(CantidadDiv));

            float resto_y = height %  (int) Math.sqrt(Double.valueOf(CantidadDiv));
            float height_r = height - resto_y;
            float height_div = height_r /  (int) Math.sqrt(Double.valueOf(CantidadDiv));

            int cantidad_dividir_x = 0;
            int cantidad_dividir_y = 0;
            int row = 1;
            int step = 1;
            while(row <= (int) Math.sqrt(Double.valueOf(CantidadDiv))){
                int column = 1;
                cantidad_dividir_x = 0;
                while(column <= (int) Math.sqrt(Double.valueOf(CantidadDiv))){
                    Rect roi = new Rect(cantidad_dividir_x,cantidad_dividir_y,(int)width_div,(int)height_div);
                    Mat image_crop = new Mat(image,roi);
                    Imgcodecs.imwrite(String.format("%s/%s_%s.jpg",FolderCropped.getAbsolutePath(),row,column),image_crop);
                    cantidad_dividir_x += width_div;
                    column += 1;
                    step += 1;
                }
                row += 1;
                cantidad_dividir_y += height_div;
            }

        }
     }


}