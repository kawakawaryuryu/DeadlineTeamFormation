package task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.Administrator;
import main.Constant;

public class Task {
	private int id;
	private int numberOfSubtask;	//1タスク中のサブタスクの数
	private int taskRequireSum = 0;	//1タスク中のリソースの合計（サブタスクを全て処理したときの報酬の合計）
	private int deadlineInTask = 0;	//タスクのデッドライン（デッドラインが一番小さいサブタスクに合わせる）
	private boolean mark = false;	//タスクがマークされているかどうか
	private List<SubTask> subtaskList = new ArrayList<SubTask>();	//タスクが持つサブタスクを保持するリスト
	public List<SubTask> subtasksByMembers = new ArrayList<SubTask>();	//リーダ以外が処理するサブタスクリスト
	
//	private static Random require_random = new Random(1009);	//1009リソースのランダムインスタンス
	
	/**
	 * コンストラクタ
	 * @param id 
	 * @param numberOfSubtask
	 * @param deadline
	 */
	public Task(int id, int numberOfSubtask, int deadline){
		this.id = id;
		this.numberOfSubtask = numberOfSubtask;	//1タスク中のサブタスクの数
		this.deadlineInTask = deadline;
		for(int i = 0; i < numberOfSubtask; i++){
			int[] require = new int[Constant.RESOURCE_NUM];
			for(int j = 0; j < Constant.RESOURCE_NUM; j++){
				require[j] = Constant.taskRequireLcm() * (Administrator.require_random.nextInt(Constant.TASK_REQUIRE_MAX) + Constant.TASK_REQUIRE_INIT);	//リソースをランダムに決定
				taskRequireSum += require[j];	//タスク中の合計リソースを計算
			}
			subtaskList.add(new SubTask(require, deadlineInTask));	//サブタスクの生成
		}
		
	}
	
	/**
	 * idを返す
	 * @return
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * タスク中のサブタスク数を返す
	 * @return
	 */
	public int getSubTaskNum(){
		return numberOfSubtask;
	}
	
	/**
	 * 1タスク中のリソースの合計を返す
	 * @return
	 */
	public int getTaskRequireSum(){
		return taskRequireSum;
	}
	
	/**
	 * タスクのデッドラインを返す
	 * @return
	 */
	public int getDeadlineInTask(){
		return deadlineInTask;
	}
	
	/**
	 * タスク、サブタスクのデッドラインを1減らす
	 */
	public void subtractDeadlineInTask(){
		for(SubTask subtask : subtaskList){
			subtask.subtractDeadlinePerTurn();
		}
		deadlineInTask -= 1;
	}
	
	/**
	 * タスクにマークをする
	 * @param isMarking
	 */
	public void markingTask(boolean isMarking){
		mark = isMarking;
	}
	
	/**
	 * マークされているかを返す
	 * @return
	 */
	public boolean getMark(){
		return mark;
	}
	
	/**
	 * サブタスクリストをリソースの降順にソート
	 */
	public void sortSubTaskListByRequire(){
		Collections.sort(subtaskList, new Comparator<SubTask>(){
			public int compare(SubTask st1, SubTask st2){
				return st2.getRequireSum() - st1.getRequireSum();
			}
		});
	}
	
	/**
	 * サブタスクリストをデッドラインの昇順にソート
	 */
	public void sortSubTaskByDeadline(){
		Collections.sort(subtaskList, new Comparator<SubTask>(){
			public int compare(SubTask st1, SubTask st2){
				return st1.getDeadline() - st2.getDeadline();
			}
		});
	}
	
	/**
	 * サブタスクから要素を削除する
	 * @param index
	 */
	public SubTask removeSubTaskList(int index){
		return subtaskList.remove(index);
	}
	
	/**
	 * サブタスクのリストを返す
	 * @param i
	 * @return
	 */
	public SubTask getSubTaskList(int i){
		return subtaskList.get(i);
	}
	
	/**
	 * サブタスクリストを返す
	 * @return
	 */
	public List<SubTask> getSubTaskList(){
		return subtaskList;
	}
	
	/**
	 * idとnumberOfSubtask（サブタスク数）を返す
	 */
	public String toString(){
		StringBuffer string = new StringBuffer();
		string.append("id = " + id + " / subtask_num = " + numberOfSubtask + " / deadline = " + deadlineInTask + " / mark = " + mark +  " / taskRequireSum = " + taskRequireSum);
		string.append(" / subtaskList(require) = ");
		for(SubTask subtask : subtaskList){
			for(int i = 0; i< Constant.RESOURCE_NUM; i++){
				string.append(subtask.getRequire()[i] + " ");
			}
			string.append("  ");
		}
		return string.toString();
	}
	
	/**
	 * 保持しているリスト、変数を初期化する
	 */
	public void clear(){
		subtasksByMembers.clear();
		for(SubTask subtask : subtaskList){
			subtask.candidates.clear();
		}
	}
	
	public void clearAgentInfo() {
		for(SubTask subtask : subtaskList){
			subtask.getAgentInfo().clear();
		}
	}
	
}
