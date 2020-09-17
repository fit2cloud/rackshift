<template>
  <div class="block">
    <el-row>
      <el-col :span=1>
        <button @click="next" class="viewer">
          <i class="el-icon-arrow-right"></i>
        </button>
      </el-col>
      <el-col :span=22>
        <div class="radio">
          <h1>部署一台裸金属服务器的步骤</h1>
        </div>

        <el-steps :active="active" finish-status="success" style="    width: 100%;">
          <el-step title="初始化RackShift">
          </el-step>
          <el-step title="发现裸金属服务器"></el-step>
          <el-step title="执行工作流"></el-step>
        </el-steps>

        <div v-show="active==1" id="1">
          <div>
            <el-timeline>
              <el-timeline-item timestamp="1" placement="top">
                <el-card>
                  <h4>打开配置菜单-Endpoint</h4>
                  <p><img src="../../assets/wizard/open_endpoint.jpg" alt="" class="wizard-pic"></p>
                </el-card>
              </el-timeline-item>
              <el-timeline-item timestamp="2" placement="top">
                <el-card>
                  <h4></h4>
                  <p><img src="../../assets/wizard/add_endpoint.jpg" alt="" class="wizard-pic"></p>
                </el-card>
              </el-timeline-item>
              <el-timeline-item timestamp="3" placement="top">
                <el-card>
                  <h4>上传用于装机的ISO镜像</h4>
                  <p><img src="../../assets/wizard/upload_iso.jpg" alt="" class="wizard-pic"></p>
                </el-card>
              </el-timeline-item>
              <el-timeline-item timestamp="4" placement="top">
                <el-card>
                  <h4>配置RackShift中用于PXE功能DHCP地址</h4>
                  <p><img src="../../assets/wizard/config_network.jpg" alt="" class="wizard-pic"></p>
                  <div class="wizard-tips">
                    注意：此IP地址段必须与RackShift所在PXE网段一致
                  </div>
                </el-card>
              </el-timeline-item>
              <el-timeline-item timestamp="3" placement="top">
                <el-card>
                  <h4>RackShift已经准备就绪，接下来您将发现一台用于部署的裸金属服务器！</h4>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>

        <div v-show="active==2" id="2">
          <div>
            <el-timeline>
              <h2>打通带外网络、PXE网络</h2>
              <el-timeline-item timestamp="1" placement="top">
                <el-card>
                  <el-row>
                    <el-col :span="12">
                      <h4>打通裸金属服务器带外网络以及PXE网络到RackShift的</h4>
                      <p><img src="../../assets/wizard/structure.jpg" alt=""></p>
                      <div class="wizard-tips">
                        物理机PXE网卡与RackShift必须是直连，也就是同一个2层网络，
                        物理机BMC卡与RackShift IP能通
                      </div>
                    </el-col>
                    <el-col :span="12">

                    </el-col>
                  </el-row>

                </el-card>
              </el-timeline-item>
              <el-timeline-item timestamp="2" placement="top">
                <el-card>
                  <h4>PXE启动服务器</h4>
                  <p></p>
                </el-card>
              </el-timeline-item>
              <el-timeline-item timestamp="3" placement="top">
                <el-card>
                  <h4>等待第一次发现结束后刷新裸金属服务器页面列表</h4>
                  <p>
                    <img src="../../assets/wizard/server_list.jpg" alt="" class="wizard-pic">
                  </p>
                </el-card>
              </el-timeline-item>

              <el-timeline-item timestamp="4" placement="top">
                <el-card>
                  <h4>补充带外（OBM）账号密码</h4>
                  <p>
                    <img src="../../assets/wizard/fill_obm.jpg" alt="" class="wizard-pic">
                    <img src="../../assets/wizard/add_obm.jpg" alt="" class="wizard-pic">
                  </p>
                </el-card>
              </el-timeline-item>

              <el-timeline-item timestamp="5" placement="top">
                <el-card>
                  <h4>裸金属服务器已经准备就绪！</h4>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>

        <div v-show="active==3" id="3">
          <div>
            <el-timeline>
              <h2>打通带外网络、PXE网络</h2>
              <el-timeline-item timestamp="1" placement="top">
                <el-card>
                  <el-row>
                    <el-col :span="12">
                      <h4>运行装机工作流</h4>
                      <p><img src="../../assets/wizard/add_workflow.jpg" alt="" 　class="wizard-pic"></p>
                      <div class="wizard-tips">
                        选择对应物理机，选择工作流下拉菜单”安装 Centos7 64位版“，并点击右侧”添加至已选工作流“
                      </div>

                      <p><img src="../../assets/wizard/config_os.jpg" alt="" 　class="wizard-pic"></p>
                      <div class="wizard-tips">
                        打开默认工作流参数配置页面，配置分区，业务IP地址以及对应的网卡最后"确定"
                      </div>

                      <p><img src="../../assets/wizard/run_os.jpg" alt="" 　class="wizard-pic"></p>
                      <div class="wizard-tips">
                        点击运行框中三角形按钮运行装机工作流
                      </div>

                      <p>
                        <img src="../../assets/wizard/running_os.jpg" alt="" 　class="wizard-pic"></p>
                      <div class="wizard-tips">
                        等待装机完成
                      </div>
                    </el-col>
                  </el-row>

                </el-card>
              </el-timeline-item>

              <el-timeline-item timestamp="5" placement="top">
                <el-card>
                  <h4>裸金属服务器已经部署完毕，请尽情使用！</h4>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>
      </el-col>
      <el-col :span=1>

      </el-col>
    </el-row>

    <!--    <div style="position:fixed; right:50px;bottom:50px;">-->
    <!--      <el-button @click="next">-->
    <!--        下一步-->
    <!--      </el-button>-->
    <!--    </div>-->
  </div>

</template>

<script>

let _ = require('lodash');
export default {
  data() {
    return {
      active: 1,
    };
  }
  , methods: {
    next() {
      if (++this.active > 3) this.active = 1;
      location.hash = "#" + this.active;
    }
  }
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.table {
  width: 100%;
  font-size: 14px;
}

.mr10 {
  margin-right: 10px;
}

.block {
  width: 100%;
}

.wizard-pic {
  width: 70vw;
}

.wizard-pic-2 {
  width: 40vw;
  height: 40vw;
}

.wizard-tips {
  font-weight: 500;
  color: red;
}

.viewer {
  position: fixed;
  right: 50px;
  bottom: 45vh;
  background-color: #faf5f3;
  height: 20vh;
  display: block;
  line-height: 18vh;
  white-space: nowrap;
  cursor: pointer;
  border: none;
  color: #606266;
  -webkit-appearance: none;
  text-align: center;
  box-sizing: border-box;
  outline: 0;
  margin: 0;
  transition: .1s;
  font-weight: 500;
  padding: 12px 20px;
  font-size: 14px;
  /*border-radius: 4px;*/
  border-bottom-right-radius: 100%;
  border-top-right-radius: 100%;
  border-bottom-left-radius: 4px;
  border-top-left-radius: 4px;
}

.viewer:hover {
  background-color: #fdeee9;
  /*background-color: #fff;*/
}
</style>