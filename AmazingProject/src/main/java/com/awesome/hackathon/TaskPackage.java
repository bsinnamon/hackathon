package com.awesome.hackathon;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskPackage implements Serializable {

	private static final long serialVersionUID = 7759411119779324160L;
	private int mIdentifier;
	private String mName;
	private ArrayList<PositionData> mPositions;
	private PersonData mPerson;
	private MapData mMap;
	
	TaskPackage(String name, int identifier)
	{
		mName = name;
		mIdentifier = identifier;
	}
	
	public void addPosition(PositionData p)
	{
		for (int i = 0; i < mPositions.size(); ++i)
		{
			if (mPositions.get(i).getmName().equals(p.getmName()))
			{
				mPositions.remove(i);
				break;
			}
		}
		
		mPositions.add(p);
	}
	
	public String getmName() {
		return mName;
	}
	public MapData getmMap() {
		return mMap;
	}
	public void setmMap(MapData mMap) {
		this.mMap = mMap;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	
	public ArrayList<PositionData> getmPositions() {
		return mPositions;
	}
	public void setmPositions(ArrayList<PositionData> mPositions) {
		this.mPositions = mPositions;
	}
	
	public int getmIdentifier() {
		return mIdentifier;
	}
	public void setmIdentifier(int mIdentifier) {
		this.mIdentifier = mIdentifier;
	}

	public PersonData getmPerson() {
		return mPerson;
	}

	public void setmPerson(PersonData mPerson) {
		this.mPerson = mPerson;
	}

}
