<?php
include_once('api/ini.php');
include_once('api/mysql.php');



//ID获取
$get_short_id = get_short_id($_SERVER['REQUEST_URI']);
//$get_short_id['id'] = mb_convert_encoding($get_short_id['id'],"gb2312","utf-8");

//ID获取失败
if (!$get_short_id['is'])
    redirect($GLOBALS['app_info']['pk_err_url']);


//数据库连接失败
$mysql_tool = MySqlTool::getMySqlTool();
if ($mysql_tool == null || $mysql_tool == '')
    redirect($GLOBALS['app_info']['pk_err_url']);
//支持中文
$get_short_id['id'] = urldecode($get_short_id['id']);
//没有此短链ID
if (!$mysql_tool->check_is_short($get_short_id['id'])['is'])
    redirect($GLOBALS['app_info']['pk_err_url']);

//短链已禁止访问
if (!$mysql_tool->check_is_short_open($get_short_id['id'])) {
    //禁止访问也加次数
    $mysql_tool->add_short_amount($get_short_id['id']);
    redirect($GLOBALS['app_info']['pk_open_url']);
}


$check_long_url = $mysql_tool->check_long_url($get_short_id['id']);
//查询失败
if (!$check_long_url['is'])
    redirect($GLOBALS['app_info']['pk_err_url']);


//如果在qq或微信中打开, 并且 短链接开启防红
if ( (is_qq() || is_weiXin()) && $mysql_tool->is_open_red($get_short_id['id'])) {
    echo get_url(  ( $_SERVER['HTTPS'] != 'on' ? 'http://' : 'https://' ) . $_SERVER['SERVER_NAME'] . $_SERVER['REQUEST_URI'], !is_weiXin());
    exit();
}

//访问+1
$mysql_tool->add_short_amount($get_short_id['id']);
redirect($check_long_url['url']);


/**
 * 重定向
 * @param $url
 */
function redirect(string $url)
{
    header("Location: $url");
    exit();
}


function get_short_id(string $path): array
{
    $r_array = array('is' => false, 'id' => '');
    if ($GLOBALS['app_info']['pk_is_htaccess']){
        if(strstr($path, '/')) {
            $r_array['is'] = true;
            $r_array['id'] = substr($path, strripos($path, '/') + 1, strlen($path));
            return $r_array;
        }
    }

    if (preg_match('/\?.*/', $path, $tt)) {
        $r_array['id'] = str_replace('?', '', $tt[0]);
        $r_array['is'] = true;
        return $r_array;
    }
    return $r_array;
}


function get_url(string $url, bool $is_weixin): string
{


    return '<!--github: https://github.com/pikachu0621  作者微信：Pikachu_WeChat -->
            <!DOCTYPE html>
            <html lang="ch">
            <head>
                <meta charset="UTF-8">
                <title>跳转中...</title>
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" name="viewport">
                <meta content="yes" name="apple-mobile-web-app-capable">
                <meta content="black" name="apple-mobile-web-app-status-bar-style">
                <meta name="format-detection" content="telephone=no">
                <meta content="false" name="twcClient" id="twcClient">
                <link rel="stylesheet" href="./css/loading.css">
                <meta http-equiv ="refresh" content ="0; url=mttbrowser://url='.$url.'">
            </head>
            <body>
            <div class="wrapper">
                <div class="title">
                    <p class="print"> '.($is_weixin? '跳转到系统浏览器中...' : '请点击右上角，用浏览器打开...' ).' </p>
                    <div></div>
                </div>
                <div class="artboard">
                    <div id="bed"></div>
                    <div id="arm-left"></div>
                    <div id="pillow"></div>
                    <div id="body"></div>
                    <div id="neck"></div>
                    <div id="head"></div>
                    <div id="hair">
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                    <div id="eye-left"></div>
                    <div id="eye-right"></div>
                    <div id="mouth"></div>
                    <div id="quilt"></div>
                    <div id="arm-right"></div>
                    <div id="braid">
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                    <div id="phone"></div>
                    <div id="hand"></div>
                    <div id="bed-end"></div>
                </div>
                <div class="goto">
                '.($is_weixin?'
                    <samp>如未跳转，请手动进行跳转<br>点击右上角，或点击以下已安装浏览器</samp>
                    <a href="mttbrowser://url='.$url.'"><img src="./img/qqweb.svg" alt="QQ浏览器"></a>
                    <a href="ucweb://'.$url.'"><img src="./img/ucweb.svg" alt="UC浏览器"></a>
                    <a href="googlechrome://browse?url='.$url.'"><img src="./img/googleweb.svg" alt="谷歌浏览器"></a>
                    <a href="mibrowser://'.$url.'"><img src="./img/miweb.svg" alt="小米浏览器"></a>
                    <a href="baiduboxapp://browse?url='.$url.'"><img src="./img/baiduweb.svg" alt="百度浏览器"></a>
                    ' : ''/*<samp style="font-size: 16px">请点击右上角，用浏览器打开</samp>*/ ).'
                </div>
            </div>
            </body>
            </html>';

}







