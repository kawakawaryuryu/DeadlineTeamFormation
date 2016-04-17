package role;

public enum FutureRole {
	LEADER_FUTURE {
		public String toString() {
			return "リーダ";
		}
	},
	MEMBER_FUTURE {
		public String toString() {
			return "メンバ";
		}
	},
	NO_ROLE_FUTURE {
		public String toString() {
			return "役割なし";
		}
	};
}
