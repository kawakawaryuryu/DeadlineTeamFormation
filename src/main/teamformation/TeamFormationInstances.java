package main.teamformation;

import post.Post;

/**
 * 1回のチーム編成の中で使われるパラメーター、インスタンス等を管理するクラス
 */
public class TeamFormationInstances {

	public TeamFormationInstances() {
		
	}
	
	
	private static TeamFormationInstances instance = new TeamFormationInstances();
	
	private static TeamFormationParameter parameter = new TeamFormationParameter();
	private static TeamFormationMeasuredData measure = new TeamFormationMeasuredData();
	private static Post post = new Post();
	

	public void initialize() {
		parameter.initialize();
		measure.initialize();
	}
	
	public TeamFormationParameter getParameter() {
		return parameter;
	}
	
	public TeamFormationMeasuredData getMeasure() {
		return measure;
	}

	public Post getPost() {
		return post;
	}
	
	
	public static TeamFormationInstances getInstance() {
		return instance;
	}

}
