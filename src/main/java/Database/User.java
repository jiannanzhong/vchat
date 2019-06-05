package Database;

import java.util.HashMap;

import bean.UserBean;
import core.MyUUID;
import core.TimeUtil;

public class User {
	private static final HashMap<String, UserBean> idActiveList = new HashMap<>();

	public static String login(String uid) {
		modList(uid, 1);
		return idActiveList.get(uid).getUuid();
	}

	public static void delChatListByUid(String uid) {
		modList(uid, 2);
	}

	public static boolean checkUserActive(String uid) {
		return idActiveList.get(uid) != null;
	}

	public static UserBean getUserByUid(String uid) {
		return idActiveList.get(uid);
	}

	public static synchronized void modList(String uid, int operationType) {
		// 1. 新增uid，并且清空原有的聊天记录
		// 2. 删除uid及其聊天记录

		switch (operationType) {
		case 1:
			UserBean user = idActiveList.get(uid);
			if (user != null) {
				Chat.removeAllChatsByUid(uid);
			}

			user = new UserBean();
			user.setLastLogin(TimeUtil.getTimeInSeconds());
			user.setUuid(MyUUID.createUUID());
			user.setUid(uid);
			idActiveList.put(uid, user);
			break;
		case 2:
			Chat.modList(null, uid, null, null, 3);
			break;
		default:
			break;
		}
	}
}
