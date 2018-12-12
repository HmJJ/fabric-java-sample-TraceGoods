package com.springboot.fabric.code;

import java.util.Map;

/**
 * 描述：BlockListener监听返回map集合
 *
 */
public interface BlockListener {
    void received(Map<String, String> map);
}
