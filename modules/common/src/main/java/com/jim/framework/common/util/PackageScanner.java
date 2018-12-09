package com.jim.framework.common.util;

import java.io.IOException;
import java.util.List;

/**
 * Created by celiang.hu on 2018-12-03.
 */
public interface PackageScanner {
    public List<String> getFullyQualifiedClassNameList() throws IOException;
}
