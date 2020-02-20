package com.itheima.health.test;

import com.itheima.health.util.ValidateCodeUtils;
import org.junit.Test;

public class TestValidateCode {

    @Test
    public void testCode(){
        Integer code4 = ValidateCodeUtils.generateValidateCode(4);
        System.out.println(code4);

        Integer code6 = ValidateCodeUtils.generateValidateCode(6);
        System.out.println(code6);

        String codeStr4 = ValidateCodeUtils.generateValidateCode4String(4);
        System.out.println(codeStr4);

        String codeStr6 = ValidateCodeUtils.generateValidateCode4String(6);
        System.out.println(codeStr6);

    }
}
