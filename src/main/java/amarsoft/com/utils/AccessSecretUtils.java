package amarsoft.com.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class AccessSecretUtils {
    public static String sign(String secret, Object... params) {
        if (params == null) {
            return null;
        }
        List<String> serializedParams = new ArrayList<>(params.length << 1);
        for (Object param : params) {
            if (param == null) {
                continue;
            }
            if (param instanceof Number || param instanceof CharSequence || param instanceof Date) {
                serializedParams.add(getValue(param));
            } else {
                serializedParams.add(generateSignParam(param));
            }
        }
        String paramsInfo = String.join("|", serializedParams);
        return MD5Utils.crypt(secret + paramsInfo);
    }

    private static String generateSignParam(Object obj) {
        JSONObject json = JSON.parseObject(JSON.toJSONString(obj), Feature.SortFeidFastMatch);
        List<String> keys = new ArrayList<>(json.keySet());
        Collections.sort(keys);
        List<String> reqParams = new ArrayList<>();
        for (String key : keys) {
            if (key != null) {
                reqParams.add(getValue(json.get(key)));
            }
        }
        return String.join("|", reqParams);
    }

    private static String getValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Date) {
            return ((Date) value).getTime() + "";
        }
        return value.toString();
    }
}
