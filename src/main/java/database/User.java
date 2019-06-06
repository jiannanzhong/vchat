package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.UserBean;
import core.AppSettings;
import core.MyUUID;
import core.TimeUtil;

public class User {
    private static final HashMap<String, UserBean> idActiveList = new HashMap<>();

    static {
        new Thread(() -> {
            while (AppSettings.APP_RUNNING) {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    //
                }
                deactivateOfflineUser();
            }
        }).start();
    }

    public static String login(String uid) {
        modList(uid, 1);
        return idActiveList.get(uid).getUuid();
    }

    private static void deactivateOfflineUser() {
        modList(null, 3);
    }

    public static void delUserAndChatListByUid(String uid) {
        modList(uid, 2);
    }

    public static void renewUserLastLogin(String uid) {
        modList(uid, 4);
    }

    public static boolean checkUserActive(String uid) {
        return idActiveList.get(uid) != null;
    }

    public static UserBean getUserByUid(String uid) {
        return idActiveList.get(uid);
    }

    public static boolean verifyUser(String uid, String uuid) {
        UserBean userInfo = User.getUserByUid(uid);
        return userInfo != null && uuid.equals(userInfo.getUuid());
    }

    private static synchronized void modList(String uid, int operationType) {
        // 1. 新增uid，并且清空原有的聊天记录
        // 2. 删除uid及其聊天记录
        // 3. 查找长时间未连接的用户并清除
        // 4. 更新用户最后连接时间

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
                idActiveList.remove(uid);
                Chat.modList(null, uid, null, null, 3);
                break;
            case 3:
                ArrayList<String> offlineIDList = new ArrayList<>();
                for (Map.Entry<String, UserBean> entry : idActiveList.entrySet()
                        ) {
                    if (entry.getValue().getLastLogin() < TimeUtil.getTimeInSeconds() - AppSettings.MAX_SPARE) {
                        offlineIDList.add(entry.getKey());
                    }
                }
                for (String offlineUid : offlineIDList
                        ) {
                    idActiveList.remove(offlineUid);
                    Chat.modList(null, offlineUid, null, null, 3);
                }
                break;
            case 4:
                UserBean userToMod = idActiveList.get(uid);
                if (userToMod != null) {
                    userToMod.setLastLogin(TimeUtil.getTimeInSeconds());
                }
                break;
            default:
                break;
        }
    }
}
