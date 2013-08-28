package com.reconinstruments.reconsdkapplication;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.io.IOException;


import com.reconinstruments.reconsdkapplication.R;

import com.reconinstruments.ReconSDK.*;


public class JumpHistory extends Activity implements IReconDataReceiver
{
   	// standard ident tag
	// should be indent tag
    @SuppressWarnings("unused")
	private static final String TAG = JumpHistory.class.getSimpleName();
    
    // SDK Manager copy for this activity
    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
    
    
    // data receiver callback
	public void onReceiveCompleted(int status, ReconDataResult result)
	{
	    // JAR API will call us back right here when jump history has been retrieved
  		
 		// check status first
  		if (status != ReconSDKManager.STATUS_OK)
  		{
  			Toast toast = Toast.makeText(this, "Communication Failure with Transcend Service", Toast.LENGTH_LONG);
  			toast.show();
  			
  			return;
  		}
  		
  		// get list view object
  	    ListView lv= (ListView)findViewById(R.id.jumplist);

        ImageView imgv = (ImageView)findViewById(R.id.imageView);

        //Bitmap bmp = Bitmap.createBitmap(150, 200, Bitmap.Config.ARGB_8888);
       // for(int i=0; i<200; i++)
        //    for(int j=0; j<150; j++)
        //        bmp.setPixel(j, i, 0xffff00ff);
        //imgv.setImageBitmap(bmp);

        // load image

        try {
            // get input stream
            InputStream ims = getAssets().open("laden.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            imgv.setImageDrawable(d);
        }
        catch(IOException ex) {
            return;
        }

  	    
  	   // create the grid item mapping
  	   String[] from = new String[] 
  	   {
  			 getString(R.string.JumpSequence),
  			 getString(R.string.JumpDate),
  			 getString(R.string.JumpTime),
  			 getString(R.string.JumpAir),
  			 getString(R.string.JumpDistance),
  			 getString(R.string.JumpDrop),
  			 getString(R.string.JumpHeight),
  	   };
  	   
  	   int[] to = new int[] { R.id.jump_sequence, R.id.jump_date, R.id.jump_time, R.id.jump_air, R.id.jump_distance, R.id.jump_drop, R.id.jump_height };
  		
  	   // prepare list of all jumps
  	   List<HashMap<String, String>> jumpMaps = new ArrayList<HashMap<String, String>>();
  	   
  	   for (int i = 0; i < result.arrItems.size(); i++)
  	   {
  	  	    HashMap<String, String> map = new HashMap<String, String>();
  	  	    ReconJump jump = (ReconJump)result.arrItems.get(i);
  	  	    
  	  	    // sequence
  	  	    String strData = String.format("%d", jump.GetSequence() );
  	  	    map.put(getString(R.string.JumpSequence), strData);
  	  	    
  	  	    // date
  	  	    Date dt = new Date (jump.GetDate() );
  	  	    DateFormat df = DateFormat.getDateInstance();
    
  	  	    map.put(getString(R.string.JumpDate), df.format(dt) );
  	  	    
  	  	    // time
  	  	    DateFormat tf = DateFormat.getTimeInstance();
  	  	    map.put(getString(R.string.JumpTime), tf.format(dt) );
  	  	 
  	  	    // Air Time
	  	    strData = String.format("%d", jump.GetAir() );
  	  	    map.put(getString(R.string.JumpAir), strData);
  	  	    
  	  	    // Distance
	  	    strData = String.format("%.02f", jump.GetDistance() );
  	  	    map.put(getString(R.string.JumpDistance), strData);
  	  	    
  	  	    // Drop
	  	    strData = String.format("%.02f", jump.GetDrop() );
  	  	    map.put(getString(R.string.JumpDrop), strData);
  	  	    
  	  	    // Height
	  	    strData = String.format("%.02f", jump.GetHeight() );
  	  	    map.put(getString(R.string.JumpHeight), strData);
  		    
  		    jumpMaps.add(map);
  	   }
  		 
  		        
  	   // fill in the runitem layout
  	   SimpleAdapter adapter = new SimpleAdapter(this, jumpMaps, R.layout.jumpitem, from, to);
  	   lv.setAdapter(adapter);

	}
	
	// full update callback -- not used
	public void onFullUpdateCompleted(int status,
			ArrayList<ReconDataResult> results)
	{
		
	}

  
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState); 
	    
	    setContentView(R.layout.jumpview);
        // check if we are started internally, in which case we'll have data array in extras
	    // Otherwise retrieve data from the server
        Intent i = getIntent();
        Bundle bdata = i.getBundleExtra(ReconSDKApplicationActivity.RECON_DATA_BUNDLE);
        if (bdata != null)
        {
			try 
			{
				ReconDataResult result = ReconDataResult.fromBundle(bdata);
				this.onReceiveCompleted(ReconSDKManager.STATUS_OK, result);
			} 
			catch (Exception ex)
			{
				Toast toast = Toast.makeText(this, "Unexpected Internal Error", Toast.LENGTH_LONG);
				toast.show();
			}
        	
        }
        else
        {
    	    // retrieve Jump history using Recon SDK API
    	    mDataManager.receiveData(this, ReconEvent.TYPE_JUMP);
        }
	}
}
