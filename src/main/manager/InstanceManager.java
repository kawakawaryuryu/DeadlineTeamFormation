package main.manager;

public class InstanceManager {

	private static MeasuredDataManager measure;

	private InstanceManager() {
		measure = new MeasuredDataManager();
	}
	

	private static InstanceManager instance = new InstanceManager();
	
	
	
	public MeasuredDataManager getMeasure() {
		return measure;
	}
	
	public static InstanceManager getInstance() {
		return instance;
	}
	
}
