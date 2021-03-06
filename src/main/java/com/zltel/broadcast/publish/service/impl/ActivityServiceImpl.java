package com.zltel.broadcast.publish.service.impl;

import com.github.pagehelper.PageInfo;
import com.zltel.broadcast.common.dao.SimpleDao;
import com.zltel.broadcast.publish.Constant;
import com.zltel.broadcast.publish.bean.Silhouette;
import com.zltel.broadcast.publish.dao.ActivityDao;
import com.zltel.broadcast.publish.dao.SilhouetteMapper;
import com.zltel.broadcast.publish.service.ActivityService;
import com.zltel.broadcast.um.bean.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * ActivityServiceImpl class
 *
 * @author Touss
 * @date 2018/5/25
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);
    @Autowired
    private SimpleDao simpleDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private SilhouetteMapper silhouetteMapper;

    @Override
    public Map<String, Object> getActivityAddition(int contentId) {
        return activityDao.getActivityAdditionByContentId(contentId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void completeActivityAddition(Map<String, Object> user, Map<String, Object> addition) {
        Date updateDate = new Date();
        Map<String, Object> data = activityDao.getActivityAdditionById((Integer) addition.get("id"));
        data.put("actual_participant_number", addition.get("actualParticipantNumber"));
        data.put("summary", addition.get("summary"));
        if (activityDao.updateActivityAddition(data) <= 0) {
            log.warn("更新数据不存在...");
            throw new RuntimeException("系统繁忙,请稍后再试...");
        }
        List<Map<String, Object>> materials = (List<Map<String, Object>>) addition.get("material");
        Map<String, Object> material = null;
        for (Map<String, Object> m : materials) {
            material = new HashMap<String, Object>();
            material.put("type", m.get("type"));
            material.put("content", m.get("content"));
            material.put("url", m.get("url"));
            material.put("description", m.get("name"));
            material.put("user_id", user.get("id"));
            material.put("org_id", user.get("orgId"));
            material.put("upload_reason", Constant.MATERIAL_UPLOAD_REASON_FEEDBACK);
            material.put("relate_content_id", addition.get("contentId"));
            material.put("add_date", updateDate);
            material.put("update_date", updateDate);
            simpleDao.add("material", material);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void participate(int contentId, Map<String, Object> user) {
        Map<String, Object> addition = activityDao.getActivityAdditionByContentId(contentId);
        if (addition == null) {
            throw new RuntimeException("活动不存在...");
        }
        Date activityDate = (Date) addition.get("activity_date");
        if (activityDate.getTime() - 24 * 60 * 60 * 1000 < System.currentTimeMillis()) {
            throw new RuntimeException("活动已截至...");
        }
        int ordainParticipantNumber = (int) addition.get("ordain_participant_number");
        int applicantNumber = (int) addition.get("applicant_number");
        if (applicantNumber >= ordainParticipantNumber) {
            // 报名人数已满
            throw new RuntimeException("报名人数已满...");
        }
        // 是否已报名
        Map<String, Object> participant = activityDao.getParticipantByContentId(contentId, (Integer) user.get("id"));
        if (participant != null) {
            // 已报名
            throw new RuntimeException("已报名参加...");
        }
        participant = new HashMap<String, Object>();
        participant.put("user_id", user.get("id"));
        participant.put("content_id", contentId);
        Date addDate = new Date();
        participant.put("add_date", addDate);
        participant.put("update_date", addDate);
        activityDao.addParticipant(participant);
        // 计数
        addition.put("applicant_number", applicantNumber + 1);
        if (activityDao.updateActivityAddition(addition) <= 0) {
            log.warn("更新数据不存在...");
            throw new RuntimeException("系统繁忙,请稍后再试...");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void updateParticipantSequel(Map<String, Object> sequel) {
        Map<String, Object> participant = activityDao.getParticipantById((Integer) sequel.get("id"));
        participant.put("is_participated", sequel.get("isParticipated"));
        participant.put("participate_length", sequel.get("participateLength"));
        if (activityDao.updateParticipant(participant) <= 0) {
            log.warn("更新数据不存在...");
            throw new RuntimeException("系统繁忙,请稍后再试...");
        }
        // TODO: 2018/05/28 根据参加情况添加积分
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void updateParticipantSequel(List<Map<String, Object>> sequels) {
        for (Map<String, Object> sequel : sequels) {
            updateParticipantSequel(sequel);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public void addIntegral(Map<String, Object> user, int moving, String sourceType, String reason) {
        Map<String, Object> integralMoving = new HashMap<String, Object>();
        integralMoving.put("user_id", user.get("id"));
        integralMoving.put("source_type", sourceType);
        integralMoving.put("moving", moving);
        integralMoving.put("reason", reason);
        Date addDate = new Date();
        integralMoving.put("add_date", addDate);
        integralMoving.put("update_date", addDate);
        activityDao.addIntegralMoving(integralMoving);
    }

    @Override
    public List<Map<String, Object>> queryParticipant(int contentId) {
        return activityDao.queryParticipant(contentId);
    }

    @Override
    public List<Map<String, Object>> queryFinishedActivity(int pageNum, int pageSize) {
        return activityDao.queryFinishedActivity(pageNum, pageSize);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int addSilhouette(Silhouette silhouette) {
        silhouette.setAddDate(new Date());
        silhouette.setUpdateDate(new Date());
        return silhouetteMapper.insertSelective(silhouette);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int deleteSilhouette(int silhouetteId) {
        return silhouetteMapper.deleteByPrimaryKey(silhouetteId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int updateSilhouette(Silhouette silhouette) {
        return silhouetteMapper.updateByPrimaryKeySelective(silhouette);
    }

    @Override
    public Silhouette getSilhouette(int silhouetteId) {
        Silhouette silhouette = silhouetteMapper.selectByPrimaryKey(silhouetteId);
        List<Integer> ids = new ArrayList<>();
        Arrays.stream(silhouette.getMaterial().split(",")).forEach(s -> {
            ids.add(Integer.parseInt(s));
        });
        silhouette.setMaterials(silhouetteMapper.queryMaterials(ids));
        return silhouette;
    }

    @Override
    public PageInfo<Silhouette> querySilhouette(int pageNum, int pageSize) {
        return new PageInfo<Silhouette>(silhouetteMapper.query(pageNum, pageSize));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int addSilhouette(Map<String, Object> silhouette) {
        Date addDate = new Date();
        silhouette.put("add_date", addDate);
        silhouette.put("update_date", addDate);
        return silhouetteMapper.mapInsert(silhouette);
    }

    @Override
    public Map<String, Object> mapGetSilhouette(int id) {
        return silhouetteMapper.mapGet(id);
    }

    @Override
    public PageInfo<Map<String, Object>> queryGallery(int pageNum, int pageSize) {
        return new PageInfo<Map<String, Object>>(silhouetteMapper.queryGallery(pageNum, pageSize));
    }
    @Override
    public PageInfo<Map<String, Object>> queryXjdx(int pageNum, int pageSize) {
        return new PageInfo<Map<String, Object>>(silhouetteMapper.queryXjdx(pageNum, pageSize));
    }
    @Override
    public PageInfo<Map<String, Object>> queryFlashing(int pageNum, int pageSize) {
        return new PageInfo<Map<String, Object>>(silhouetteMapper.queryFlashing(pageNum, pageSize));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int delete(int id) {
        return silhouetteMapper.deleteByPrimaryKey(id);
    }
}
