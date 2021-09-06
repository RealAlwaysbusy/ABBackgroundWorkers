package com.ab.abbackgroundworkers;

import java.lang.reflect.Method;

import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Hide;

@Hide
public class ABBackgroundWorkersManager {
	private final boolean debug;
	private final BA ba;
	
	public ABBackgroundWorkersManager(BA ba, boolean debug) {
		this.debug = debug;
		this.ba = ba;
	}
	
	public void startWorker(final Class<?> className) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				try {
					Method m = null;
					try {
						m = className.getDeclaredMethod("_initialize", BA.class);
					} catch (NoSuchMethodException e) {
						m = className.getDeclaredMethod("innerInitializeHelper", BA.class);
					}
					
					@SuppressWarnings("deprecation")
					B4AClass handler = (B4AClass)className.newInstance();
					m.invoke(handler, (Object)null);
					BA ba = handler.getBA();
					if (BA.isShellModeRuntimeCheck(ba)) {
						ba.raiseEvent(null, "initialize", (Object)null);
					}
					
					System.out.println("Worker ended (" + className + ")");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		if (debug) {
			ba.postRunnable(r);			
		}
		else {
			Thread t = new Thread(r);
			t.setDaemon(true);
			t.start();			
		}
		
	}	
}