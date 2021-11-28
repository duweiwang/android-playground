package com.example.wangduwei.project.takephoto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 绘制工具类
 * 
 * @author luopeihuan
 * 
 */
public class DrawUtils {
	public static float sDensity = 1.0f;
	public static int sDensityDpi;
	public static float sFontDensity;
    
	public static int sWidthPixels = -1;
	public static int sHeightPixels = -1;
	public static int sRealWidthPixels = -1;
	public static int sRealHeightPixels = -1;
	
	private static Class<?> sClass = null;
	private static Method sMethodForWidth = null;
	private static Method sMethodForHeight = null;

	// 在某些机子上存在不同的density值，所以增加两个虚拟值
	public static float sVirtualDensity = -1;
	public static float sVirtualDensityDpi = -1;
	
	public static int sTouchSlop = 15;
	
	private static Point sOutSize = new Point();
	/**
	 * dip/dp转像素
	 * 
	 * @param dipValue
	 *            dip或 dp大小
	 * @return 像素值
	 */
	public static int dip2px(float dipValue) {
//		return (int) (dipValue * sDensity + 0.5f);
		if (dipValue >= 0) {
			return (int) (dipValue * sDensity - 0.5f);
		} else {
			return (int) (dipValue * sDensity + 0.5f);
		}
	}

	/**
	 * 像素转dip/dp
	 * 
	 * @param pxValue
	 *            像素大小
	 * @return dip值
	 */
	public static int px2dip(float pxValue) {
//		final float scale = sDensity;
//		return (int) (pxValue / scale + 0.5f);
		if (pxValue >= 0) {
			return (int) (pxValue / sDensity + 0.5f);
		} else {
			return (int) (pxValue / sDensity - 0.5f);
		}
	}

	/**
	 * sp 转 px
	 * 
	 * @param spValue
	 *            sp大小
	 * @return 像素值
	 */
	public static int sp2px(float spValue) {
		final float scale = sDensity;
		return (int) (scale * spValue);
	}

	/**
	 * px转sp
	 * 
	 * @param pxValue
	 *            像素大小
	 * @return sp值
	 */
	public static int px2sp(float pxValue) {
		final float scale = sDensity;
		return (int) (pxValue / scale);
	}

	@SuppressLint("NewApi")
	public synchronized static void resetDensity(Context context) {
		if (context != null && null != context.getResources()) {
			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			sDensity = metrics.density;
			sFontDensity = metrics.scaledDensity;
			sDensityDpi = metrics.densityDpi;
			
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			if (true) {
				display.getSize(sOutSize);
				sWidthPixels = sOutSize.x;
				sHeightPixels = sOutSize.y;
			} else {
				sWidthPixels = display.getWidth();
				sHeightPixels = display.getHeight();
			}
			
			try {
				Class<?> clazz = Class.forName("android.view.Display");
				Point realSize = new Point();
				Method method = clazz.getMethod("getRealSize", Point.class);
				method.invoke(display, realSize);
				sRealWidthPixels = realSize.x;
				sRealHeightPixels = realSize.y;
			} catch (Throwable e) {
				sRealWidthPixels = sWidthPixels;
				sRealHeightPixels = sHeightPixels;
			}
			
			try {
				final ViewConfiguration configuration = ViewConfiguration.get(context);
				if (null != configuration) {
					sTouchSlop = configuration.getScaledTouchSlop();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public static int getTabletScreenWidth(Context context) {
		int width = 0;
		if (context != null) {
			try {
				WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
				Display display = wm.getDefaultDisplay();
				if (sClass == null) {
					sClass = Class.forName("android.view.Display");
				}
				if (sMethodForWidth == null) {
					sMethodForWidth = sClass.getMethod("getRealWidth");
				}
				width = (Integer) sMethodForWidth.invoke(display);
			} catch (Exception e) {
			}
		}

		// Rect rect= new Rect();
		// ((Activity)
		// context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		// int statusbarHeight = height - rect.bottom;
		if (width == 0) {
			width = getRealWidth(context);
		}

		return width;
	}

	public static int getTabletScreenHeight(Context context) {
		int height = 0;
		if (context != null) {
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			try {
				if (sClass == null) {
					sClass = Class.forName("android.view.Display");
				}
				if (sMethodForHeight == null) {
					sMethodForHeight = sClass.getMethod("getRealHeight");
				}
				height = (Integer) sMethodForHeight.invoke(display);
			} catch (Exception e) {
			}
		}

		// Rect rect= new Rect();
		// ((Activity)
		// context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		// int statusbarHeight = height - rect.bottom;
		if (height == 0) {
			height = getRealHeight(context);
		}

		return height;
	}
	
	public static void setVirtualDensity(float density) {
		sVirtualDensity = density;
	}
	
	public static void setVirtualDensityDpi(float densityDpi) {
		sVirtualDensityDpi = densityDpi;
	}
	
	/**
	 * 注意：尽量不要使用此方法，建议使用WindowController.getScreenWidth()
	 * @param context
	 * @return
	 */
	public static int getRealWidth(Context context) {
		if (sRealWidthPixels == -1 || sWidthPixels == -1) {
			resetDensity(context);
		}
		if (/*Machine.IS_SDK_ABOVE_KITKAT*/true) {
			return sRealWidthPixels;
		}
		return sWidthPixels;
	}
	
	/**
	 * 注意：尽量不要使用此方法，建议使用WindowController.getScreenHeight()
	 * @param context
	 * @return
	 */
	public static int getRealHeight(Context context) {
		if (sRealHeightPixels == -1 || sHeightPixels == -1) {
			resetDensity(context);
		}
		if (/*Machine.IS_SDK_ABOVE_KITKAT*/true) {
			return sRealHeightPixels;
		}
		return sHeightPixels;
	}
	
	/**
	 * 根据TextSize计算字体的高度
	 * @param fontSize
	 * @return
	 */
	public static double getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + dip2px(1);
//		return Math.ceil(fm.descent - fm.ascent);
	}
	
	public static boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}
}
