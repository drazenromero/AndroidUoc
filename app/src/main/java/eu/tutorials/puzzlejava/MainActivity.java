package eu.tutorials.puzzlejava;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionProvider;
import android.view.DragEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.w3c.dom.Text;

import Utilidades.FileUtils;
import entidades_helper.ConexionSQLiteHelper;
import entidades_helper.Puntaje_Esquema;
import entidades_helper.UsuarioEsquema;

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

    private int checkPuzzleResolve(){
        int ErrorDetect = 0;
         for (int i = 0; i < AllImageViewPuzzle.size(); i++){
            ImageView act = (ImageView) AllImageViewPuzzle.get(i);
            dataImageview dt = (dataImageview) act.getTag();
            if(dt.ImaAct.equals(dt.ImaCorr)){

            }else{
                ErrorDetect = 1;
            }
        }


        return ErrorDetect;
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
         else //Log.d("ACTION_DRAG_DROG", "ACTION_DROP");
             if(dragEvent.getAction() == DragEvent.ACTION_DRAG_ENDED) {
             //Log.d("ACTION_DRAG_DROG", "ACTION_DRAG_ENDED");
             dataImageview datac = (dataImageview)view.getTag();
             //Log.d("action_tag_imageview", datac.ImaAct);
             if(actual_dataImageview_on_long_click != null && actual_dataImageview_on_entered_drag != null){
                 if(datac.ImaAct.equals(actual_dataImageview_on_entered_drag.ImaAct)) {
                     if (actual_dataImageview_on_long_click.ImaAct.equals(actual_dataImageview_on_entered_drag.ImaCorr)) {
                         Log.d("ImaCorrect", actual_dataImageview_on_entered_drag.ImaCorr);
                         String actual_dataImageview_on_entered_drag_ImaAct = String.format("%s", actual_dataImageview_on_entered_drag.ImaAct);
                         String actual_dataImageview_on_long_click_ImaAct = String.format("%s", actual_dataImageview_on_long_click.ImaAct);

                         actual_dataImageview_on_entered_drag.ImaAct = actual_dataImageview_on_long_click_ImaAct;
                         actual_dataImageview_on_long_click.ImaAct = actual_dataImageview_on_entered_drag_ImaAct;

                         Bitmap actual_dataImageview_on_entered_drag_Bitmap = Bitmap.createBitmap(actual_dataImageview_on_entered_drag.BitmapAct);
                         Bitmap actual_dataImageview_on_long_click_Bitmap = Bitmap.createBitmap(actual_dataImageview_on_long_click.BitmapAct);

                         //actual_dataImageview_on_long_click.imageView.setImageResource(0);
                         //actual_dataImageview_on_entered_drag.imageView.setImageResource(0);
                         actual_dataImageview_on_long_click.imageView.setImageBitmap(actual_dataImageview_on_entered_drag_Bitmap);
                         actual_dataImageview_on_entered_drag.imageView.setImageBitmap(actual_dataImageview_on_entered_drag.BitmapCorr);

                         actual_dataImageview_on_entered_drag.BitmapAct = Bitmap.createBitmap(actual_dataImageview_on_long_click_Bitmap);
                         actual_dataImageview_on_long_click.BitmapAct = Bitmap.createBitmap(actual_dataImageview_on_entered_drag_Bitmap);

                        actual_dataImageview_on_entered_drag = null;
                        actual_dataImageview_on_long_click = null;

                         //ClipboardManager mCbm = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                         //mCbm.clearPrimaryClip();
                         if(enableSound) {
                             soundPool.play(soundDrap, 1.0f, 1.0f, 1, 0, 1.0f);
                         }
                         if(checkPuzzleResolve() == 0) {
                             Toast.makeText(MainActivity.this, String.format("Level: %d",IndexImageLevels + 2), Toast.LENGTH_LONG).show();


                             if (IndexImageLevels <= ImageLevels.size() - 1){
                                 //String firstKey = (String)ImageLevels.keySet().toArray()[IndexImageLevels];
                                 //String valueForFirstKey = ImageLevels.get(firstKey);


                                 try {

                                     String firstKey = (String) ImageLevels.keySet().toArray()[IndexImageLevels];
                                     Integer valueForFirstKey = ImageLevels.get(firstKey);

                                     //File folder_image_im_cropped = new File(String.format("%s/Images/%s/Cropped",getApplicationContext().getFilesDir().toString(),firstKey.replace('.','-')));

                                     //Aqui debo de guardar los puntos el menor tiempo vale mas

                                     ContentValues values = new ContentValues();

                                     Integer levelgame;
                                     switch (IndexImageLevels){
                                         case 0:
                                             levelgame = 1;
                                             break;
                                         case 1:
                                             levelgame = 2;
                                             break;
                                         case 2:
                                             levelgame = 3;
                                             break;
                                         default:
                                             levelgame = -1;
                                             break;

                                     }
                                     /*if(levelgame == 1){
                                         LevelTime1 = threadTimeObjectSync.getSeconds();
                                     }else if(levelgame == 2){
                                         LevelTime2 = threadTimeObjectSync.getSeconds();
                                     }else if(levelgame == 3){
                                         LevelTime3 = threadTimeObjectSync.getSeconds();
                                     }*/
                                     values.put(Puntaje_Esquema.CAMPO_NIVEL, levelgame);
                                     values.put(Puntaje_Esquema.CAMPO_PUNTAJE,threadTimeObjectSync.getSeconds());
                                     values.put(Puntaje_Esquema.CAMPO_USER,NameUser);

                                     /*String pattern = "MM-dd-yyyy";
                                     SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                     Date date = simpleDateFormat.parse("12-01-2018");*/


                                     SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");//dd/MM/yyyy
                                     Date now = new Date();
                                     String strDate = sdfDate.format(now);




                                     //System.out.println(date);


                                     values.put(Puntaje_Esquema.CAMPO_FECHA,strDate);

                                     SQLiteDatabase db = conex.getWritableDatabase();
                                     Long id_resultante = db.insert(Puntaje_Esquema.TABLA_PUNTAJE,UsuarioEsquema.CAMPO_ID,values);



                                     //NameUser
                                     if(IndexImageLevels == ImageLevels.size() - 1){
                                         threadTimeObjectSync.setContinueTime(false);
                                         threadTimeObjectSync.setSeconds(0);
                                     }else{
                                         threadTimeObjectSync.setSeconds(0);

                                     }

                                     IndexImageLevels += 1;
                                     if (IndexImageLevels <= ImageLevels.size() - 1) {
                                         String firstKey2 = (String) ImageLevels.keySet().toArray()[IndexImageLevels];
                                         Integer valueForFirstKey2 = ImageLevels.get(firstKey2);
                                         //checkCroppedSaved(firstKey, valueForFirstKey);
                                         //cropImagesPuzzle(folder_image_im_cropped,file_image_a,16);
                                         TableLayout game_container = (TableLayout) findViewById(R.id.TablePuzzleContainer);
                                         game_container.removeViews(0, Math.max(0, game_container.getChildCount()));
                                         game_container.post(new Runnable() {
                                             @Override
                                             public void run() {
                                                 try {
                                                     checkCroppedSaved(firstKey2, valueForFirstKey2,SourceImageCrop,"");
                                                     createImageViewsPuzzle(firstKey2,SourceImageCrop);
                                                 } catch (IOException e) {
                                                     e.printStackTrace();
                                                 }

                                             }
                                         });
                                     }else{
                                         //testCalendar(LevelTime1,LevelTime2,LevelTime3);

                                         Intent intent = new Intent(getApplicationContext(), finish_game.class);
                                         intent.putExtra("NAME_USER",NameUser);
                                         intent.putExtra("LevelTime1", String.valueOf(LevelTime1));
                                         intent.putExtra("LevelTime2",String.valueOf(LevelTime2));
                                         intent.putExtra("LevelTime3",String.valueOf(LevelTime3));
                                         startActivity(intent);
                                     }

                                 } catch (Exception e) {
                                     Log.e("error level", "fallo");
                                     e.printStackTrace();
                                     Toast.makeText(getApplicationContext(), "FALLO", Toast.LENGTH_LONG).show();
                                 }

                            }else{
                                 Intent intent = new Intent(getApplicationContext(), finish_game.class);
                                 startActivity(intent);
                             }

                         }
                         /*actual_dataImageview_on_long_click.ImaAct = actual_dataImageview_on_long_click.ImaCorr;
                         actual_dataImageview_on_entered_drag.ImaAct = actual_dataImageview_on_entered_drag.ImaCorr;

                         actual_dataImageview_on_long_click.imageView.setImageBitmap(actual_dataImageview_on_entered_drag.BitmapAct);
                         actual_dataImageview_on_entered_drag.imageView.setImageBitmap(actual_dataImageview_on_entered_drag.BitmapCorr);

                         //actual_dataImageview_on_long_click.BitmapAct = actual_dataImageview_on_entered_drag.BitmapAct;

                         //actual_dataImageview_on_entered_drag.BitmapAct = actual_dataImageview_on_long_click.BitmapAct;

                         if(checkPuzzleResolve() == 0){
                             Toast.makeText(MainActivity.this,"Puzzle Resuelto",Toast.LENGTH_LONG).show();

                         }*/
                     }
                 }
             }

             return true;
         }
         else return dragEvent.getAction() == DragEvent.ACTION_DROP;


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
         public Bitmap BitmapAct;
         public Bitmap BitmapCorr;
         public ImageView imageView;
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
    private Button ButtonBackToStart;
    private Button ButtonPickImages;
    private Button ButtonToTakePhoto;
    private Button ButtonUseInnerImages;
    private ArrayList<Uri> ImagesUrisSelected = new ArrayList<>();
    private static final int PICK_IMAGES_CODE = 0;

    private ConexionSQLiteHelper conex;

    private SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
    private int soundTap;
    private int soundDrap;
    private CompoundButton switchSound;
    private boolean enableSound = true;

    private Thread threadTimeJava;
    private ThreadTimeObjectSync threadTimeObjectSync;

    public int IndexImageLevels = 0;

    public LinkedHashMap<String, Integer> ImageLevels = new LinkedHashMap<String, Integer>() {{
        put("2B.jpg",4);
        put("Medusa.png",9);
        put("Anime1.jpg",16);

    }};
    public LinkedHashMap<String, Integer> ImageLevelsRespaldo = new LinkedHashMap<String, Integer>() {{
        put("2B.jpg",4);
        put("Medusa.png",9);
        put("Anime1.jpg",16);

    }};
    private int SourceImageCrop = 0;
    public ArrayList AllImageViewPuzzle;

    private String NameUser;

    private int LevelTime1 = 999999;
    private int LevelTime2 = 999999;
    private int LevelTime3 = 999999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         File dir_to_clear = new File(getApplicationContext().getCacheDir().toString());
         cleanDir(dir_to_clear);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        soundTap = soundPool.load(getApplicationContext(),R.raw.blip,1);
        soundDrap = soundPool.load(getApplicationContext(),R.raw.boing_x,1);
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         conex = new ConexionSQLiteHelper(this,"bd_usuarios",null,10);

         NameUser = getIntent().getStringExtra("NAME_USER").toString();
        getMinimunScoreLevel(NameUser);
         TextView nameTextview = (TextView)findViewById(R.id.nameUser);
        nameTextview.post(new Runnable() {
             @Override
             public void run() {
                 nameTextview.setText(NameUser);
             }
         });



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

        ButtonBackToStart = (Button)findViewById(R.id.buttonBackToStart);
        AppCompatActivity _this = this;

         class MyButtonClicklistener implements View.OnClickListener
        {
            private AppCompatActivity reference;
            @Override
            public void onClick(View v)
            {
                //Do something on the button click
                //Toast.makeText(reference.getApplicationContext(),"volver",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this.reference.getApplicationContext(), home_portal.class);

                startActivity(intent);
            }

            public MyButtonClicklistener(AppCompatActivity r){
                this.reference = r;
            }


        }



        ButtonPickImages = (Button)findViewById(R.id.Select_Images);


        class ButtonToPickImagesListener implements View.OnClickListener{
             @Override
             public void onClick(View v){
                 Toast.makeText(MainActivity.this,String.format("Se selecciono el boton de seleccionar images"),Toast.LENGTH_LONG).show();
                 pickImagesIntent();
             }

        }


        MyButtonClicklistener ll = new MyButtonClicklistener(this);
        ButtonBackToStart.setOnClickListener(ll);

        ButtonToPickImagesListener listener_to_pick_images = new ButtonToPickImagesListener();
        ButtonPickImages.setOnClickListener(listener_to_pick_images);


        class ButtonToTakePhotoListener implements View.OnClickListener{
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this,"Se presiono el boton de tomar la foto",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }

        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            },100);
        }
        ButtonToTakePhoto = (Button) findViewById(R.id.buttonTakePhoto);
        ButtonToTakePhotoListener ButtonToTakePhoto_listener = new ButtonToTakePhotoListener();
        ButtonToTakePhoto.setOnClickListener(ButtonToTakePhoto_listener);

        class ButtonUseInnerImagesListener implements View.OnClickListener{
            @Override
            public void onClick(View v){
                Toast.makeText(MainActivity.this,"Se presiono el boton de usar las imagenes internas",Toast.LENGTH_LONG).show();
                IndexImageLevels = 0;
                SourceImageCrop = 0;
                ImageLevels = (LinkedHashMap<String, Integer>) ImageLevelsRespaldo.clone();
                String firstKey = (String)ImageLevels.keySet().toArray()[IndexImageLevels];
                Integer valueForFirstKey =  ImageLevels.get(firstKey);
                try {
                    checkCroppedSaved(firstKey,valueForFirstKey,SourceImageCrop,"");
                    createImageViewsPuzzle(firstKey,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        ButtonUseInnerImages = (Button)findViewById(R.id.buttonInternalImages);
        ButtonUseInnerImagesListener ButtonUseInnerImagesListenerVar = new ButtonUseInnerImagesListener();
        ButtonUseInnerImages.setOnClickListener(ButtonUseInnerImagesListenerVar);
        class SwitchSoundPoolListener implements  CompoundButton.OnCheckedChangeListener{

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Toast.makeText(getApplicationContext(),"Cambio el switch",Toast.LENGTH_LONG).show();
                enableSound = switchSound.isChecked();
            }
        }
        switchSound = (CompoundButton)findViewById(R.id.switchSoundPool);
        SwitchSoundPoolListener SwitchSoundPoolListenerVar = new SwitchSoundPoolListener();
        switchSound.setOnCheckedChangeListener(SwitchSoundPoolListenerVar);
        web_view_help = findViewById(R.id.WebViewHelp);
        web_view_help.loadUrl("file:///android_asset/help/help_page/index.html");
        GameLayout = (TableLayout) findViewById(R.id.GameLayout);
        HelpLayout = (LinearLayout) findViewById(R.id.HelpLayout);

        threadTimeObjectSync = new ThreadTimeObjectSync();

        threadTimeJava = new ThreadTime(threadTimeObjectSync,this);
        threadTimeJava.start();

     }

     private void backToStart(){

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
        String firstKey = (String)ImageLevels.keySet().toArray()[IndexImageLevels];
        Integer valueForFirstKey =  ImageLevels.get(firstKey);
        checkCroppedSaved(firstKey,valueForFirstKey,SourceImageCrop,"");

        createImageViewsPuzzle(firstKey,0);
    }
    private void createImageViewsPuzzle(String NameImage,int ModeImage){

        if(ModeImage == 1){
            NameImage = new File(NameImage).getName();
        }
        File folder_cropped = new File(String.format("%s/Images", getApplicationContext().getFilesDir().toString()));







        /*if(!folder_cropped.exists()){
            try {
                checkCroppedSaved();
            }
            catch(Exception e){

            }
        }*/
        String searchName = NameImage.replace('.','-');
        for(String ImageN: folder_cropped.list()){
            if(searchName.equals(ImageN)){
                //Toast.makeText(MainActivity.this,String.format("Encontrada Imagen %s",searchName),Toast.LENGTH_LONG).show();
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
                                /*for (int i = 0; i < container_images.getChildCount(); i++) {
                                    container_images.removeViewAt(i);
                                }*/
                                container_images.removeViews(0, Math.max(0, container_images.getChildCount() ));
                                ArrayList<String> list_images = new ArrayList();
                                for(int row = 1; row <= numero_division; row++){
                                    for(int column = 1; column <= numero_division; column++){
                                        list_images.add(String.format("%s_%s.jpg",row,column));
                                    }
                                }

                                /*ArrayList list_images_to_random = (ArrayList<String>) list_images.clone();
                                ArrayList list_images_ram = new ArrayList<String>();



                                while(list_images_to_random.size() > 0) {
                                    int index = (int) (Math.random() * list_images_to_random.size());
                                    list_images_ram.add(list_images_to_random.get(index));
                                    list_images_to_random.remove(index);


                                }*/
                                ArrayList<String> list_images_ram = shuffleArrayList(list_images);
                                int StepRandom = 0;
                                AllImageViewPuzzle = new ArrayList<ImageView>();
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
                                        Bitmap bmImg = BitmapFactory.decodeFile(String.format("%s/Images/%s/Cropped/%s", getApplicationContext().getFilesDir().toString(), searchName,list_images_ram.get(StepRandom)));
                                        int border = 2;
                                        Bitmap bmImgScale = Bitmap.createScaledBitmap(bmImg,(int)width_imageview - (border*2),(int)height_imageview - (border*2),false);
                                        new_image_container.setImageBitmap(bmImgScale);

                                        Bitmap bmImgCorr = BitmapFactory.decodeFile(String.format("%s/Images/%s/Cropped/%s_%s.jpg", getApplicationContext().getFilesDir().toString(), searchName,row,column));
                                        Bitmap bmImgCorrScale = Bitmap.createScaledBitmap(bmImgCorr,(int)width_imageview - (border*2),(int)height_imageview - (border*2),false);
                                        new_image_container.setPadding(2,2,2,2);
                                        //Arrays.asList(listCroppedImages).contains("1_2.jpg")

                                        //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(120,120);
                                        //new_image_container.setLayoutParams(parms);

                                        new_image_container.setMaxWidth((int)width_imageview);
                                        new_image_container.setMaxHeight((int)height_imageview);

                                        dataImageview data_ima = new dataImageview();
                                        data_ima.ImaAct = String.format("%s", list_images_ram.get(StepRandom));
                                        data_ima.ImaCorr = String.format("%s_%s.jpg", row,column);
                                        data_ima.BitmapAct = bmImgScale;
                                        data_ima.BitmapCorr = bmImgCorrScale;
                                        data_ima.imageView = new_image_container;
                                        new_image_container.setTag(data_ima);
                                        AllImageViewPuzzle.add(new_image_container);

                                        StepRandom += 1;

                                        new_image_container.setOnLongClickListener(_this::onLongClick);
                                        new_image_container.setOnDragListener(_this::onDrag);
                                        //new_image_container.setOnDragListener(new MyDragListener());
                                        tableRow.addView(new_image_container);
                                        Log.d("Añadido ImageView","xx");

                                        //tableRow
                                    }

                                    //TextView txt =(TextView)findViewById(R.id.TimeCounter);
                                    //txt.setText("xxxxxx");

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
    public ArrayList shuffleArrayList(ArrayList list_images){
        ArrayList list_images_to_random = (ArrayList<String>) list_images.clone();
        ArrayList list_images_ram = new ArrayList<String>();


        do{
            while(list_images_to_random.size() > 0) {
                int index = (int) (Math.random() * list_images_to_random.size());
                list_images_ram.add(list_images_to_random.get(index));
                list_images_to_random.remove(index);


            }
        }while(list_images_ram.equals(list_images));



        return list_images_ram;
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
    private void checkCroppedSaved(String NameImage,Integer NumberDivision,int type_search,String FolderSelected) throws IOException {

        File file_x = new File(String.format("%s/Images", getApplicationContext().getFilesDir().toString()));

        String pathImage = "";
        if(type_search == 1){
            pathImage = NameImage;
            NameImage = new File(NameImage).getName();
            NameImage = NameImage.replace(";","_");
        }





        if (file_x.exists()) {
            //deleteRecursive(file_x);
        } else {
            file_x.mkdirs();
            Log.d("FOLDER", "Folder Created");
        }
        String[] ListFolderImages;
        ListFolderImages = getApplicationContext().getAssets().list("ImagesPuzzle");
        try{
            File folder_image_im = new File(String.format("%s/Images/%s",getApplicationContext().getFilesDir().toString(),NameImage.replace('.','-')));
            File folder_image_im_cropped;
            if(!folder_image_im.exists()){
                folder_image_im.mkdirs();
                folder_image_im_cropped = new File(String.format("%s/Images/%s/Cropped",getApplicationContext().getFilesDir().toString(),NameImage.replace('.','-')));
                folder_image_im_cropped.mkdirs();

            }
            else{
                deleteRecursive(folder_image_im);
                folder_image_im.mkdirs();
                folder_image_im_cropped = new File(String.format("%s/Images/%s/Cropped",getApplicationContext().getFilesDir().toString(),NameImage.replace('.','-')));
                if(!folder_image_im_cropped.exists()){
                    folder_image_im_cropped.mkdirs();
                }
            }
            File file_image_a = new File(String.format("%s/tmpImage",getApplicationContext().getCacheDir().toString()));
            file_image_a.createNewFile();


            //Aqui debo de introducir la ueva lógica para manejar las imagenes de la camara
            //File sd = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/"+"PuzzleImages");
            //FileInputStream();
            InputStream is;
            if(type_search == 0){
                is = getApplicationContext().getAssets().open(String.format("ImagesPuzzle/%s",NameImage));
            }else   {
                //is = new FileInputStream(new File(String.format("%s/PuzzleImages/%s",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),NameImage)));
                is = new FileInputStream(new File(pathImage));
            }






            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            FileOutputStream fos = new FileOutputStream(file_image_a);
            fos.write(buffer);
            fos.close();
            cropImagesPuzzle(folder_image_im_cropped,file_image_a,NumberDivision);

        }catch(Exception e){
            throw e;
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
        //Toast.makeText(MainActivity.this,"OnLongClick",Toast.LENGTH_LONG).show();
        if(enableSound) {
            soundPool.play(soundTap, 1.0f, 1.0f, 1, 0, 1.0f);
        }

        dataImageview datac = (dataImageview)v.getTag();
        actual_dataImageview_on_long_click = datac;
        Log.d("ONLONGCLICK",datac.ImaAct);
        dataImageview data_ima = (dataImageview) v.getTag();
        ClipData.Item item = new ClipData.Item(data_ima.ImaAct);
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData dataClip = new ClipData(data_ima.ImaAct,mimeTypes,item);
        MyDragShadowBuilder myShadow = new MyDragShadowBuilder(v);
        v.startDragAndDrop(dataClip,myShadow,null,0);
        //Log.i("PUZZLE_TERMINADO",String.valueOf(checkPuzzleResolve()));
        return false;
    }
    private void pickImagesIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Images To Puzzle"),PICK_IMAGES_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_IMAGES_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data.getClipData() != null){
                    int count = data.getClipData().getItemCount();
                    if(count < 3){
                        Toast.makeText(MainActivity.this, "Debes de seleccionar al menos 3 fotos", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try{
                            ImagesUrisSelected.clear();
                            ArrayList<String> lista_to_desordenar = new ArrayList<>();
                            for (int i = 0; i < count; i++) {

                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                //String nombre_imagen_buscar = new File(data.getClipData().getItemAt(i).getUri().getPath()).getAbsolutePath();
                                String nombre_imagen_buscar = new FileUtils(getApplicationContext()).getPath(imageUri);
                                ImagesUrisSelected.add(imageUri);
                                lista_to_desordenar.add(nombre_imagen_buscar);

                            }
                            ArrayList<String> lista_desordenada = shuffleArrayList(lista_to_desordenar);
                            ArrayList<String> lista_usar = new ArrayList<String>(lista_desordenada.subList(0,3));
                            int level = 1;
                            IndexImageLevels = 0;
                            SourceImageCrop = 1;
                            ImageLevels.clear();
                            for (String nombre_image: lista_usar){
                                if(level == 1){
                                    ImageLevels.put(nombre_image,4);
                                }else if(level == 2){
                                    ImageLevels.put(nombre_image,4);
                                }else if(level == 3){
                                    ImageLevels.put(nombre_image,4);
                                }
                               level += 1;
                            }
                            String firstKey = (String)ImageLevels.keySet().toArray()[IndexImageLevels];
                            Integer valueForFirstKey =  ImageLevels.get(firstKey);
                            checkCroppedSaved(firstKey,valueForFirstKey,SourceImageCrop,"");
                            createImageViewsPuzzle(firstKey,1);

                        }catch (Exception e){

                            e.printStackTrace();
                        }
                    }
                }else{

                    Toast.makeText(MainActivity.this, "Debes de seleccionar al menos 3 fotos", Toast.LENGTH_LONG).show();
                    Uri imageUri = data.getData();
                    ImagesUrisSelected.add(imageUri);
                }
            }

        }else if(requestCode == 100){
            //Bitmap captureImage = (Bitmap) data.getExtras().get("data");




            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            200);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }


























            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));
            String filename = String.format("%s.png",dateFormat.format(date));
            //File sd =new File(Environment.getExternalStorageDirectory(), "puzzleImages");
            File sd = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/"+"PuzzleImages");
            //File sd = new File(String.format("%s/ImagesCamera", getApplicationContext().getFilesDir().toString()));
            if (!sd.exists()) {
                sd.mkdirs();
            }

            File dest = new File(sd, filename);

            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            try {
                dest.createNewFile();
                FileOutputStream out = new FileOutputStream(dest);
                captureImage.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                Log.d("err","un error");
                e.printStackTrace();
            }





        }


    }
    @SuppressLint("Range")
    public String getImageFilePath(Uri uri) {
        String path = null, image_id = null;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            image_id = cursor.getString(0);
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
            cursor.close();
        }

        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }


    @SuppressLint("Range")
    private void getMinimunScoreLevel(String NameUser){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,10);
        SQLiteDatabase db2 = conn.getReadableDatabase();
        Cursor c = db2.rawQuery(String.format("SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.user = Puntaje.user WHERE Usuario.user = %s AND Puntaje.nivel = 1 AND Puntaje.puntaje = (SELECT min(puntaje) FROM Puntaje WHERE user = %s AND nivel = 1 )", DatabaseUtils.sqlEscapeString(NameUser),DatabaseUtils.sqlEscapeString(NameUser)),null);
        try {
            while (c.moveToNext()) {
                LevelTime1 =(int) c.getFloat(c.getColumnIndex("puntaje"));
            }
        } finally {
            c.close();
        }
        c = db2.rawQuery(String.format("SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.user = Puntaje.user WHERE Usuario.user = %s AND Puntaje.nivel = 2 AND Puntaje.puntaje = (SELECT min(puntaje) FROM Puntaje WHERE user = %s AND nivel = 2 )", DatabaseUtils.sqlEscapeString(NameUser),DatabaseUtils.sqlEscapeString(NameUser)),null);
        try {
            while (c.moveToNext()) {
                LevelTime2 =(int) c.getFloat(c.getColumnIndex("puntaje"));
            }
        } finally {
            c.close();
        }
        c = db2.rawQuery(String.format("SELECT * FROM Usuario INNER JOIN Puntaje ON Usuario.user = Puntaje.user WHERE Usuario.user = %s AND Puntaje.nivel = 3 AND Puntaje.puntaje = (SELECT min(puntaje) FROM Puntaje WHERE user = %s AND nivel = 3 )", DatabaseUtils.sqlEscapeString(NameUser),DatabaseUtils.sqlEscapeString(NameUser)),null);
        try {
            while (c.moveToNext()) {
                LevelTime3 =(int) c.getFloat(c.getColumnIndex("puntaje"));
            }
        } finally {
            c.close();
        }
    }
}