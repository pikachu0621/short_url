<?php
include_once 'ini.php';

//mysql工具类
class MySqlTool
{
    private $conn;
    public static $mySqlTool;

    /**
     *连接数据库
     */
    public function __construct()
    {
        @$this->conn = mysqli_connect($GLOBALS['servername'], $GLOBALS['username'], $GLOBALS['password']);
        mysqli_options($this->conn, MYSQLI_OPT_INT_AND_FLOAT_NATIVE, true);
        if ($this->conn->connect_error) {
            $this->conn = null;
            exit(returnJson(false, "error：{$this->conn->error}"));
        }
        $result = mysqli_query($this->conn, "SHOW DATABASES LIKE '{$GLOBALS['data']}'");
        $row = mysqli_fetch_assoc($result);
        if ($row != null && sizeof($row) > 0) {
            mysqli_select_db($this->conn, $GLOBALS['data']);
            return;
        }
        $sql = "CREATE DATABASE {$GLOBALS['data']}";
        if (!$this->conn->query($sql)) { //失败退出脚本
            exit(returnJson(false, "error：{$this->conn->error}"));
        }
        mysqli_select_db($this->conn, $GLOBALS['data']);



        $sql_str = array(
            "SET NAMES utf8mb4;",
            "SET FOREIGN_KEY_CHECKS = 0;",
            "DROP TABLE IF EXISTS `{$GLOBALS['data_table']}`;",
            "CREATE TABLE `{$GLOBALS['data_table']}`  (
				`id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
				`long_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '原链接',
				`short_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '短链接id',
				`is_open` int(1) NOT NULL DEFAULT 1 COMMENT '是否开放访问',
				`url_visits` int(11) NOT NULL DEFAULT 0 COMMENT '访问量',
				`create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
				PRIMARY KEY (`id`) USING BTREE
			) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;",
            "SET FOREIGN_KEY_CHECKS = 1;");
        foreach ($sql_str as $str) {
            if ($str != "" && $str != null && $str != " " && $str != ";") {
                if (!$this->conn->query($str)) {
                    $this->conn = null;
                    exit(returnJson(false, 'error：创建失败'.$str));
                }
            }
        }
    }

    /**
     * 获取对象
     */
    public static function getMySqlTool(): MySqlTool
    {
        if (self::$mySqlTool == null || self::$mySqlTool == '') {
            /*try {*/
                self::$mySqlTool = new MySqlTool();
            /*}catch (Error $error){
                exit(returnJson(false, $error->getMessage()));
            }*/
        }
        return self::$mySqlTool;
    }


    /**
     * 写入短链
     *
     * @param $short_str
     * @param $log_url
     * @return bool
     */
    public function add_short(string $short_str, string $log_url): bool
    {
        if ($short_str == null || $short_str == '' || $log_url == null || $log_url == '' || $this->conn == null)
            return false;
        $sql = /** @lang text */
            "INSERT INTO {$GLOBALS['data_table']} (long_url, short_url) VALUES ('{$log_url}', '{$short_str}')";
        if (mysqli_query($this->conn, $sql))
            return true;
        return false;
    }


    /**
     * 查是否有此短链ID
     *
     * @param $short_id
     * @return bool
     */
    public function check_is_short(string $short_id): bool
    {
        if ($short_id == null || $short_id == '' || $this->conn == null)
            return false;
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT short_url FROM {$GLOBALS['data_table']} WHERE short_url='{$short_id}'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) >= 1)
            return true;
        return false;
    }


    /**
     * 查此长链接是否有短链
     *
     * @param string $log_url
     * @return array
     */
    public function check_is_url(string $log_url): array
    {
        $arr = array('is' => false, 'id' => '');
        if ($log_url == null || $log_url == '' || $this->conn == null)
            return $arr;
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT long_url,short_url FROM {$GLOBALS['data_table']} WHERE long_url='{$log_url}'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) >= 1) {
            $arr['is'] = true;
            $arr['id'] = $row['short_url'];
        }
        return $arr;
    }


    /**
     * 查是否可访问
     *
     * @param string $short_id
     * @return bool
     */
    public function check_is_short_open(string $short_id): bool
    {
        if ($short_id == null || $short_id == '' || $this->conn == null)
            return false;
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT is_open FROM {$GLOBALS['data_table']} WHERE short_url='{$short_id}'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) >= 1)
            return $row['is_open'] == 0 ? false : true;
        return false;
    }


    /**
     * 修改是否访问
     *
     * @param string $short_id
     * @param bool $is_open
     * @return bool
     */
    public function change_open_short(string $short_id, bool $is_open): bool
    {
        if ($short_id == null || $short_id == '' || $this->conn == null)
            return false;
        $is_open_n = $is_open ? 1 : 0;
        $sql = /** @lang text */
            "UPDATE {$GLOBALS['data_table']} SET is_open = {$is_open_n} WHERE short_url='{$short_id}'";
        if (mysqli_query($this->conn, $sql))
            return true;
        return false;
    }


    /**
     * 查访问量
     *
     * @param string $short_id
     * @return int
     */
    public function check_short_amount(string $short_id): int
    {
        if ($short_id == null || $short_id == '' || $this->conn == null)
            return 0;
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT url_visits FROM {$GLOBALS['data_table']} WHERE short_url='{$short_id}'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) >= 1)
            return (int)$row['url_visits'];
        return 0;
    }


    /**
     * 更具id 返回long_url
     *
     * @param string $short_id
     * @return array
     */
    public function check_long_url(string $short_id): array
    {
        $arr = array('is' => false, 'url' => '');
        if ($short_id == null || $short_id == '' || $this->conn == null)
            return $arr;
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT long_url FROM {$GLOBALS['data_table']} WHERE short_url='{$short_id}'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) >= 1){
            $arr['is'] = true;
            $arr['url'] = $row['long_url'];
        }
        return $arr;
    }





    /**
     * 添加访问量 +1
     *
     * @param string $short_id
     * @return bool
     */
    public function add_short_amount(string $short_id): bool
    {
        if ($short_id == null || $short_id == '' || $this->conn == null)
            return false;
        $amount = $this->check_short_amount($short_id) + 1;
        $sql = /** @lang text */
            "UPDATE {$GLOBALS['data_table']} SET url_visits = {$amount} WHERE short_url='{$short_id}'";
        if (mysqli_query($this->conn, $sql))
            return true;
        return false;
    }


    //关闭连接
    public function send()
    {
        mysqli_close($this->conn);
    }

}


?>