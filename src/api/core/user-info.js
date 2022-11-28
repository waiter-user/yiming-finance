import request from "@/utils/request";
const prefix = "/admin/core/userInfo";
export default {
  getPageList(current, size, searchObj) {
    return request({
      url: `${prefix}/list/${current}/${size}`,
      method: "get",
      params: searchObj, //params指定GET请求的查询参数     data指定POST请求的请求体参数(json对象)
    });
  },
  lock(id, status) {
    return request({
      url: `${prefix}/lock/${id}/${status}`,
      method: "put",
    });
  },
};
