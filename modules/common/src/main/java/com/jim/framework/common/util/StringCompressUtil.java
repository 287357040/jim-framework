package com.jim.framework.common.util;

import com.jim.framework.common.util.encode.Base64Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class StringCompressUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringCompressUtil.class);

    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * 先将字符串gzip, 再将它转成base64 encoded字符串, 实测请求跟踪压缩比30%
     *
     * @param s       字串
     * @param charset 字符集
     * @return 压缩和base64编码后的字符串
     * @throws IOException
     */
    public static String compress2Base64(String s, String charset) {
        return Base64Code.encodeToString(GZipUtils.compress(s, charset));
    }

    public static String compress2Base64(String s) {
        return compress2Base64(s, DEFAULT_CHARSET);
    }

    /**
     * 先将字符串base64 decode, 然后再gunzip
     *
     * @param s       经<code>gzipBase64Encode</code>压缩的字符串
     * @param charset 字符集
     * @return 原始字符串
     * @throws IOException
     */
    public static String decompressFromBase64(String s, String charset) {
        return GZipUtils.decompress(Base64Code.decodeFromString(s), charset);
    }

    public static String decompressFromBase64(String s) {
        return decompressFromBase64(s, DEFAULT_CHARSET);
    }
}
