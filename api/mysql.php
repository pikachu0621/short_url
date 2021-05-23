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
        try {
            @$this->conn = $this->add_sql($GLOBALS['servername'], $GLOBALS['username'], $GLOBALS['password'],
                $GLOBALS['data'], $GLOBALS['data_table'],
                "SET NAMES utf8mb4;
                SET FOREIGN_KEY_CHECKS = 0;
                DROP TABLE IF EXISTS `{table_name}`;
                CREATE TABLE `{table_name}`  (
                `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                    `long_url` text CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT '原链接',
                    `short_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '短链接id',
                    `is_open` int(1) NOT NULL DEFAULT 1 COMMENT '是否开放访问',
                    `url_visits` int(11) NOT NULL DEFAULT 0 COMMENT '访问量',
                    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
                    PRIMARY KEY (`id`) USING BTREE
                ) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;
                SET FOREIGN_KEY_CHECKS = 1;");
        } catch (Exception $exception) {
            exit(returnJson(false, $exception->getMessage()));
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
            "INSERT INTO {$GLOBALS['data_table']} (long_url, short_url) VALUES ('{$log_url}', '{$short_str}' )";
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
            "UPDATE {$GLOBALS['data_table']} SET is_open = $is_open_n WHERE short_url='{$short_id}'";
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
        if ($row != null && sizeof($row) >= 1) {
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
            "UPDATE {$GLOBALS['data_table']} SET url_visits = $amount WHERE short_url='{$short_id}'";
        if (mysqli_query($this->conn, $sql))
            return true;
        return false;
    }


    //关闭连接
    public function send()
    {
        mysqli_close($this->conn);
    }


    /**
     * 连接数据库 没有则创建
     * @param string $host 地址
     * @param string $user 用户名
     * @param string $psd 用户密码
     * @param string $data 数据库名
     * @param string $data_table 数据表单名
     * @param string $sql_str 创建表的数据库语法 表名用 ‘{table_name}’ 替代
     * @return mysqli|false
     * @throws Exception 抛出异常
     */
    public function add_sql(string $host, string $user, string $psd, string $data, string $data_table, string $sql_str){

        $conn = mysqli_connect($host, $user, $psd);
        mysqli_options($conn, MYSQLI_OPT_INT_AND_FLOAT_NATIVE, true);
        if ($conn->connect_error)
            throw new Exception("error：$conn->error");

        //查看是否有此数据库
        $result = mysqli_query($conn, "SHOW DATABASES LIKE '{$data}';");
        $row = mysqli_fetch_assoc($result);
        if ($row != null && sizeof($row) > 0) {
            mysqli_select_db($conn, $data);
            //查看是否有此数据表单
            $res2 = mysqli_query($conn, "SHOW TABLES LIKE '{$data_table}';");
            $row2 = mysqli_fetch_assoc($res2);
            if ($row2 != null && sizeof($row2) > 0)
                return $conn;//有
        }

        //->1 有数据库 但没表
        //->2 没数据库 没表
        if ($row == null || sizeof($row) <= 0) {
            //没数据库情况
            $sql = "CREATE DATABASE $data";
            if (!$conn->query($sql))  //失败退出脚本
                throw new Exception("error：$conn->error");
        }
        mysqli_select_db($conn, $data);
        //-> 有数据库 但没表
        //读取文件 	$_sql = file_get_contents($sqlpath);
        $sql_str = str_replace('{table_name}', $data_table, $sql_str);
        $sql_arr_str = explode(';', $sql_str);
        foreach ($sql_arr_str as $str) {
            if ($str != "" && $str != null && $str != " " && $str != ";") {
                if (!$conn->query($str))
                    throw new Exception("error：创建失败 $str");
            }
        }
        return $conn;
    }

}


?>