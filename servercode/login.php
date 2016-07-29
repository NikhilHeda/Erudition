<?php
	require 'core.inc.php';
	require 'connect.inc.php';

	$email = $_POST ['email'];
	$password = $_POST ['password'];

	$query = "
		SELECT `user_id`
		FROM `users`
		WHERE `email_id`='$email'
		AND `password`='" . md5 ( $password ) . "'
	";

	($query_run = mysql_query ( $query )) or die(mysql_error());

	$num_rows = mysql_num_rows ( $query_run );

	if ($num_rows == 0) {
		echo "false";
	} else {
		$user_id = mysql_result ( $query_run, 0, 'user_id' );
		echo $user_id;
	}
?>