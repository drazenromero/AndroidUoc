package eu.tutorials.puzzlejava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener,View.OnDragListener {
     static {
         if(!OpenCVLoader.initDebug()){
             Log.d("ERROR","Unable to load OpenCV");
         }else{
             Log.d("SUCCES","OpenCV loaded");
         }
         //Log.d("INICIO_OPENCV","Inicio el Opencv");
    }
    public dataImageview actual_dataImageview_on_long_click;
     public dataImageview actual_dataImageview_on_entered_drag;
    class MyDragListener implements View.OnDragListener {


        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:

                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //Log.d("ACTION_DRAG_DROG2", "ACTION_DRAG_ENDED");
                    dataImageview datac = (dataImageview)v.getTag();
                    //Log.d("action_tag_imageview", datac.ImaAct);
                default:
                    break;
            }
            return true;
        }
    }
    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        //Log.d("ACTION_DRAG_DROG", String.valueOf(dragEvent.getAction()));
         if(dragEvent.getAction() == DragEvent.ACTION_DRAG_STARTED){
             //Log.d("ACTION_DRAG_DROG","ACTION_DRAG_STARTED");
             return true;
         }
         else if(dragEvent.getAction() == DragEvent.ACTION_DRAG_ENTERED){
             Log.d("ACTION_DRAG_DROG","ACTION_DRAG_ENTERED");

             dataImageview datac = (dataImageview)view.getTag();
             actual_dataImageview_on_entered_drag = datac;
             //Log.d("action_tag_imageview", datac.ImaAct);
             return true;
         }
         else if(dragEvent.getAction() == DragEvent.ACTION_DRAG_LOCATION) {
             //Log.d("ACTION_DRAG_DROG", "ACTION_DRAG_LOCATION");
             return true;
         }else if(dragEvent.getAction() ==DragEvent.ACTION_DRAG_EXITED) {
            //Log.d("ACTION_DRAG_DROG", "ACTION_DRAG_EXITED");
            return true;
         }
         else if(dragEvent.getAction() == DragEvent.ACTION_DRAG_ENDED) {
             //Log.d("ACTION_DRAG_DROG", "ACTION_DRAG_ENDED");
             dataImageview datac = (dataImageview)view.getTag();
             //Log.d("action_tag_imageview", datac.ImaAct);
             if(datac.ImaCorr.equals(actual_dataImageview_on_entered_drag.ImaCorr)) {
                 if (actual_dataImageview_on_long_click.ImaAct.equals(actual_dataImageview_on_entered_drag.ImaCorr)) {
                     Log.d("ImaCorrect", actual_dataImageview_on_entered_drag.ImaCorr);
                 }
             }

             return true;
         }
         else if( dragEvent.getAction() == DragEvent.ACTION_DROP) {
             //Log.d("ACTION_DRAG_DROG", "ACTION_DROP");
            return true;
         }
         else{
             return false;
         }


    }

    public class MyDragShadowBuilder extends View.DragShadowBuilder {

        public MyDragShadowBuilder(View view) {
            super(view);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            // get width, height
            int width = getView().getWidth();
            int height = getView().getHeight();

            // set shadow
            shadowSize.set(width, height);
            shadowTouchPoint.set(width/2, height/2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            super.onDrawShadow(canvas);
        }
    }
    public class dataImageview{
         public String ImaAct;
         public String ImaCorr;
         dataImageview(){

         }
    }


    private View action_bar;
    private ImageButton button_options;
    private ImageButton button_help;
    private ImageButton button_game;
    private WebView web_view_help;
    private NavigationView NavigationOptions;
    private TableLayout GameLayout;
    private LinearLayout HelpLayout;

    private Thread threadTimeJava;
    private ThreadTimeObjectSync threadTimeObjectSync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         File dir_to_clear = new File(getApplicationContext().getCacheDir().toString());
         cleanDir(dir_to_clear);




        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

        try {
            //checkCroppedSaved();
            starPuzzle();
        } catch (Exception e) {
            e.printStackTrace();
        }

         NavigationOptions = (NavigationView) findViewById(R.id.NavigationOptions);
        //NavigationOptions.setVisibility(View.GONE);
        //NavigationOptions.setVisibility(View.VISIBLE);

        action_bar = (View)findViewById(R.id.appbarinclude);
        button_options = action_bar.findViewById(R.id.barOptions);
        button_options.setOnClickListener(this);

        button_help = action_bar.findViewById(R.id.barHelp);
        button_help.setOnClickListener(this);

        button_game = action_bar.findViewById(R.id.barGame);
        button_game.setOnClickListener(this);

        web_view_help = findViewById(R.id.WebViewHelp);
        web_view_help.loadUrl("file:///android_asset/help/help_page/index.html");
        GameLayout = (TableLayout) findViewById(R.id.GameLayout);
        HelpLayout = (LinearLayout) findViewById(R.id.HelpLayout);

        threadTimeObjectSync = new ThreadTimeObjectSync();

        threadTimeJava = new ThreadTime(threadTimeObjectSync,this);
        threadTimeJava.start();

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
    private void starPuzzle() throws IOException {
        checkCroppedSaved();

        createImageViewsPuzzle("2B.jpg");
    }
    private void createImageViewsPuzzle(String NameImage){
        File folder_cropped = new File(String.format("%s/Images", getApplicationContext().getFilesDir().toString()));
        if(!folder_cropped.exists()){
            try {
                checkCroppedSaved();
            }
            catch(Exception e){

            }
        }
        String searchName = NameImage.replace('.','-');
        for(String ImageN: folder_cropped.list()){
            if(searchName.equals(ImageN)){
                Toast.makeText(MainActivity.this,String.format("Encontrada Imagen %s",searchName),Toast.LENGTH_LONG).show();
                File folder_copped_images = new File(String.format("%s/Images/%s/Cropped", getApplicationContext().getFilesDir().toString(),searchName));
                String[] listCroppedImages = folder_copped_images.list();
                if(listCroppedImages.length > 0){
                    try {
                        View game_container = (View) findViewById(R.id.GameLayout);
                        Handler handler = new Handler();
//                        TableLayout container_images = (TableLayout) findViewById(R.id.TablePuzzleContainer);
//                        TableRow tableRow = new TableRow(getApplicationContext());
//                        //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(500,500);
//                        //tableRow.setLayoutParams(parms);
//                        //tableRow.setLayoutParams(layoutParams);
//                        //tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, 100));
//                        tableRow.setMinimumHeight(100);
//                        //tableRow.setWeightSum(1F);
//                        tableRow.setBackgroundColor(Color.GREEN);
//                        container_images.addView(tableRow);
//                        TextView txt = new TextView(getApplicationContext());
//                        //txt.setLayoutParams(parms);
//                        txt.setText("dddd");
//                        txt.setHeight(200);
//                        tableRow.addView(txt);
//
//                        TableRow tableRow2 = new TableRow(getApplicationContext());
//                        //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(500,500);
//                        //tableRow.setLayoutParams(parms);
//                        //tableRow.setLayoutParams(layoutParams);
//                        tableRow2.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
//                        //tableRow.setWeightSum(1F);
//                        tableRow2.setBackgroundColor(Color.MAGENTA);
//                        container_images.addView(tableRow2);
//                        /*TextView txt2 = new TextView(getApplicationContext());
//                        //txt.setLayoutParams(parms);
//                        txt2.setText("dddd222222");
//                        tableRow2.addView(txt2);*/
//                        ImageView imagee = new ImageView(getApplicationContext());
//                        Bitmap bmImg = BitmapFactory.decodeFile(String.format("%s/Images/%s/Cropped/1_1.jpg", getApplicationContext().getFilesDir().toString(), searchName));
//                        imagee.setImageBitmap(bmImg);
//                        tableRow2.addView(imagee);
                        /*ImageView x = new ImageView(getApplicationContext());
                        x.setOnLongClickListener(this::onLongClick);*/
                        MainActivity _this = this;
                        game_container.post(new Runnable() {
                            @Override
                            public void run() {
                                game_container.getHeight();//height is ready
                                //File image_file = readFileForOpencv(String.format("%s/Images/%s/Cropped/$s", getApplicationContext().getFilesDir().toString(), searchName, listCroppedImages[0]));
                                Mat image = Imgcodecs.imread(String.format("%s/Images/%s/Cropped/%s", getApplicationContext().getFilesDir().toString(), searchName, listCroppedImages[0]));
                                int width = image.width();
                                int height = image.height();
                                TableLayout container_images = (TableLayout) findViewById(R.id.TablePuzzleContainer);
                                int width_container = container_images.getWidth();
                                int height_container = container_images.getHeight();
                                float width_container_dp = width_container / getApplicationContext().getResources().getDisplayMetrics().density;
                                float height_container_dp = height_container / getApplicationContext().getResources().getDisplayMetrics().density;
                                int numero_division = (int)Math.sqrt(listCroppedImages.length);
                                //float scale_x = (float)width_container/(float)(numero_division * width);
                                //float scale_y = (float)height_container/(float)(numero_division * height_container);
                                float scale_x = (float)width_container/(float)(numero_division * width);
                                float scale_y = (float)height_container/(float)(numero_division * height);
                                float min_scale;
                                if(scale_x < scale_y){
                                    min_scale = scale_x;
                                }else{
                                    min_scale = scale_y;
                                }
                                float width_imageview = width * min_scale;
                                float height_imageview = height * min_scale;
                                /*float width_ratio = (float)(numero_division * width) * min_scale;
                                float height_ratio = (float)(numero_division * height) * min_scale;
                                float width_imageview = (width_ratio /numero_division);
                                float height_imageview = (height_ratio/numero_division);*/
                                for (int i = 0; i < container_images.getChildCount(); i++) {
                                    container_images.removeViewAt(i);
                                }


                                for(int row = 1; row <= numero_division; row++){
                                    TableRow tableRow = new TableRow(getApplicationContext());
                                    //tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                    /*tableRow.setLayoutParams(new TableRow.LayoutParams(
                                            TableRow.LayoutParams.MATCH_PARENT,
                                            TableRow.LayoutParams.MATCH_PARENT
                                    ));*/
                                    /*tableRow.setLayoutParams(new TableRow.LayoutParams(
                                            150,
                                            150
                                    ));*/
                                    //tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                                    tableRow.setWeightSum(1f);
                                    tableRow.setBackgroundColor(Color.GREEN);
                                    //tableRow.setWeightSum();
                                    container_images.addView(tableRow);
                                    //tableRow.setMinimumHeight(100);
                                    for(int column = 1; column <= numero_division; column++){
                                        ImageView new_image_container = new ImageView(getApplicationContext());
                                        //new_image_container.setBackgroundColor(Color.rgb(100, 100, 50));
                                        Bitmap bmImg = BitmapFactory.decodeFile(String.format("%s/Images/%s/Cropped/%s_%s.jpg", getApplicationContext().getFilesDir().toString(), searchName,row,column));
                                        int border = 2;
                                        Bitmap bmImgScale = Bitmap.createScaledBitmap(bmImg,(int)width_imageview - (border*2),(int)height_imageview - (border*2),false);
                                        new_image_container.setImageBitmap(bmImgScale);

                                        new_image_container.setPadding(2,2,2,2);
                                        //Arrays.asList(listCroppedImages).contains("1_2.jpg")

                                        //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(120,120);
                                        //new_image_container.setLayoutParams(parms);

                                        new_image_container.setMaxWidth((int)width_imageview);
                                        new_image_container.setMaxHeight((int)height_imageview);

                                        dataImageview data_ima = new dataImageview();
                                        data_ima.ImaAct = String.format("%s/Images/%s/Cropped/%s_%s.jpg", getApplicationContext().getFilesDir().toString(), searchName,row,column);
                                        data_ima.ImaCorr = String.format("%s/Images/%s/Cropped/%s_%s.jpg", getApplicationContext().getFilesDir().toString(), searchName,row,column);
                                        new_image_container.setTag(data_ima);

                                        new_image_container.setOnLongClickListener(_this::onLongClick);
                                        new_image_container.setOnDragListener(_this::onDrag);
                                        //new_image_container.setOnDragListener(new MyDragListener());
                                        tableRow.addView(new_image_container);
                                        Log.d("Añadido ImageView","xx");

                                        //tableRow
                                    }

                                    TextView txt =(TextView)findViewById(R.id.TimeCounter);
                                    txt.setText("xxxxxx");

                                }



                            }
                        });




                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
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
                cropImagesPuzzle(folder_image_im_cropped,file_image_a,9);

            }catch(Exception e){
                throw e;
            }


        }

    }
    private File readFileForOpencv(String PathImage) throws IOException {
        getApplicationContext().getFilesDir().toString();


        File file_image_a = new File(String.format("%s/tmpImage",getApplicationContext().getCacheDir().toString()));
        file_image_a.createNewFile();
        InputStream is = getApplicationContext().getAssets().open(String.format("%s",PathImage));
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        FileOutputStream fos = new FileOutputStream(file_image_a);
        fos.write(buffer);
        fos.close();
        return file_image_a;
        //return new File();
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
    private void showOptionsMenu(View v){
        //NavigationOptions

        NavigationOptions.setVisibility(View.VISIBLE);
        GameLayout.setVisibility(View.GONE);
        HelpLayout.setVisibility(View.GONE);
    }
    private void showHelpView(View v){
        NavigationOptions.setVisibility(View.GONE);
        GameLayout.setVisibility(View.GONE);
        HelpLayout.setVisibility(View.VISIBLE);
    }
    private void showGameLayout(View v){
        NavigationOptions.setVisibility(View.GONE);
        GameLayout.setVisibility(View.VISIBLE);
        HelpLayout.setVisibility(View.GONE);
        /*try {
            //checkCroppedSaved();
            starPuzzle();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    private void startsThreadTime(){

    }
     @Override
    public void onClick(View v)
    {
        //Toast.makeText(MainActivity.this,getResources().getResourceEntryName(v.getId()),Toast.LENGTH_LONG).show();
        if(getResources().getResourceEntryName(v.getId()).equals("barOptions")){
            //Toast.makeText(MainActivity.this,"Entro en el bar options",Toast.LENGTH_LONG).show();
            showOptionsMenu(v);
        }else if(getResources().getResourceEntryName(v.getId()).equals("barHelp")){
            showHelpView(v);
        }else if(getResources().getResourceEntryName(v.getId()).equals("barGame")){
            showGameLayout(v);
        }
        //String name = tvName.getText().toString();
        //Toast.makeText(MainActivity.this,name,Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onLongClick (View v){
        Toast.makeText(MainActivity.this,"OnLongClick",Toast.LENGTH_LONG).show();
        dataImageview datac = (dataImageview)v.getTag();
        actual_dataImageview_on_long_click = datac;
        Log.d("ONLONGCLICK",datac.ImaAct);
        dataImageview data_ima = (dataImageview) v.getTag();
        ClipData.Item item = new ClipData.Item(data_ima.ImaAct);
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData dataClip = new ClipData(data_ima.ImaAct,mimeTypes,item);
        MyDragShadowBuilder myShadow = new MyDragShadowBuilder(v);
        v.startDragAndDrop(dataClip,myShadow,null,0);
        return false;
    }


}