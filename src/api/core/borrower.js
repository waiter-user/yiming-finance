import request from "@/utils/request";
const prefix = "/admin/core/borrower";
export default {
  getPageList(current, size, keyword) {
    return request({
      url: `${prefix}/list/${current}/${size}`,
      method: "get",
      params: { keyword },
    });
  },
  //获取借款人详情
  show(id) {
    return request({
      url: `${prefix}/show/${id}`,
      method: "get",
    });
  },
  approval(borrowerApproval) {
    return request({
      url: `${prefix}/approval`,
      method: "post",
      data: borrowerApproval,
    });
  },
};
