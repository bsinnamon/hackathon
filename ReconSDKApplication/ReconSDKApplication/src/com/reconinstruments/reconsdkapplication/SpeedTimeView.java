package com.reconinstruments.reconsdkapplication;

import java.util.ArrayList;
import java.util.Date;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.reconinstruments.ReconSDK.*;

/* This Activity demonstrates one possible way of handling multiple Recon Data Types within single Activity 
 * Views are Implemented inside standard TabHost; each Tab relates to one Recon Data Type
 * 
 * Containing Activity instantiates handlers, then relegates view management inside
 * each handler "OnDataReceived" Callback */

public class SpeedTimeView extends TabActivity
{
	    // SDK Manager copy for this activity
        ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);
    
        // Tab Host
	    TabHost tabHost;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState)
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tabview);
	 
	        // set up the tabs
	        tabHost = getTabHost();
	        tabHost.addTab(tabHost.newTabSpec("Time").setIndicator("Time").setContent(R.id.layout_tab_time));
	        tabHost.addTab(tabHost.newTabSpec("Speed").setIndicator("Speed").setContent(R.id.layout_tab_speed));
	 
	        tabHost.setCurrentTab(0);
	        
	        // trigger data fetch with corresponding handlers
	        // This is the only one from full update that will refresh, for variety only
	        mDataManager.receiveData(new TimeHandler(),  ReconEvent.TYPE_TIME);
	        mDataManager.receiveData(new SpeedHandler(), ReconEvent.TYPE_SPEED);
	
	    }
	    
	    // inner class that handles Speed
	    class SpeedHandler implements IReconDataReceiver
	    { 
		    // speed views
		    private TextView mSpeed            = (TextView) findViewById(R.id.id_speed_value);
		    private TextView mSpeedHorizontal  = (TextView) findViewById(R.id.id_speed_horizontal);
		    private TextView mSpeedVertical    = (TextView) findViewById(R.id.id_speed_vertical);
		    private TextView mSpeedAverage     = (TextView) findViewById(R.id.id_speed_average);
		    private TextView mSpeedMax         = (TextView) findViewById(R.id.id_speed_maximum);
		    private TextView mSpeedMaxAllTime  = (TextView) findViewById(R.id.id_speed_alltimemax);
		    
	    	public void onReceiveCompleted(int status, ReconDataResult result)
	    	{
	    		// check status first
	      		if (status != ReconSDKManager.STATUS_OK)
	      		{
	      			Toast toast = Toast.makeText(SpeedTimeView.this, "Speed Handler: Communication Failure with Transcend Service", Toast.LENGTH_LONG);
	      			toast.show();
	      			
	      			return;
	      		}
	      		
	      		// now simply dump data into text fields
	      		ReconSpeed rs = (ReconSpeed)result.arrItems.get(0);
	      		
	      		String strText = String.format(" %.2f", rs.GetSpeed() );
	      		mSpeed.setText(strText );
	      		
	      		strText = String.format(" %.2f", rs.GetHorizontalSpeed() );
	      		mSpeedHorizontal.setText(strText );
	      		
	      		strText = String.format(" %.2f", rs.GetVerticalSpeed() );
	      		mSpeedVertical.setText(strText );
	      		
	      		strText = String.format(" %.2f", rs.GetAverageSpeed() );
	      		mSpeedAverage.setText(strText );
	      		
	      		strText = String.format(" %.2f", rs.GetMaximumSpeed() );
	      		mSpeedMax.setText(strText );
	      		
	      		strText = String.format(" %.2f", rs.GetAllTimeMaxSpeed() );
	      		mSpeedMaxAllTime.setText(strText );
	    	}
	    	
	    	// full update callback -- not used
	    	public void onFullUpdateCompleted(int status,
	    			ArrayList<ReconDataResult> results)
	    	{
	    		
	    	}
	    }
	    
	    // inner class that handles Time
	    class TimeHandler implements IReconDataReceiver
	    {
	    	// time views
	        private TextView mTimeUTC          = (TextView) findViewById(R.id.id_time_utc);
	        private TextView mTimeLastUpdate   = (TextView) findViewById(R.id.id_time_lastupdate);
	        private TextView mTimeUpdateBefore = (TextView) findViewById(R.id.id_time_updatebefore);
	        
	    	public void onReceiveCompleted(int status, ReconDataResult result)
	    	{
	    		// check status first
	      		if (status != ReconSDKManager.STATUS_OK)
	      		{
	      			Toast toast = Toast.makeText(SpeedTimeView.this, "Time Handler: Communication Failure with Transcend Service", Toast.LENGTH_LONG);
	      			toast.show();
	      			
	      			return;
	      		}
	      		
	      		// now simply dump data into text fields
	      		ReconTime rtime = (ReconTime)result.arrItems.get(0);
	      		
	      		Date d = new Date(rtime.GetUTCTime() );
		  		mTimeUTC.setText(d.toString() );
		  		
		  		d = new Date(rtime.GetLastUpdate() );
		  		mTimeLastUpdate.setText(d.toString() );
		  		
		  		d = new Date(rtime.GetUpdateBefore() );
		  		mTimeUpdateBefore.setText(d.toString() );
	    	}
	    	
	    	// full update callback -- not used
	    	public void onFullUpdateCompleted(int status,
	    			ArrayList<ReconDataResult> results)
	    	{
	    		
	    	}
	    }

}
