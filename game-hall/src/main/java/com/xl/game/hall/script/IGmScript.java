package com.xl.game.hall.script;


/**
 * gm脚本
 * @author xuliang
 * @QQ 359135103
 * 2020年1月2日 下午6:04:35
 */
public interface IGmScript {



    default String executeGm(long roleId,String gmCmd) {
        return String.format("GM {}未执行", gmCmd);
    }

    /**
     * 是否为gm命令
     * @author JiangZhiYong
     * @QQ 359135103
     * 2017年10月16日 下午6:07:32
     * @param gmCmd
     */
    default boolean isGMCmd(String gmCmd) {
        return gmCmd!=null&&gmCmd.startsWith("&");
    }




}
