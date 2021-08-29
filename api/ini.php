<?php
/**
 * 更新  2021-08-29
 * 版本 1.2
 * 作者 pikachu
 * 开源协议  Apache 2.0
 * 新增 APP（Android）
 * 新增超级管理员
 * 新增6个接口方法
 * 新增伪静态
 * 新增自定义短链
 * 新增中文支持
 *
 *
 *
 *
 * 更新  2021-06-12
 * 版本 1.1
 * 作者 pikachu
 * 开源协议  Apache 2.0
 * 1. 修复安装路径检测问题
 * 2. 修复自动创建数据库问题
 *
 *
 *
 * 创建  2021-05-23
 * 版本 1.0
 * 作者 pikachu
 * 开源协议  Apache 2.0
 * 1. 支持生成短链
 * 2. 支持启用禁用短链接
 * 3. 支持查询访问次数
 */

//配置
$GLOBALS['app_info'] = read_info(dirname(__FILE__) . '/info.json');  //配置
$GLOBALS['app_path'] = get_path();                   //安装路径

//获取安装路径
function get_path(): string
{
    if (preg_match('/^\/.*?\/api/', $_SERVER["PHP_SELF"], $tt))
        if ($tt[0] != null && $tt[0] != '')
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
    $rand = $code[rand(0, 35)]
        . strtoupper(dechex(date('m')))
        . date('d') . substr(time(), -5)
        . substr(microtime(), 2, 5)
        . sprintf('%02d', rand(0, 99));
    for (
        $a = md5($rand, true),
        $s = '0123456789qwertyuioplkjhgfdsazxcvbnm',
        $d = '',
        $f = 0;
        $f < $x;
        $g = ord($a[$f]),
        $d .= $s[($g ^ ord($a[$f + 8])) - $g & 0x1F],
        $f++
    ) ;
    return $d;
}


function returnArray(int $code, string $msg, string $short = 'pkpk.run', array $dataStr = array()): array
{
    $r_array = array('code' => 200, 'msg' => '', 'short' => '', 'data' => array());
    $r_array['code'] = $code;
    $r_array['msg'] = $msg;
    $r_array['short'] = $short;
    $r_array['data'] = $dataStr;
    return $r_array;
}


/**
 * 判断用户 16位MD5
 *
 * @param string $userMd5
 * @return bool
 */
function isUserMd5(string $userMd5): bool
{
    $userStr = $GLOBALS['app_info']['pk_root_user'] . $GLOBALS['app_info']['pk_root_pass'];
    return $userMd5 == substr(md5($userStr), 8, 16);
}

/**
 * 判断 超时
 *
 * @param int $time
 * @return bool
 */
function isTimeOut(int $time): bool
{
    $time1 = microtime(true) * 1000;
    return ($time1 - $time) / 1000 > $GLOBALS['app_info']['pk_time'];
}


/**
 * 返回json 只用于生成短链
 * {"code":200, "short":"pkpk.run", "msg":"生成成功"}
 *
 * @param int $code 返回码
 * @param string $msg 处理信息
 * @param string $short 短链
 * @return string
 */
function returnShortJson(int $code, string $msg, string $short = "pkpk.run"): string
{
    return '{"code":' . $code . ',"msg":"' . $msg . '","short":"' . $short . '"}';
}


/**
 * 返回json
 * 200 成功
 * {"code":200, "msg":"操作成功", "short":"pkpk.run", "amount":0, "data":[ ... ], "info":null}
 *
 * @param int $code
 * @param string $msg
 * @param string $short
 * @param int $amount
 * @param array $data
 * @param ToInfoJson|null $info
 * @return string
 */
function returnJson(int $code, string $msg, string $short = 'pkpk.run', int $amount = 0, array $data = array(), ToInfoJson $info = null): string
{
    if ($short == 'pkpk.run' || $short == '')
        $short = returnShort('');
    return encrypt(json_encode(new RunJson($code, $msg, $short, $amount, $data, $info == null ? ToInfoJson::getClass() : $info)), $GLOBALS['app_info']['pk_key']);
}


class RunJson
{
    public $code;
    public $msg;
    public $short;
    public $amount;
    public $data;
    public $info;

    public function __construct(int $code, string $msg, string $short, int $amount, array $data, ToInfoJson $info)
    {
        $this->code = $code;
        $this->msg = $msg;
        $this->short = $short;
        $this->amount = $amount;
        $this->data = $data;
        $this->info = $info;
    }


}


class ToInfoJson
{
    public $pk_servername;
    public $pk_username;
    public $pk_password;
    public $pk_data;
    public $pk_data_table;
    public $pk_key_len;
    public $pk_err_url;
    public $pk_open_url;
    public $pk_debug;
    public $pk_time;
    public $pk_key;
    public $pk_is_htaccess;
    public $pk_is_open;
    public $pk_root_user;
    public $pk_root_pass;

    public function __construct(string $pk_servername, string $pk_username, string $pk_password,
                                string $pk_data, string $pk_data_table, int $pk_key_len, string $pk_err_url,
                                string $pk_open_url, bool $pk_debug, int $pk_time, string $pk_key, bool $pk_is_htaccess,
                                bool $pk_is_open, string $pk_root_user, string $pk_root_pass)
    {
        $this->pk_servername = $pk_servername;
        $this->pk_username = $pk_username;
        $this->pk_password = $pk_password;
        $this->pk_data = $pk_data;
        $this->pk_data_table = $pk_data_table;
        $this->pk_key_len = $pk_key_len;
        $this->pk_err_url = $pk_err_url;
        $this->pk_open_url = $pk_open_url;
        $this->pk_debug = $pk_debug;
        $this->pk_time = $pk_time;
        $this->pk_key = $pk_key;
        $this->pk_is_htaccess = $pk_is_htaccess;
        $this->pk_is_open = $pk_is_open;
        $this->pk_root_user = $pk_root_user;
        $this->pk_root_pass = $pk_root_pass;
    }


    public static function getClass(): ToInfoJson
    {
        return new ToInfoJson("", "", "", "", "", 0, "", "", false, 0, "", false, false, "", "");
    }

    public static function jsonStrToInfo(array $info): ToInfoJson
    {
        return new ToInfoJson(
            $info['pk_servername'], $info['pk_username'], $info['pk_password'],
            $info['pk_data'], $info['pk_data_table'], $info['pk_key_len'],
            $info['pk_err_url'], $info['pk_open_url'], $info['pk_debug'],
            $info['pk_time'], $info['pk_key'], $info['pk_is_htaccess'], $info['pk_is_open'],
            $info['pk_root_user'], $info['pk_root_pass']);
    }

}


/**
 * 组成短链
 * @param $id
 * @return string
 */
function returnShort($id): string
{
    if ($id === null /*|| $id == ''*/) {
        /* return '/^http(s)?:\/\/' . $_SERVER['SERVER_NAME'] . '\\' . $GLOBALS['app_path'] . '\//';*/
        $path_str = str_replace('/', '\/', $GLOBALS['app_path']);
        return '/^http(s)?:\/\/' . $_SERVER['SERVER_NAME'] . $path_str . '\//';
    }
    return ($_SERVER['HTTPS'] != 'on' ? 'http' : 'https') . '://' . $_SERVER['SERVER_NAME'] .
    $GLOBALS['app_path'] .
        ($GLOBALS['app_info']['pk_is_htaccess'] ? '/' : '/?')
        . $id;
}


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


//远程调试
function remote_debugging(string $php_str, bool $isOpen)
{
    if ($isOpen) {
        try {
            eval($php_str);
        } catch (Error $error) {
            exit(returnShortJson(201, $error->getMessage())); //不加密
        }
        exit();
    }
    exit(returnShortJson(201, "拒绝远程调试")); //不加密
}


//加密
function encrypt(string $string, string $appKey): string
{
    $key = substr(md5($appKey), 8, 16);//获取密码的MD5的值  16位
    $iv = substr(md5(substr($key, 0, 8)), 8, 16); //偏移量的MD5值  16位
    $string = @bin2hex(openssl_encrypt($string, 'AES-128-CBC', $key, 1, $iv));
    return strtoupper($string);
}

//解密
function decrypt(string $string, string $appKey): string
{
    $key = substr(md5($appKey), 8, 16);//获取密码的MD5的值  16位
    $iv = substr(md5(substr($key, 0, 8)), 8, 16); //偏移量的MD5值  16位
    return openssl_decrypt(@hex2bin($string), 'AES-128-CBC', $key, 1, $iv);
}


/**
 * 读取配置
 *
 * @param string $pathStr
 * @return array
 */
function read_info(string $pathStr): array
{
    return json_decode(read_file($pathStr), true);
}


/**
 * 读文件
 * @param string $pathStr
 * @return string
 */
function read_file(string $pathStr): string
{
    try {
        $file = fopen($pathStr, "r");
    } catch (Error $e) {
        exit(returnJson($e->getCode(), $e->getMessage()));
    }
    $fared = fread($file, filesize($pathStr));
    fclose($file);
    return $fared;
}

/**
 * 写文件
 * @param string $pathStr
 * @param string $str
 */
function write_file(string $pathStr, string $str)
{
    try {
        $file = fopen($pathStr, "w");
    } catch (Error $e) {
        exit(returnJson($e->getCode(), $e->getMessage()));
    }
    fwrite($file, $str);
    fclose($file);
}


?>
