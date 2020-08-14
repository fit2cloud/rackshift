<template>
    <div>
        <el-card>
            <el-form>
                <el-form-item :label="$t('hostname')">
                    <el-input v-model="payLoad.options.defaults.hostname" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item :label="$t('root_pwd')">
                    <el-input v-model="payLoad.options.defaults.rootPassword" autocomplete="off"
                              type="password"></el-input>
                </el-form-item>
                <el-form-item :label="$t('image')">
                    <el-select v-model="payLoad.options.defaults.repo" style="float: left;">
                        <el-option v-for="g in allImages" :label="g.name"
                                   :value="g.url"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item :label="$t('network_devices')">
                    <table class="detail-info" style="float: left;">
                        <tr>
                            <td>{{$t('network_card_mac')}}</td>
                            <td>{{$t('ip_addr')}}</td>
                            <td>{{$t('gateway')}}</td>
                            <td>{{$t('netmask')}}</td>
                        </tr>

                        <tr v-for="d in payLoad.options.defaults.networkDevices">
                            <td>
                                <el-select v-model="d.device">
                                    <el-option :label="n.number + '(' + n.mac + ')'" :value="n.mac"
                                               v-for="n in nics"></el-option>
                                </el-select>
                            </td>
                            <td>
                                <el-input v-model="d.ipv4.ipAddr"></el-input>
                            </td>
                            <td>
                                <el-input v-model="d.ipv4.gateway"></el-input>
                            </td>
                            <td>
                                <el-input v-model="d.ipv4.netmask"></el-input>
                            </td>
                        </tr>
                    </table>
                </el-form-item>

            </el-form>
        </el-card>
    </div>
</template>
<script>
    import HttpUtil from "../common/utils/HttpUtil";

    export default {
        activated() {
        },
        created() {
        },
        deactivated() {

        },
        data() {
            return {
                payLoad: this.$attrs.params ? JSON.parse(JSON.stringify(this.$attrs.params)) : {
                    "options": {
                        "defaults": {
                            "version": "7",
                            "repo": "http://172.31.128.1:8080/centos/7/os/x86_64",
                            "rootPassword": "RackHDRocks!",
                            "hostname": "rackhd-node",
                            "networkDevices": [
                                {
                                    "device": "em1",
                                    "ipv4": {
                                        "ipAddr": "172.31.128.10",
                                        "gateway": "172.31.128.1",
                                        "netmask": "255.255.255.0"
                                    }
                                }
                            ],
                            "installDisk": "/dev/sda",
                            "installPartitions": [
                                {
                                    "mountPoint": "/",
                                    "size": "auto",
                                    "fsType": "ext3"
                                },
                                {
                                    "mountPoint": "swap",
                                    "size": "5000",
                                    "fsType": "swap"
                                },
                                {
                                    "mountPoint": "/boot",
                                    "size": "5000",
                                    "fsType": "ext3"
                                },
                                {
                                    "mountPoint": "biosboot",
                                    "size": "1",
                                    "fsType": "biosboot"
                                }
                            ]
                        }
                    }
                },
                allImages: [],
                cpus: [],
                memories: [],
                disks: [],
                nics: [],
            };
        },
        mounted() {
            this.getAllImage();
            this.getHardware();
        },
        methods: {
            getAllImage: function () {
                HttpUtil.post("/image/list/" + 1 + "/" + 1000, {}, (res) => {
                    this.allImages = res.data.listObject;
                    if (this.payLoad.options.defaults.repo == 'http://172.31.128.1:8080/centos/7/os/x86_64') {
                        this.payLoad.options.defaults.repo = this.allImages[0].url;
                    }
                });
            },
            getHardware() {
                HttpUtil.get("/bare-metal/hardwares/" + this.$attrs.bareMetalId, null, (res) => {
                    this.cpus = res.data.cpus;
                    this.memories = res.data.memories;
                    this.disks = res.data.disks;
                    this.nics = res.data.nics;

                    if (this.payLoad.options.defaults.networkDevices[0].device == 'em1' && this.nics.length > 0) {
                        this.payLoad.options.defaults.networkDevices[0].device = this.nics[0].mac;
                    }
                })
            },
        }
    }
</script>