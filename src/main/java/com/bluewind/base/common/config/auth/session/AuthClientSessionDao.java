package com.bluewind.base.common.config.auth.session;

import com.bluewind.base.common.config.auth.constant.AuthConstant;
import com.bluewind.base.common.config.auth.util.SerializableUtil;
import com.bluewind.base.common.util.redis.RedisUtils;
import com.bluewind.base.common.util.spring.SpringContextUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liuxingyu01
 * @date 2022-08-26 12:43
 * @description 基于redis的sessionDao，缓存共享session
 **/
public class AuthClientSessionDao extends CachingSessionDAO {
    private static final Logger logger = LoggerFactory.getLogger(AuthClientSessionDao.class);

    private static Map<String, Session> sessionCacheMap = new HashMap<>();


    private static RedisUtils redisUtils;

    private static RedisUtils getRedisUtils() {
        if (redisUtils == null) {
            Object bean = SpringContextUtil.getBean("redisUtils");
            if (bean == null) {
                logger.error("redisUtils bean is null!");
            }
            redisUtils = (RedisUtils) bean;
        }
        return redisUtils;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        getRedisUtils().set(AuthConstant.LAMBO_SSO_SHIRO_SESSION_ID + ":" + sessionId, SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
        if (logger.isInfoEnabled()) {
            logger.info("doCreate >>>>> sessionId={}", sessionId);
        }

        return sessionId;
    }

    @Override
    public Session doReadSession(Serializable sessionId) {

        Session session = sessionCacheMap.get(sessionId.toString());
        if (session == null) {
            String sessionStr = getRedisUtils().getStr(AuthConstant.LAMBO_SSO_SHIRO_SESSION_ID + ":" + sessionId);
            if (logger.isInfoEnabled()) {
                logger.info("doReadSession >>>>> sessionId={}", sessionId);
            }
            session = SerializableUtil.deserialize(sessionStr);
            sessionCacheMap.put(sessionId.toString(), session);
        }
        return session;
    }

    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要再更新了
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        String sessionId = session.getId().toString();
        if (logger.isInfoEnabled()) {
            logger.info("doUpdateSession >>>>> sessionId={}", sessionId);
        }
        //清除缓存
        if (sessionCacheMap.get(sessionId) != null) {
            sessionCacheMap.remove(sessionId);
        }
        String code = getRedisUtils().getStr(AuthConstant.LAMBO_SSO_CODE + ":" + sessionId);
        int timeout = (int) session.getTimeout() / 1000;
        //只要这个属性还活着，其他的丢了就丢了，再创建就是了
        getRedisUtils().expire(AuthConstant.LAMBO_SSO_CODE_USERNAME + ":" + code, timeout);
    }

    @Override
    protected void doDelete(Session session) {
        String sessionId = session.getId().toString();
        //清除缓存
        if (sessionCacheMap.get(sessionId) != null) {
            sessionCacheMap.remove(sessionId);
        }
        // 当前会话code
        String code = getRedisUtils().getStr(AuthConstant.LAMBO_SSO_CODE + ":" + sessionId);
        // 清除code校验值
        getRedisUtils().del(AuthConstant.LAMBO_SSO_CODE_USERNAME + ":" + code);

        // 清除所有局部会话(这一块的代码暂时没看懂，不知道干啥的)
        Set<Object> sessionIds = getRedisUtils().sGet(AuthConstant.LAMBO_SSO_SESSION_IDS + ":" + code);
        if (CollectionUtils.isNotEmpty(sessionIds)) {
            for (Object sId : sessionIds) {
                getRedisUtils().del(AuthConstant.LAMBO_SSO_CODE + ":" + (String) sId);
            }
        }

        getRedisUtils().del(AuthConstant.LAMBO_SSO_SESSION_IDS + ":" + code);
        if (logger.isInfoEnabled()) {
            logger.info("doUpdate >>>>> sessionId={}", sessionId);
        }
    }


    /**
     * 强制退出
     *
     * @param ids
     * @return
     */
    public int forceout(String ids) {
        String[] sessionIds = ids.split(",");
        for (String sessionId : sessionIds) {
            // 会话增加强制退出属性标识，当此会话访问系统时，判断有该标识，则退出登录
            String session = getRedisUtils().getStr(AuthConstant.LAMBO_SSO_SHIRO_SESSION_ID + ":" + sessionId);
            AuthClientSession authClientSession = (AuthClientSession) SerializableUtil.deserialize(session);
            authClientSession.setStatus(AuthClientSession.OnlineStatus.force_logout);
            authClientSession.setAttribute("FORCE_LOGOUT", "FORCE_LOGOUT");
            getRedisUtils().set(AuthConstant.LAMBO_SSO_SHIRO_SESSION_ID + ":" + sessionId, SerializableUtil.serialize(authClientSession), (int) authClientSession.getTimeout() / 1000);
        }
        return sessionIds.length;
    }

    /**
     * 更改在线状态
     *
     * @param sessionId
     * @param onlineStatus
     */
    public void updateStatus(Serializable sessionId, AuthClientSession.OnlineStatus onlineStatus) {
        AuthClientSession session = (AuthClientSession) doReadSession(sessionId);
        if (null == session) {
            return;
        }
        session.setStatus(onlineStatus);
        getRedisUtils().set(AuthConstant.LAMBO_SSO_SHIRO_SESSION_ID + ":" + session.getId(), SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
    }
}
