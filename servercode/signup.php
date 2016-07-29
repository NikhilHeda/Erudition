<?php
	require 'core.inc.php';
	require 'connect.inc.php';

	$username = $_POST ['username'];
	$pass = $_POST ['password'];
	$check_pass = $_POST ['r_password'];
	$email_id = $_POST ['email_id'];
	$ph_num = $_POST ['ph_number'];
	$gender = $_POST ['gender'];

	if ($pass != $check_pass)
		echo "Password Error";
	else {
		$query = "
			SELECT `email_id`
			FROM `users`
			WHERE `email_id`='$email_id'
		";

		($query_run = mysql_query ( $query )) or die(mysql_error());

		$num_rows = mysql_num_rows ( $query_run );

		if ($num_rows == 1) {
			echo "Email Error";
		} else {

			$query = "
				INSERT INTO `users` VALUES (
					'',
					'$username',
					'" . md5 ( $pass ) . "',
					'$email_id',
					'$gender',
					'$ph_num'
				)
			";

			(mysql_query ( $query )) or die(mysql_error());

			$query = "
				SELECT `user_id`
				FROM `users`
				WHERE `email_id`='$email_id'
			";

			($query_run = mysql_query ( $query )) or die(mysql_error());
			$user_id = mysql_result ( $query_run, 0, 'user_id' );

			echo $user_id;
		}
	}
?>