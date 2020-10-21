import RSButton from "./RS-Button"
import Vue from "vue"

const loading = {
    install: function () {
        Vue.component('RSButton', RSButton);
    }
}
export default loading