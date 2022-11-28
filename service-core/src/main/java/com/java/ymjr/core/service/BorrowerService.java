package com.java.ymjr.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.ymjr.core.pojo.Borrower;
import com.baomidou.mybatisplus.extension.service.IService;
import com.java.ymjr.core.vo.BorrowerApprovalVO;
import com.java.ymjr.core.vo.BorrowerDetailVO;
import com.java.ymjr.core.vo.BorrowerVO;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author LXG
 * @since 2022-09-18
 */
public interface BorrowerService extends IService<Borrower> {

    void saveBorrowerVoByUserId(BorrowerVO borrowerVO, Long userId);

    Integer getStatusByUserId(Long userId);

    IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword);

    BorrowerDetailVO getBorrowerDetailVOById(Long id);

    void approval(BorrowerApprovalVO borrowerApprovalVO);

    Long getIdByUserId(Long userId);
}
