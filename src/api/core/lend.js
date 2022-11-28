import request from "@/utils/request";
const prefix = "/admin/core/lend";
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
};
