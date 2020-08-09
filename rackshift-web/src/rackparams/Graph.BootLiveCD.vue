<template>
    <div>
        <el-card>
            <el-form>
                <el-form-item :label="$t('hostname')">
                    CDDDDDDDDDDDDDD
                </el-form-item>
                <el-form-item :label="$t('root_pwd')">
                    <el-input v-model="payLoad.options.defaults.rootPassword" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item :label="$t('image')">
                    <el-select v-model="payLoad.options.defaults.repo">
                        <el-option v-for="g in allImages" :label="g.name"
                                   :value="g.url"></el-option>
                    </el-select>
                </el-form-item>

                <!--                <el-form-item :label="$t('network_devices')">-->
                <!--                    <el-table>-->
                <!--                        <el-table-column :label="$t('ip_addr')">-->
                <!--                            {{$t('network_card')}}-->
                <!--                        </el-table-column>-->

                <!--                        <el-table-column :label="$t('ip_addr')">-->
                <!--                            <el-input v-model="payLoad.options.defaults.hostname" autocomplete="off"></el-input>-->
                <!--                        </el-table-column>-->
                <!--                    </el-table>-->
                <!--                </el-form-item>-->

                <table>
                    <thead>{{$t('network_devices')}}</thead>
                    <tr>
                        <td>{{$t('network_card_mac')}}</td>
                        <td>{{$t('ip_addr')}}</td>
                        <td>{{$t('gateway')}}</td>
                        <td>{{$t('netmask')}}</td>
                    </tr>

                    <tr v-for="d in payLoad.options.defaults.networkDevices">
                        <td>
                            <el-input v-model="d.device"></el-input>
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
                <el-button @click="test">df</el-button>
            </el-form>
        </el-card>
    </div>
</template>
<script>
    import HttpUtil from "../common/utils/HttpUtil";

    export default {
        props: {
            params: {
                type: Object,
                default: function () {
                    return this.payLoad;
                }
            }
        },
        methods: {
            setParams(params) {
                this.params = params;
            },
            alert() {
                alert(this.name);
            }
        },
        activated() {
            alert("act");
        },
        deactivated() {
            alert("dact");
        },
        data() {
            return {
                currentWorkflowIndex: null,
                payLoad: this.params ? this.params : {
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
                allImages: []
            };
        },
        mounted() {
            this.getAllImage();
            this.$on('save-params', (res) => {
                this.$emit('post-params', this.payLoad);
            });
        },
        methods: {
            test() {
                this.$emit('post-params', this.params);
            },
            getAllImage: function () {
                HttpUtil.post("/image/list/" + 1 + "/" + 1000, {}, (res) => {
                    this.allImages = res.data.listObject;
                });
            }
        }
    }
</script>