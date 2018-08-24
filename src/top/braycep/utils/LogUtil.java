package top.braycep.utils;

import java.util.logging.Logger;

/**
 * 辅助记录日志
 *
 * @author Braycep
 */
public class LogUtil {

    /**
     * 记录日志
     *
     * @param text 日志信息
     */
    public static void Log(String text) {
        Logger.getGlobal().info(text);
    }
}
