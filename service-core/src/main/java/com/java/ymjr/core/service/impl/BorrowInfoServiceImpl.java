package com.java.ymjr.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.ymjr.common.pojo.Assert;
import com.java.ymjr.common.pojo.ResponseEnum;
import com.java.ymjr.core.enums.BorrowInfoStatusEnum;
import com.java.ymjr.core.enums.BorrowerStatusEnum;
import com.java.ymjr.core.enums.DictEnum;
import com.java.ymjr.core.enums.UserBindEnum;
import com.java.ymjr.core.mapper.DictMapper;
import com.java.ymjr.core.mapper.IntegralGradeMapper;
import com.java.ymjr.core.mapper.UserInfoMapper;
import com.java.ymjr.core.pojo.BorrowInfo;
import com.java.ymjr.core.mapper.BorrowInfoMapper;
import com.java.ymjr.core.pojo.IntegralGrade;
import com.java.ymjr.core.pojo.UserInfo;
import com.java.ymjr.core.service.BorrowInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.ymjr.core.service.BorrowerService;
import com.java.ymjr.core.service.DictService;
import com.java.ymjr.core.service.LendService;
import com.java.ymjr.core.vo.BorrowInfoApprovalVO;
import com.java.ymjr.core.vo.BorrowInfoVO;
import com.java.ymjr.core.vo.BorrowerDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private IntegralGradeMapper integralGradeMapper;
    @Autowired
    private DictService dictService;
    @Autowired
    private BorrowerService borrowerService;

    @Override
    public BigDecimal getBorrowAmount(Long userId) {
        //获取借款人积分
        QueryWrapper<UserInfo> UserInfoqueryWrapper = new QueryWrapper<>();
        UserInfoqueryWrapper.select("integral").eq("id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(UserInfoqueryWrapper);
        //用户不存在
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);
        Integer integral = userInfo.getIntegral();
        //根据积分获取借款额度
        QueryWrapper<IntegralGrade> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("borrow_amount")
                .le("integral_start", integral)
                .ge("integral_end", integral);
        IntegralGrade integralGrade = integralGradeMapper.selectOne(queryWrapper);
        return integralGrade.getBorrowAmount();
    }

    @Override
    public void saveBorrowInfo(BorrowInfo borrowInfo, Long userId) {
        //获取用户
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.select("bind_status", "borrow_auth_status")
                .eq("id", userId);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
        //判断用户绑定状态
        Assert.isTrue(userInfo.getBindStatus().intValue() == UserBindEnum.BIND_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_BIND_ERROR);
        //判断用户借款额度是否审批通过
        Assert.isTrue(userInfo.getBorrowAuthStatus().intValue() == BorrowerStatusEnum.AUTH_OK.getStatus().intValue(),
                ResponseEnum.USER_NO_AMOUNT_ERROR);
        //判断借款金额是否在额度范围内
        BigDecimal borrowAmount = this.getBorrowAmount(userId);
        Assert.isTrue(borrowInfo.getAmount().doubleValue() <= borrowAmount.doubleValue(),
                ResponseEnum.USER_AMOUNT_LESS_ERROR);
        //保存数据
        borrowInfo.setUserId(userId);
        BigDecimal borrowYearRate = borrowInfo.getBorrowYearRate().divide(new BigDecimal(100));
        borrowInfo.setBorrowYearRate(borrowYearRate);
        borrowInfo.setStatus(BorrowInfoStatusEnum.CHECK_RUN.getStatus());
        baseMapper.insert(borrowInfo);
    }

    @Override
    public Integer getStatusByUserId(Long userId) {
        QueryWrapper<BorrowInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status").eq("user_id", userId);
        BorrowInfo borrowInfo = baseMapper.selectOne(queryWrapper);
        if (null == borrowInfo) {
            //借款人未提交借款信息
            return BorrowInfoStatusEnum.NO_AUTH.getStatus();
        }
        return borrowInfo.getStatus();
    }

    @Override
    public List<BorrowInfoVO> getList() {
        List<BorrowInfoVO> borrowInfoVOS = baseMapper.selectBorrowInfoList();
        borrowInfoVOS.forEach(borrowInfoVO -> {
            packBorrowInfoVO(borrowInfoVO);
        });
        return borrowInfoVOS;
    }

    /**
     * 包装BorrowInfoVO对象
     */
    private void packBorrowInfoVO(BorrowInfoVO borrowInfoVO) {
        String returnMethod = dictService.getNameByParentDictCodeAndValue(DictEnum.RETURN_METHOD.getDictCode(), borrowInfoVO.getReturnMethod());
        String moneyUse = dictService.getNameByParentDictCodeAndValue(DictEnum.MONEY_USER.getDictCode(), borrowInfoVO.getMoneyUse());
        String status = BorrowInfoStatusEnum.getMsgByStatus(borrowInfoVO.getStatus());
        borrowInfoVO.getParams().put("returnMethod", returnMethod);
        borrowInfoVO.getParams().put("moneyUse", moneyUse);
        borrowInfoVO.getParams().put("status", status);
    }

    @Override
    public Map<String, Object> getBorrowInfoDetail(Long id) {
        QueryWrapper<BorrowInfo> wrapper = new QueryWrapper<>();
        wrapper.select("user_id", "amount", "period", "borrow_year_rate", "return_method"
                        , "money_use", "status", "create_time")
                .eq("id", id);

        BorrowInfo borrowInfo = baseMapper.selectOne(wrapper);

        BorrowInfoVO borrowInfoVO = new BorrowInfoVO();
        BeanUtils.copyProperties(borrowInfo, borrowInfoVO);
        this.packBorrowInfoVO(borrowInfoVO);
        //获取借款人的编号
        Long bid = borrowerService.getIdByUserId(borrowInfo.getUserId());
        //获取借款人详情
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(bid);
        //组装数据
        Map<String, Object> result = new HashMap<>();
        result.put("borrowInfo", borrowInfoVO);
        result.put("borrower", borrowerDetailVO);
        return result;
    }

    @Autowired
    private LendService lendService;

    @Override
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO) {
        //修改借款信息状态
        Long borrowInfoId = borrowInfoApprovalVO.getId();
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoId);
        borrowInfo.setStatus(borrowInfoApprovalVO.getStatus());
        baseMapper.updateById(borrowInfo);

        //审核通过则创建标的
        if (borrowInfoApprovalVO.getStatus().intValue() == BorrowInfoStatusEnum.CHECK_OK.getStatus().intValue()) {
            //创建标的
            lendService.createLend(borrowInfoApprovalVO, borrowInfo);
        }
    }
}
