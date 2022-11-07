package com.ebook.multbooks.util;

import com.ebook.multbooks.global.util.Util;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UtilTest {
    @Test
    public void t1(){
        Map<String,Integer> map= Util.mapOf("철수",22,"영희",33);
        assertThat(map.get("철수")).isEqualTo(22);
        assertThat(map.get("영희")).isEqualTo(33);
    }
}
