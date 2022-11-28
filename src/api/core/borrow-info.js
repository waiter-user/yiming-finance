import request from "@/utils/request";
const prefix = "/admin/core/borrowInfo";
export default {
  getList() {
    return request({
      url: `${prefix}/list`,
      method: "get",
    });
  },
  show(id) {
    return request({
      url: `${prefix}/show/${id}`,
      method: "get",
    });
  },
  approval(borrowInfoApproval) {
    return request({
      url: `${prefix}/approval`,
      method: "post",
      data: borrowInfoApproval,
    });
  },
};
