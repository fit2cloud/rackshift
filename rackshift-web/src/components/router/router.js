import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "../login/Login.vue"
import Index from "../index/Index.vue"

Vue.use(VueRouter);

const routes = [
    {path: '/', redirect: '/login'},
    {path: '/login', component: Login},
    {path: '/index', component: Index},
]
const router = new VueRouter({routes})

export default router;