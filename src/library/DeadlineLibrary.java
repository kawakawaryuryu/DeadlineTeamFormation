package library;

import constant.Constant;

public class DeadlineLibrary {


	/**
	 * タスクコピー前に削減すべきデッドラインを返す
	 * @param delay
	 * @return
	 */
	public static int getReducedDeadlineAtInitialTurn(int delay) {
		if(delay == 0) {
			return Constant.WAIT_TURN + Constant.DEADLINE_MIN_2;
		}
		else {
			return Constant.WAIT_TURN + (delay * 2) * Constant.DEADLINE_MIN_2;
		}
	}

	/**
	 * タスクコピー後、リーダ・メンバ前に削減すべきデッドラインを返す
	 * @param delay
	 * @return
	 */
	public static int getReducedDeadlineAtFirstTurn(int delay) {
		if(delay == 0) {
			return Constant.DEADLINE_MIN_2;
		}
		else {
			return (Constant.MESSAGE_DELAY * 2) * Constant.DEADLINE_MIN_2;
		}
	}

	/**
	 * リーダ・メンバ時に削減すべきデッドラインを返す
	 * @param delay
	 * @return
	 */
	public static int getReducedDeadlineAtSecondTurn(int delay) {
		if(delay == 0) {
			return Constant.DEADLINE_MIN_1;
		}
		else {
			return (Constant.MESSAGE_DELAY * 2) * Constant.DEADLINE_MIN_1;
		}
	}
}
