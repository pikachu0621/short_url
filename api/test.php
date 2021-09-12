<?php


//qq内置的
function is_qq(): bool
{

    $bro_msg = $_SERVER['HTTP_USER_AGENT'];
    if (strpos($bro_msg, 'iPhone') || strpos($bro_msg, 'iPad')) {
        if (strpos($_SERVER['HTTP_USER_AGENT'], ' QQ') !== false)
            return true;//苹果qq打开
    }

    if (strpos($bro_msg, 'Android')) {
        if (strpos($bro_msg, 'MQQBrowser') !== false) {
            if (strpos($bro_msg, ' QQ') !== false)
                return true;//安卓qq打开
        }
    }
    return false;
}

//微信
function is_weiXin(): bool
{
    $bro_msg = $_SERVER['HTTP_USER_AGENT'];
    if (strpos($bro_msg, 'MicroMessenger') !== false)
        return true;
    return false;
}


echo '当前处于'. (is_qq() ? 'QQ内置' : (is_weiXin() ? '微信内置' : '外置')).'浏览器';