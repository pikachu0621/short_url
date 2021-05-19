<?php  
include_once 'api/ini.php';
include_once 'api/mysql.php';

//ID获取失败
$get_short_id = get_short_id($_SERVER['REQUEST_URI']);
if (!$get_short_id['is'])
    redirect($GLOBALS['err_url']);

//数据库连接失败
$mysql_tool = MySqlTool::getMySqlTool();
if ($mysql_tool == null || $mysql_tool == '')
    redirect($GLOBALS['err_url']);

//没有此短链ID
if(!$mysql_tool->check_is_short($get_short_id['id']))
    redirect($GLOBALS['err_url']);

//短链已禁止访问
if (!$mysql_tool->check_is_short_open($get_short_id['id'])){
    //禁止访问也加次数
    $mysql_tool->add_short_amount($get_short_id['id']);
    redirect($GLOBALS['open_url']);
}


$check_long_url = $mysql_tool->check_long_url($get_short_id['id']);
//查询失败
if (!$check_long_url['is'])
    redirect($GLOBALS['err_url']);

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
    if (preg_match('/\?.*/', $path, $tt)) {
        $r_array['id'] = str_replace('?', '', $tt[0]);
        $r_array['is'] = true;
        return $r_array;
    }
    return $r_array;
}









?>