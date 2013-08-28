package com.reconinstruments.reconsdkapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.reconinstruments.webapi.SDKWebService;
import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.reconinstruments.webapi.WebRequestMessage.WebMethod;
import com.reconinstruments.webapi.WebRequestMessage.WebRequestBundle;

/**
 * Created by Samana on 24/08/13.
 */
public class TaskView extends Activity {

    TextView mTextViewTaskIDName;
    TextView mTextViewPersonName;
    TextView mTextViewPersonGender;
    TextView mTextViewPersonAge;
    TextView mTextViewComments;
    ImageView mImageViewPersonPhoto;
    MapView mMapView;

    final Runnable myRunnable = new Runnable() {
        public void run() {
            mMapView.invalidate();
            //Log.d("D", "XXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    };
    final Handler myHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.taskview);

        mTextViewTaskIDName = (TextView)findViewById(R.id.textViewTaskIDName);
        mTextViewPersonName = (TextView)findViewById(R.id.textViewPersonName);
        mTextViewPersonGender = (TextView)findViewById(R.id.textViewGender);
        mTextViewPersonAge = (TextView)findViewById(R.id.textViewAge);
        mTextViewComments = (TextView)findViewById(R.id.textViewComments);
        mImageViewPersonPhoto = (ImageView)findViewById(R.id.imageViewPhoto);


        LinearLayout layout = (LinearLayout)findViewById(R.id.layoutParent);
        mMapView = new MapView(this);
        mMapView.setMinimumWidth(428);
        mMapView.setMinimumHeight(400);
        mMapView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(mMapView);

        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {UpdateGUI();}
        }, 0, 100);

        UpdateTaskFromServer();
    }

    void UpdateGUI()
    {
        myHandler.post(myRunnable);
    }

    void UpdateTaskFromServer()
    {
        // Setting HTTP Headers
        List<NameValuePair> header = new ArrayList<NameValuePair>();
        header.add(new BasicNameValuePair("Accept","application/json"));

        // Setting Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("callback","process"));

        WebRequestBundle wrb = new WebRequestBundle(
                "IntentFilterActionName",
                "http://192.168.0.129:8080/hackathon/RequestTask",
                WebMethod.GET,
                "1",
                header,
                params
        );

        SDKWebService.httpRequest(getBaseContext(), false, 0, wrb, new WebResponseListener()
        {
            @Override
            public void onComplete(byte[] response, String statusCode, String statusId, String requestId)
            {
                String strJson = new String(response);
                ParseJSON(strJson);
            }

            @Override
            public void onComplete(String response, String statusCode, String statusId, String requestId)
            {
                String strJson = new String(response);
                ParseJSON(strJson);
            }

            void ParseJSON(String strJson)
            {
                try
                {
                    Task tsk = new Task();
                    JSONObject jo = new JSONObject(strJson);
                    tsk.fromJSONObject(jo);
                    mTextViewTaskIDName.setText(String.format("%d : %s", tsk.mIdentifier, tsk.mName));
                    mTextViewPersonName.setText(tsk.mPerson.mName);
                    mTextViewPersonGender.setText(tsk.mPerson.mGender);
                    mTextViewPersonAge.setText(String.format("%d", tsk.mPerson.mAge));
                    mTextViewComments.setText(tsk.mPerson.mComments);
                    mImageViewPersonPhoto.setImageBitmap(tsk.mPerson.mPhoto);
                    mMapView.setMapImage(tsk.mMap.mImage);
                }
                catch (JSONException ex)
                {
                }

                //UpdateTaskFromServer();
            }
        });
    }

    public class MapView extends View
    {
        Paint paint = new Paint();
        Bitmap mMapImage;


        public MapView(Context context) {
            super(context);
        }

        void setMapImage(Bitmap img)
        {
            mMapImage = img;
        }

        boolean bOff = false;
        int posX = 10;
        int posY = 10;
        int speed = 1;

        @Override
        public void onDraw(Canvas canvas)
        {
            paint.setColor(Color.WHITE);
            if(mMapImage != null)
            {
                canvas.drawBitmap(mMapImage, 0, 0, paint);
            }
            paint.setColor(Color.RED);
            paint.setStrokeWidth(3);

            if(!bOff)
            {
                canvas.drawCircle(posX, posY, 4, paint);
                posX += speed;
                posY += speed;

                if(posX > 100)
                    speed = -1;
                else if(posX < 10)
                    speed = 1;
            }
            bOff = !bOff;
        }
    }

    public class ImageDescriptor
    {
        public int mWidth;
        public int mHeight;
        public int[] mData;

        public void fromJSONObject(JSONObject jo)
        {
            try
            {
                mWidth = jo.getInt("mWidth");
                mHeight = jo.getInt("mHeight");

                Log.d("Img", String.format(" ------------ %d, %d -------------", mWidth, mHeight));

                JSONArray jaData = jo.getJSONArray("mPixels");
                mData = new int[mWidth * mHeight];
                for(int i=0; i<mData.length && i<jaData.length(); i++)
                {
                    mData[i] = jaData.getInt(i);
                }
                Log.d("X", mData == null ? "mData is NOT null" : "mData is null");
            }
            catch (JSONException jex)
            {
            }
        }

        public Bitmap ToBitmap()
        {
            return Bitmap.createBitmap(mData, mWidth, mHeight, Bitmap.Config.ARGB_8888);
        }
    }

    public class Person
    {
        public String mName;
        public Bitmap mPhoto;
        public String mGender;
        public String mComments;
        public int mAge;

        public JSONObject toJSONObject()
        {
            return null;
        }

        public void fromJSONObject(JSONObject jo)
        {
            try
            {
                mName = jo.getString("mName");
                mGender = jo.getString("mGender");
                mComments = jo.getString("mComments");
                mAge = jo.getInt("mAge");
                JSONObject joPhoto = jo.getJSONObject("mPhoto");
                ImageDescriptor imgDesc = new ImageDescriptor();
                imgDesc.fromJSONObject(joPhoto);
                mPhoto = imgDesc.ToBitmap();
            }
            catch(JSONException ex)
            {
            }
        }
    }

    public class Map
    {
        public Bitmap mImage;
        public int mHomePosX;
        public int mHomePosY;
        public int mHomePositionDataIndex;
        public float mPixelToCoordsScaleX;
        public float mPixelToCoordsScaleY;

        public void fromJSONObject(JSONObject jo)
        {
            try
            {
                mHomePosX = jo.getInt("mHomePosX");
                mHomePosY = jo.getInt("mHomePosY");
                mHomePositionDataIndex = jo.getInt("mHomePositionDataIndex");
                mPixelToCoordsScaleX = (float)jo.getDouble("mPixelToCoordsScaleX");
                mPixelToCoordsScaleY = (float)jo.getDouble("mPixelToCoordsScaleY");
                JSONObject joPhoto = jo.getJSONObject("mImage");
                ImageDescriptor imgDesc = new ImageDescriptor();
                imgDesc.fromJSONObject(joPhoto);
                mImage = imgDesc.ToBitmap();
            }
            catch(JSONException ex)
            {
            }
        }
    }

    public class Position
    {
        public float mX;
        public float mY;
        public int mColor;
        public int mShape;
        public String mName;

        public void fromJSONObject(JSONObject jo)
        {
            try
            {
                mX = (float)jo.getDouble("mX");
                mY = (float)jo.getDouble("mY");
                mColor = jo.getInt("mColor");
                mShape = jo.getInt("mShape");
                mName = jo.getString("mName");
            }
            catch(JSONException jex)
            {
            }
        }
    }

    public class Task
    {
        public int mIdentifier;
        public String mName;
        public Person mPerson;
        public Map mMap;
        public Position[] mPositions;

        public void fromJSONObject(JSONObject jo)
        {
            try
            {
                mIdentifier = jo.getInt("mIdentifier");
                mName = jo.getString("mName");

                JSONObject joPerson = jo.getJSONObject("mPerson");
                mPerson = new Person();
                mPerson.fromJSONObject(joPerson);

                JSONObject joMap = jo.getJSONObject("mMap");
                mMap = new Map();
                mMap.fromJSONObject(joMap);

                JSONArray jaPositions = jo.getJSONArray("mPositions");
                mPositions = new Position[jaPositions.length()];
                for(int i=0; i<mPositions.length; i++)
                {
                    mPositions[i] = new Position();
                    mPositions[i].fromJSONObject(jaPositions.getJSONObject(i));
                }
            }
            catch(JSONException ex)
            {
            }
        }
    }

}