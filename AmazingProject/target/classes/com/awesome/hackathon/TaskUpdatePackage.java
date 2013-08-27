package com.awesome.hackathon;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskUpdatePackage implements Serializable {

	private static final long serialVersionUID = 5247015454669069255L;
	private ArrayList<PositionData> mPositions;
	
	public ArrayList<PositionData> getmPositions() {
		return mPositions;
	}
	public void setmPositions(ArrayList<PositionData> mPositions) {
		this.mPositions = mPositions;
	}
	
}
