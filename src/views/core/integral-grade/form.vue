<template>
  <!-- 有1个根元素 -->
  <div>
    <div class="app-container">
      <!-- 输入表单 -->
      <el-form label-width="120px">
        <el-form-item label="借款额度">
          <el-input-number v-model="integralGrade.borrowAmount" :min="0" />
        </el-form-item>
        <el-form-item label="积分区间开始">
          <el-input-number v-model="integralGrade.integralStart" :min="0" />
        </el-form-item>
        <el-form-item label="积分区间结束">
          <el-input-number v-model="integralGrade.integralEnd" :min="0" />
        </el-form-item>
        <el-form-item>
          <el-button
            :disabled="saveBtnDisabled"
            type="primary"
            @click="saveOrUpdate()"
          >
            保存
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
//导入js模块
import integralGradeApi from "@/api/core/integral-grade";
export default {
  data() {
    return {
      integralGrade: {}, // 积分等级对象
      saveBtnDisabled: false, // 保存按钮是否禁用，防止表单重复提交
    };
  },
  created() {
    //获取路由中的id参数值,根据路由path中的参数名获取值，例如edit/:id中的id就是参数名
    const id = this.$route.params.id;
    if (id) {
      //根据id获取积分等级进行回显
      integralGradeApi.getById(id).then((response) => {
        this.integralGrade = response.data.integralGrade;
      });
    }
  },
  methods: {
    saveOrUpdate() {
      //禁用保存按钮，防止表单重复提交
      this.saveBtnDisabled = true;
      if (this.integralGrade.id) {
        //更新积分等级
        this.updateData();
      } else {
        //新增积分等级
        this.saveData();
      }
    },
    //根据id获取积分等级数据
    fetchDataById(id) {},
    //新增数据
    saveData() {
      //跳转组件
      integralGradeApi.addDate(this.integralGrade).then((response) => {
        if (response.code == 0) {
          //成功，跳转到积分等级列表页面
          this.$router.push("/core/integral-grade/list");
        }
      });
    },
    //更新数据
    updateData() {
      integralGradeApi.updateById(this.integralGrade).then((response) => {
        if (response.code == 0) {
          //成功，跳转到积分等级列表页面
          this.$router.push("/core/integral-grade/list");
        }
      });
    },
  },
};
</script>

<style scoped>
</style>