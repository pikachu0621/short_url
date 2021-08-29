<?php
include_once 'ini.php';

//mysql工具类
class MySqlTool
{
    private $conn;
    public static  $mySqlTool;
    private  $servername;
    private  $username;
    private  $password;
    private  $data;
    private  $data_table;


    /**
     *连接数据库
     */
    public function __construct()
    {
        $this->servername = $GLOBALS['app_info']['pk_servername'];
        $this->username = $GLOBALS['app_info']['pk_username'];
        $this->password = $GLOBALS['app_info']['pk_password'];
        $this->data = $GLOBALS['app_info']['pk_data'];
        $this->data_table = $GLOBALS['app_info']['pk_data_table'];

        try {
            @$this->conn = $this->add_sql(
                $this->servername,
                $this->username,
                $this->password,
                $this->data,
                $this->data_table,
                "SET NAMES utf8mb4;
                    SET FOREIGN_KEY_CHECKS = 0;
                    DROP TABLE IF EXISTS `{table_name}`;
                    CREATE TABLE `{table_name}`  (
                      `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
                      `long_url` text CHARACTER SET utf8 COLLATE utf8_unicode_ci COMMENT '原链接',
                      `short_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '短链接id',
                      `is_win` int(1) NOT NULL DEFAULT 1 COMMENT '是否开启防洪 1开',
                      `is_open` int(1) NOT NULL DEFAULT 1 COMMENT '是否开放访问 1开',
                      `url_visits` int(11) NOT NULL DEFAULT 0 COMMENT '访问量',
                      `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      PRIMARY KEY (`id`) USING BTREE
                    ) ENGINE = MyISAM AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;
                    SET FOREIGN_KEY_CHECKS = 1;");
        } catch (Exception $exception) {
            exit(returnJson($exception->getCode(), $exception->getMessage()));
        }
    }

    /**
     * 获取对象
     */
    public static function getMySqlTool(): MySqlTool
    {
        if (self::$mySqlTool == null || self::$mySqlTool == '') {
            self::$mySqlTool = new MySqlTool();
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
        //echo $log_url;
        if ($short_str == null || $short_str == '' || $log_url == null || $log_url == '' || $this->conn == null)
            return false;
        $sql = /** @lang text */
            "INSERT INTO $this->data_table (long_url, short_url) VALUES ('$log_url', '$short_str' )";
        if (mysqli_query($this->conn, $sql))
            return true;
        return false;
    }


    /**
     * 查是否有此短链ID
     *
     * @param string $short_id
     * @return array
     */
    public function check_is_short(string $short_id): array
    {

        if ($short_id == null || $short_id == '' || $this->conn == null)
            return array('is'=>false, 'id'=>0);
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT id,short_url FROM $this->data_table WHERE short_url='$short_id'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) > 0)
            return array('is'=>true, 'id'=>$row['id']);
        return array('is'=>false, 'id'=>0);
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
            "SELECT long_url,short_url FROM $this->data_table WHERE long_url='$log_url'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) > 0) {
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
            "SELECT is_open FROM $this->data_table WHERE short_url='$short_id'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) > 0)
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
            "UPDATE $this->data_table SET is_open = $is_open_n WHERE short_url='$short_id'";
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
            "SELECT url_visits FROM $this->data_table WHERE short_url='$short_id'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) > 0)
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
            "SELECT long_url FROM $this->data_table WHERE short_url='$short_id'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) > 0) {
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
            "UPDATE $this->data_table SET url_visits = $amount WHERE short_url='$short_id'";
        if (mysqli_query($this->conn, $sql))
            return true;
        return false;
    }


    /**
     * 查是否开启防红
     *
     * @param string $short_id
     * @return bool
     */
    public function is_open_red(string $short_id) : bool{
        if ($short_id == null || $short_id == '' || $this->conn == null)
            return false;
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT is_win FROM $this->data_table WHERE short_url='$short_id'");
        $row = mysqli_fetch_assoc($result);//查询
        if ($row != null && sizeof($row) > 0)
            return ((int)$row['is_win']) == 0 ? false : true;
        return false;
    }



    /**
     * 修改是否开启防红
     *
     * @param string $short_id
     * @param bool $is_open
     * @return bool
     */
    public function change_open_red(string $short_id, bool $is_open): bool
    {
        if ($short_id == null || $short_id  == '' || $this->conn == null)
            return false;
        $is_open_n = $is_open ? 1 : 0;
        $sql = /** @lang text */
            "UPDATE $this->data_table SET is_win = $is_open_n WHERE short_url='$short_id'";
        if (mysqli_query($this->conn, $sql))
            return true;
        return false;
    }


    /**
     * 删除一条短链
     *
     * @param string $short_id
     * @return bool
     */
    public function delete_short(string $short_id): bool
    {
        if ($short_id == null || $short_id  == '' || $this->conn == null)
            return false;
        $sql = /** @lang text */
            "DELETE FROM $this->data_table WHERE short_url = '$short_id'";
        if (mysqli_query($this->conn, $sql))
            return true;
        return false;
    }




    /**
     * 根据短链短链 查看全部数据
     *
     * @param string $short_id
     * @return array
     */
    public function check_short_all(string $short_id) : array{
        if ($short_id == null || $short_id  == '' || $this->conn == null)
            return array();
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT * FROM $this->data_table WHERE short_url='$short_id'");
        $row = mysqli_fetch_assoc($result);
        if ($row != null && sizeof($row) > 0)
            return array($row);
        return array();
    }




    /**
     * 查全部数据
     *
     * @param int $page 第几页
     * @param int $num 每页多少条数据
     * @return array
     */
    public function check_all(int $page, int $num) : array{
        $f = $page * $num;
        $result = mysqli_query($this->conn,
            /** @lang text */
            "SELECT * FROM $this->data_table ORDER BY create_time DESC LIMIT $f , $num ");
        $row = $this->sql_arr($result);
        if ($row != null && sizeof($row) > 0)
            return $row;
        return array();
    }




    /**
     * @param $result
     * @return array
     */
    private function sql_arr($result) : array{
        $array = array();
        while($row = mysqli_fetch_assoc($result)) {
            $array[] = $row;
        }
        return $array;
    }


    /**
     * 根据id 修改数据
     *
     * @param int $id
     * @param string $long_url
     * @param string $short_url
     * @param bool $is_open
     * @param bool $is_open_win
     * @param int $amount
     * @return bool
     */
    public function update_data(int $id, string $long_url, string $short_url, bool $is_open, bool $is_open_win, int $amount): bool
    {
        if ( $this->conn == null || $long_url == null || $long_url == '' || $short_url == null || $short_url == '')
            return false;
        $is_open_n = $is_open ? 1 : 0;
        $is_open_w = $is_open_win ? 1 : 0;
        $sql = /** @lang text */
            "UPDATE $this->data_table SET  long_url ='$long_url', short_url = '$short_url', is_win = $is_open_w, is_open = $is_open_n, url_visits = $amount WHERE id= $id";
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
    public function add_sql(string $host, string $user, string $psd, string $data, string $data_table, string $sql_str)
    {

        $conn = mysqli_connect($host, $user, $psd);
        mysqli_options($conn, MYSQLI_OPT_INT_AND_FLOAT_NATIVE, true);
        if ($conn->connect_error)
            throw new Exception("error：$conn->error");

        //查看是否有此数据库
        $result = mysqli_query($conn, "SHOW DATABASES LIKE '$data';");
        $row = mysqli_fetch_assoc($result);
        if ($row != null && sizeof($row) > 0) {
            mysqli_select_db($conn, $data);
            //查看是否有此数据表单
            $res2 = mysqli_query($conn, "SHOW TABLES LIKE '$data_table';");
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
