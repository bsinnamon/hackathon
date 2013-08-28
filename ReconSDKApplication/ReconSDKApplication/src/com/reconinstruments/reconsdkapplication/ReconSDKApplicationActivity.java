package com.reconinstruments.reconsdkapplication;

import java.lang.reflect.Method;

import com.reconinstruments.ReconSDK.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ReconSDKApplicationActivity extends Activity implements IReconEventListener
{
	 private static final String TAG = ReconSDKApplicationActivity.class.getSimpleName();
	 ReconSDKManager mDataManager    = ReconSDKManager.Initialize(this);
	 
	 public static final String  RECON_DATA_BUNDLE  = "RECON_DATA_BUNDLE";      
	 
    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)  
    {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.main);
         
        // we use main screen as listeners and menu. Subscreens (activities) display history data for particular recon event
        try 
        {
        	//WHY WWERE TEMP AND JUMP SELECTED HERE? CONFUSED BY THIS TRY
        	//mDataManager.registerListener(this, ReconEvent.TYPEI_RUN | ReconEvent.TYPE_ALTITUDE);
        	mDataManager.registerListener(this, ReconEvent.TYPE_TEMPERATURE | ReconEvent.TYPE_JUMP); 
		} 
        catch (Exception ex) 
        {
			Log.e(TAG, ex.getMessage() );
		}        
        
        // ----------------------------------
        ListView listView = (ListView) findViewById(R.id.mylist);
        //String[] values = new String[] { "Altitude", "Distance", "Jumps", "Temperature", "Vertical", "Speed", "Runs", "Full Items", "Task"};
        String[] values = new String[] { "Task"};

        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the View to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() 
        {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        	{
        		Toast.makeText(getApplicationContext(),	"Click ListItem Number " + position, Toast.LENGTH_LONG).show();
        		launchIntent(position);
        	}
        });
    }
    
    @Override 
    public void onStart()
    {
    	super.onStart();
    	//this.openOptionsMenu();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		 this.getMenuInflater().inflate(R.menu.mainmenu, menu);
		 return true;  
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// show new UI screen that will fetch Jump Bundle & dump in list view
		Intent intent = null;
		
		switch (item.getItemId() )
		{
		 
			case R.id.mnuJumpData:
				
				intent = new Intent(this, JumpHistory.class);
				break;
				
			case R.id.mnuFullUpdate:
				
				intent = new Intent(this, FullUpdate.class);
				break;
				
	        case R.id.mnuRunData:
				
				intent = new Intent(this, RunHistory.class);
				break;
				
			case R.id.mnuTemperatureData:
				
			    intent = new Intent(this, TemperatureView.class);
				break;
				
			case R.id.mnuAltitudeData:
				
				intent = new Intent(this, AltitudeView.class);
				break;
				
			case R.id.mnuDistanceData:
				
				intent = new Intent(this, DistanceView.class);
				break;
				
			case R.id.mnuVerticalData:
				
				intent = new Intent(this, VerticalView.class);
				break;
			
				
			case R.id.mnuSpeedTimeData:
				
				intent = new Intent(this, SpeedTimeView.class);
				break;

            case R.id.mnuTaskData:
                intent = new Intent(this, TaskView.class);
                break;

		    default:
				return false;
		}
		
		try
		{
		   this.startActivity(intent);
		}
		catch (Exception ex)
		{
			Toast toast = Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG );
			toast.show();
		}
		return true;  
	}
	
	
	public void launchIntent(int id)
	{
		Intent intent = null;
		switch(id)
		{
/*
case 0:
intent = new Intent(this, AltitudeView.class);
break;
case 1:
intent = new Intent(this, DistanceView.class);
break;
case 2:
intent = new Intent(this, JumpHistory.class);
break;
case 3:
intent = new Intent(this, TemperatureView.class);
break;
case 4:
intent = new Intent(this, VerticalView.class);
break;
case 5:
intent = new Intent(this, SpeedTimeView.class);
break;
case 6:
intent = new Intent(this, RunHistory.class);
break;
case 7:
intent = new Intent(this, FullUpdate.class);
break;
*/
            case 0:
                intent = new Intent(this, TaskView.class);
                break;
		    default:
				return;
		}
		
		try
		{
		   this.startActivity(intent);
		}
		catch (Exception ex)
		{
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG ).show();
		}
	}
	
	// event change notifier callback
	public void onDataChanged(ReconEvent event, Method m)
	{	
		Toast toast = null;
		
		if (event.getType() == ReconEvent.TYPE_TEMPERATURE)
		{
			// example how to use changed field. If not broadcasted, it can be null
			if (m != null)
			{
			   
			    try 
			    {
				   if (m.getName().equals("GetMaxTemperature") == true)
					   toast = Toast.makeText(this, String.format("Maximum Temperature Broadcast: [%d Celsius]", (Integer)m.invoke(event) ), Toast.LENGTH_LONG);
		
				   else if (m.getName().equals("GetMinTemperature") == true)
					   toast = Toast.makeText(this, String.format("Minimum Temperature Broadcast: [%d] Celsius", (Integer)m.invoke(event) ), Toast.LENGTH_LONG);
				   
				   else if (m.getName().equals("GeAllTimeMaxTemperature") == true)
					   toast = Toast.makeText(this, String.format("All Time Maximum Temperature Broadcast: [%d] Celsius", (Integer)m.invoke(event) ), Toast.LENGTH_LONG);
				  
				   else if (m.getName().equals("GetAllTimeMinTemperature") == true)
					   toast = Toast.makeText(this, String.format("All Time Minimum Temperature Broadcast: [%d] Celsius", (Integer)m.invoke(event) ), Toast.LENGTH_LONG); 
					   
				   else  // just plain broadcast
					   toast = Toast.makeText(this, "Temperature Broadcast Received",  Toast.LENGTH_LONG);
				} 
			    catch (Exception e)
				{
					toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
				}
			}
			else
				toast = Toast.makeText(this, "Temperature Broadcast Received; No Changed Indication",  Toast.LENGTH_LONG);
		}
		
		else if (event.getType() == ReconEvent.TYPE_JUMP)
		{
			toast = Toast.makeText(this, "Jump Broadcast Received!!",  Toast.LENGTH_LONG);
		}
		
		else if (event.getType() == ReconEvent.TYPE_DISTANCE)
		{
			toast = Toast.makeText(this, "Distance Broadcast Received!!",  Toast.LENGTH_LONG);
		}
		
		else if (event.getType() == ReconEvent.TYPE_VERTICAL)
		{
			toast = Toast.makeText(this, "Vertical Broadcast Received!!",  Toast.LENGTH_LONG);
		}
		
		else if (event.getType() == ReconEvent.TYPE_RUN)
		{
			toast = Toast.makeText(this, "Run Broadcast Received!!",  Toast.LENGTH_LONG);
		}
		
		else if (event.getType() == ReconEvent.TYPE_ALTITUDE)
		{
			toast = Toast.makeText(this, "Altitude Broadcast Received!!",  Toast.LENGTH_LONG);
		}
		
		else
			toast = Toast.makeText(this, "Unknown Broadcast Recieved", Toast.LENGTH_LONG);
		
		toast.show();
	}
}
