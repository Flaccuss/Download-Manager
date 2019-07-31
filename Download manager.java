package com.mycompany.myapp3;

import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;

public class MainActivity extends Activity 
{
	private DownloadManager downloadManager;
    private long enqueue;
    private final static String DOWNLOAD_URL ="https://img5.goodfon.com/original/1920x1080/b/cd/timothy-adry-by-timothy-adry-amc-javelin.jpg";
	
	//"http://colorsfx.com/android/files/smartlogo.png";
    Button download;
    ProgressDialog dialog = null;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	
		
		setContentView(R.layout.main);
	
	
			 
		 
		BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                            //     ImageView view = (ImageView) findViewById(R.id.imageView);
                            // String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            //  view.setImageURI(Uri.parse(uriString));
                            Toast.makeText(MainActivity.this, "Download completed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };

        MainActivity.this.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        download=(Button)findViewById(R.id.down);
        download.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					downloadManager = (DownloadManager) MainActivity.this.getSystemService(DOWNLOAD_SERVICE);
					DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DOWNLOAD_URL));

					//Get download file name
					String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(DOWNLOAD_URL);
					String name = URLUtil.guessFileName(DOWNLOAD_URL, null, fileExtenstion);

					//Save file to destination folder
					request.setDestinationInExternalPublicDir("/", name);

					enqueue = downloadManager.enqueue(request);
				}
			});
    }
}
