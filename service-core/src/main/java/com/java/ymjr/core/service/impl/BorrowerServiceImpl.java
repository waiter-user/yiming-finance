package com.java.ymjr.core.service.impl;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.ymjr.core.enums.BorrowerStatusEnum;
import com.java.ymjr.core.enums.DictEnum;
import com.java.ymjr.core.enums.IntegralEnum;
import com.java.ymjr.core.mapper.BorrowerAttachMapper;
import com.java.ymjr.core.mapper.UserInfoMapper;
import com.java.ymjr.core.mapper.UserIntegralMapper;
import com.java.ymjr.core.pojo.Borrower;
import com.java.ymjr.core.mapper.BorrowerMapper;
import com.java.ymjr.core.pojo.BorrowerAttach;
import com.java.ymjr.core.pojo.UserInfo;
import com.java.ymjr.core.pojo.UserIntegral;
import com.java.ymjr.core.service.BorrowerAttachService;
import com.java.ymjr.core.service.BorrowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.ymjr.core.service.DictService;
import com.java.ymjr.core.vo.BorrowerApprovalVO;
import com.java.ymjr.core.vo.BorrowerAttachVO;
import com.java.ymjr.core.vo.BorrowerDetailVO;
import com.java.ymjr.core.vo.BorrowerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {
    @Resource
    private BorrowerAttachMapper borrowerAttachMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private DictService dictService;
    @Autowired
    private BorrowerAttachService borrowerAttachService;

    @Resource
    private UserIntegralMapper userIntegralMapper;

    @Override
    public void saveBorrowerVoByUserId(BorrowerVO borrowerVO, Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //保存借款人信息
        Borrower borrower = new Borrower();
        BeanUtils.copyProperties(borrowerVO, borrower);
        borrower.setUserId(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());//认证中
        baseMapper.insert(borrower);

        //保存附件
        List<BorrowerAttach> borrowerAttachList = borrowerVO.getBorrowerAttachList();
        borrowerAttachList.forEach(borrowerAttach -> {
            borrowerAttach.setBorrowerId(borrower.getId());
            borrowerAttachMapper.insert(borrowerAttach);
        });

        //更新会员状态，更新为认证中
        userInfo.setBorrowAuthStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.select("status").eq("user_id", userId);
        List<Object> objects = baseMapper.selectObjs(borrowerQueryWrapper);

        if (objects.size() == 0) {
            //借款人尚未提交信息
            return BorrowerStatusEnum.NO_AUTH.getStatus();
        }
        Integer status = (Integer) objects.get(0);
        return status;
    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            //直接查
            return baseMapper.selectPage(pageParam, null);
        }
        QueryWrapper<Borrower> queryWrapper = new QueryWrapper();
        queryWrapper.like("name", keyword)
                .or().like("mobile", keyword)
                .or().like("id_card", keyword).orderByAsc("id");
        return baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        Borrower borrower = baseMapper.selectById(id);
        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        //拷贝属性
        BeanUtils.copyProperties(borrower, borrowerDetailVO);
        //婚否
        borrowerDetailVO.setMarry(borrower.getMarry() ? "是" : "否");
        //性别
        borrowerDetailVO.setSex(borrower.getSex() == 1 ? "男" : "女");
        //审批状态
        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVO.setStatus(status);
        //根据borrower的education,industry,returnSource,contactsRelation获取对应的字典name
        String education = dictService.getNameByParentDictCodeAndValue(DictEnum.EDUCATION.getDictCode(), borrower.getEducation());
        String industry = dictService.getNameByParentDictCodeAndValue(DictEnum.INDUSTRY.getDictCode(), borrower.getIndustry());
        String returnSource = dictService.getNameByParentDictCodeAndValue(DictEnum.RETURN_SOURCE.getDictCode(), borrower.getReturnSource());
        String contactsRelation = dictService.getNameByParentDictCodeAndValue(DictEnum.RELATION.getDictCode(), borrower.getContactsRelation());
        borrowerDetailVO.setEducation(education);
        borrowerDetailVO.setIndustry(industry);
        borrowerDetailVO.setReturnSource(returnSource);
        borrowerDetailVO.setContactsRelation(contactsRelation);
        //获取附件集合
        List<BorrowerAttach> borrowerAttachList = borrowerAttachService.getBorrowerAttachList(id);
        List<BorrowerAttachVO> borrowerAttachVOList = new ArrayList<>();
        borrowerAttachList.forEach(attach -> {
            BorrowerAttachVO attachVO = new BorrowerAttachVO();
            attachVO.setImageType(attach.getImageType());
            attachVO.setImageUrl(attach.getImageUrl());
            borrowerAttachVOList.add(attachVO);
        });
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachVOList);
        return borrowerDetailVO;
    }

    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {
        //从参数对象获取当前借款人id号
        Long borrowerId = borrowerApprovalVO.getBorrowerId();
        //根据id获取借款人信息

        Borrower borrower = baseMapper.selectById(borrowerId);

        Long userId = borrower.getUserId();
        Integer status = borrowerApprovalVO.getStatus();
        //修改借款人的认证状态(2或-1),borrower表
        borrower.setStatus(status);
        baseMapper.updateById(borrower);

        //获取用户对象
        UserInfo userInfo = userInfoMapper.selectById(userId);
        //如果审核通过，还要操作积分
        //1.向user_integral表中插入积分明细
        //2. 计算总积分，更新user_info表的integral字段

        if (status==2) {
            //总积分
            Integer totalScore=0;
            //基本信息积分
            UserIntegral  integral=new UserIntegral();
            integral.setUserId(userId);
            integral.setIntegral(borrowerApprovalVO.getInfoIntegral());
            integral.setContent("借款人基本信息");
            userIntegralMapper.insert(integral);
            //计算总积分
            totalScore+=borrowerApprovalVO.getInfoIntegral();
            //身份证积分
            if(borrowerApprovalVO.getIsIdCardOk()){
                //得到身份证积分
                Integer score1 = IntegralEnum.BORROWER_IDCARD.getIntegral();
                integral=new UserIntegral();
                integral.setUserId(userId);
                integral.setIntegral(score1);
                integral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
                userIntegralMapper.insert(integral);
                totalScore+=score1;
            }

            //车辆信息积分
            if(borrowerApprovalVO.getIsCarOk()){
                //得到车辆信息积分
                Integer score2 = IntegralEnum.BORROWER_CAR.getIntegral();
                integral=new UserIntegral();
                integral.setUserId(userId);
                integral.setIntegral(score2);
                integral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
                userIntegralMapper.insert(integral);
                totalScore+=score2;
            }
            //房产信息积分
            if(borrowerApprovalVO.getIsHouseOk()){
                //得到房产信息积分
                Integer score3 = IntegralEnum.BORROWER_HOUSE.getIntegral();
                integral=new UserIntegral();
                integral.setUserId(userId);
                integral.setIntegral(score3);
                integral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
                userIntegralMapper.insert(integral);
                totalScore+=score3;
            }
            //更新用户的integral,设置为最新的积分
            userInfo.setIntegral(totalScore);
        }
        //修改user_info表中用户的borrow_auth_status字段
        userInfo.setBorrowAuthStatus(status);
        userInfoMapper.updateById(userInfo);
    }
    public Long getIdByUserId(Long userId) {
        QueryWrapper<Borrower> wrapper=new QueryWrapper<>();
        wrapper.select("id").eq("user_id",userId);
        Borrower borrower = baseMapper.selectOne(wrapper);
        return borrower.getId();
    }
}
