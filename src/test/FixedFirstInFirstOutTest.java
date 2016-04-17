package test;

import static org.junit.Assert.*;
import main.TeamFormationMain;

import org.junit.Test;

import strategy.taskselection.FixedFirstInFirstOutStrategy;
import task.Failure;
import task.FixedTask;
import team.FixedTeam;
import agent.FixedAgent;

public class FixedFirstInFirstOutTest {

	@Test
	public void testCanExecuteTaskInTeam1() {
		// チーム履歴がないときはtrue
		FixedAgent agent = new FixedAgent(0);
		FixedTask task = new FixedTask(0, 3, 5);
		
		FixedFirstInFirstOutStrategy strategy = new FixedFirstInFirstOutStrategy();
		assertEquals(strategy.canExecuteTaskInTeam(agent, task), true);
	}
	
	@Test
	public void testCanExecuteTaskInTeam2() {
		// チームの平均リソースでタスクがデッドラインまでに処理できる場合
		FixedAgent agent = new FixedAgent(0);
		FixedTeam team = new FixedTeam(agent);
		team.addMember(new FixedAgent(1));
		team.addMember(new FixedAgent(2));
		agent.getParameter().getPastTeam().addTeam(team);
		FixedTask task = new FixedTask(0, 3, 5);
		
		FixedFirstInFirstOutStrategy strategy = new FixedFirstInFirstOutStrategy();
		assertEquals(strategy.canExecuteTaskInTeam(agent, task), true);
	}
	
	@Test
	public void testCanExecuteTaskInTeam3() {
		// チームの平均リソースでタスクがデッドラインまでに処理できない場合
		FixedAgent agent = new FixedAgent(0);
		FixedTeam team = new FixedTeam(agent);
		team.addMember(new FixedAgent(1));
		team.addMember(new FixedAgent(2));
		agent.getParameter().getPastTeam().addTeam(team);
		FixedTask task = new FixedTask(0, 3, 3);
				
		FixedFirstInFirstOutStrategy strategy = new FixedFirstInFirstOutStrategy();
		assertEquals(strategy.canExecuteTaskInTeam(agent, task), false);
	}
	
	@Test
	public void testSelectTask1() {
		FixedTask task1 = new FixedTask(0, 3, 3);
		FixedTask task2 = new FixedTask(1, 3, 5);
		TeamFormationMain.getParameter().taskQueue.clear();
		TeamFormationMain.getParameter().taskQueue.add(task1);
		TeamFormationMain.getParameter().taskQueue.add(task2);
		
		FixedAgent agent = new FixedAgent(0);
		FixedFirstInFirstOutStrategy strategy = new FixedFirstInFirstOutStrategy();
		assertEquals(strategy.selectTask(agent), task1);
	}
	
	@Test
	public void testSelectTask2() {
		FixedTask task1 = new FixedTask(0, 3, 3);
		FixedTask task2 = new FixedTask(1, 3, 5);
		TeamFormationMain.getParameter().taskQueue.clear();
		TeamFormationMain.getParameter().taskQueue.add(task1);
		TeamFormationMain.getParameter().taskQueue.add(task2);
		
		FixedAgent agent = new FixedAgent(0);
		FixedTeam team = new FixedTeam(agent);
		team.addMember(new FixedAgent(1));
		team.addMember(new FixedAgent(2));
		agent.getParameter().getPastTeam().addTeam(team);
		FixedFirstInFirstOutStrategy strategy = new FixedFirstInFirstOutStrategy();
		assertEquals("チーム平均リソース = " + agent.getParameter().getPastTeam().getAverageAbilitiesPerTeam(), strategy.selectTask(agent), task2);
	}
	
	@Test
	public void testSelectTask3() {
		FixedTask task1 = new FixedTask(0, 3, 5);
		task1.markingTask(true, Failure.MARK_TURE);
		FixedTask task2 = new FixedTask(1, 3, 5);
		TeamFormationMain.getParameter().taskQueue.clear();
		TeamFormationMain.getParameter().taskQueue.add(task1);
		TeamFormationMain.getParameter().taskQueue.add(task2);
		
		FixedAgent agent = new FixedAgent(0);
		FixedTeam team = new FixedTeam(agent);
		team.addMember(new FixedAgent(1));
		team.addMember(new FixedAgent(2));
		agent.getParameter().getPastTeam().addTeam(team);
		FixedFirstInFirstOutStrategy strategy = new FixedFirstInFirstOutStrategy();
		assertEquals(strategy.selectTask(agent), task2);
	}

}
