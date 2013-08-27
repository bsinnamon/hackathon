package com.awesome.hackathon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TaskController {
	
	enum ReadMode
	{
		Task,
		Person,
		Map,
		Home,
	};
	
	private TaskPackage mCurrentTask;
	
	TaskController()
	{
		mCurrentTask = null;
	}
	
	@RequestMapping(value = "/RequestTask", method = RequestMethod.GET)
	public @ResponseBody TaskPackage  requestTask() {
		TaskPackage taskPackage = GetCurrentTask();
		return taskPackage;
	}
	
	@RequestMapping(value = "/ResetTask", method = RequestMethod.GET)
	public @ResponseBody String resetTask() {
		mCurrentTask = null;
		return "Done";
	}
	
	@RequestMapping(value = "/UpdateTask", method = RequestMethod.GET)
	public @ResponseBody TaskUpdatePackage updateTask() {
		TaskUpdatePackage update = GetCurrentTaskUpdate();
		return update;
	}
	
	@RequestMapping(value = "/AddPositions", method = RequestMethod.POST) //, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String  addPositions(HttpServletRequest request) {
		
		if (mCurrentTask != null)
		{
			String name = request.getParameter("name");
			String x = request.getParameter("x");
			String y = request.getParameter("y");
			String colour = request.getParameter("colour");
			String shape = request.getParameter("shape");
			
			int ix = 0, iy = 0, icolour = 0, ishape = 0;
			if (x != null) ix = Integer.parseInt(x);
			if (y != null) iy = Integer.parseInt(y);
			if (colour != null) icolour = Integer.parseInt(colour);
			if (shape != null) ishape = Integer.parseInt(shape);
			
			PositionData newPos = new PositionData(name, ix, iy, icolour, ishape);
			
			mCurrentTask.addPosition(newPos);
			
			return "Thank you";
		}
		
		return "Not ready yet";
	}
	
	private TaskPackage GetCurrentTask()
	{
		if (mCurrentTask == null)
		{
			try {
				String path = "/Ben/hackathon/";
				String taskfile = "task.txt";
				
				ReadMode mode = ReadMode.Task;
				String taskName = "";
				String personName = "", personGender = "", personComments = "", personPhoto = "";
				int personAge = 0;
				String mapImage = "";
				float mapScaleX = 0, mapScaleY = 0;
				int homePixelX = 0, homePixelY = 0, homeRealX = 0, homeRealY = 0, homeColour = 0;
				
				BufferedReader readTask = new BufferedReader(new FileReader(path + taskfile));
				String line = readTask.readLine();
				while (line != null)
				{
					if (line.length() > 0)
					{
						String tag = line.substring(0, line.indexOf(':')).trim();
						
						switch (mode)
						{
						case Task:
							if (tag.equals("name"))
								taskName = GetStringValue(line);
							else
								mode = ChangeToMode(line, mode);
							break;
						case Person:
							if (tag.equals("name"))
								personName = GetStringValue(line);
							else if (tag.equals("gender"))
								personGender = GetStringValue(line);
							else if (tag.equals("age"))
								personAge = GetIntValue(line);
							else if (tag.equals("comments"))
								personComments = GetStringValue(line);
							else if (tag.equals("photo"))
								personPhoto = GetStringValue(line);
							else
								mode = ChangeToMode(line, mode);
							break;
						case Map:
							if (tag.equals("image"))
								mapImage = GetStringValue(line);
							else if (tag.equals("scaleX"))
								mapScaleX = GetFloatValue(line);
							else if (tag.equals("scaleY"))
								mapScaleY = GetFloatValue(line);
							else
								mode = ChangeToMode(line, mode);
							break;
						case Home:
							if (tag.equals("pixelX"))
								homePixelX = GetIntValue(line);
							else if (tag.equals("pixelY"))
								homePixelY = GetIntValue(line);
							else if (tag.equals("realX"))
								homeRealX = GetIntValue(line);
							else if (tag.equals("realY"))
								homeRealY = GetIntValue(line);
							else if (tag.equals("colour"))
								homeColour = GetColourValue(line);
							else
								mode = ChangeToMode(line, mode);
							break;
						}
					}
					
					line = readTask.readLine();
				}
				
				readTask.close();
				
				PositionData p = new PositionData("home", homeRealX, homeRealY, homeColour, 0);
				ArrayList<PositionData> pos = new ArrayList<PositionData>();
				pos.add(p);
				
				int w, h;
				int[] pixels = null;
				BufferedImage img = ImageIO.read(new File(path + personPhoto));
				
				w = img.getWidth();
				h = img.getHeight();
				pixels = img.getRGB(0, 0, w, h, null, 0, w);
				
				ImageData photo = new ImageData(pixels, w, h);
				PersonData person = new PersonData(photo, personName, personGender, personAge, personComments);
				
				int[] mapPixels = null;
				BufferedImage mapImg = ImageIO.read(new File(path + mapImage));
				
				w = mapImg.getWidth();
				h = mapImg.getHeight();
				mapPixels = mapImg.getRGB(0, 0, w, h, null, 0, w);
				
				ImageData map = new ImageData(mapPixels,w, h);
				MapData m = new MapData(map, homePixelX, homePixelY, 0, mapScaleX, mapScaleY);
				
				mCurrentTask = new TaskPackage(taskName, 1);
				mCurrentTask.setmPerson(person);
				mCurrentTask.setmMap(m);
				mCurrentTask.setmPositions(pos);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return mCurrentTask;
	}
	
	private TaskUpdatePackage GetCurrentTaskUpdate()
	{
		if (mCurrentTask != null)
		{
			TaskUpdatePackage taskUpdate = new TaskUpdatePackage();
			
			taskUpdate.setmPositions(mCurrentTask.getmPositions());
			
			return taskUpdate;
		}
		
		return null;
	}
	
	private String GetStringValue(String line)
	{
		return line.substring(line.indexOf(':')+1).trim();
	}
	
	private int GetIntValue(String line)
	{
		String contents = line.substring(line.indexOf(':')+1).trim();
		if (contents != null)
			return Integer.parseInt(contents);
		return 0;
	}
	
	private float GetFloatValue(String line)
	{
		String contents = line.substring(line.indexOf(':')+1).trim();
		if (contents != null)
			return Float.parseFloat(contents);
		return 0;
	}
	
	private int GetColourValue(String line)
	{
		String contents = line.substring(line.indexOf(':')+1).trim();
		Color c = Color.getColor(contents);
		if (c != null)
			return c.getRGB();
		return 0;
	}
	
	private ReadMode ChangeToMode(String line, ReadMode currentMode)
	{
		String tag = line.substring(0, line.indexOf(':')).trim();
		if (tag.equals("Map"))
			return ReadMode.Map;
		else if (tag.equals("Person"))
			return ReadMode.Person;
		else if (tag.equals("Home Position"))
			return ReadMode.Home;
		else 
			return currentMode;
	}
	
}
