package org.proygrad.einstein.util;

import org.proygrad.einstein.api.CommonItemTO;

import java.util.Iterator;
import java.util.List;

public class CommonItemUtil {


    public static Double getValue(List<CommonItemTO> parameters, String code) {
        Iterator pr = parameters.iterator();

        while(pr.hasNext()) {
            CommonItemTO obj = (CommonItemTO)pr.next();

            if(code.equals(obj.getCode())){
                return Double.valueOf(obj.getValue());
            }
        }

        // TODO: deberia dar error.
        return 0d;
    }
}
