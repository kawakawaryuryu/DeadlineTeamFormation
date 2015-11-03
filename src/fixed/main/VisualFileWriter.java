package fixed.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import fixed.agent.FixedAgent;
import fixed.constant.FixedConstant;
import fixed.role.Role;

public class VisualFileWriter {
	/**
	 * 一定ターンまでのエージェントの主な役割を返す
	 * @param agent
	 * @return
	 * @throws Exception 
	 */
	private static String getStringMainRole(FixedAgent agent) throws IOException{
		if(agent.getParameter().getElement(Role.LEADER).getRoleNum() > agent.getParameter().getElement(Role.MEMBER).getRoleNum() * 2){
			return "leader";
		}
		else if(agent.getParameter().getElement(Role.LEADER).getRoleNum() * 2 < agent.getParameter().getElement(Role.MEMBER).getRoleNum()){
			return "member";
		}
		else{
			return "neither";
		}
	}
	
	/**
	 * 相手とチーム編成をする際に担当する主な役割を返す
	 * @param me
	 * @param you
	 * @return
	 */
	private static String getStringMainRoleWithYou(FixedAgent me, FixedAgent you){
		if(me.getMeasure().getTeamFormationNumWithMember(you) > me.getMeasure().getTeamFormationNumWithLeader(you) * 2){
			return "asLeader";
		}
		else if(me.getMeasure().getTeamFormationNumWithMember(you) * 2 < me.getMeasure().getTeamFormationNumWithLeader(you)){
			return "asMember";
		}
		else{
			return "asNeither";
		}
	}
	
	/**
	 * 可視化のための書き込み
	 * ＜有向グラフ用＞
	 * 自分のID | 相手のID | 自分の能力 | 自分の能力の合計 | メンバとのチーム編成回数 | メンバとのチーム編成回数の割合
	 *  | メンバとのチーム編成回数の割合が閾値以上か | 主に担当した役割
	 *  ＜無向グラフ用＞
	 *  自分のID | 相手のID | 自分の能力 | 自分の能力の合計 | 相手とのチーム編成合計回数 | 相手とのチーム編成合計回数の割合
	 *   | 相手とのチーム編成合計回数の割合が閾値以上か | 主に担当した役割
	 * @param agents
	 * @param turn
	 * @param experimentNumber 実験回数
	 * @param successTeamingEdgeNum ターンごとのチーム編成成功回数
	 * @throws IOException
	 */
	public static void writeVisualizedData(ArrayList<FixedAgent> agents, int turn, int experimentNumber, int successTeamingEdgeNum) throws IOException{
		File directory = new File(FileWriteManager.path + "data/visualization/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/" + FileWriteManager.fileNumber + "/visual/");
		// ディレクトリが存在しない場合はディレクトリを作成 
		if(!directory.exists()){
			directory.mkdirs();
		}
		//無向グラフ
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(FileWriteManager.path + "data/visualization/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/" + FileWriteManager.fileNumber + "/visual/non_directed_" + turn + ".csv", FileWriteManager.isWrite), "Shift_JIS")));
		pw.print("my_id" + "," + "your_id");
		pw.print(",");
		pw.print("my_ability");
		pw.print(",");
		pw.print("my_ability_sum");
		pw.print(",");
		pw.print("main_role");
		pw.print(",");
		pw.print("leaderNum" + "," + "memberNum");
		pw.print(",");
		pw.print("team_formation_sum");
		pw.print(",");
		pw.print("team_formation_num_as_leader" + "," + "team_formation_num_as_member");
		pw.print(",");
		pw.print("main_role_with_you");
		pw.print(",");
		pw.print("team_formation_sum_percentage");
		pw.print(",");
		pw.print("team_formation_sum_percentage_over_border");
		pw.println();
		
		for(int i = 0; i < FixedConstant.AGENT_NUM; i++){
			FixedAgent me = agents.get(i);
			
			//無向グラフ用
			for(int j = i; j < FixedConstant.AGENT_NUM; j++){
				boolean isWrite = false;	//書き込むかどうか
				double threshold = 0.00;	//可視化の閾値
				FixedAgent you = agents.get(j);
				
				//相手とのチーム編成回数の割合
				double teamingRateWithYou = 
						(double)(me.getMeasure().getTeamFormationNumWithLeader(you)
								+ me.getMeasure().getTeamFormationNumWithMember(you))
						/ (double)successTeamingEdgeNum * 100;
				
				//チーム編成割合がどの閾値よりも下回る場合は書き込まない
				for(int k = 0; k < FixedConstant.TEAM_FORMATION_PERCENTAGE_BORDER_NUM; k++){
					if(teamingRateWithYou > FixedConstant.TEAM_FORMATION_PERCENTAGE_BORDER[k]){
						isWrite = true;
						threshold = FixedConstant.TEAM_FORMATION_PERCENTAGE_BORDER[k];
						break;
					}
				}
				if(!isWrite && i != j){
					continue;
				}
				
				//自分id、相手id
				pw.print(me.getId() + "," + you.getId());
				pw.print(",");
				
				//自分の能力、自分の能力の合計
				pw.print(me.getAbility(0) + " " + me.getAbility(1) + "," + me.getAbilitySum());
				pw.print(",");
				
				//主に担当した役割
				pw.print(getStringMainRole(me));
				pw.print(",");
				
				//リーダ回数、メンバ回数
				pw.print(me.getParameter().getElement(Role.LEADER).getRoleNum() + "," + me.getParameter().getElement(Role.MEMBER).getRoleNum());
				pw.print(",");
				
				if(j != i){
					//相手とのチーム編成合計回数
					pw.print(me.getMeasure().getTeamFormationNumWithLeader(you) + me.getMeasure().getTeamFormationNumWithMember(you));
					pw.print(",");
					
					//相手とのリーダとしての回数、相手とのメンバとしての回数
					pw.print(me.getMeasure().getTeamFormationNumWithMember(you) + "," + me.getMeasure().getTeamFormationNumWithLeader(you));
					pw.print(",");
					
					//相手とチーム編成をする際に担当する主な役割
					pw.print(getStringMainRoleWithYou(me, you));
					pw.print(",");

					//相手とのチーム編成合計回数の割合
					pw.print(teamingRateWithYou);
					pw.print(",");
					
					//チーム編成合計回数の割合が閾値以上か（パーセンテージ）
					pw.print("more_than_" + threshold);
				}
//				else{
//					pw.print("0" + "," + "0" + "," + "0" + "," + "0" + "," + "0");
//				}
				
				pw.println();
			}
		}

		pw.close();
	}
	
	/**
	 * 可視化の際の補足的データ
	 * @param agents
	 * @param turn
	 * @param experimentNumber
	 * @param successTeamingEdgeNum
	 * @throws IOException
	 */
	public static void writeVisualizedMoreData(ArrayList<FixedAgent> agents, int turn) throws IOException{
		File directory = new File(FileWriteManager.path + "data/visualization/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/" + FileWriteManager.fileNumber + "/data/");
		// ディレクトリが存在しない場合はディレクトリを作成 
		if(!directory.exists()){
			directory.mkdirs();
		}
		PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream
				(FileWriteManager.path + "data/visualization/" + FixedConstant.AGENT_NUM + "agents/" + FixedConstant.TURN_NUM + "t/" + FileWriteManager.fileNumber + "/data/teaming_" + turn + ".csv", FileWriteManager.isWrite), "Shift_JIS")));
		pw.println(turn + "ターン目");
		pw.print("エージェントID");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getId());
		}
		pw.println();
		pw.print("リソース");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getAbility(0) + " " + agent.getAbility(1));
		}
		pw.println();
		pw.print("リーダ回数");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getParameter().getElement(Role.LEADER).getRoleNum());
		}
		pw.println();
		pw.print("メンバ回数");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getParameter().getElement(Role.MEMBER).getRoleNum());
		}
		pw.println();
		pw.print("合計回数");
		for(FixedAgent agent : agents){
			pw.print(",");
			pw.print(agent.getParameter().getElement(Role.LEADER).getRoleNum() 
					+ agent.getParameter().getElement(Role.MEMBER).getRoleNum());
		}
		pw.println();
		//それぞれの役割担当エージェント数
		int leaders = 0;
		int members = 0;
		int neithers = 0;
		pw.print("主な役割");
		for(FixedAgent agent : agents){
			String role = getStringMainRole(agent);
			pw.print(",");
			pw.print(role);
			if(role.equals("leader")){
				leaders++;
			}
			else if(role.equals("member")){
				members++;
			}
			else{
				neithers++;
			}
		}
		pw.println();
		
		pw.println();
		pw.println("リーダ数" + "," + leaders);
		pw.println("メンバ数" + "," + members);
		pw.println("どちらでもない数" + "," + neithers); 
		
		pw.close();
	}
}