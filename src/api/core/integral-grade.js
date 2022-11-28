// @ 符号在vue.config.js 中配置， 表示 'src' 路径的别名
import request from "@/utils/request";
const FREFIX = "/admin/core/integralGrade";
// 模块化导出
export default {
  list() {
    return request({
      url: `${FREFIX}/findAll`,
      method: "get",
    });
  },
  //新增积分等级
  addDate(data) {
    return request({
      url: `${FREFIX}/save`,
      method: "post",
      data,
    });
  },
  //获取积分等级
  getById(id) {
    return request({
      url: `${FREFIX}/getById/${id}`,
      method: "get",
    });
  },
  //更新积分等级
  updateById(data) {
    return request({
      url: `${FREFIX}/updateById`,
      method: "put",
      data,
    });
  },
};
