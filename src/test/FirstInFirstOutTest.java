package test;

import static org.junit.Assert.*;
import main.teamformation.TeamFormationInstances;
import main.teamformation.TeamFormationMain;

import org.junit.Test;

import strategy.taskselection.FirstInFirstOutStrategy;
import task.Failure;
import task.Task;
import team.Team;
import agent.Agent;

public class FirstInFirstOutTest {

	@Test
	public void testCanExecuteTaskInTeam1() {
		// チーム履歴がないときはtrue
		Agent agent = new Agent(0);
		Task task = new Task(0, 3, 5);
		
		FirstInFirstOutStrategy strategy = new FirstInFirstOutStrategy();
		assertEquals(strategy.canExecuteTaskInTeam(agent, task), true);
	}
	
	@Test
	public void testCanExecuteTaskInTeam2() {
		// チームの平均リソースでタスクがデッドラインまでに処理できる場合
		Agent agent = new Agent(0);
		Team team = new Team(agent);
		team.addMember(new Agent(1));
		team.addMember(new Agent(2));
		agent.getParameter().getPastTeam().addTeam(team);
		Task task = new Task(0, 3, 5);
		
		FirstInFirstOutStrategy strategy = new FirstInFirstOutStrategy();
		assertEquals(strategy.canExecuteTaskInTeam(agent, task), true);
	}
	
	@Test
	public void testCanExecuteTaskInTeam3() {
		// チームの平均リソースでタスクがデッドラインまでに処理できない場合
		Agent agent = new Agent(0);
		Team team = new Team(agent);
		team.addMember(new Agent(1));
		team.addMember(new Agent(2));
		agent.getParameter().getPastTeam().addTeam(team);
		Task task = new Task(0, 3, 3);
				
		FirstInFirstOutStrategy strategy = new FirstInFirstOutStrategy();
		assertEquals(strategy.canExecuteTaskInTeam(agent, task), false);
	}
	
	@Test
	public void testSelectTask1() {
		Task task1 = new Task(0, 3, 3);
		Task task2 = new Task(1, 3, 5);
		TeamFormationInstances.getInstance().getParameter().taskQueue.clear();
		TeamFormationInstances.getInstance().getParameter().taskQueue.add(task1);
		TeamFormationInstances.getInstance().getParameter().taskQueue.add(task2);
		
		Agent agent = new Agent(0);
		FirstInFirstOutStrategy strategy = new FirstInFirstOutStrategy();
		assertEquals(strategy.selectTask(agent), task1);
	}
	
	@Test
	public void testSelectTask2() {
		Task task1 = new Task(0, 3, 3);
		Task task2 = new Task(1, 3, 5);
		TeamFormationInstances.getInstance().getParameter().taskQueue.clear();
		TeamFormationInstances.getInstance().getParameter().taskQueue.add(task1);
		TeamFormationInstances.getInstance().getParameter().taskQueue.add(task2);
		
		Agent agent = new Agent(0);
		Team team = new Team(agent);
		team.addMember(new Agent(1));
		team.addMember(new Agent(2));
		agent.getParameter().getPastTeam().addTeam(team);
		FirstInFirstOutStrategy strategy = new FirstInFirstOutStrategy();
		assertEquals("チーム平均リソース = " + agent.getParameter().getPastTeam().getAverageAbilitiesPerTeam(), strategy.selectTask(agent), task2);
	}
	
	@Test
	public void testSelectTask3() {
		Task task1 = new Task(0, 3, 5);
		task1.markingTask(true, Failure.MARK_TURE);
		Task task2 = new Task(1, 3, 5);
		TeamFormationInstances.getInstance().getParameter().taskQueue.clear();
		TeamFormationInstances.getInstance().getParameter().taskQueue.add(task1);
		TeamFormationInstances.getInstance().getParameter().taskQueue.add(task2);
		
		Agent agent = new Agent(0);
		Team team = new Team(agent);
		team.addMember(new Agent(1));
		team.addMember(new Agent(2));
		agent.getParameter().getPastTeam().addTeam(team);
		FirstInFirstOutStrategy strategy = new FirstInFirstOutStrategy();
		assertEquals(strategy.selectTask(agent), task2);
	}

}
