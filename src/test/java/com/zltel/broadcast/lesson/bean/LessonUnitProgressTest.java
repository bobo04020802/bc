package com.zltel.broadcast.lesson.bean;


import javax.annotation.Generated;

import org.junit.Test;

import com.zltel.BaseTests;
import com.zltel.broadcast.common.codegenerator.MyBatisScriptMain;

@Generated(value = "org.junit-tools-1.0.6")
public class LessonUnitProgressTest extends BaseTests {


    @Test
    public void testEquals() throws Exception {
        StringBuilder sb = MyBatisScriptMain.createCRUDScript(LessonUnitProgress.class);
        logout.info(sb.toString());
    }
}
