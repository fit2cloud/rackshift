package io.rackshift.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import java.util.Locale;

public class Translator {

    private static MessageSource messageSource;

    @Resource
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static String get(String key) {
        return messageSource.getMessage(key, null, "not exists key:" + key, Locale.CHINA);
    }
}
