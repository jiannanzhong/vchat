package Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import bean.ChatBean;
import core.MyUUID;

public class Chat {
	private static final HashMap<String, ChatBean> chatDataList = new HashMap<>();
	private static final HashMap<String, ArrayList<String>> userLastGetChats = new HashMap<>();

	public static void addChat(ChatBean ctb) {
		if (ctb != null) {
			modList(null, null, ctb, null, 1);
		}
	}

	public static ArrayList<ChatBean> getChatByUid(String id1, String id2) {
		return modList(id1, id2, null, null, 2);
	}

	public static void removeAllChatsByUid(String uid) {
		Chat.modList(null, uid, null, null, 3);
	}

	public static void setUserLastChatsByUid(String uid, ArrayList<String> lastChats) {
		modChatUUIDList(uid, lastChats, 1);
	}

	public static void removeUserLastChatsByUid(String uid) {
		ArrayList<String> chatUUIDList = modChatUUIDList(uid, null, 2);
		modList(null, null, null, chatUUIDList, 4);
		modChatUUIDList(uid, null, 1);
	}

	public static void resetUserLastChatsByUid(String uid) {
		modChatUUIDList(uid, null, 1);
	}

	public static synchronized ArrayList<String> modChatUUIDList(String uid, ArrayList<String> lastChats,
			int operationType) {
		// 1. 设置lastChats
		// 2. 获取lastChats
		ArrayList<String> lastChatsData = null;
		switch (operationType) {
		case 1:
			if (lastChats == null) {
				lastChats = new ArrayList<>();
			}
			userLastGetChats.put(uid, lastChats);
			break;
		case 2:
			lastChatsData = userLastGetChats.get(uid);
			break;
		default:
			break;
		}
		if (lastChatsData == null) {
			lastChatsData = new ArrayList<>();
		}
		return lastChatsData;
	}

	public static synchronized ArrayList<ChatBean> modList(String id1, String id2, ChatBean ctb,
			ArrayList<String> chatUUIDList, int operationType) {
		// 1. 增加聊天，uid为空，ctb为有效
		// 2. 拉取某个uid要接收的信息，uid有效，ctb为空
		// 3. 删除某个uid的所有相关信息，uid有效，ctb为空
		// 4. 根据ArrayList<UUID> 删除聊天
		ArrayList<ChatBean> chatList = new ArrayList<>();
		ChatBean temp;
		switch (operationType) {
		case 1:
			if (ctb != null) {
				chatDataList.put(MyUUID.createUUID(), ctb);
			}
			break;
		case 2:
			for (Entry<String, ChatBean> entry : chatDataList.entrySet()) {
				ArrayList<String> list = new ArrayList<>();
				temp = entry.getValue();
				if (temp.getId1().equals(id1) && temp.getId2().equals(id2)) {
					chatList.add(0, temp);
					list.add(entry.getKey());
				}
				setUserLastChatsByUid(id2, list);
			}
			break;
		case 3:
			ArrayList<String> tempList = new ArrayList<>();
			for (Entry<String, ChatBean> entry : chatDataList.entrySet()) {
				temp = entry.getValue();
				if (temp.getId1().equals(id2) || temp.getId2().equals(id2)) {
					tempList.add(entry.getKey());
				}
			}
			for (String string : tempList) {
				chatDataList.remove(string);
			}
			break;
		case 4:
			for (String chatUUID : chatUUIDList) {
				chatDataList.remove(chatUUID);
			}
			break;
		default:
		}
		return chatList;
	}
}
