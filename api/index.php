<?php

include_once 'ini.php';
include_once 'mysql.php';
header('Content-type:text/json');

try {
    $str = getApi($_GET['url'], $_GET['t']);
}catch (Error $error){
    $str = returnJson(false, $error->getMessage());
}
echo $str;
exit();

/**
 * @param $url
 * @param $type ==1生成短链  ==2启禁短链   ==3查访问量
 * @return string
 */
function getApi($url, $type): string
{

    //远程调试
    $file_get_contents = file_get_contents("php://input");
    if ($file_get_contents != null && $file_get_contents != '')
        remote_debugging($file_get_contents);



    if (!isUrl($url))
        return returnJson(false, "请输入有效网址");

    $mysql_tool = MySqlTool::getMySqlTool();
    if ($mysql_tool == null || $mysql_tool == '')
        return returnJson(false, "数据库连接失败，请重试");

    if($type == 1 && preg_match('/^http(s)?:\/\/'.$_SERVER['SERVER_NAME'].'/', $url))
        return returnJson(false, "不能生成本域名网址");
    $is_short = isShort($mysql_tool, $url);
    if ($type == 1 && $is_short['is'])
        return returnJson(false, "已是短链，无需生成");
    if (($type == 2 || $type == 3) && !$is_short['is'])
        return returnJson(false, "无此短链，请确认后重试");

    if ($type == 1) return addShort($mysql_tool, $url);
    if ($type == 2) return openShort($mysql_tool, $is_short['id']);
    if ($type == 3) return lookShort($mysql_tool, $is_short['id']);
    return returnJson(false, "接口t字段无效(1,2,3)");
}


/**
 * 是否为有效url
 * @param $url
 * @return bool
 */
function isUrl($url): bool
{
    if ($url == null || $url == '')
        return false;
    if (preg_match('/^http(s)?:\\/\\/.+/',
            $url))
        return true;
    return false;
}


/**
 * 是否短链
 * @param MySqlTool $connect
 * @param $url
 * @return array {is->false, id-> '1sdf27'}
 */
function isShort(MySqlTool $connect, $url): array{
    $r_array = array('is' => false, 'id' => '');
    if (preg_match(returnShort(null), $url)) {
        if (preg_match('/\/\?.*/', $url, $tt)) {
            $r_array['id'] = str_replace('/?', '', $tt[0]);
            $r_array['is'] = $connect->check_is_short($r_array['id']);
            return $r_array;
        }
        return $r_array;
    }
    return $r_array;
}


/**
 * 生成并且添加短链
 * @param MySqlTool $content
 * @param $long_url
 * @return string
 */
function addShort(MySqlTool $content, $long_url): string{
    //echo $long_url;
    //如果存在此长链则返回此长链的短链
    $check_is_url = $content->check_is_url($long_url);
    if ($check_is_url['is'])
        return returnJson(true, '短链已存在',  returnShort($check_is_url['id']));
    //如果生成的id已存在则重新生成
    id: $short_id = make_coupon_card($GLOBALS['key_len']);
    if ($content->check_is_short($short_id))
        goto id;
    if($content->add_short($short_id, $long_url))
        return returnJson(true, '短链生成成功',  returnShort($short_id));
    return returnJson(false, '短链生成失败');
}


/**
 * 启用或关闭短链
 * @param MySqlTool $content
 * @param $shortId
 * @return string
 */
function openShort(MySqlTool $content, $shortId): string{
    $is_open = $content->check_is_short_open($shortId);
    $str_open = $is_open ? '禁用' : '启用';
    if($content->change_open_short($shortId, !$is_open))
        return returnJson(true, $str_open.'短链成功');
    return returnJson(false, $str_open.'短链失败');

}


/**
 * 查看短链访问量
 * @param MySqlTool $content
 * @param $shortId
 * @return string
 */
function lookShort(MySqlTool $content, $shortId): string{
    return returnJson(true, '查询成功', null, $content->check_short_amount($shortId));
}


?>








