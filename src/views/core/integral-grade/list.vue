<template>
  <div class="app-container">
    <!-- 表格 -->
    <el-table :data="list" border stripe>
      <el-table-column type="index" width="50" />
      <el-table-column prop="borrowAmount" label="借款额度" />
      <el-table-column prop="integralStart" label="积分区间开始" />
      <el-table-column prop="integralEnd" label="积分区间结束" />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            type="danger"
            size="mini"
            @click="removeById(scope.row.id)"
          >
            删除
          </el-button>
          <router-link
            :to="'/core/integral-grade/edit/'+scope.row.id"
            style="margin-left:15px;"
          >
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-edit"
            >
              编辑
            </el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
//导入js模块
import integralGradeApi from '@/api/core/integral-grade'
export default {
       // 定义数据模型
  data() {
    return {
      list: [] // 数据列表
    }
  },
  // 页面渲染成功后获取数据
  created() {
    this.fetchData()
  },
  // 定义方法 
   methods:{
       fetchData(){
           integralGradeApi.list().then(response=>{
               //  console.log(response);
              this.list=response.data.list;
           })
       }
   } 
}
</script>

<style scoped>

</style>