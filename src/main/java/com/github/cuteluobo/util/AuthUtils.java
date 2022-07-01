package com.github.cuteluobo.util;

import com.github.cuteluobo.CuteExtra;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.permission.PermitteeId;

/**
 * 验证工具类
 *
 * @author CuteLuoBo
 * @date 2022/7/1 11:59
 */
public class AuthUtils {
    /**
     * 是否为管理员
     * @param permitteeId 权限人ID
     * @return 校验结果
     */
    public static boolean isAdmin(PermitteeId permitteeId) {
        return PermissionService.testPermission(CuteExtra.INSTANCE.getAdminPermission(), permitteeId);
    }
}
