import Vue from 'vue';
import VueI18n from "vue-i18n";
import axios from "axios";
import enLocale from "element-ui/lib/locale/lang/en";
import zh_CNLocale from "element-ui/lib/locale/lang/zh-CN";
import zh_TWLocale from "element-ui/lib/locale/lang/zh-TW";
import zh_CN from "./zh-CN";
import en_US from "./en-US";
import zh_TW from "./zh-TW";
import bus from '../common/bus/bus'

Vue.use(VueI18n);

const messages = {
    'en_US': {
        ...enLocale,
        ...en_US
    },
    'zh_CN': {
        ...zh_CNLocale,
        ...zh_CN
    },
    'zh_TW': {
        ...zh_TWLocale,
        ...zh_TW
    }
};

const i18n = new VueI18n({
    locale: localStorage.getItem('lang') || 'zh',
    messages,
});

i18n.setLocale = function (lang) {
    this.options = {
        locale: lang,
        messages,
    };
}
const loadedLanguages = ['en_US', 'zh_CN', 'zh_TW'];

function setI18nLanguage(lang) {
    i18n.locale = lang;
    axios.defaults.headers.common['Accept-Language'] = lang;
    document.querySelector('html').setAttribute('lang', lang);
    localStorage.setItem("lang", lang);
    //工作流如果选中需要刷新国际化
    bus.$emit('refresh_workflow');
    return lang;
}

Vue.prototype.$setLang = function (lang) {
    if (i18n.locale !== lang) {
        if (!loadedLanguages.includes(lang)) {
            let file = lang.replace("_", "-");
            return import(`./${file}`).then(response => {
                i18n.mergeLocaleMessage(lang, response.default);
                loadedLanguages.push(lang);
                return setI18nLanguage(lang)
            })
        }
        return Promise.resolve(setI18nLanguage(lang))
    }
    return Promise.resolve(lang)
};

export default i18n;
