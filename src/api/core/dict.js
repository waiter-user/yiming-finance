import request from "@/utils/request";
const prefix = "/admin/core/dict";
export default {
  listByParentId(parentId) {
    return request({
      url: `${prefix}/getListByParentId/${parentId}`,
      method: "get",
    });
  },
};
