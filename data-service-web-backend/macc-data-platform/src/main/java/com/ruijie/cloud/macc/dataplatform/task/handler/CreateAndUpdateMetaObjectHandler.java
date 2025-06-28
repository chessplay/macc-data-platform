package com.ruijie.cloud.macc.dataplatform.task.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruijie.cloud.dc.core.domain.BaseEntity;
import com.ruijie.cloud.dc.core.exception.ServiceException;
import com.ruijie.cloud.dc.core.utils.StringUtils;
import com.ruijie.cloud.dc.privilege.core.LoginUserInfo;
import com.ruijie.cloud.dc.privilege.utils.PrivilegeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * MP注入处理器
 *
 * @author Lion Li
 * @date 2021/4/25
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

    private static final String ANONYMOUS_USER = "anonymous";

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                Date current = ObjectUtil.isNotNull(baseEntity.getCreateTime())
                        ? baseEntity.getCreateTime() : new Date();
                baseEntity.setCreateTime(current);
                baseEntity.setUpdateTime(current);
                String username = StringUtils.isNotBlank(baseEntity.getCreator())
                        ? baseEntity.getCreator() : getLoginUsername();
                // 当前已登录 且 创建人为空 则填充
                baseEntity.setCreator(username);
                // 当前已登录 且 更新人为空 则填充
                baseEntity.setLastUpdater(username);
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                Date current = new Date();
                // 更新时间填充(不管为不为空)
                baseEntity.setUpdateTime(current);
                String username = getLoginUsername();
                // 当前已登录 更新人填充(不管为不为空)
                if (StringUtils.isNotBlank(username)) {
                    baseEntity.setLastUpdater(username);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    /**
     * 获取登录用户名
     */
    /**
     * 从请求的header中获取登录用户名
     * 如果不存在则返回匿名用户
     */
    private String getLoginUsername() {
        try {
            LoginUserInfo loginUserInfo= PrivilegeUtils.getLoginUser();
            return loginUserInfo.getUserName();
        } catch (Exception e) {
            log.warn("自动注入警告 => 无法获取请求的header");
        }
        return ANONYMOUS_USER;
    }

}
