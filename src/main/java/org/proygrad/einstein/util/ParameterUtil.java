package org.proygrad.einstein.util;

import org.proygrad.einstein.api.ParameterTO;

import java.util.Iterator;
import java.util.List;

public class ParameterUtil {

    public static ParameterTO getParameter(List<ParameterTO> parameters, String code) {
        Iterator pr = parameters.iterator();

        while(pr.hasNext()) {
            ParameterTO obj = (ParameterTO)pr.next();

            if(code.equals(obj.getCode())){
                return obj;
            }
        }

        // TODO: deberia dar error.
        return null;
    }
}
