package com.jim.framework.core.mvc;

import com.jim.framework.common.util.time.DateUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by celiang.hu on 2018-11-11.
 */
public class DateRequestParameterBinder extends PropertyEditorSupport {
    private boolean useGMTTimeZone;
    private static final String DATE_FORMAT_10 = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT_16 = "yyyy-MM-dd HH:mm";
    private static final String DATETIME_FORMAT_19 = "yyyy-MM-dd HH:mm:ss";
    private static final String DATETIME_FORMAT_23 = "yyyy-MM-dd HH:mm:ss:SSS";
    private static final String DATETIME_FORMAT_DEFAULT = "EEE MMM dd HH:mm:ss zzz yyyy";

    public DateRequestParameterBinder() {
        this(false);
    }

    public DateRequestParameterBinder(boolean useGMTTimeZone) {
        this.useGMTTimeZone = useGMTTimeZone;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            try {
                try {
                    long l = Long.parseLong(text);
                    this.setValue(new Date(l));
                    return;
                } catch (NumberFormatException e) {
                    ;// handled
                }

                switch (text.length()) {
                    case 10: {
                        setDateValue(text, DATE_FORMAT_10);
                        break;
                    }
                    case 16: {
                        setDateValue(text, DATETIME_FORMAT_16);
                        break;
                    }
                    case 19: {
                        setDateValue(text, DATETIME_FORMAT_19);
                        break;
                    }
                    case 23: {
                        setDateValue(text, DATETIME_FORMAT_23);
                        break;
                    }
                    default: {
                        /**
                         * 兼容Feign调用时, 传过来的Date#toString()字符串
                         * */
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT_DEFAULT, Locale.ENGLISH);
                        setDateValue(text,simpleDateFormat);
                        break;
                    }
                }
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
            }
        }
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return DateUtils.formatDateTime(value);
    }

    private void setDateValue(String strDate, String datetimeFormat) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datetimeFormat);
        setDateValue(strDate, simpleDateFormat);
    }

    private void setDateValue(String strDate, SimpleDateFormat simpleDateFormat) throws ParseException {
        if (this.useGMTTimeZone) {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        setValue(simpleDateFormat.parse(strDate));
    }
}

