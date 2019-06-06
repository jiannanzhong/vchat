package database;

import bean.ChatBean;
import core.MyUUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Chat {
    private static final HashMap<String, ChatBean> chatDataList = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> userLastGetChats = new HashMap<>();
    private static final HashMap<String, String> getMsgSignature = new HashMap<>();

    public static String renewSign(String id1, String id2) {
        return modMsgSignature(id1 + "|" + id2, 1);
    }

    public static String getSign(String id1, String id2) {
        return modMsgSignature(id1 + "|" + id2, 2);
    }

    public static boolean checkSign(String id1, String id2, String uuid) {
        return uuid.equals(modMsgSignature(id1 + "|" + id2, 2));
    }

    private static synchronized String modMsgSignature(String id1_id2, int operationType) {
        // 1. 修改uuid并返回
        // 2. 不修改，直接返回uuid
        String uuid = null;
        switch (operationType) {
            case 1:
                uuid = MyUUID.createUUID();
                getMsgSignature.put(id1_id2, uuid);
                break;
            case 2:
                uuid = getMsgSignature.get(id1_id2);
                break;
            default:
        }
        return uuid;
    }

    public static void addChat(ChatBean ctb) {
        if (ctb != null) {
            modList(null, null, ctb, null, 1);
        }
    }

    public static ArrayList<ChatBean> getChatByUid(String id1, String id2) {
        return modList(id1, id2, null, null, 2);
    }

    static void removeAllChatsByUid(String uid) {
        Chat.modList(null, uid, null, null, 3);
    }

    private static void setUserLastChatsByUid(String uid, ArrayList<String> lastChats) {
        //System.out.println("set " + lastChats.toString());
        modChatUUIDList(uid, lastChats, 1);
    }

    public static void removeUserLastChatsByUid(String uid) {
        ArrayList<String> chatUUIDList = modChatUUIDList(uid, null, 2);
        modList(null, null, null, chatUUIDList, 4);
        //System.out.println("remove " + chatUUIDList.toString());
        modChatUUIDList(uid, null, 1);
    }

    public static void resetUserLastChatsByUid(String uid) {
        modChatUUIDList(uid, null, 1);
    }

    private static synchronized ArrayList<String> modChatUUIDList(String uid, ArrayList<String> lastChats,
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

    static synchronized ArrayList<ChatBean> modList(String id1, String id2, ChatBean ctb,
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
                ArrayList<String> list = new ArrayList<>();
                for (Entry<String, ChatBean> entry : chatDataList.entrySet()) {
                    temp = entry.getValue();
                    if (temp.getId1().equals(id1) && temp.getId2().equals(id2)) {
                        chatList.add(0, temp);
                        list.add(entry.getKey());
                    }
                }
                chatList.sort((o1, o2) -> (int) (o1.getTime() - o2.getTime()));
                setUserLastChatsByUid(id2, list);
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
