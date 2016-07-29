<?php

	$mysql_host = 'localhost';
	$mysql_user = 'root';
	$mysql_pass = '';
		
	$mysql_db = 'quiz';
	
	( mysql_connect($mysql_host, $mysql_user, $mysql_pass) && mysql_select_db ($mysql_db) ) or die(mysql_error());

?>