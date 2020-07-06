import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "../login/Login.vue"
import Index from "../index/Index.vue"

Vue.use(VueRouter);

const routes = [
    {path: '/', redirect: '/index'},
    {path: '/login', component: Login},
    {path: '/index', component: Index},
]
const router = new VueRouter({
    routes: routes
})

router.beforeEach((to, from, next) => {
    if (to.path != '/login') {
        if (localStorage.getItem('login') == "true") {
            next();
        } else {
            router.push("login");
        }
    } else {
        next();
    }
})
export default router;