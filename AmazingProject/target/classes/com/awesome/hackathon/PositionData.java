package com.awesome.hackathon;

import java.io.Serializable;

public class PositionData implements Serializable {

	private static final long serialVersionUID = 6064816632611716902L;
	private float mX, mY;
	private int mColour, mShape;
	private String mName;
	
	PositionData(String name, float x, float y, int colour, int shape)
	{
		mName = name;
		mX = x;
		mY = y;
		mColour = colour;
		mShape = shape;
	}
	
	public float getmX() {
		return mX;
	}

	public void setmX(float mX) {
		this.mX = mX;
	}

	public float getmY() {
		return mY;
	}

	public void setmY(float mY) {
		this.mY = mY;
	}

	public int getmColour() {
		return mColour;
	}

	public void setmColour(int mColour) {
		this.mColour = mColour;
	}

	public int getmShape() {
		return mShape;
	}

	public void setmShape(int mShape) {
		this.mShape = mShape;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}
}
