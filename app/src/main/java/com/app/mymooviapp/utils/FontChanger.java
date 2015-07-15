package com.app.mymooviapp.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontChanger {
	

	
	public static Typeface setRobotoRegular(Context context)
	{
		 return Typeface.createFromAsset(context.getAssets(),"roboto_regular.ttf");
	}

    public static Typeface setRobotoBold(Context context)
    {
        return Typeface.createFromAsset(context.getAssets(),"roboto_bold_new.ttf");
    }
	
	
	public static Typeface setRobotoSlab(Context context)
	{
		 return Typeface.createFromAsset(context.getAssets(),"roboto_slab.ttf");
	}
	
	public static Typeface setRobotoLight(Context context)
	{
		return Typeface.createFromAsset(context.getAssets(),"roboto_light.ttf");
	}
	
	public static Typeface setRobotoCondesed(Context context)
	{
		return Typeface.createFromAsset(context.getAssets(),"roboto_condensed.ttf");
	}
	public static Typeface setImpact(Context context)
	{
		return Typeface.createFromAsset(context.getAssets(),"impact.ttf");
	}
	public static Typeface setRobotoMedium(Context context)
	{
		return Typeface.createFromAsset(context.getAssets(),"robot_medium.ttf");
	}
	public static Typeface setRobotoThin(Context context)
	{
		return Typeface.createFromAsset(context.getAssets(),"roboto_thin_new.ttf");
	}
	
	public static Typeface setRobotoCondensedLight(Context context)
	{
		return Typeface.createFromAsset(context.getAssets(),"Roboto-CondensedItalic.ttf");
	}
	
	

}
