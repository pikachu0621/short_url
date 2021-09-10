<?php
/**
 * 短链生成
 * by ： pikachu
 *
 *
 *
 * 普通用户
 *
 * 生成短链 ->  get    ?url=长链接
 * 返回 ->     json   {"code":200, "msg":"生成成功", "short":"pkpk.run"}
 *
 *
 *
 * 超级管理员  post  AES
 *
 * 登录 ->         post        json     type = 0  $user_md5 16位  - $time
 * 返回 ->                     json     {"code":200, "msg":"成功", "short":"pkpk.run", "amount":0, "data":[], "info":null}
 *
 * 生成短链 ->     post         json    type = 1   $long_url - $user_md5  - $time
 * 返回 ->                     json    {"code":200, "msg":"生成成功", "short":"pkpk.run", "amount":0, "data":[], "info":null}
 *
 * 启用/禁用 ->    post         json    type = 2   $short_url - $user_md5  - $time
 * 返回 ->                     json    {"code":200, "msg":"启/禁用成功", "short":"pkpk.run", "amount":0, "data":[], "info":null}
 *
 * 查看访问量 ->   post         json    type = 3  $short_url  $user_md5  - $time
 * 返回 ->                     json    {"code":200, "msg":"成功", "short":"pkpk.run", "amount":10, "data":[], "info":null}
 *
 * 编辑 ->        post         json    type = 4  id = 0    $long_url - $short_url - $is_open - $is_open_win - $amount - $user_md5  - $time
 * 返回 ->                     json    {"code":200, "msg":"成功", "short":"", "amount":0, "data":[], "info":null}
 *
 *
 *
 * 读取本短链全部数据 -> post    json    type = 5   $short_url - $user_md5  - $time
 *                             json    同
 *
 * 读取全部数据      -> post    json    type = 6  page = 1  num = 10     $user_md5  - $time
 *                            json    同
 *
 *
 *
 *
 * 读取全部配置数据  -> post   json   type = 7  $user_md5  - $time
 *                           json   同
 *
 * 修改配置数据    -> post    json   type = 8 $user_md5  - $time - 配置数据
 *                          json    同
 *
 *
 * 删除短链    -> post    json   type = 9  $short_url  $user_md5  - $time
 *                       json    同
 *
 */


include_once 'ini.php';
include_once 'mysql.php';
header('Content-type:text/json');

try {
    $str = getApi($_GET['url'] == null ? '' : $_GET['url'], file_get_contents("php://input") == null ? '' : file_get_contents("php://input"));
} catch (Error $error) {
    $str = returnShortJson($error->getCode(), $error->getMessage()/*"错误，请联系作者"*/); // 不加密
}
echo $str;
exit();


/**
 * @param string $url
 * @param string $json
 * @return string
 */
function getApi(string $url, string $json): string
{

    if ($url == '' && $json == '')
        return returnShortJson(206, "参数错误，请联系作者");


    if ($url != null && $url != '' && $url != "") {
        $mysql_tool = MySqlTool::getMySqlTool();
        if ($mysql_tool == null || $mysql_tool == '')
            return returnShortJson(201, "数据库连接失败，请重试");
        if ($GLOBALS['app_info']['pk_is_open']) {
            if (!isUrl($url))
                return returnShortJson(201, "请输入有效网址");
            if (preg_match('/^http(s)?:\/\/' . $_SERVER['SERVER_NAME'] . '/', $url))
                return returnShortJson(201, "不能生成本域名网址");
            $is_short = isShort($mysql_tool, $url);
            if ($is_short['is'])
                return returnShortJson(201, "已是短链，无需生成");
            $addShort = addShort($mysql_tool, $url);
            return returnShortJson($addShort['code'], $addShort['msg'], $addShort['short']);
        } else return returnShortJson(201, "短链生成暂时关闭");
    }


    $encode_post = encode_post($json);
    if ($encode_post['code'] != 200) {
        return returnJson($encode_post['code'], $encode_post['msg']);
    }


    if ($encode_post['data']['type'] == 0)
        return returnJson(200, '登录成功');


    if ($encode_post['data']['type'] == 7)
        return readInfo();

    if ($encode_post['data']['type'] == 8)
        return writeInfo($encode_post['data']);


    $mysql_tool = MySqlTool::getMySqlTool();
    if ($mysql_tool == null || $mysql_tool == '')
        return returnJson(201, "数据库连接失败，请重试");

    if ($encode_post['data']['type'] == 1) {
        if (!isUrl($encode_post['data']['long_url']))
            return returnJson(201, "请输入有效网址");
        if (preg_match('/^http(s)?:\/\/' . $_SERVER['SERVER_NAME'] . '/', $encode_post['data']['long_url']))
            return returnJson(201, "不能生成本域名网址");
        $is_short = isShort($mysql_tool, $encode_post['data']['long_url']);
        if ($is_short['is'])
            return returnJson(201, "已是短链，无需生成");
        $addShort = addShort($mysql_tool, $encode_post['data']['long_url']);
        return returnJson($addShort['code'], $addShort['msg'], $addShort['short']);
    }


    if ($encode_post['data']['type'] == 2) {
        $is_short = isShort($mysql_tool, $encode_post['data']['short_url']);
        if (!$is_short['is'])
            return returnJson(201, "无此短链，请确认后重试");
        return openShort($mysql_tool, $is_short['id']);
    }

    if ($encode_post['data']['type'] == 3) {
        $is_short = isShort($mysql_tool, $encode_post['data']['short_url']);
        if (!$is_short['is'])
            return returnJson(201, "无此短链，请确认后重试");
        return lookShort($mysql_tool, $is_short['id']);
    }


    if ($encode_post['data']['type'] == 4) {
        return editShort($mysql_tool,
            $encode_post['data']['id'], $encode_post['data']['long_url'],
            $encode_post['data']['short_url'], $encode_post['data']['is_open'],
            $encode_post['data']['is_open_win'], $encode_post['data']['amount']);
    }


    if ($encode_post['data']['type'] == 5) {
        $is_short = isShort($mysql_tool, $encode_post['data']['short_url']);
        if (!$is_short['is'])
            return returnJson(201, "无此短链，请确认后重试");
        return lookShortData($mysql_tool, $is_short['id']);
    }


    if ($encode_post['data']['type'] == 6) {
        return returnJson(200, "读取完毕", '', 0,
            $mysql_tool->check_all($encode_post['data']['page'], $encode_post['data']['num']));
    }



    if ($encode_post['data']['type'] == 9)
        return deleteShort($mysql_tool, $encode_post['data']['short_url']);


    return returnJson(201, "接口type字段无效0-8");
}


/**
 * 解析数据
 *
 * @param string $json
 * @return array
 */
function encode_post(string $json): array
{
    try {
        $decrypt = decrypt($json, $GLOBALS['app_info']['pk_key']);
    } catch (Error $e) {
        return returnArray($e->getCode(), '解密失败：' . $e->getMessage());
        //return returnShortJson(/*$error->getCode()*/204, /*$error->getMessage()*/"错误，请联系作者");
    }
    if ($decrypt == null || $decrypt == '')
        return returnArray(103, '解密失败');
    $json_decode = json_decode($decrypt, true);
    if ($json_decode == null || $json_decode['user_md5'] == null)
        return returnArray(102, '解析失败');
    if (!isUserMd5($json_decode['user_md5']))
        return returnArray(101, '用户认证失败');
    if (isTimeOut($json_decode['time']))
        return returnArray(203, '链接已过时');
    return returnArray(200, '解密成功', '', $json_decode);
}


/**
 * 是否为有效url
 * @param $url
 * @return bool
 */
function isUrl(string $url): bool
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
function isShort(MySqlTool $connect, $url): array
{
    $r_array = array('is' => false, 'id' => '');

    if ($GLOBALS['app_info']['pk_is_htaccess']){
         if(strstr($url, '/')){
             $r_array['id'] = substr($url, strripos($url, '/') + 1, strlen($url));
             $r_array['is'] = $connect->check_is_short($r_array['id'])['is'];
             return $r_array;
         }else{
             $r_array['is'] = $connect->check_is_short($url)['is'];
             $r_array['id'] = $url;
             return $r_array;
         }
    }


    /*echo  returnShort(null);*/
    if (preg_match(returnShort(null), $url)) {
        if (preg_match('/\/\?.*/', $url, $tt)) {
            $r_array['id'] = str_replace('/?', '', $tt[0]);
            $r_array['is'] = $connect->check_is_short($r_array['id'])['is'];
            return $r_array;
        }
        return $r_array;
    } else {
        $r_array['is'] = $connect->check_is_short($url)['is'];
        $r_array['id'] = $url;
    }
    return $r_array;
}


/**
 * 生成并且添加短链
 * @param MySqlTool $content
 * @param $long_url
 * @return array
 */
function addShort(MySqlTool $content, $long_url): array
{
    //如果存在此长链则返回此长链的短链
    $check_is_url = $content->check_is_url($long_url);
    if ($check_is_url['is'])
        return returnArray(200, '短链已存在', returnShort($check_is_url['id']));

    //如果生成的id已存在则重新生成
    id:
    $short_id = make_coupon_card($GLOBALS['app_info']['pk_key_len']);
    if ($content->check_is_short($short_id)['is'])
        goto id;
    if ($content->add_short($short_id, $long_url))
        return returnArray(200, '短链生成成功', returnShort($short_id));
    return returnArray(201, '短链生成失败');
}


/**
 * 启用或关闭短链
 * @param MySqlTool $content
 * @param $shortId
 * @return string
 */
function openShort(MySqlTool $content, $shortId): string
{
    $is_open = $content->check_is_short_open($shortId);
    $str_open = $is_open ? '禁用' : '启用';
    if ($content->change_open_short($shortId, !$is_open))
        return returnJson(200, $str_open . '短链成功');
    return returnJson(201, $str_open . '短链失败');

}


/**
 * 查看短链访问量
 * @param MySqlTool $content
 * @param $shortId
 * @return string
 */
function lookShort(MySqlTool $content, $shortId): string
{
    return returnJson(200, '查询成功', '', $content->check_short_amount($shortId));
}


/**
 * 编辑
 * @param MySqlTool $content
 * @param int $id
 * @param string $long_url
 * @param string $short_url
 * @param bool $is_open
 * @param bool $is_open_win
 * @param int $amount
 * @return string
 */
function editShort(MySqlTool $content, int $id, string $long_url, string $short_url, bool $is_open, bool $is_open_win, int $amount): string
{

    $check_is_short = $content->check_is_short($short_url);
    if ($check_is_short['is'] && $check_is_short['id'] != $id) {
        return returnJson(201, '修改失败, 短链ID被占用');
    }
    if ($content->update_data($id, $long_url, $short_url, $is_open, $is_open_win, $amount)) {
        return returnJson(200, '修改成功', returnShort($short_url));
    }
    //echo $id.'<>'.$long_url.'<>'.$short_url.'<>'.$is_open.'<>'.$is_open_win.'<>'.$amount.'<>';
    return returnJson(201, '修改失败');
}


/**
 * 查询全部数据
 * @param MySqlTool $content
 * @param $shortId
 * @return string
 */
function lookShortData(MySqlTool $content, $shortId): string
{
    $check_short_all = $content->check_short_all($shortId);
    return returnJson(200, '查询成功', '', 0, $check_short_all);
}


function readInfo(): string
{
    return returnJson(200, '查询成功', '', 0, array(),
        ToInfoJson::jsonStrToInfo(json_decode(read_file(dirname(__FILE__) . '/info.json'), true)));
}


function writeInfo(array $jsonArr): string
{
    $toInfoJson = ToInfoJson::jsonStrToInfo($jsonArr);
    $json_encode = json_encode($toInfoJson);
    write_file(dirname(__FILE__) . '/info.json', $json_encode);
    return returnJson(200, '写入成功', '', 0, array(), $toInfoJson);
}


function deleteShort(MySqlTool $content, string $short): string
{
    $isShort = isShort($content, $short);
    if ($isShort['is']){
        if ($content->delete_short($isShort['id']))
            return returnJson(200,   '删除成功');
        return returnJson(201,  '删除失败');
    }
    return returnJson(202,  '未查询到短链');
}




