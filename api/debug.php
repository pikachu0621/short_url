<?php

include_once 'ini.php';
//远程调试
$file_get_contents = file_get_contents("php://input");
if ($file_get_contents != null && $file_get_contents != '')
    remote_debugging($file_get_contents, $GLOBALS['app_info']['pk_debug']);
