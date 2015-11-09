package com.xw.uploadavatar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener{
	private ImageButton img_btn;
	private Button btn1;
	private Button btn2;
	final static int PHOTO_REQUEST_GALLERY = 1;
	final static int PHOTO_REQUEST_TAKEPHOTO = 2;
	final static int PHOTO_REQUEST_CUT = 3;
	File tempFile = new File(Environment.getExternalStorageDirectory()
			+ "/Postcard", getPhotoFileName());
	Uri photoUri;
	File CameFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();
    }

private void init() {
	btn1 = (Button) findViewById(R.id.button1);
	btn2 = (Button) findViewById(R.id.button2);
	img_btn = (ImageButton) findViewById(R.id.imageView1);
	btn1.setOnClickListener(this);
	btn2.setOnClickListener(this);
}
    
// ����¼�
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	int ResponseId = v.getId();
	Log.d("��Ӧ��ťID",String.valueOf(ResponseId));
	switch (ResponseId ) {
		case R.id.button1:
			Log.d("��Ӧ��ť�¼�","����");
			Album();
			break;
		case R.id.button2:
			Log.d("��Ӧ��ť�¼�","���");
			Photograph();
			break;
		case R.id.imageView1:
			break;
	}
	

}  

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	Log.e("onActivityResult",String.valueOf(requestCode));
	switch (requestCode) {
	case PHOTO_REQUEST_TAKEPHOTO:
		Bitmap bitmap = getBitmapFromUrl(getPhotopath(), 313.5, 462.0);  
        saveScalePhoto(bitmap);  
        //img_btn.setImageBitmap(bitmap);
     	Uri uri = Uri.fromFile(new File(getPhotopath()));
     	startPhotoZoom(uri, 250);
		break;
//		 if (resultCode == Activity.RESULT_OK) {  
//	            String sdStatus = Environment.getExternalStorageState();  
//	            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����  
//	                Log.i("TestFile",  
//	                        "SD card is not avaiable/writeable right now.");  
//	                return;  
//	            }  
//	            new DateFormat();  
//	            String name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";   
//	            Toast.makeText(this, name, Toast.LENGTH_LONG).show();  
//	            Bundle bundle = data.getExtras();  
//	            Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
//	          
//	            FileOutputStream b = null;           
//	            File file = new File("/sdcard/Image/");  
//	            file.mkdirs();// �����ļ���  
//	            String fileName = Environment.getExternalStorageDirectory()+ "/Postcard" + getPhotoFileName(); 
//	            File file_name = new File(Environment.getExternalStorageDirectory()+ "/Postcard",getPhotoFileName());
//	            Toast.makeText(this, fileName, Toast.LENGTH_LONG).show();  
//	            Log.d("54416546",fileName);
//	            Log.d("54416546",file_name.toString());
//	            try {  
//	                b = new FileOutputStream(fileName);  
//	                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�  
//	                
//	            } catch (FileNotFoundException e) {  
//	                e.printStackTrace();  
//	                Log.d("ccccccccccc","cccccccccccccccc");
//	            } finally {  
//	                try {  
//	                    b.flush();  
//	                    b.close();  
//	                } catch (IOException e) {  
//	                    e.printStackTrace(); 
//	                    Log.d("fgsergaga","adgasdfads");
//	                }  
//	            }  
//	            try  
//	            {  
//	            	Log.d("fileName",fileName);
//	            	Uri uri = Uri.parse(fileName);
//	            	Log.d("aaaaaaaaa","aaaaaaaaaa");
//	                startPhotoZoom(Uri.fromFile(new File(fileName)), 150);
//	            	//startPhotoZoom(photoUri,150);
//	            }catch(Exception e)  
//	            {  
//	            	Log.d("bbbbbb","bbbbbbb");
//	                Log.e("error", e.getMessage());  
//	            }  
//	        }  
//		//startPhotoZoom(Uri.fromFile(tempFile), 150);
//		break;

	case PHOTO_REQUEST_GALLERY:// ��ѡ��ӱ��ػ�ȡͼƬʱ
		// ���ǿ��жϣ������Ǿ��ò����������¼��õ�ʱ��㲻�ᱨ�쳣����ͬ
		if (data != null) {
			Log.d("qeqweqrqwerqwer",data.getData().toString());
			startPhotoZoom(data.getData(), 150);
		}
			
		break;
	case PHOTO_REQUEST_CUT:// ��ѡ��ӱ��ػ�ȡͼƬʱ
		// ���ǿ��жϣ������Ǿ��ò����������¼��õ�ʱ��㲻�ᱨ�쳣����ͬ
		if (data != null) {
			Log.d("PHOTO_REQUEST_CUT","PHOTO_REQUEST_CUT");
			//img_btn.setImageBitmap();// ��ͼƬ��ʾ��ImageView�� 
			setPicToView(data);
		}
		
		break;

	}
	super.onActivityResult(requestCode, resultCode, data);
}


private void Photograph(){
	Intent intent = new Intent(Intent.ACTION_PICK, null);
	intent.setDataAndType(
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
	startActivityForResult(intent, PHOTO_REQUEST_GALLERY);	
}

private void Album(){
	//Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//����android�Դ�������� 
	//photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;		
	intent1.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	File out = new File(getPhotopath());  
	Uri uri = Uri.fromFile(out);  
	intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);  
	
	startActivityForResult(intent1, PHOTO_REQUEST_TAKEPHOTO);	
}	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		Log.d("startPhotoZoom",uri.toString());
		// cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
		intent.putExtra("crop", "true");

		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY �Ǽ���ͼƬ�Ŀ��
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
  //�����м��ú��ͼƬ��ʾ��UI������
	@SuppressWarnings("deprecation")
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			img_btn.setBackgroundDrawable(drawable);
		}
    }
    
	// ʹ��ϵͳ��ǰ���ڼ��Ե�����Ϊ��Ƭ������
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
     
	private String getPhotopath() {  
        // 照片全路径  
        String fileName = "";  
        // 文件夹路径  
        String pathUrl = Environment.getExternalStorageDirectory()+"/mymy/";  
        String imageName = "imageTest.jpg";  
        File file = new File(pathUrl);  
        file.mkdirs();// 创建文件夹  
        fileName = pathUrl + imageName;  
        return fileName;  
    }  
	
	/** 
     * 根据路径获取图片资源（已缩放） 
     * @param url 图片存储路径 
     * @param width 缩放的宽度 
     * @param height 缩放的高度 
     * @return 
     */  
    private Bitmap getBitmapFromUrl(String url, double width, double height) {  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false  
        Bitmap bitmap = BitmapFactory.decodeFile(url);  
        // 防止OOM发生  
        options.inJustDecodeBounds = false;  
        int mWidth = bitmap.getWidth();  
        int mHeight = bitmap.getHeight();  
        Matrix matrix = new Matrix();  
        float scaleWidth = 1;  
        float scaleHeight = 1;  
//        try {  
//            ExifInterface exif = new ExifInterface(url);  
//            String model = exif.getAttribute(ExifInterface.TAG_ORIENTATION);  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//        }  
        // 按照固定宽高进行缩放  
        // 这里希望知道照片是横屏拍摄还是竖屏拍摄  
        // 因为两种方式宽高不同，缩放效果就会不同  
        // 这里用了比较笨的方式  
        if(mWidth <= mHeight) {  
            scaleWidth = (float) (width/mWidth);  
            scaleHeight = (float) (height/mHeight);  
        } else {  
            scaleWidth = (float) (height/mWidth);  
            scaleHeight = (float) (width/mHeight);  
        }  
//        matrix.postRotate(90); /* 翻转90度 */  
        // 按照固定大小对图片进行缩放  
        matrix.postScale(scaleWidth, scaleHeight);  
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true);  
        // 用完了记得回收  
        bitmap.recycle();  
        return newBitmap;  
    }  
      
    /** 
     * 存储缩放的图片 
     * @param data 图片数据 
     */  
    private void saveScalePhoto(Bitmap bitmap) {  
        // 照片全路径  
        String fileName = "";  
        // 文件夹路径  
        String pathUrl = Environment.getExternalStorageDirectory().getPath()+"/myPicture/";  
        String imageName = getPhotoFileName(); 
        FileOutputStream fos = null;  
        File file = new File(pathUrl);  
        file.mkdirs();// 创建文件夹  
        fileName = pathUrl + imageName;  
        try {  
            fos = new FileOutputStream(fileName);  
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                fos.flush();  
                fos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}
