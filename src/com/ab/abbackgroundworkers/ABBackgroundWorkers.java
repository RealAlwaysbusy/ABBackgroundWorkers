package com.ab.abbackgroundworkers;

import java.util.ArrayList;

//import anywheresoftware.b4a.B4AClass;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.Author;
import anywheresoftware.b4a.BA.CustomClass;
import anywheresoftware.b4a.BA.CustomClasses;
import anywheresoftware.b4a.BA.Hide;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.BA.Version;

@CustomClasses(values = {
		@CustomClass(name = "ABBackgroundWorker", fileNameWithoutExtension = "backgroundworker_handler"),	
})

@Author("Alain Bailleul")
@Version(1.03f)
@ShortName("ABBackgroundWorkers")
public class ABBackgroundWorkers {
	private BA ba;
	@SuppressWarnings("unused")
	private String eventName;
	private ArrayList<ABHandlerData> backgroundWorkers = new ArrayList<ABHandlerData>();
	private ABBackgroundWorkersManager backWorkers;
	
	/**
	 * Initializes the Background Worker Handler.
	 */
	public void Initialize(BA ba) {
		this.ba = ba;			
	}
	
	public void Start() throws Exception {
		final boolean debug = BA.isShellModeRuntimeCheck(ba);
		if (backgroundWorkers.size() > 0) {
			ba.postRunnable(new Runnable() {

				@Override
				public void run() {
					try {
						backWorkers = new ABBackgroundWorkersManager(ba, debug);
						for (ABHandlerData hd : backgroundWorkers)
							backWorkers.startWorker(Class.forName(fixClassName(hd)));
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

		}
	}
			
	private String fixClassName(ABHandlerData hd) {
		String className = hd.clazz.toLowerCase(BA.cul);
		if (className.contains(".") == false)
			className = BA.packageName + "." + className;
		return className;
	}
	
	/**
	 * Adds a background worker. An instance of the specified class will be created and initialized from a background thread.
	 * Class - The class name. The class should be a standard class
	 *
	 * You can call StartMessageLoop in the Initialize sub to keep the class instance running for as long as needed.
	 * Note that in debug mode the code will be executed by the main thread.
	 */
	public void AddBackgroundWorker (String ClassName) {
		ABHandlerData hd = new ABHandlerData(ClassName);
		backgroundWorkers.add(hd);
	}
		
	@Hide
	static class ABHandlerData {
		public final String clazz;
		public Object extra;	
		public ABHandlerData(String clazz) {
			this.clazz = clazz;			
		}

	}	
}
