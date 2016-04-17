package task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import main.RandomKey;
import main.RandomManager;
import constant.FixedConstant;

public class FixedTask {
	private int id;
	private int numberOfSubtask;	//1タスク中のサブタスクの数
	private int taskRequireSum = 0;	//1タスク中のリソースの合計（サブタスクを全て処理したときの報酬の合計）
	private int deadlineInTask = 0;	//タスクのデッドライン（デッドラインが一番小さいサブタスクに合わせる）
	private boolean mark = false;	//タスクがマークされているかどうか
	private int removedMarkNumByEstimationFailure = 0;	//見積もり失敗によってマークを外された回数
	private int removedMarkNumByTeamFormationFailure = 0;	//チーム編成失敗によってマークを外された回数
	private ArrayList<FixedSubtask> subtasks = new ArrayList<FixedSubtask>();	//タスクが持つサブタスクを保持するリスト
	public ArrayList<FixedSubtask> subtasksByMembers = new ArrayList<FixedSubtask>();	//リーダ以外が処理するサブタスクリスト
	
//	private static Random require_random = new Random(1009);	//1009リソースのランダムインスタンス
	
	/**
	 * コンストラクタ
	 * @param id 
	 * @param numberOfSubtask
	 * @param deadline
	 */
	public FixedTask(int id, int numberOfSubtask, int deadline){
		this.id = id;
		this.numberOfSubtask = numberOfSubtask;	//1タスク中のサブタスクの数
		this.deadlineInTask = deadline;
		for(int i = 0; i < numberOfSubtask; i++){
			int[] require = new int[FixedConstant.RESOURCE_NUM];
			for(int j = 0; j < require.length; j++){
				require[j] = FixedConstant.TASK_REQUIRE_MALTIPLE * 
						FixedConstant.TASK_DEADLINE_MULTIPLE *
						(RandomManager.getRandom(RandomKey.REQUIRE_RANDOM).nextInt(FixedConstant.TASK_REQUIRE_MAX) 
								+ FixedConstant.TASK_REQUIRE_INIT);
//				require[j] = 3;
				taskRequireSum += require[j];	//タスク中の合計リソースを計算
			}
			subtasks.add(new FixedSubtask(require, deadlineInTask));	//サブタスクの生成
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
		for(FixedSubtask subtask : subtasks){
			subtask.subtractDeadlinePerTurn();
		}
		deadlineInTask -= 1;
	}
	
	/**
	 * タスクにマークをする
	 * @param isMarking
	 * @param failure チーム編成失敗 or 見積もり失敗
	 */
	public void markingTask(boolean isMarking, Failure failure){
		mark = isMarking;
		if(!isMarking && failure == Failure.ESTIMATION_FAILURE){
			removedMarkNumByEstimationFailure++;
		}
		else if(!isMarking && failure == Failure.TEAM_FORMATION_FAILURE){
			removedMarkNumByTeamFormationFailure++;
		}
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
		Collections.sort(subtasks, new Comparator<FixedSubtask>(){
			public int compare(FixedSubtask st1, FixedSubtask st2){
				return st2.getRequireSum() - st1.getRequireSum();
			}
		});
	}
	
	/**
	 * サブタスクリストをデッドラインの昇順にソート
	 */
	public void sortSubTaskByDeadline(){
		Collections.sort(subtasks, new Comparator<FixedSubtask>(){
			public int compare(FixedSubtask st1, FixedSubtask st2){
				return st1.getDeadline() - st2.getDeadline();
			}
		});
	}
	
	/**
	 * サブタスクから要素を削除する
	 * @param index
	 */
	public FixedSubtask removeSubTaskList(int index){
		return subtasks.remove(index);
	}
	
	/**
	 * サブタスクのリストを返す
	 * @param i
	 * @return
	 */
	public FixedSubtask getSubTaskList(int i){
		return subtasks.get(i);
	}
	
	/**
	 * サブタスクリストを返す
	 * @return
	 */
	public ArrayList<FixedSubtask> getSubTaskList(){
		return subtasks;
	}
	
	/**
	 * idとnumberOfSubtask（サブタスク数）を返す
	 */
	public String toString(){
		StringBuffer string = new StringBuffer();
		string.append("id = " + id + " / subtask_num = " + numberOfSubtask + " / deadline = " + deadlineInTask
				+ " / mark = " + mark +  " / removedMarkNum(EstimationFailure TeamFormationFailure) = "
				+ (removedMarkNumByEstimationFailure + removedMarkNumByTeamFormationFailure)
				+ "(" + removedMarkNumByEstimationFailure + " " + removedMarkNumByTeamFormationFailure + ")"
				+ " / taskRequireSum = " + taskRequireSum);
		string.append(" / subtaskList(require) = ");
		for(FixedSubtask subtask : subtasks){
			for(int i = 0; i< FixedConstant.RESOURCE_NUM; i++){
				string.append(subtask.getRequire()[i] + " ");
			}
			string.append("  ");
		}
		return string.toString();
	}
	
	public void clear() {
		clearAgentInfo();
		subtasksByMembers.clear();
	}
	
	private void clearAgentInfo() {
		for(FixedSubtask subtask : subtasks){
			subtask.getAgentInfo().clear();
		}
	}
	
}
