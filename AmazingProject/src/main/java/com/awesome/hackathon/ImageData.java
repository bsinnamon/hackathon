package com.awesome.hackathon;

import java.io.Serializable;

public class ImageData implements Serializable {

	private static final long serialVersionUID = -6617332040639785269L;
	private int[] mPixels;
	private int mWidth, mHeight;
	
	ImageData(int[] pixels, int width, int height)
	{
		mPixels = pixels;
		mWidth = width;
		mHeight = height;
	}
	
	public int[] getmPixels() {
		return mPixels;
	}

	public void setmPixels(int[] mPixels) {
		this.mPixels = mPixels;
	}

	public int getmWidth() {
		return mWidth;
	}

	public void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	public int getmHeight() {
		return mHeight;
	}

	public void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}
}
