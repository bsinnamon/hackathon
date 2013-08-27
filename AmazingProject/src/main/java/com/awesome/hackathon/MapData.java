package com.awesome.hackathon;

import java.io.Serializable;

public class MapData implements Serializable {
	
	private static final long serialVersionUID = 1965002087104154101L;
	private ImageData mImage;
	private int mHomePosX, mHomePosY, mHomePositionDataIndex;
	private float mPixelToCoordsScaleX, mPixelToCoordsScaleY;
	
	MapData(ImageData image, int homeX, int homeY, int homeIndex, float scaleX, float scaleY)
	{
		mImage = image;
		mHomePosX = homeX;
		mHomePosY = homeY;
		mHomePositionDataIndex = homeIndex;
		mPixelToCoordsScaleX = scaleX;
		mPixelToCoordsScaleY = scaleY;
	}

	public ImageData getmImage() {
		return mImage;
	}

	public void setmImage(ImageData mImage) {
		this.mImage = mImage;
	}

	public int getmHomePosX() {
		return mHomePosX;
	}

	public void setmHomePosX(int mHomePosX) {
		this.mHomePosX = mHomePosX;
	}

	public int getmHomePosY() {
		return mHomePosY;
	}

	public void setmHomePosY(int mHomePosY) {
		this.mHomePosY = mHomePosY;
	}

	public int getmHomePositionDataIndex() {
		return mHomePositionDataIndex;
	}

	public void setmHomePositionDataIndex(int mHomePositionDataIndex) {
		this.mHomePositionDataIndex = mHomePositionDataIndex;
	}

	public float getmPixelToCoordsScaleX() {
		return mPixelToCoordsScaleX;
	}

	public void setmPixelToCoordsScaleX(float mPixelToCoordsScaleX) {
		this.mPixelToCoordsScaleX = mPixelToCoordsScaleX;
	}

	public float getmPixelToCoordsScaleY() {
		return mPixelToCoordsScaleY;
	}

	public void setmPixelToCoordsScaleY(float mPixelToCoordsScaleY) {
		this.mPixelToCoordsScaleY = mPixelToCoordsScaleY;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
