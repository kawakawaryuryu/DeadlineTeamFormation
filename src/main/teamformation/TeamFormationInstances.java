package main.teamformation;

import post.DelayPost;
import post.Post;

/**
 * 1回のチーム編成の中で使われるパラメーター、インスタンス等を管理するクラス
 */
public class TeamFormationInstances {

	private static TeamFormationParameter parameter;
	private static TeamFormationMeasuredData measure;
	private static Post post;

	private TeamFormationInstances() {
		parameter = new TeamFormationParameter();
		measure = new TeamFormationMeasuredData();
		post = new DelayPost();
	}
	
	
	private static TeamFormationInstances instance = new TeamFormationInstances();
	

	public void initialize() {
		parameter.initialize();
		measure.initialize();
		post.initialize();
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
