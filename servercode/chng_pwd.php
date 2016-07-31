<?php
	require 'core.inc.php';
	require 'connect.inc.php';
	
	$user_id = $_POST['user_id'];

	$old_password = $_POST['old_password'];
	$new_password = $_POST['new_password'];
	$re_new_password = $_POST['re_new_password'];

	$query = "
		SELECT user_id
		FROM users
		WHERE user_id = $user_id
		AND password = '" . md5 ( $old_password ) . "'
	";

	( $query_run = mysql_query($query) ) or die(mysql_error());

	$check = mysql_num_rows($query_run);
	
	if($check == 0) {
		
		echo "Old Password dont match";
		
	} else {

		if ($new_password != $re_new_password) {
			echo "New Passwords dont match. Re-enter password.";
		} else {
			$query = "
				UPDATE users
				SET 
					password = '" . md5($new_password) . "'
				WHERE user_id = $user_id
			";

			( $query_run = mysql_query($query) ) or die(mysql_error());
			echo "Password Changed !";
		}
	}
?>
