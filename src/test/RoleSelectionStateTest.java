package test;

import static org.junit.Assert.*;

import org.junit.Test;

import role.FutureRole;
import state.RoleSelectionState;

public class RoleSelectionStateTest {

	@Test
	public void testDecideRole1() {
		double expectedLeaderReward = -1;
		double expectedMemberReward = 0;
		
		RoleSelectionState state = (RoleSelectionState)RoleSelectionState.getState();
//		assertEquals(state.decideRole(expectedLeaderReward, expectedMemberReward), FutureRole.LEADER_FUTURE);
	}
	
	@Test
	public void testDecideRole2() {
		double expectedLeaderReward = 0;
		double expectedMemberReward = 0;
		
		RoleSelectionState state = (RoleSelectionState)RoleSelectionState.getState();
//		assertEquals(state.decideRole(expectedLeaderReward, expectedMemberReward), FutureRole.NO_ROLE_FUTURE);
	}
	
	@Test
	public void testDecideRole3() {
		double expectedLeaderReward = 2;
		double expectedMemberReward = 1;
		
		RoleSelectionState state = (RoleSelectionState)RoleSelectionState.getState();
//		assertEquals(state.decideRole(expectedLeaderReward, expectedMemberReward), FutureRole.LEADER_FUTURE);
	}
	
	@Test
	public void testDecideRole4() {
		double expectedLeaderReward = 1;
		double expectedMemberReward = 2;
		
		RoleSelectionState state = (RoleSelectionState)RoleSelectionState.getState();
//		assertEquals(state.decideRole(expectedLeaderReward, expectedMemberReward), FutureRole.MEMBER_FUTURE);
	}

}
