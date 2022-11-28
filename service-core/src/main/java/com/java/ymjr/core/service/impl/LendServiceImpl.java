package com.java.ymjr.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.java.ymjr.core.enums.DictEnum;
import com.java.ymjr.core.enums.LendStatusEnum;
import com.java.ymjr.core.pojo.BorrowInfo;
import com.java.ymjr.core.pojo.Lend;
import com.java.ymjr.core.mapper.LendMapper;
import com.java.ymjr.core.service.BorrowerService;
import com.java.ymjr.core.service.DictService;
import com.java.ymjr.core.service.LendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.ymjr.core.utils.LendNoUtil;
import com.java.ymjr.core.vo.BorrowInfoApprovalVO;
import com.java.ymjr.core.vo.BorrowerDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {
    @Override
    public void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo) {
        Lend lend = new Lend();
        lend.setUserId(borrowInfo.getUserId());
        //借款信息id
        lend.setBorrowInfoId(borrowInfo.getId());
        //生成标的编号
        lend.setLendNo(LendNoUtil.getLendNo());
        //设置标题
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        //设置标的金额
        lend.setAmount(borrowInfo.getAmount());
        //设置还款期数
        lend.setPeriod(borrowInfo.getPeriod());
        //从审批对象中获取
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));
        lend.setReturnMethod(borrowInfo.getReturnMethod());
        //设置最低投资金额
        lend.setLowestAmount(new BigDecimal(100));
        //设置已投金额
        lend.setInvestAmount(new BigDecimal(0));
        //设置投资人数
        lend.setInvestNum(0);
        //发布日期   LocalDate.now()不包含时间
        lend.setPublishDate(LocalDateTime.now());
        //起始日期
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //LocalDate.format:将对象转化为字符串
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVO.getLendStartDate(), dtf);
        lend.setLendStartDate(lendStartDate);
        //结束日期
        LocalDate lendEndDate = lendStartDate.plusMonths(borrowInfo.getPeriod());
        lend.setLendEndDate(lendEndDate);
        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());//描述
        //平台预期收益
        //        月化 = 年化 / 12
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        //        预期收益 = 标的金额 * 月化 * 期数
        BigDecimal expectAmount = lend.getAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));
        lend.setExpectAmount(expectAmount);

        //实际收益
        lend.setRealAmount(new BigDecimal(0));
        //状态
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());
        //审核时间
        lend.setCheckTime(LocalDateTime.now());
        //审核人
        lend.setCheckAdminId(1L);

        baseMapper.insert(lend);
    }
    @Autowired
    private DictService dictService;
    @Override
    public List<Lend> getList() {
        QueryWrapper<Lend> wrapper = new QueryWrapper<>();
        wrapper.select("id","title", "lend_no", "amount", "period", "lend_year_rate", "invest_amount",
                "invest_num", "publish_date", "lend_start_date", "lend_end_date", "return_method", "status");
        List<Lend> lendList = baseMapper.selectList(wrapper);
        lendList.forEach(lend -> {
            //给lend对象设置还款方式字符串
            String returnMethod = dictService.getNameByParentDictCodeAndValue(DictEnum.RETURN_METHOD.getDictCode(), lend.getReturnMethod());
            String status = LendStatusEnum.getMsgByStatus(lend.getStatus().intValue());
            lend.getParams().put("returnMethod", returnMethod);
            lend.getParams().put("status", status);
        });
        return lendList;
    }

    @Autowired
    private BorrowerService borrowerService;
    @Override
    public Map<String, Object> getLendDetail(Long id) {
        //获取标的
        Lend lend = baseMapper.selectLend(id);
        //包装Lend对象
        this.packLend(lend);
        //获取借款人编号
        Long bid = borrowerService.getIdByUserId(lend.getUserId());
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(bid);
        //组装数据
        Map<String,Object> result=new HashMap<>();
        result.put("lend",lend);
        result.put("borrower", borrowerDetailVO);
        return result;
    }

    private void packLend(Lend lend){
        String returnMethod = dictService.getNameByParentDictCodeAndValue(DictEnum.RETURN_METHOD.getDictCode(), lend.getReturnMethod());
        String status = LendStatusEnum.getMsgByStatus(lend.getStatus().intValue());
        lend.getParams().put("returnMethod", returnMethod);
        lend.getParams().put("status", status);
    }
}
